package com.semmelzahntiger.arms.common.items;

import com.semmelzahntiger.arms.common.blocks.base.GunBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;

import java.util.List;

public class GunBlockItem extends BlockItem {
    protected final GunBaseBlock<?> gunBaseBlock;
    public GunBlockItem(GunBaseBlock<?> block, Properties properties) {
        super(block, properties);
        this.gunBaseBlock = block;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Direction dir = context.getHorizontalDirection();
        List<Vec3i> offsets = gunBaseBlock.getOffsetsFromDirection(dir);
        BlockPos pos = context.getClickedPos();
        for (Vec3i offset : offsets) {
            BlockPos offsetPos = pos.offset(offset);
            if(!context.getLevel().getBlockState(offsetPos).isAir()) {
                return InteractionResult.FAIL;
            }
        }
        return super.place(context);
    }
}
