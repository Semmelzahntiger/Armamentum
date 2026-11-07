package com.semmelzahntiger.arms.client.input;

import com.semmelzahntiger.arms.client.data.ClientDataManager;
import com.semmelzahntiger.arms.client.data.InputModifierData;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InputPriorityHandler {
    public static final String INPUT_MODIFIER_DATA = "input_modifier";

    public static @Nullable InputModifierData getInputModifierData() {
        return ClientDataManager.getDataManagerOptional()
                .flatMap(manager -> manager.getDataOptional(INPUT_MODIFIER_DATA, InputModifierData.class))
                .orElse(null);
    }

    public static @Nullable InputPriorityProfile getActiveProfile() {
        InputModifierData data = getInputModifierData();
        return data == null ? null : data.getActiveProfile();
    }

    public static List<KeyMapping> filter(List<KeyMapping> found, boolean releasing) {
        if (releasing || found.isEmpty()) {
            return found;
        }
        InputPriorityProfile profile = getActiveProfile();
        if (profile == null) {
            return found;
        }
        List<KeyMapping> prioritized = new ArrayList<>();
        for (KeyMapping keyMapping : found) {
            if (profile.isPrioritized(keyMapping)) {
                prioritized.add(keyMapping);
            }
        }
        return prioritized.isEmpty() ? found : prioritized;
    }

    public static void suppressOverlapping(InputPriorityProfile profile) {
        for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
            if (profile.isPrioritized(keyMapping) || keyMapping.isUnbound()) {
                continue;
            }
            for (KeyMapping prioritized : profile.getKeys()) {
                if (!prioritized.isUnbound() && prioritized.getKey().equals(keyMapping.getKey())) {
                    keyMapping.setDown(false);
                    while (keyMapping.consumeClick()) {}
                    break;
                }
            }
        }
    }
}
