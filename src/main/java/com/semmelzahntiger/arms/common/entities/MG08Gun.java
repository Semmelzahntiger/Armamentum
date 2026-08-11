package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.common.skeletons.Skeletons;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.data.MG08GunData;
import com.semmelzahntiger.arms.core.obb.OBBEntity;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Supplier;

public class MG08Gun extends Entity implements GeoAnimatable, OBBEntity, GunEntity, CTSEntity<MG08Gun, MG08GunData> {
    protected final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    protected Pose pose = new Pose(getSkeleton());

    public MG08Gun(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Vec3 getOffsetFromControllerCenter() {
        return null;
    }

    @Override
    public Pose getOBBPose() {
        return pose;
    }

    @Override
    public Skeleton getSkeleton() {
        return Skeletons.MG08_SKELETON;
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }

    @Override
    public double getTick(Object object) {
        return 0;
    }

    @Override
    public @NotNull Supplier<MG08GunData> getDataInstance() {
        return () -> new MG08GunData(getEntity());
    }

    @Override
    public @NotNull MG08Gun getEntity() {
        return this;
    }
}
