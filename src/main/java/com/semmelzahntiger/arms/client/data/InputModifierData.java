package com.semmelzahntiger.arms.client.data;

import com.semmelzahntiger.arms.core.input.InputListener;

import javax.annotation.Nullable;

@ClientData(registryName = "input_modifier", global = false)
public class InputModifierData {
    private @Nullable InputListener currentInputListener = null;
    public InputModifierData() {}

    public void setCurrentInputListener() {
        if(currentInputListener != null) {

        }
    }
    public @Nullable InputListener getCurrentInputListener() {
        return currentInputListener;
    }
}
