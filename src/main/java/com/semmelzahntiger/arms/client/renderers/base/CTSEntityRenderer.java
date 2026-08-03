package com.semmelzahntiger.arms.client.renderers.base;

import com.semmelzahntiger.arms.core.cts.CTSData;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public abstract class CTSEntityRenderer<E extends Entity & GeoAnimatable & CTSEntity<E,D>,D extends CTSData<E>> extends GeoEntityRenderer<E> {
    public CTSEntityRenderer(EntityRendererProvider.Context context, GeoModel<E> geoModel) {
        super(context,geoModel);
    }
}
