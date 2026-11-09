package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.core.projectile.SimulatableProjectile;
import com.semmelzahntiger.arms.core.projectile.SimulatedProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TestSimulatableProjectile extends Projectile implements SimulatableProjectile<TestSimulatableProjectile> {
    protected TestSimulatableProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @NotNull SimulatedProjectile getSimulatedProjectile(TestSimulatableProjectile test, CompoundTag tag) {
        return new SimulatedProjectile(tag, test.position(), test.getDeltaMovement(), test.getGravity());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
