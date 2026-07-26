package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.Vec3;

public class Pose {
    Transform[] rotations;
    Vec3[] translations;

    public Pose(Skeleton skeleton) {
        rotations = new Transform[skeleton.getBoneCount()];
        translations = new Vec3[skeleton.getBoneCount()];
    }
}
