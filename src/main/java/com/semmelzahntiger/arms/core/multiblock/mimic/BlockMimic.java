package com.semmelzahntiger.arms.core.multiblock.mimic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BlockMimic extends Block implements EntityBlock {
    public BlockMimic(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        BlockEntity mimic = level.getBlockEntity(pos);
        BlockMimicBlockEntity mimicBlockEntity = (BlockMimicBlockEntity) mimic;
        if(mimicBlockEntity == null) {
            return super.getCloneItemStack(state, target, level, pos, player);
        }
        BlockState mimickedState = mimicBlockEntity.getMimickedBlockState();
        return super.getCloneItemStack(mimickedState, target, level, pos, player);
    }



    @Override
    public final @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return getMimicBlockEntity(blockPos, blockState);
    }
    public abstract BlockMimicBlockEntity getMimicBlockEntity(BlockPos blockPos, BlockState blockState);

}
