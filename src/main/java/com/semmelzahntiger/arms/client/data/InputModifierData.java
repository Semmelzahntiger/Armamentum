package com.semmelzahntiger.arms.client.data;

import com.semmelzahntiger.arms.client.input.InputPriorityHandler;
import com.semmelzahntiger.arms.client.input.InputPriorityProfile;
import com.semmelzahntiger.arms.core.input.InputListener;

import javax.annotation.Nullable;

@ClientData(registryName = "input_modifier", global = false)
public class InputModifierData {
    private @Nullable InputListener currentInputListener = null;
    private @Nullable InputPriorityProfile activeProfile = null;
    public InputModifierData() {}

    public void setCurrentInputListener() {
        if(currentInputListener != null) {

        }
    }
    public @Nullable InputListener getCurrentInputListener() {
        return currentInputListener;
    }

    public @Nullable InputPriorityProfile getActiveProfile() {
        return activeProfile;
    }

    public void setActiveProfile(@Nullable InputPriorityProfile profile) {
        this.activeProfile = profile;
        if (profile != null) {
            InputPriorityHandler.suppressOverlapping(profile);
        }
    }
}
