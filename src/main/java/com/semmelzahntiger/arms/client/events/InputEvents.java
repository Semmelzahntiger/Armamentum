package com.semmelzahntiger.arms.client.events;

import com.semmelzahntiger.arms.client.input.InputPriorityProfileRegistry;
import com.semmelzahntiger.arms.client.input.InputPriorityProfiles;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class InputEvents {
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        InputPriorityProfileRegistry.registerKeyMappings(event);
        event.register(InputPriorityProfiles.DEBUG_TOGGLE);
    }
}
