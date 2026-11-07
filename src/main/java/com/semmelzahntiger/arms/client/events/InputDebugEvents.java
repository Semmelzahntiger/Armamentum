package com.semmelzahntiger.arms.client.events;

import com.semmelzahntiger.arms.client.data.InputModifierData;
import com.semmelzahntiger.arms.client.input.InputPriorityHandler;
import com.semmelzahntiger.arms.client.input.InputPriorityProfiles;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import util.LogUtil;

public class InputDebugEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        InputModifierData data = InputPriorityHandler.getInputModifierData();
        if (data == null) {
            return;
        }
        while (InputPriorityProfiles.DEBUG_TOGGLE.consumeClick()) {
            if (data.getActiveProfile() == null) {
                data.setActiveProfile(InputPriorityProfiles.TEST);
                LogUtil.getLogger().info("Input priority profile '{}' activated", InputPriorityProfiles.TEST.getId());
            }
            else {
                data.setActiveProfile(null);
                LogUtil.getLogger().info("Input priority profile deactivated");
            }
        }
        while (InputPriorityProfiles.TEST_LEFT.consumeClick()) {
            LogUtil.getLogger().info("test_left fired");
        }
    }
}
