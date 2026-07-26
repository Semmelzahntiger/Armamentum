package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.AABB;

public record Part(AABB localShape, int boneIndex) {

}
