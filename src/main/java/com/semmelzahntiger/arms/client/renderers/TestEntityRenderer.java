package com.semmelzahntiger.arms.client.renderers;

import com.semmelzahntiger.arms.common.TestEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TestEntityRenderer extends EntityRenderer<TestEntity> {
    public TestEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(TestEntity testEntity) {
        return null;
    }
}
