package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.blockentities.MG08BlockEntity;
import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MG08Block extends GunBaseBlock<MG08> {
    public MG08Block(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BiFunction<BlockPos, BlockState, BlockEntity> getBlockEntityFactory() {
        return null;
    }

    @Override
    public @NotNull Function<Level, MG08> getEntityFactory() {
        return null;
    }

    @Override
    protected BlockState getBlockStateForOffset(Direction direction, Vec3i offset) {
        return null;
    }

    @Override
    public @NotNull BlockEntityType<GunBaseBlockEntity<?>> getCheckedBlockEntityType() {
        return null;
    }

}
