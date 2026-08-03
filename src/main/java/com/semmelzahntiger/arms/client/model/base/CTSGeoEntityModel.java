package com.semmelzahntiger.arms.client.model.base;

import com.semmelzahntiger.arms.core.cts.CTSData;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public abstract class CTSGeoEntityModel<E extends Entity & GeoAnimatable & CTSEntity<E,D>,D extends CTSData<E>> extends GeoModel<E> {

}
