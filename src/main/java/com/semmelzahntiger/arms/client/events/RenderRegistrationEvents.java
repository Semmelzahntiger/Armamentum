package com.semmelzahntiger.arms.client.events;

import com.semmelzahntiger.arms.client.renderers.MG08Renderer;
import com.semmelzahntiger.arms.client.renderers.TestEntityRenderer;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class RenderRegistrationEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegister.TEST_ENTITY.get(), TestEntityRenderer::new);
        event.registerEntityRenderer(EntityRegister.MG08_GUN.get(), MG08Renderer::new);
    }
}
