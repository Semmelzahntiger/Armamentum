package com.semmelzahntiger.arms.core.simulate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

/**
 * Generic Base Class for any Simulated Projectile
 */
public class SimulatedProjectile implements INBTSerializable<CompoundTag> {
    private boolean reassemble = false;
    private CompoundTag tag;
    private Vec3 currentSimulatedPosition;
    private Vec3 deltaMovement;
    private double gravity;
    private double drag;
    public SimulatedProjectile() {}
    public SimulatedProjectile(CompoundTag tag, Vec3 lastPosition, Vec3 initialDeltaMovement, double gravity, double dragModifier) {
        this.tag = tag;
        this.currentSimulatedPosition = lastPosition;
        this.deltaMovement = initialDeltaMovement;
        this.gravity = gravity;
        this.drag = dragModifier;
    }

    /**
     * Ticks the simulated Projectile. Base Class simulates flight path with standard values.
     * Todo: Check for collision to reassemble
     * @param level Level the original Projectile belonged to
     */
    public void onSimulationTick(ServerLevel level) {
        currentSimulatedPosition = currentSimulatedPosition.add(deltaMovement);
        deltaMovement = deltaMovement.add(0,-gravity, 0).scale(drag);
        if(level.isPositionEntityTicking(new BlockPos((int) currentSimulatedPosition.x, (int) currentSimulatedPosition.y, (int) currentSimulatedPosition.z))) {
            signalReassemble();
        }
    }

    /**
     * @return Position where the reassembled Entity will appear
     */
    public Vec3 getReassemblyPosition() {
        return currentSimulatedPosition;
    }

    /**
     * @return deltaMovement of the reassembled Entity
     */
    public Vec3 getDeltaMovement() {
        return deltaMovement;
    }

    /**
     * @return NBT Data of the actual Projectile
     */
    public CompoundTag getProjectileTag() {
        return tag;
    }

    /**
     * Marks projectile for reassembly
     */
    protected void signalReassemble() {
        reassemble = true;
    }

    public boolean shouldReassemble() {
        return reassemble;
    }

    @Override
    public final CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.put("projectile_tag", tag);
        tag.putDouble("simulated_x", currentSimulatedPosition.x);
        tag.putDouble("simulated_y", currentSimulatedPosition.y);
        tag.putDouble("simulated_z", currentSimulatedPosition.z);
        tag.putDouble("delta_x", deltaMovement.x);
        tag.putDouble("delta_y", deltaMovement.y);
        tag.putDouble("delta_z", deltaMovement.z);
        tag.putDouble("gravity", gravity);
        tag.putDouble("drag", drag);
        saveAdditional(tag, provider);
        return tag;
    }
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {

    }

    @Override
    public final void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag compoundTag) {
        this.tag = compoundTag.getCompound("projectile_tag");
        double x = tag.getDouble("simulated_x");
        double y = tag.getDouble("simulated_y");
        double z = tag.getDouble("simulated_z");
        this.currentSimulatedPosition = new Vec3(x,y,z);
        double delta_x = tag.getDouble("delta_x");
        double delta_y = tag.getDouble("delta_y");
        double delta_z = tag.getDouble("delta_z");
        this.deltaMovement = new Vec3(delta_x, delta_y, delta_z);
        this.gravity = tag.getDouble("gravity");
        this.drag = tag.getDouble("drag");
        loadAdditional(provider, compoundTag);
    }
    public void loadAdditional(HolderLookup.Provider provider, CompoundTag compoundTag) {

    }
}
