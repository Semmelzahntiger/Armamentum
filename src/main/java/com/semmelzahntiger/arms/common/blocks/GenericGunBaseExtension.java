package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.GenericExtensionBlockEntity;
import com.semmelzahntiger.arms.common.blockentities.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GenericGunBaseExtension<G extends Entity & GunEntity> extends GunBaseExtension<G>{
    protected final Class<G> gunType;
    public GenericGunBaseExtension(Properties properties, Class<G> gunType) {
        super(properties);
        this.gunType = gunType;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GenericExtensionBlockEntity<>(null, blockPos, blockState, gunType);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
