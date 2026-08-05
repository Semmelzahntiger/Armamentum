package com.semmelzahntiger.arms.client.renderers;

import com.semmelzahntiger.arms.client.model.MG08DualModel;
import com.semmelzahntiger.arms.client.renderers.base.CTSEntityRenderer;
import com.semmelzahntiger.arms.common.entities.MG08Dual;
import com.semmelzahntiger.arms.core.cts.data.MG08DualData;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class MG08DualRenderer extends CTSEntityRenderer<MG08Dual, MG08DualData> {
    public MG08DualRenderer(EntityRendererProvider.Context context) {
        super(context, new MG08DualModel());
    }
}
