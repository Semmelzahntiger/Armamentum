package com.semmelzahntiger.arms.client.input;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class InputPriorityProfileRegistry {
    private static final Map<ResourceLocation, InputPriorityProfile> PROFILES = new LinkedHashMap<>();

    public static InputPriorityProfile register(InputPriorityProfile profile) {
        if (PROFILES.containsKey(profile.getId())) {
            throw new IllegalStateException("Duplicate input priority profile " + profile.getId());
        }
        PROFILES.put(profile.getId(), profile);
        return profile;
    }

    public static @Nullable InputPriorityProfile get(ResourceLocation id) {
        return PROFILES.get(id);
    }

    public static Collection<InputPriorityProfile> getAll() {
        return Collections.unmodifiableCollection(PROFILES.values());
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        for (InputPriorityProfile profile : PROFILES.values()) {
            for (KeyMapping key : profile.getKeys()) {
                event.register(key);
            }
        }
    }
}
