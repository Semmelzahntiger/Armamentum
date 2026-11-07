package com.semmelzahntiger.arms.client.input;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class InputPriorityProfile {
    private final ResourceLocation id;
    private final Set<KeyMapping> keys;

    public InputPriorityProfile(ResourceLocation id, KeyMapping... keys) {
        this.id = id;
        Set<KeyMapping> keySet = Collections.newSetFromMap(new IdentityHashMap<>());
        ProfileKeyConflictContext context = new ProfileKeyConflictContext(this);
        for (KeyMapping key : keys) {
            key.setKeyConflictContext(context);
            keySet.add(key);
        }
        this.keys = Collections.unmodifiableSet(keySet);
    }

    public ResourceLocation getId() {
        return id;
    }

    public Set<KeyMapping> getKeys() {
        return keys;
    }

    public boolean isPrioritized(KeyMapping keyMapping) {
        return keys.contains(keyMapping);
    }

    public boolean isActive() {
        return InputPriorityHandler.getActiveProfile() == this;
    }
}
