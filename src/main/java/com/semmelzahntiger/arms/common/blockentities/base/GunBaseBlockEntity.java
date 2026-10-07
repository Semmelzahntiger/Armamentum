package com.semmelzahntiger.arms.common.blockentities.base;

import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import util.LogUtil;

import java.util.List;
import java.util.UUID;

public abstract class GunBaseBlockEntity<T extends Entity & GunEntity> extends BlockEntity {
    protected static final int LINK_TIMEOUT_TICKS = 10;
    protected boolean initialized = false;
    protected final List<Vec3i> offsets;
    protected boolean pendingLink = false;
    protected UUID pendingLinkUUID  = null;
    protected int pendingLinkTicks = 0;
    protected boolean pendingMultiBlockLink = false;
    protected final Class<T> entityClass;
    protected T managedEntity;

    public GunBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<T> entityClass, List<Vec3i> offsets) {
        super(type, pos, blockState);
        this.entityClass = entityClass;
        this.offsets = offsets;
    }
    public Class<T> getEntityClass() {
        return entityClass;
    }
    public void setManagedEntity(T entity) {
        this.managedEntity = entity;
    }
    public T getManagedEntity() {
        return managedEntity;
    }

    public void onBreak() {
        T entity = getManagedEntity();
        if(entity != null) {
            entity.discard();
            setManagedEntity(null);
        }
    }
    public static void tick(Level level, BlockPos pos, BlockState state, GunBaseBlockEntity<?> blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    protected abstract T createGunEntity();

    protected Vec3 getEntityPlacementOffset() {
        return Vec3.ZERO;
    }

    protected void tick(Level level, BlockPos blockPos, BlockState blockState) {
        initialize(level, blockPos, blockState);
        restore(level, blockPos, blockState);
        checkIfEntityValid(level, blockPos, blockState);
    }
    protected void initialize(Level level, BlockPos blockPos, BlockState blockState) {
        if(!initialized && !level.isClientSide) {
            T gunEntity = createGunEntity();
            gunEntity.setPos(blockPos.getBottomCenter().add(getEntityPlacementOffset()));
            level.addFreshEntity(gunEntity);
            setManagedEntity(gunEntity);
            for (Vec3i offset : offsets) {
                BlockPos partPos = getBlockPos().offset(offset);
                BlockEntity blockEntity = getLevel().getBlockEntity(partPos);
                GunBaseBlockExtensionBlockEntity<T> extBe = (GunBaseBlockExtensionBlockEntity<T>) blockEntity;
                extBe.setLinkedBlockEntity(this);
            }
            initializeHook(level, blockPos, blockState);
            initialized = true;
        }
    }
    protected abstract void initializeHook(Level level, BlockPos blockPos, BlockState blockState);
    protected final void checkIfEntityValid(Level level, BlockPos blockPos, BlockState blockState) {
        T entity = getManagedEntity();
        if(entity == null || !entity.isRemoved()) {
            return;
        }
        Entity.RemovalReason reason = entity.getRemovalReason();
        if(reason != null && reason.shouldDestroy()) {
            level.removeBlockEntity(blockPos);
        }
        else {
            pendingLink = true;
            pendingLinkUUID = entity.getUUID();
            pendingLinkTicks = 0;
            setManagedEntity(null);
        }
    }
    @SuppressWarnings("unchecked")
    protected void restore(Level level, BlockPos blockPos, BlockState blockState) {
        if(pendingLink && level instanceof ServerLevel serverLevel) {
            Entity detectedEntity = serverLevel.getEntity(pendingLinkUUID);
            if(entityClass.isInstance(detectedEntity)) {
                setManagedEntity(entityClass.cast(detectedEntity));
                pendingLink = false;
                pendingLinkUUID = null;
                pendingLinkTicks = 0;
            }
            else if(++pendingLinkTicks >= LINK_TIMEOUT_TICKS) {
                LogUtil.getLogger().warn("Linked entity {} of gun at {} did not load within {} ticks, destroying structure.",
                        pendingLinkUUID, blockPos, LINK_TIMEOUT_TICKS);
                pendingLink = false;
                pendingLinkUUID = null;
                pendingLinkTicks = 0;
                level.destroyBlock(blockPos, true);
                return;
            }
        }
        if(pendingMultiBlockLink) {
            for (Vec3i offset : offsets) {
                BlockPos offsetPos = blockPos.offset(offset);
                try {
                    GunBaseBlockExtensionBlockEntity<T> extEntity = (GunBaseBlockExtensionBlockEntity<T>) level.getBlockEntity(offsetPos);
                    extEntity.setLinkedBlockEntity(this);
                } catch (ClassCastException e) {
                    LogUtil.getLogger().error("BlockEntity at offset was unexpectedly of another type.", e);
                }
            }
            pendingMultiBlockLink = false;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("initialized", initialized);
        UUID linkedUUID = managedEntity != null ? managedEntity.getUUID() : pendingLinkUUID;
        if(linkedUUID != null) {
            tag.putUUID("linked_entity", linkedUUID);
        }
        tag.putBoolean("has_offsets", offsets != null && !offsets.isEmpty());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        initialized = tag.getBoolean("initialized");
        if(tag.hasUUID("linked_entity")) {
            pendingLink = true;
            pendingLinkUUID = tag.getUUID("linked_entity");
        }
        if(tag.contains("has_offsets")) {
            pendingMultiBlockLink = tag.getBoolean("has_offsets");
        }
    }
}
