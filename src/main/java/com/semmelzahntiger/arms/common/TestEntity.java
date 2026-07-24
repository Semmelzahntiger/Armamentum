package com.semmelzahntiger.arms.common;

import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.TestCTSData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TestEntity extends Entity implements CTSEntity<TestEntity, TestCTSData> {

    public TestEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @NotNull Supplier<TestCTSData> getDataInstance() {
        return () -> new TestCTSData(this);
    }

    @Override
    public @NotNull TestEntity getEntity() {
        return this;
    }

    @Override
    public void updateData(@NotNull TestEntity entity) {

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
