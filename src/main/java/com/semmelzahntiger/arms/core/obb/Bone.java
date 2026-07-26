package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

public record Bone(BoneId id, BoneId parent, Vec3 distFromParent, Matrix3f initialTransformation) {
}
