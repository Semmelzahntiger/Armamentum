package com.semmelzahntiger.arms.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Generates {@code InstanceEventDistributor} from every type annotated with
 * {@code @InstanceEventListener}.
 *
 * <p>The union of all {@code events()} arrays becomes one static bus handler each. Every handler
 * walks a queue of weak references to {@code InstanceListener} instances, drops the ones the GC
 * has already cleared, and forwards the event to the rest. That is what lets block entities and
 * entities subscribe without ever having to unregister.
 *
 * <p>This module deliberately has no dependency on the mod, NeoForge or Minecraft. Everything it
 * needs — the annotation, {@code InstanceListener} and the event classes — is resolved by name
 * against the compile classpath of the compilation being processed.
 */
@SupportedAnnotationTypes("*")
public class InstanceEventProcessor extends AbstractProcessor {

    private static final String ANNOTATION = "com.semmelzahntiger.arms.core.misc.InstanceEventListener";
    private static final String LISTENER = "com.semmelzahntiger.arms.core.misc.InstanceListener";
    private static final String MOD_BUS_EVENT = "net.neoforged.fml.event.IModBusEvent";
    private static final String EVENT_BUS = "net.neoforged.bus.api.IEventBus";
    private static final String EVENTS_MEMBER = "events";
    private static final String DISTRIBUTOR = "InstanceEventDistributor";

    private Elements elements;
    private Types types;
    private Messager messager;
    private Filer filer;

    /** The distributor is emitted exactly once, in the first round. */
    private boolean emitted;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.elements = env.getElementUtils();
        this.types = env.getTypeUtils();
        this.messager = env.getMessager();
        this.filer = env.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Emitting in the final round would trip javac's "created in the last round will not be
        // subject to annotation processing" warning on every build.
        if (roundEnv.processingOver()) {
            return false;
        }

        TypeElement annotationType = elements.getTypeElement(ANNOTATION);
        Set<? extends Element> annotated = annotationType == null
                ? Set.of()
                : roundEnv.getElementsAnnotatedWith(annotationType);

