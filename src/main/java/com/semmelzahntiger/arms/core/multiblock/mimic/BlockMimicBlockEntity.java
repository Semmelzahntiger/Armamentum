package com.semmelzahntiger.arms.core.multiblock.mimic;

import com.semmelzahntiger.arms.core.multiblock.general.MultiBlockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
public class BlockMimicBlockEntity extends BlockEntity {
    private BlockState mimicBlock = Blocks.AIR.defaultBlockState();
    private Vec3i controllerOffset = Vec3i.ZERO;
    public BlockMimicBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void setMimickedBlock(Block block) {
        if(block == null) {
            mimicBlock = Blocks.AIR.defaultBlockState();
            return;
        }
        this.mimicBlock = block.defaultBlockState();
    }
    public @NotNull BlockState getMimickedBlockState() {
        return this.mimicBlock;
    }
    public MutableComponent getMimickedBlockName() {
        return mimicBlock.getBlock().getName();
    }
    public Vec3i getControllerOffset() {
        return controllerOffset;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if(!tag.contains("id")) {
            return;
        }
        mimicBlock = Block.stateById(tag.getInt("id"));
        int x = tag.getInt("x");
        int y = tag.getInt("y");
        int z = tag.getInt("z");
        controllerOffset = new Vec3i(x, y, z);
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        int id = Block.getId(mimicBlock);
        tag.putInt("id", id);
        tag.putInt("x", controllerOffset.getX());
        tag.putInt("y", controllerOffset.getY());
        tag.putInt("z", controllerOffset.getZ());
        super.saveAdditional(tag, registries);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        Level level = getLevel();
        if(level == null) {
            return;
        }
        BlockEntity controller = level.getBlockEntity(getBlockPos().offset(controllerOffset));
        if(controller instanceof MultiBlockController mbController) {
            mbController.notifyPartRemoved(getBlockPos());
        }
    }
}
