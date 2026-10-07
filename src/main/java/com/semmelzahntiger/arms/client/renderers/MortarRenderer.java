package com.semmelzahntiger.arms.client.renderers;

import com.semmelzahntiger.arms.client.model.MG08Model;
import com.semmelzahntiger.arms.client.model.MortarModel;
import com.semmelzahntiger.arms.client.renderers.base.CTSEntityRenderer;
import com.semmelzahntiger.arms.common.entities.Mortar;
import com.semmelzahntiger.arms.core.cts.data.MortarData;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class MortarRenderer extends CTSEntityRenderer<Mortar, MortarData> {
    public MortarRenderer(EntityRendererProvider.Context context) {
        super(context, new MortarModel());
    }
}
