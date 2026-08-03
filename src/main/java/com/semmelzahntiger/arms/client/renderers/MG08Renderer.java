package com.semmelzahntiger.arms.client.renderers;

import com.semmelzahntiger.arms.client.model.MG08Model;
import com.semmelzahntiger.arms.client.renderers.base.CTSEntityRenderer;
import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.core.cts.data.MG08Data;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MG08Renderer extends CTSEntityRenderer<MG08, MG08Data> {
    public MG08Renderer(EntityRendererProvider.Context context) {
        super(context, new MG08Model());
    }
}
