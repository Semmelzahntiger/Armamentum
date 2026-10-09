package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.MortarBlockEntity;
import com.semmelzahntiger.arms.common.blockentities.base.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.blocks.base.GunBaseBlock;
import com.semmelzahntiger.arms.common.entities.Mortar;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import com.semmelzahntiger.arms.common.register.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MortarBlock extends GunBaseBlock<Mortar> {
    public MortarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull TriFunction<BlockPos, BlockState, List<Vec3i>, BlockEntity> getBlockEntityFactory() {
        return (pos, state, offsets) -> new MortarBlockEntity(pos, state, Mortar.class, offsets);
    }

    @Override
    protected BlockState getBlockStateForOffset( Vec3i offset) {
        return BlockRegister.MORTAR_BLOCK_EXTENSION.get().defaultBlockState();
    }

    @Override
    public @NotNull BlockEntityType<? extends GunBaseBlockEntity<?>> getCheckedBlockEntityType() {
        return BlockEntityRegister.MORTAR_BE.get();
    }

    @Override
    public int getPlatformLength() {
        return 3;
    }

    @Override
    public int getPlatformWidth() {
        return 3;
    }

}
