package com.semmelzahntiger.arms.core.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class SimulatedProjectile {
    private final CompoundTag tag;
    private Vec3 currentSimulatedPosition;
    private final Vec3 initialDeltaMovement;
    private final double gravity;
    public SimulatedProjectile(CompoundTag tag, Vec3 lastPosition, Vec3 initialDeltaMovement, double gravity) {
        this.tag = tag;
        this.currentSimulatedPosition = lastPosition;
        this.initialDeltaMovement = initialDeltaMovement;
        this.gravity = gravity;
    }

    void onSimulationTick() {

    }
    Vec3 getReassemblyPosition() {
        return currentSimulatedPosition;
    }
}
