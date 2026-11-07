package com.semmelzahntiger.arms.client.data;

import com.google.common.collect.ImmutableMap;
import com.semmelzahntiger.arms.Arms;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.Type;
import util.LogUtil;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientDataManager {
    private static boolean registryOpen = true;
    private static ClientDataManager CLIENT_DATA_MANAGER;
    private static final HashMap<String, Object> globalClientData = new HashMap<>();
    private static ImmutableMap<String, Constructor<?>> CONSTRUCTORS;
    private static ImmutableMap<String, Constructor<?>> GLOBAL_CONSTRUCTORS;
    private final ImmutableMap<String, Object> clientData;


    private ClientDataManager() {
        HashMap<String, Object> clientData = new HashMap<>();
        CONSTRUCTORS.forEach((registryName, constructor) -> {
            try {
                clientData.put(registryName, constructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        this.clientData = ImmutableMap.copyOf(clientData);
        CLIENT_DATA_MANAGER = this;
    }


    public <T> Optional<T> getDataOptional(String id, Class<T> clazz) {
        return Optional.ofNullable(getDataRaw(id, clazz));
    }
    @SuppressWarnings("unchecked")
    public <T> T getDataRaw(String id, Class<T> clazz) {
        try {
            return (T) clientData.get(id);
        }
        catch (ClassCastException e) {
            return null;
        }
    }
    public Optional<Object> getDataOptional(String id) {
        return Optional.ofNullable(clientData.get(id));
    }
    public Object getDataRaw(String id) {
        return clientData.get(id);
    }
    public static <T> Optional<T> getGlobalDataOptional(String id, Class<T> clazz) {
        return Optional.ofNullable(getGlobalDataRaw(id, clazz));
    }
    @SuppressWarnings("unchecked")
    public static <T> T getGlobalDataRaw(String id, Class<T> clazz) {
        try {
            return (T) globalClientData.get(id);
        }
        catch (ClassCastException e) {
            return null;
        }
    }
    public static Optional<Object> getGlobalDataOptional(String id) {
        return Optional.ofNullable(globalClientData.get(id));
    }
    public static Object getGlobalDataRaw(String id) {
        return globalClientData.get(id);
    }
    @ApiStatus.Internal
    public static void init() {
        if(registryOpen) {
            //Begin Registration
            LogUtil.getLogger().info("Registering Client Data...");

            HashMap<String, Constructor<?>> constructors = new HashMap<>();
            HashMap<String, Constructor<?>> globalConstructors = new HashMap<>();

            registryOpen = false;
            Type clientDataAnnotation = Type.getType(ClientData.class);
            //Get Reflection Data from NeoForge
            ModFileScanData data = ModList.get().getModFileById(Arms.MODID).getFile().getScanResult();
            for (ModFileScanData.AnnotationData annotation : data.getAnnotations()) {
                // Filter if Class annotated with @ClientData
                if(annotation.annotationType().equals(clientDataAnnotation)) {
                    Map<String, Object> params = annotation.annotationData();
                    String className = annotation.clazz().getClassName();
                    // Get Registry Name
                    String registryName = (String) params.get("registryName");
                    // New ClientData per Level or per Session
                    Boolean global = (Boolean) params.get("global");
                    if(registryName != null && !registryName.isEmpty() && global != null) {
                        try {
                            Class<?> dataClass = Class.forName(className);
                            Constructor<?> constructor = dataClass.getDeclaredConstructor();
                            constructor.setAccessible(true);
                            if(!global) {
                                if(constructors.containsKey(registryName)) {
                                    throw new IllegalStateException("Duplicate registry name " + registryName);
                                }
                                constructors.put(registryName, constructor);
                                LogUtil.getLogger().info("Registering Client Data of: '{}.class' under RegistryName '{}'", dataClass.getSimpleName(), registryName);
                            }
                            else {
                                if(globalConstructors.containsKey(registryName)) {
                                    throw new IllegalStateException("Duplicate global registry name " + registryName);
                                }
                                globalConstructors.put(registryName, constructor);
                                LogUtil.getLogger().info("Registering Global Client Data of: '{}.class' under RegistryName '{}'", dataClass.getSimpleName(), registryName);
                            }
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("No no-arg constructor found for: " + className + ".class", e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        throw new IllegalStateException(className + ".class has " + (registryName == null ? "missing" : "invalid") +" registryName");
                    }
                }
            }
            CONSTRUCTORS = ImmutableMap.copyOf(constructors);
            GLOBAL_CONSTRUCTORS = ImmutableMap.copyOf(globalConstructors);
            LogUtil.getLogger().info("Completed Registering Client Data");
            LogUtil.getLogger().info("Initializing Global Client Data");
            initGlobalData();
            LogUtil.getLogger().info("Completed Initializing Global Client Data");

        }
        else {
            LogUtil.getLogger().warn("Client Data Manager Initialization called outside of Initialization window");
        }
    }
    @ApiStatus.Internal
    public static void createData() {
        CLIENT_DATA_MANAGER = new ClientDataManager();
    }
    @ApiStatus.Internal
    public static void discardData() {
        CLIENT_DATA_MANAGER = null;
    }
    private static void initGlobalData() {
        GLOBAL_CONSTRUCTORS.forEach((registryName, constructor) -> {
            try {
                globalClientData.put(registryName, constructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static void resetGlobalData() {
        globalClientData.clear();
        initGlobalData();
    }
    public static @Nullable ClientDataManager getDataManager() {
        return CLIENT_DATA_MANAGER;
    }
    public static Optional<ClientDataManager> getDataManagerOptional() {
        return Optional.ofNullable(CLIENT_DATA_MANAGER);
    }
}
