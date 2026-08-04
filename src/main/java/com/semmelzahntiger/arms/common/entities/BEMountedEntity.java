package com.semmelzahntiger.arms.common.entities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public interface BEMountedEntity {
    default Vec3 getInitialOffset() {
        return Vec3.ZERO;
    }
    void setOwnerPos(Vec3 ownerPos);
}
