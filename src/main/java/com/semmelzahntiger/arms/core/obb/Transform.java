package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.Vec3;

public record Transform(double m00, double m01, double m02,
                        double m10, double m11, double m12,
                        double m20, double m21, double m22) {
    public static Transform IDENTITY = new Transform(
            1,0,0,
            0,1,0,
            0,0,1
    );

}
