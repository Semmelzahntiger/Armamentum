package com.semmelzahntiger.arms.client.model;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.client.model.base.CTSDefaultedGeoEntityModel;
import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.core.cts.data.MG08Data;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;

public class MG08Model extends CTSDefaultedGeoEntityModel<MG08, MG08Data> {

    public MG08Model() {
        super(ResourceLocation.fromNamespaceAndPath(Arms.MODID, "mg_08"), MG08Data.class);
    }

    @Override
    public void setCustomAnimations(MG08 animatable, long instanceId, AnimationState<MG08> animationState, MG08Data ctsData) {
        super.setCustomAnimations(animatable, instanceId, animationState, ctsData);
    }
}
