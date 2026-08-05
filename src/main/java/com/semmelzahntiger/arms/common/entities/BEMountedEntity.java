package com.semmelzahntiger.arms.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

public interface BEMountedEntity {
    default Vec3 getInitialOffset() {
        return Vec3.ZERO;
    }
    void setOwnerPos(BlockPos ownerPos);
    void setRotation(Matrix3f rotation);
    void setState(boolean state);
}
