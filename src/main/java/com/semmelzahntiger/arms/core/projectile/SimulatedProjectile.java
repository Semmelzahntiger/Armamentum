package com.semmelzahntiger.arms.core.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class SimulatedProjectile {
    private boolean reassemble = false;
    private final CompoundTag tag;
    private Vec3 currentSimulatedPosition;
    private Vec3 deltaMovement;
    private final double gravity;
    private final double drag;
    public SimulatedProjectile(CompoundTag tag, Vec3 lastPosition, Vec3 initialDeltaMovement, double gravity, double dragModifier) {
        this.tag = tag;
        this.currentSimulatedPosition = lastPosition;
        this.deltaMovement = initialDeltaMovement;
        this.gravity = gravity;
        this.drag = dragModifier;
    }

    public void onSimulationTick(ServerLevel level) {
        currentSimulatedPosition = currentSimulatedPosition.add(deltaMovement);
        deltaMovement = deltaMovement.add(0,-gravity, 0).scale(drag);
        if(level.isPositionEntityTicking(new BlockPos((int) currentSimulatedPosition.x, (int) currentSimulatedPosition.y, (int) currentSimulatedPosition.z))) {
            signalReassemble();
        }
    }
    public Vec3 getReassemblyPosition() {
        return currentSimulatedPosition;
    }
    public Vec3 getDeltaMovement() {
        return deltaMovement;
    }
    public CompoundTag getProjectileTag() {
        return tag;
    }
    protected void signalReassemble() {
        reassemble = true;
    }
    public boolean shouldReassemble() {
        return reassemble;
    }
}
