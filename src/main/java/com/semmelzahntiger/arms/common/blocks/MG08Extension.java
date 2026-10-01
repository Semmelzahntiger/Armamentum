package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.MG08BlockEntityExtension;
import com.semmelzahntiger.arms.common.entities.MG08;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class MG08Extension extends GunBaseBlockExtension<MG08, MG08BlockEntityExtension> {
    public MG08Extension(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BiFunction<BlockPos, BlockState, MG08BlockEntityExtension> getBlockEntityFactory() {
        return null;
    }

    @Override
    public BlockEntityType<MG08BlockEntityExtension> getCheckedBlockEntityType() {
        return null;
    }
}
