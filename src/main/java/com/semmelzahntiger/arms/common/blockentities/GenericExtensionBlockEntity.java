package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GenericExtensionBlockEntity<T extends Entity & GunEntity> extends GunBaseExtensionBlockEntity<T> {
    public GenericExtensionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<T> gunType) {
        super(type, pos, blockState);
    }
}
