package com.semmelzahntiger.arms.common.entities;

import net.minecraft.world.phys.Vec3;

public interface BEMountedEntity {
    default Vec3 getInitialOffset() {
        return Vec3.ZERO;
    }
}
