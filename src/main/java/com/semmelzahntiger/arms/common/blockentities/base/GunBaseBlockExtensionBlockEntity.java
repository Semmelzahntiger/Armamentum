package com.semmelzahntiger.arms.common.blockentities.base;

import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class GunBaseBlockExtensionBlockEntity<T extends Entity & GunEntity> extends BlockEntity {
    protected BlockPos linkedBlockPos = null;
    protected GunBaseBlockEntity<T> linkedBlockEntity = null;

    public GunBaseBlockExtensionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
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
    public static void tick(Level level, BlockPos pos, BlockState state, GunBaseBlockExtensionBlockEntity<?> blockEntity) {
        blockEntity.tick(level, pos, state);
    }
    public void tick(Level level, BlockPos pos, BlockState state) {

    }
    public void onBreak() {
        Level level = getLevel();
        if(level != null && linkedBlockPos != null) {
            level.destroyBlock(linkedBlockPos, true);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(linkedBlockPos != null) {
            tag.putIntArray("link_pos", new int[] {linkedBlockPos.getX(), linkedBlockPos.getY(), linkedBlockPos.getZ()});
        }
    }
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("link_pos")) {
            int[] pos = tag.getIntArray("link_pos");
            linkedBlockPos = new BlockPos(pos[0], pos[1], pos[2]);
        }
    }
}
