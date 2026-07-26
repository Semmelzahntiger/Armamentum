package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.Vec3;

public record Transformation3x3(double m00, double m01, double m02,
                                double m10, double m11, double m12,
                                double m20, double m21, double m22) {
    static Transformation3x3 IDENTITY;

    static Transformation3x3 fromAxisAngle(Vec3 axis, float radians) {

    }
    static Transformation3x3 fromEulerXYZ(double x, double y, double z) {

    }
    static Transformation3x3 fromQuaternion(float x, float y, float z, float w) {

    }

    public Transformation3x3 mul(Transformation3x3 child) {

    }
    public Vec3 apply(Vec3 v) {

    }
    public Vec3 applyInverse(Vec3 v) {

    }
    public Vec3 axis(int i) {

    }
    public boolean isOrthonormal(float tol) {

    }
}
