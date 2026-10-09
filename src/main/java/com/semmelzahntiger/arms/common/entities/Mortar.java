package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.common.skeletons.Skeletons;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.data.MortarData;
import com.semmelzahntiger.arms.core.obb.OBBEntity;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import com.semmelzahntiger.arms.helper.PoseUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Supplier;

public class Mortar extends Entity implements GunEntity, GeoAnimatable, OBBEntity, CTSEntity<Mortar, MortarData> {
    protected final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    protected final Pose pose = new Pose(getSkeleton());

    private static final EntityDataAccessor<Pose.Snapshot> SYNCED_POSE =
            SynchedEntityData.defineId(Mortar.class, PoseUtil.POSE_SERIALIZER);

    public Mortar(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Vec3 getOffsetFromControllerCenter() {
        return Vec3.ZERO;
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
    public @NotNull Supplier<MortarData> getDataInstance() {
        return () -> new MortarData(getEntity());
    }

    @Override
    public @NotNull Mortar getEntity() {
        return this;
    }

    @Override
    public Pose getOBBPose() {
        return pose;
    }

    @Override
    public Skeleton getSkeleton() {
        return Skeletons.DEFAULT_BOX_SKELETON;
    }

    @Override
    public @NotNull AABB getBoundingBox() {
        return getOBBPose().getEncapsulatingAABB(position());
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
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        return interactPart(position(), player, hand);
    }
}
