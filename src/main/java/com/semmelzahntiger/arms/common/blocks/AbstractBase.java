package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.BaseBlockEntity;
import com.semmelzahntiger.arms.common.items.MountableWeaponItem;
import com.semmelzahntiger.arms.common.items.UnmounterItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBase extends Block implements EntityBlock {
    public static final BooleanProperty POWERED = RedstoneTorchBlock.LIT;

    public AbstractBase(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BaseBlockEntity(blockPos, blockState);
    }
    public BaseBlockEntity getBaseBlockEntity(Level level, BlockPos blockPos) {
        return (BaseBlockEntity) level.getBlockEntity(blockPos);
    }
    protected abstract int getPlatformSize();

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Item item = stack.getItem();
        if(item instanceof MountableWeaponItem<?> mountableWeaponItem && mountableWeaponItem.requiredSize() == getPlatformSize()) {
            BaseBlockEntity blockEntity = getBaseBlockEntity(level, pos);
            if(blockEntity == null) {
                return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
            }
            blockEntity.setMountedEntity(mountableWeaponItem.getNewMountedEntity(level));
            return ItemInteractionResult.SUCCESS;
        }
        else if(item instanceof UnmounterItem) {
            BaseBlockEntity blockEntity = getBaseBlockEntity(level, pos);
            blockEntity.unmountEntity();
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        if(level.hasNeighborSignal(pos)) {
            state.setValue(POWERED, true);
            BaseBlockEntity be = (BaseBlockEntity) level.getBlockEntity(pos);
            if(be != null) {
                be.setEnabled(true);
            }
        }
        else {
            state.setValue(POWERED, false);
            BaseBlockEntity be = (BaseBlockEntity) level.getBlockEntity(pos);
            if(be != null) {
                be.setEnabled(false);
            }
        }
        super.onNeighborChange(state, level, pos, neighbor);
    }

}
