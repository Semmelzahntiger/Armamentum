package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.Vec3;

public record Bone(BoneId id, BoneId parent, Vec3 pivot, Transform initialTransformation) {
}
