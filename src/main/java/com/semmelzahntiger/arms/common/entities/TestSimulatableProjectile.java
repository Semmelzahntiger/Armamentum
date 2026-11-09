package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.core.simulate.SimulatableProjectile;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class TestSimulatableProjectile extends Projectile implements SimulatableProjectile<TestSimulatableProjectile> {
    protected TestSimulatableProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
