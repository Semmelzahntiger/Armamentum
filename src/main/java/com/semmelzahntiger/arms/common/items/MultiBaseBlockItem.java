package com.semmelzahntiger.arms.common.items;

import com.semmelzahntiger.arms.common.blocks.mountablebases.AbstractMultiBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MultiBaseBlockItem extends BlockItem {
    private final AbstractMultiBase base;

    public MultiBaseBlockItem(AbstractMultiBase block, Properties properties) {
        super(block, properties);
        this.base = block;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        BlockPos pos = context.getClickedPos();
        for (Vec3i offset : base.getOffsets()) {
            BlockState offsetState = context.getLevel().getBlockState(pos.offset(offset));
            if(offsetState.getBlock() != Blocks.AIR) {
                return false;
            }
        }
        return super.canPlace(context, state);
    }
}
