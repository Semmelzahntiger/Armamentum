package com.semmelzahntiger.arms.core.obb;

import net.minecraft.world.phys.AABB;

public record Part(String partName,BoneId boneId, AABB localAABB) {

}
