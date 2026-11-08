package com.semmelzahntiger.arms.core.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.function.BiFunction;

public interface SimulatableProjectile<T extends Entity> {
    BiFunction<T, CompoundTag, SimulatedProjectile> getSimulatedProjectile();
    void onSimulationTick();
}
