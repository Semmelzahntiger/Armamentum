package com.semmelzahntiger.arms.core.misc;

import net.neoforged.bus.api.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotating classes with this {@link InstanceEventListener} creates a global handler for the specified events.
 * It's required that classes annotated with {@link InstanceEventListener} also implement the {@link InstanceListener} interface
 * Implementors are responsible to register themselves to the {@link InstanceEventDistributor} via {@link InstanceEventDistributor#register(InstanceListener)}
 * and unregister themselves via {@link InstanceEventDistributor#unregister(InstanceListener)}. While the event distributor holds all listeners via a
 * {@link java.lang.ref.WeakReference WeakReference} so the Garbage Collector can collect listeners already removed by the game its best to remove it yourself because
 * it's not guaranteed that GC collects immediately when you want it to not listen anymore.
 * See {@link InstanceListener} for a list of supported events and override if you need listening behaviour.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface InstanceEventListener {
    Class<? extends Event>[] events();
}
