package com.semmelzahntiger.arms.client.model;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.client.model.base.CTSDefaultedGeoEntityModel;
import com.semmelzahntiger.arms.common.entities.MG08Dual;
import com.semmelzahntiger.arms.core.cts.data.MG08Data;
import com.semmelzahntiger.arms.core.cts.data.MG08DualData;
import net.minecraft.resources.ResourceLocation;

public class MG08DualModel extends CTSDefaultedGeoEntityModel<MG08Dual, MG08DualData> {
    public MG08DualModel() {
        super(ResourceLocation.fromNamespaceAndPath(Arms.MODID, "mg_08_dual"), MG08DualData.class);
    }
}
