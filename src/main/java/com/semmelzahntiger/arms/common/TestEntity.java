package com.semmelzahntiger.arms.common;

import com.semmelzahntiger.arms.common.skeletons.Skeletons;
import com.semmelzahntiger.arms.core.cts.CTSEntity;
import com.semmelzahntiger.arms.core.cts.TestCTSData;
import com.semmelzahntiger.arms.core.misc.InstanceEventDistributor;
import com.semmelzahntiger.arms.core.misc.InstanceEventListener;
import com.semmelzahntiger.arms.core.misc.InstanceListener;
import com.semmelzahntiger.arms.core.obb.BoneId;
import com.semmelzahntiger.arms.core.obb.OBBEntity;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.core.obb.Skeleton;
import com.semmelzahntiger.arms.helper.MatrixHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@InstanceEventListener(events =
        {
                BlockEvent.PortalSpawnEvent.class,
                ServerTickEvent.Pre.class
        })
public class TestEntity extends Entity implements OBBEntity, CTSEntity<TestEntity, TestCTSData>, InstanceListener {
    private final Pose pose = new Pose(getSkeleton());

    public TestEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        InstanceEventDistributor.register(this);
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
    public void remove(@NotNull RemovalReason reason) {
        super.remove(reason);
        InstanceEventDistributor.unregister(this);
    }


    @Override
    public void tick() {
        super.tick();
//        getOBBPose().addRotation(new BoneId("main"), MatrixHelper.getRotationMatrix(2, 2,2));
//        getOBBPose().addRotation(new BoneId("ChildRotated"), MatrixHelper.getRotationMatrix(0, 6,0));
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
    public AABB getBoundingBox() {
        return pose.getEncapsulatingAABB(position());
    }

    @Override
    public Pose getOBBPose() {
        return pose;
    }

    @Override
    public Skeleton getSkeleton() {
        return Skeletons.TEST_SKELETON;
    }


}
