package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class GunBase<G extends Entity & GunEntity, B extends GunBaseBlockEntity<G>> extends Block implements EntityBlock {
    public GunBase(Properties properties) {
        super(properties);
    }

    public abstract Class<G> getGunEntityClass();

    public abstract BiFunction<BlockPos, BlockState, B> getBlockEntityFactory();

    public abstract Function<Level, G> getEntityFactory();

    @Override
    public final @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return getBlockEntityFactory().apply(blockPos, blockState);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        GunBaseBlockEntity<G> gunBase = (GunBaseBlockEntity<G>) level.getBlockEntity(pos);
        if(gunBase == null) {
            return;
        }
        G entity = getEntityFactory().apply(level);
        Vec3 currentPos = Vec3.atLowerCornerOf(pos);
        Vec3 offsetPos = currentPos.add(entity.getOffsetFromController());
        level.addFreshEntity(entity);
        entity.setPos(offsetPos);

        gunBase.setManagedEntity(entity);
    }
}
