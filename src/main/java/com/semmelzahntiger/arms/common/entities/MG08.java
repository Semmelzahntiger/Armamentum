package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.common.register.EntityRegister;
import com.semmelzahntiger.arms.common.skeletons.Skeletons;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.data.MG08Data;
import com.semmelzahntiger.arms.core.obb.BoneId;
import com.semmelzahntiger.arms.core.obb.OBBEntity;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import com.semmelzahntiger.arms.helper.PoseUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Supplier;

public class MG08 extends Entity implements GeoAnimatable, OBBEntity, GunEntity, CTSEntity<MG08, MG08Data> {
    private static final EntityDataAccessor<Pose.Snapshot> SYNCED_POSE =
            SynchedEntityData.defineId(MG08.class, PoseUtil.POSE_SERIALIZER);

    protected final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    protected final Pose pose = new Pose(getSkeleton());

    public MG08(EntityType<?> type, Level level) {
        super(type,level);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SYNCED_POSE.equals(key) && level().isClientSide) {
            pose.apply(entityData.get(SYNCED_POSE));
        }
    }

    @Override
    public Vec3 getOffsetFromControllerCenter() {
        return Vec3.ZERO;
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
        builder.define(SYNCED_POSE, new Pose(getSkeleton()).snapshot());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("pose")) {
            pose.load(compoundTag.getCompound("pose"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put("pose", pose.save());
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
    public @NotNull Supplier<MG08Data> getDataInstance() {
        return () -> new MG08Data(getEntity());
    }

    @Override
    public @NotNull MG08 getEntity() {
        return this;
    }

    @Override
    public @NotNull AABB getBoundingBox() {
        return pose.getEncapsulatingAABB(position());
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && pose.consumeSyncDirty()) {
            entityData.set(SYNCED_POSE, pose.snapshot());
        }
    }
    
}
