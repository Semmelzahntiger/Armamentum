package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class GunBaseExtensionBlockEntity<T extends Entity & GunEntity> extends BlockEntity {
    protected BlockPos linkedBlockPos = null;
    protected GunBaseBlockEntity<T> linkedBlockEntity = null;

    public GunBaseExtensionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
    public @Nullable GunBaseBlockEntity<T> getLinkedBlockEntity() {
        return linkedBlockEntity;
    }
    public @Nullable BlockPos getLinkedBlockPos() {
        return linkedBlockPos;
    }
    public @Nullable T getLinkedEntity() {
        if (linkedBlockEntity != null) {
            return getLinkedBlockEntity().getManagedEntity();
        }
        return null;
    }
    public void setLinkedBlockEntity(GunBaseBlockEntity<T> linkedBlockEntity) {
        this.linkedBlockEntity = linkedBlockEntity;
        setLinkedBlockPos(linkedBlockEntity.getBlockPos());
    }
    public void setLinkedBlockPos(BlockPos linkedBlockPos) {
        this.linkedBlockPos = linkedBlockPos;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GunBaseExtensionBlockEntity<?> blockEntity) {
        blockEntity.tick(level, pos, state);
    }
    public void tick(Level level, BlockPos pos, BlockState state) {

    }
}
