package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.common.entities.MG08Gun;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MG08BlockEntity extends GunBaseBlockEntity<MG08Gun> {
    public MG08BlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<MG08Gun> entityClass, List<Vec3i> offsets) {
        super(type, pos, blockState, entityClass, offsets);
    }
}
