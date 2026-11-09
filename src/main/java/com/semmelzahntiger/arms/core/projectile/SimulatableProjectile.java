package com.semmelzahntiger.arms.core.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SimulatableProjectile<T extends Entity> {
    default @NotNull SimulatedProjectile getSimulatedProjectile(T entity, CompoundTag compoundTag) {
        return new SimulatedProjectile(compoundTag, entity.position(), entity.getDeltaMovement(), entity.getGravity(), 0.99f);
    }
    @Nullable
    static SimulatedProjectile getDefaultSimulatedProjectile(Entity entity) {
        CompoundTag tag = new CompoundTag();
        boolean success = entity.save(tag);
        if(!success) {
            return null;
        }
        return new SimulatedProjectile(tag, entity.position(), entity.getDeltaMovement(), entity.getGravity(), 0.99f);
    }
}
