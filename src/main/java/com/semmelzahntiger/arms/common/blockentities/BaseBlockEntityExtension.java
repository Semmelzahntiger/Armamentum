package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.blocks.AbstractBase;
import com.semmelzahntiger.arms.common.blocks.AbstractMultiBase;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BaseBlockEntityExtension extends BlockEntity {
    private Vec3i ownerBlockPosOffset = null;
    public BaseBlockEntityExtension(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegister.BASE_BE_EXTENSION.get(), pos, blockState);
    }
    public void setOwnerPosOffset(Vec3i pos) {
        this.ownerBlockPosOffset = pos;
    }
    public Vec3i getOwnerPosOffset() {
        return ownerBlockPosOffset;
    }
    public ItemInteractionResult delegateInteraction(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockState bs = level.getBlockState(pos);
        if(bs.getBlock() instanceof AbstractBase base) {
            return base.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    public void setRemoved() {
        Level level = getLevel();
        if(level != null) {
            BlockState state = level.getBlockState(getBlockPos().offset(ownerBlockPosOffset));
            if(state.getBlock() instanceof AbstractMultiBase base) {
                level.removeBlock(getBlockPos().offset(ownerBlockPosOffset), false);
            }
        }
        super.setRemoved();
    }
}
