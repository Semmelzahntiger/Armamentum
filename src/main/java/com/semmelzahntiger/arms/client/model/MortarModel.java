package com.semmelzahntiger.arms.client.model;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.client.model.base.CTSDefaultedGeoEntityModel;
import com.semmelzahntiger.arms.common.entities.Mortar;
import com.semmelzahntiger.arms.core.cts.data.MortarData;
import net.minecraft.resources.ResourceLocation;

public class MortarModel extends CTSDefaultedGeoEntityModel<Mortar, MortarData> {
    public MortarModel() {
        super(ResourceLocation.fromNamespaceAndPath(Arms.MODID, "mortar"), MortarData.class);
    }

}
