package com.semmelzahntiger.arms.core.simulate;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SimulationRegistry {
    private static final Map<ResourceLocation, Supplier<? extends SimulatedProjectile>> registry = new HashMap<>();
    private static final Map<Class<?>, ResourceLocation> classRegistry = new HashMap<>();

    public static <T extends SimulatedProjectile> void register(ResourceLocation location, Supplier<T> supplier) {
        registry.put(location, supplier);
        T instance = supplier.get();
        classRegistry.put(instance.getClass(), location);
    }

    public static SimulatedProjectile getSimulationInstance(ResourceLocation location) {
        Supplier<? extends SimulatedProjectile> sup = registry.get(location);
        if(sup == null) {
            throw new RuntimeException("Tried getting unregistered Simulation Type");
        }
        return sup.get();
    }
    public static ResourceLocation getRegistryLocation(Class<?> simulationClass) {
        ResourceLocation location = classRegistry.get(simulationClass);
        if(location == null) {
            throw new RuntimeException("No valid Location for class found");
        }
        return location;
    }
}
