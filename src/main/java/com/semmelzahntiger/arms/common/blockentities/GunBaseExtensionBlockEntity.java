package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GunBaseExtensionBlockEntity<T extends Entity & GunEntity> extends BlockEntity {
    protected boolean pendingLink = false;
    protected BlockPos linkedBlockPos = null;
    protected GunBaseBlockEntity<T> linkedBlockEntity = null;

    public GunBaseExtensionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    public void setLinkedBlockEntity(GunBaseBlockEntity<T> linkedBlockEntity) {
        this.linkedBlockEntity = linkedBlockEntity;
        linkedBlockPos = linkedBlockEntity.getBlockPos();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }

}
