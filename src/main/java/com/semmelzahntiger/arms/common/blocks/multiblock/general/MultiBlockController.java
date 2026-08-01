package com.semmelzahntiger.arms.common.blocks.multiblock.general;

import com.semmelzahntiger.arms.core.misc.InstanceEventListener;
import com.semmelzahntiger.arms.core.misc.InstanceListener;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MultiBlockController extends BlockEntity {
    public MultiBlockController(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public abstract boolean validateStructure(Level level);
    protected abstract void destroyStructure();
    public abstract void notifyPartRemoved(BlockPos pos);
}
