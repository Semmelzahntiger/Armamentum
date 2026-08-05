package com.semmelzahntiger.arms.client.events;

import com.semmelzahntiger.arms.client.renderers.MG08DualRenderer;
import com.semmelzahntiger.arms.client.renderers.MG08Renderer;
import com.semmelzahntiger.arms.client.renderers.TestEntityRenderer;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class RenderRegistrationEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegister.TEST_ENTITY.get(), TestEntityRenderer::new);
        event.registerEntityRenderer(EntityRegister.MG08_ENTITY.get(), MG08Renderer::new);
        event.registerEntityRenderer(EntityRegister.MG08DUAL_ENTITY.get(), MG08DualRenderer::new);
    }
}
