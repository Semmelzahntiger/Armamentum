package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.common.items.AmmoItem;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import com.semmelzahntiger.arms.common.skeletons.Skeletons;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.data.MG08Data;
import com.semmelzahntiger.arms.core.obb.OBBEntity;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Supplier;

public class MG08 extends Entity implements CTSEntity<MG08, MG08Data>, OBBEntity, BEMountedGun, GeoAnimatable {
    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Pose pose = new Pose(Skeletons.MG08_SKELETON);

    public MG08(Level level) {
        super(EntityRegister.MG08_ENTITY.get(), level);
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


    }

    @Override
    public boolean validAmmo(AmmoItem ammo) {
        return false;
    }

    @Override
    public void setAmmoItem(ItemStack ammoItemStack) {

    }

    @Override
    public void toggleAutoFire(boolean fire) {

    }
}
