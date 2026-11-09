package com.semmelzahntiger.arms.core.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface SimulatableProjectile<T extends Entity> {
    @NotNull SimulatedProjectile getSimulatedProjectile(T entity, CompoundTag compoundTag);
}
