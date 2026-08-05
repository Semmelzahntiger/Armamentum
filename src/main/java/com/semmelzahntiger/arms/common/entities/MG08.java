package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.common.blockentities.BaseBlockEntity;
import com.semmelzahntiger.arms.common.items.AmmoItem;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import com.semmelzahntiger.arms.common.register.SoundRegister;
import com.semmelzahntiger.arms.common.skeletons.Skeletons;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.data.MG08Data;
import com.semmelzahntiger.arms.core.obb.OBBEntity;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Supplier;

public class MG08 extends Entity implements CTSEntity<MG08, MG08Data>, OBBEntity, BEMountedGun, GeoAnimatable {
    protected boolean on = false;
    protected BlockPos ownerPos = null;
    protected final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    protected final Pose pose = new Pose(Skeletons.MG08_SKELETON);
    protected final int tickCooldown = 2;
    protected int currentTickCooldown = 0;
    protected @NotNull ItemStack ammoItem = ItemStack.EMPTY;

    public MG08(EntityType<?> type, Level level) {
        super(type, level);
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
    public Pose getOBBPose() {
        return pose;
    }

    @Override
    public Skeleton getSkeleton() {
        return Skeletons.MG08_SKELETON;
    }

    @Override
    public void fire() {
        if(on && currentTickCooldown <= 0) {
            currentTickCooldown = tickCooldown;
            this.playSound(SoundRegister.SHOT.get(), 1.0f, 1.0f);
        }
    }

    @Override
    public void tick() {
        super.tick();
        fire();
        if(currentTickCooldown > 0) {
            currentTickCooldown--;
        }
    }

    @Override
    public @NotNull AABB getBoundingBox() {
        return pose.getEncapsulatingAABB(position());
    }

    @Override
    public void setOwnerPos(BlockPos ownerPos) {
        this.ownerPos = ownerPos;
    }

    @Override
    public void setRotation(Matrix3f rotation) {

    }

    @Override
    public void setState(boolean state) {
        on = state;
    }
}
