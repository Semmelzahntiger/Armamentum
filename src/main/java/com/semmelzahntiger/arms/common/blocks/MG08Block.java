package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.base.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.blockentities.MG08BlockEntity;
import com.semmelzahntiger.arms.common.blocks.base.GunBaseBlock;
import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MG08Block extends GunBaseBlock<MG08> {
    public MG08Block(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull TriFunction<BlockPos, BlockState,List<Vec3i>, BlockEntity> getBlockEntityFactory() {
        return (pos, state, offsets) -> new MG08BlockEntity(pos,state, MG08.class, offsets);
    }
    @Override
    protected BlockState getBlockStateForOffset(Vec3i offset) {
        return null;
    }

    @Override
    public @NotNull BlockEntityType<? extends GunBaseBlockEntity<?>> getCheckedBlockEntityType() {
        return BlockEntityRegister.MG_08_BE.get();
    }

}