        if (emitted) {
            // Nothing in this build generates annotated types, so this should not happen. Fail loudly
            // rather than silently dropping the events.
            for (Element element : annotated) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        "@InstanceEventListener was found after " + DISTRIBUTOR + " had already been "
                                + "generated, so its events will not be dispatched.", element);
            }
            return false;
        }

        // Note: the distributor is generated even when nothing is annotated, otherwise code that
        // references it (Arms#subscribe) would not compile. That is also why this processor claims
        // "*" — javac only invokes a processor whose declared annotations are present in the round.
        TypeElement listenerType = elements.getTypeElement(LISTENER);
        if (listenerType == null) {
            if (!annotated.isEmpty()) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        LISTENER + " is not on the compile classpath, cannot generate " + DISTRIBUTOR + ".");
            }
            // Without InstanceListener there is nothing sensible to generate.
            return false;
        }

        Map<String, String> handlerByEvent = indexListenerMethods(listenerType);

        List<Element> originating = new ArrayList<>();
        originating.add(listenerType);

        // Sorted by fully qualified name so the generated source is byte-for-byte stable.
        Map<String, TypeElement> collected = new TreeMap<>();

        for (Element element : annotated) {
            if (!(element instanceof TypeElement annotatedType)) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        "@InstanceEventListener is only supported on types.", element);
                continue;
            }
            originating.add(annotatedType);
            collectFrom(annotatedType, listenerType, handlerByEvent, collected);
        }

        emitted = true;
        write(listenerType, collected, originating);
        return false;
    }

    /** Validates one annotated type's {@code events()} and adds the surviving entries to {@code collected}. */
    private void collectFrom(TypeElement annotatedType,
                             TypeElement listenerType,
                             Map<String, String> handlerByEvent,
                             Map<String, TypeElement> collected) {

        if (!types.isAssignable(types.erasure(annotatedType.asType()), types.erasure(listenerType.asType()))) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    annotatedType.getQualifiedName() + " is annotated with @InstanceEventListener but does "
                            + "not implement " + LISTENER + ", so it can never receive an event.",
                    annotatedType);
        }

        TypeElement modBusEvent = elements.getTypeElement(MOD_BUS_EVENT);

        for (TypeMirror declared : readEvents(annotatedType)) {
            TypeElement event = asTypeElement(declared);
            if (event == null) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        "Could not resolve the event type " + declared + " listed by @InstanceEventListener.",
                        annotatedType);
                continue;
            }

            String eventName = event.getQualifiedName().toString();

            // NeoForge's EventBus rejects abstract events at registration time; catch it here instead.
            if (event.getModifiers().contains(Modifier.ABSTRACT)) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        eventName + " is abstract and cannot be subscribed to. List one of its subclasses "
                                + "instead.", annotatedType);
                continue;
            }

            // subscribe() targets the game bus; a mod bus event would blow up at startup with a much
            // less obvious message.
            if (modBusEvent != null && types.isAssignable(types.erasure(event.asType()),
                    types.erasure(modBusEvent.asType()))) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        eventName + " is a mod bus event. " + DISTRIBUTOR + " subscribes to the game bus "
                                + "only.", annotatedType);
                continue;
            }

            if (!handlerByEvent.containsKey(eventName)) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        LISTENER + " has no method taking " + eventName + ". Add one before listing this "
                                + "event in @InstanceEventListener.", annotatedType);
                continue;
            }

            collected.put(eventName, event);
        }
    }

    /**
     * Reads the {@code events()} member off the annotation mirror.
     *
     * <p>Going through the mirror rather than the annotation instance is deliberate: this module
     * cannot reference the annotation class, and reading a {@code Class} member would throw
     * {@link javax.lang.model.type.MirroredTypesException} anyway.
     */
    private List<TypeMirror> readEvents(TypeElement annotatedType) {
        for (AnnotationMirror mirror : annotatedType.getAnnotationMirrors()) {
            if (!(mirror.getAnnotationType().asElement() instanceof TypeElement annotationType)) {
                continue;
            }
            if (!annotationType.getQualifiedName().contentEquals(ANNOTATION)) {
                continue;
            }
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                    : elements.getElementValuesWithDefaults(mirror).entrySet()) {

                if (!entry.getKey().getSimpleName().contentEquals(EVENTS_MEMBER)) {
                    continue;
                }
                if (!(entry.getValue().getValue() instanceof List<?> values)) {
                    continue;
                }
                List<TypeMirror> events = new ArrayList<>(values.size());
                for (Object value : values) {
                    if (value instanceof AnnotationValue annotationValue
                            && annotationValue.getValue() instanceof TypeMirror event) {
                        events.add(event);
                    }
                }
                return events;
            }
        }
        return List.of();
    }

    /**
     * Maps each event type to the {@code InstanceListener} method that accepts it.
     *
     * <p>Keyed on the parameter type, never on the method name: {@code InstanceListener}'s naming is
     * inconsistent ({@code onPistonPreEvent} vs {@code onLivingDamageEventPre}, and
     * {@code onServerLifecycleEvent} actually takes {@code ServerStartedEvent}), but every method's
     * parameter type is unique.
     */
    private Map<String, String> indexListenerMethods(TypeElement listenerType) {
        Map<String, String> handlerByEvent = new HashMap<>();

        for (ExecutableElement method : ElementFilter.methodsIn(elements.getAllMembers(listenerType))) {
            if (method.getModifiers().contains(Modifier.STATIC)) {
                continue;
            }
            if (method.getParameters().size() != 1) {
                continue;
            }
            // Skip Object#equals(Object) and friends inherited into the interface.
            if (method.getEnclosingElement() instanceof TypeElement owner
                    && owner.getQualifiedName().contentEquals("java.lang.Object")) {
                continue;
            }
            TypeElement parameter = asTypeElement(method.getParameters().get(0).asType());
            if (parameter == null) {
                continue;
            }
            String name = method.getSimpleName().toString();
            String previous = handlerByEvent.putIfAbsent(parameter.getQualifiedName().toString(), name);
            if (previous != null && !previous.equals(name)) {
                messager.printMessage(Diagnostic.Kind.WARNING,
                        LISTENER + " declares more than one method taking " + parameter.getQualifiedName()
                                + "; dispatching to " + previous + " and ignoring " + name + ".", method);
            }
        }
        return handlerByEvent;
    }

    private TypeElement asTypeElement(TypeMirror mirror) {
        Element element = types.asElement(types.erasure(mirror));
        return element instanceof TypeElement type ? type : null;
    }

    /**
     * {@code ServerTickEvent.Pre} becomes {@code onServerTickEventPre}, {@code LivingDeathEvent}
     * becomes {@code onLivingDeathEvent}.
     */
    private static String handlerName(TypeElement event) {
        Deque<String> parts = new ArrayDeque<>();
        Element current = event;
        while (current instanceof TypeElement type) {
            parts.addFirst(type.getSimpleName().toString());
            current = type.getEnclosingElement();
        }
        StringBuilder name = new StringBuilder("on");
        for (String part : parts) {
            name.append(part);
        }
        return name.toString();
    }

    private void write(TypeElement listenerType,
                       Map<String, TypeElement> collected,
                       List<Element> originating) {

        String packageName = elements.getPackageOf(listenerType).getQualifiedName().toString();

        // event fqn -> generated handler method name, in the sorted order of `collected`.
        Map<String, String> handlerNames = new LinkedHashMap<>();
        Set<String> used = new HashSet<>();
        for (Map.Entry<String, TypeElement> entry : collected.entrySet()) {
            String base = handlerName(entry.getValue());
            String name = base;
            for (int suffix = 2; !used.add(name); suffix++) {
                name = base + "_" + suffix;
            }
            if (!name.equals(base)) {
                messager.printMessage(Diagnostic.Kind.WARNING,
                        entry.getKey() + " collides with another event's handler name; it is dispatched by "
                                + name + " instead of " + base + ".");
            }
            handlerNames.put(entry.getKey(), name);
        }

        Map<String, String> listenerMethods = indexListenerMethods(listenerType);

        try {
            JavaFileObject file = filer.createSourceFile(
                    packageName.isEmpty() ? DISTRIBUTOR : packageName + "." + DISTRIBUTOR,
                    originating.toArray(new Element[0]));

            try (PrintWriter out = new PrintWriter(file.openWriter())) {
                writeSource(out, packageName, collected, handlerNames, listenerMethods);
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Failed to write " + DISTRIBUTOR + ": " + e);
        }
    }

    private void writeSource(PrintWriter out,
                             String packageName,
                             Map<String, TypeElement> collected,
                             Map<String, String> handlerNames,
                             Map<String, String> listenerMethods) {

        if (!packageName.isEmpty()) {
            out.printf("package %s;%n%n", packageName);
        }
        out.printf("import %s;%n%n", EVENT_BUS);
        out.println("import java.lang.ref.WeakReference;");
        out.println("import java.util.Iterator;");
        out.println("import java.util.Queue;");
        out.println("import java.util.concurrent.ConcurrentLinkedQueue;");
        out.println();
        out.println("/**");
        out.println(" * Generated by InstanceEventProcessor from @InstanceEventListener. Do not edit.");
        out.println(" *");
        out.println(" * <p>Listeners are held weakly, so a block entity or entity is collected by the GC");
        out.println(" * without having to unregister first. Cleared references are dropped the next time an");
        out.println(" * event is dispatched.");
        out.println(" */");
        out.println("public final class InstanceEventDistributor {");
        out.println("    private static final Queue<WeakReference<InstanceListener>> LISTENERS =");
        out.println("            new ConcurrentLinkedQueue<>();");
        out.println();
        out.println("    private InstanceEventDistributor() {}");
        out.println();
        out.println("    /** Adds {@code listener} as a weak reference. Does not deduplicate. */");
        out.println("    public static void register(InstanceListener listener) {");
        out.println("        LISTENERS.add(new WeakReference<>(listener));");
        out.println("    }");
        out.println();
        out.println("    /** Drops {@code listener} without waiting for the GC, along with any cleared references. */");
        out.println("    public static void unregister(InstanceListener listener) {");
        out.println("        Iterator<WeakReference<InstanceListener>> iterator = LISTENERS.iterator();");
        out.println("        while (iterator.hasNext()) {");
        out.println("            InstanceListener registered = iterator.next().get();");
        out.println("            if (registered == null || registered == listener) {");
        out.println("                iterator.remove();");
        out.println("            }");
        out.println("        }");
        out.println("    }");
        out.println();
        out.println("    /** Subscribes every generated handler to {@code bus}. */");
        out.println("    public static void subscribe(IEventBus bus) {");
        for (Map.Entry<String, TypeElement> entry : collected.entrySet()) {
            out.printf("        bus.addListener(%s.class, InstanceEventDistributor::%s);%n",
                    entry.getKey(), handlerNames.get(entry.getKey()));
        }
        out.println("    }");

        for (Map.Entry<String, TypeElement> entry : collected.entrySet()) {
            String eventName = entry.getKey();
            out.println();
            out.printf("    public static void %s(%s event) {%n", handlerNames.get(eventName), eventName);
            out.println("        Iterator<WeakReference<InstanceListener>> iterator = LISTENERS.iterator();");
            out.println("        while (iterator.hasNext()) {");
            out.println("            InstanceListener listener = iterator.next().get();");
            out.println("            if (listener == null) {");
            out.println("                iterator.remove();");
            out.println("                continue;");
            out.println("            }");
            out.printf("            listener.%s(event);%n", listenerMethods.get(eventName));
            out.println("        }");
            out.println("    }");
        }

        out.println("}");
    }
}
