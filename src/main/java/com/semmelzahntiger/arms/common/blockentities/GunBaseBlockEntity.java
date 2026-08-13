package com.semmelzahntiger.arms.common.blockentities;

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
import util.LogUtil;

import java.util.List;
import java.util.UUID;

public abstract class GunBaseBlockEntity<T extends Entity & GunEntity> extends BlockEntity {
    protected final List<Vec3i> offsets;
    protected boolean pendingLink = false;
    protected UUID pendingLinkUUID  = null;
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

    public void onBlockRemove() {
        T entity = getManagedEntity();
        if(entity != null) {
            entity.discard();
            setManagedEntity(null);
        }
    }
    public static void tick(Level level, BlockPos pos, BlockState state, GunBaseBlockEntity<?> blockEntity) {
        blockEntity.tick(level, pos, state);
    }

    protected void tick(Level level, BlockPos blockPos, BlockState blockState) {
        restore(level, blockPos, blockState);
        checkIfEntityValid(level, blockPos, blockState);
    }
    protected final void checkIfEntityValid(Level level, BlockPos blockPos, BlockState blockState) {
        if(getManagedEntity() != null && getManagedEntity().isRemoved()) {
            level.removeBlockEntity(blockPos);
        }
    }
    @SuppressWarnings("unchecked")
    protected final void restore(Level level, BlockPos blockPos, BlockState blockState) {
        if(pendingLink && level instanceof ServerLevel serverLevel) {
            T detectedEntity = (T) serverLevel.getEntity(pendingLinkUUID);
            if(detectedEntity == null) {
                level.removeBlock(blockPos, false);
                return;
            }
            setManagedEntity(detectedEntity);
            pendingLink = false;
            pendingLinkUUID = null;
        }
        if(pendingMultiBlockLink) {
            for (Vec3i offset : offsets) {
                BlockPos offsetPos = blockPos.offset(offset);
                try {
                    GunBaseExtensionBlockEntity<T> extEntity = (GunBaseExtensionBlockEntity<T>) level.getBlockEntity(offsetPos);
                    if(extEntity == null) {
                        level.removeBlock(offsetPos, false);
                        return;
                    }
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
        if(managedEntity != null) {
            tag.putBoolean("link", true);
            tag.putUUID("linked_entity",getManagedEntity().getUUID());
        }
        tag.putBoolean("has_offsets", offsets != null && !offsets.isEmpty());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("link")) {
            pendingLink = tag.getBoolean("link");
            pendingLinkUUID = tag.getUUID("linked_entity");
        }
        if(tag.contains("has_offsets")) {
            pendingMultiBlockLink = tag.getBoolean("has_offsets");
        }
    }
}
