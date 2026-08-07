package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.BaseBlockEntityExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import util.LogUtil;

public class BaseExtension extends Block implements EntityBlock {
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BaseBlockEntityExtension(blockPos, blockState);
    }

    public BaseExtension(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BaseBlockEntityExtension extension = (BaseBlockEntityExtension) level.getBlockEntity(pos);
        if(extension instanceof BaseBlockEntityExtension) {
            return extension.delegateInteraction(stack, state, level, pos, player, hand, hitResult);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        BaseBlockEntityExtension extension = (BaseBlockEntityExtension) level.getBlockEntity(pos);
        if(extension instanceof BaseBlockEntityExtension ext) {
            BlockPos ownerPos = pos.offset(ext.getOwnerPosOffset());
            if(level.getBlockState(ownerPos).getBlock() instanceof AbstractMultiBase) {
                level.removeBlock(ownerPos, false);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
