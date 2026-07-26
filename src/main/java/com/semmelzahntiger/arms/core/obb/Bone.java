package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.Vec3;

public record Bone(int parent, Vec3 pivot, Transformation3x3 initialTransformation, BoneId id) {
}
