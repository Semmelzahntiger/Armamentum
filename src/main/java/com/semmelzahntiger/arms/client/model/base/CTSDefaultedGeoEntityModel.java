package com.semmelzahntiger.arms.client.model.base;

import com.semmelzahntiger.arms.core.cts.CTSData;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.CTSManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public abstract class CTSDefaultedGeoEntityModel<E extends Entity & GeoAnimatable & CTSEntity<E,D>,D extends CTSData<E>> extends DefaultedEntityGeoModel<E> {
    private final Class<D> dataClass;
    public CTSDefaultedGeoEntityModel(ResourceLocation assetSubpath, Class<D> dataClass) {
        super(assetSubpath);
        this.dataClass = dataClass;
    }

    @Override
    public final void setCustomAnimations(E animatable, long instanceId, AnimationState<E> animationState) {
        D data = CTSManager.getData(animatable, dataClass);
        setCustomAnimations(animatable, instanceId, animationState, data);
    }
    public void setCustomAnimations(E animatable, long instanceId, AnimationState<E> animationState, D ctsData) {

    }
}
