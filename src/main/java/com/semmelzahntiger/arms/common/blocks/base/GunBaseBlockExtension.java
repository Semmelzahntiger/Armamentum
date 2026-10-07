package com.semmelzahntiger.arms.common.blocks.base;

import com.semmelzahntiger.arms.common.blockentities.base.GunBaseBlockExtensionBlockEntity;
import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public abstract class GunBaseBlockExtension<G extends Entity & GunEntity> extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public GunBaseBlockExtension(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return getBlockEntityFactory().apply(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public abstract @NotNull BiFunction<BlockPos,BlockState,BlockEntity> getBlockEntityFactory();

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        GunBaseBlockExtensionBlockEntity<G> be = getBlockEntity(level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
        if(be != null) {
            be.onBreak();
        }
    }
    @SuppressWarnings("unchecked")
    private static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker
    ) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, getCheckedBlockEntityType(), GunBaseBlockExtensionBlockEntity::tick);
    }
    public abstract BlockEntityType<? extends GunBaseBlockExtensionBlockEntity<?>> getCheckedBlockEntityType();
    @SuppressWarnings("unchecked")
    public GunBaseBlockExtensionBlockEntity<G> getBlockEntity(Level level, BlockPos pos) {
        return (GunBaseBlockExtensionBlockEntity<G>) level.getBlockEntity(pos);
    }
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

}
