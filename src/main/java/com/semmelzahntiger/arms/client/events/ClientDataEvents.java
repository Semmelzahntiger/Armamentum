package com.semmelzahntiger.arms.client.events;

import com.semmelzahntiger.arms.client.data.ClientDataManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;


public class ClientDataEvents {
    @SubscribeEvent
    public static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientDataManager.createData();
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientDataManager.discardData();
    }
}
