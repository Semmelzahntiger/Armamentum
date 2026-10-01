package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.MG08;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MG08BlockEntityExtension extends GunBaseExtensionBlockEntity<MG08> {
    public MG08BlockEntityExtension(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
