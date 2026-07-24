package com.semmelzahntiger.arms.client.events;

import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.CTSManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class ClientEventListeners {

    @SubscribeEvent
    public static void onEntityJoinLevelEvent(EntityJoinLevelEvent event) {
        if(event.getLevel().isClientSide() && event.getEntity() instanceof CTSEntity<?, ?> entity) {
            CTSManager.registerEntity(entity);
        }
    }
    @SubscribeEvent
    public static void onClientTickEvent(ClientTickEvent.Pre event) {
        CTSManager.clientTickUpdate();
    }
}
