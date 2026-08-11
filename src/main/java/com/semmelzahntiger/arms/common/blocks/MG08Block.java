package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.MG08BlockEntity;
import com.semmelzahntiger.arms.common.entities.MG08Gun;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Function;

public class MG08Block extends GunBase<MG08Gun, MG08BlockEntity> {
    public MG08Block(Properties properties) {
        super(properties);
    }

    @Override
    public Class<MG08Gun> getGunEntityClass() {
        return MG08Gun.class;
    }

    @Override
    public BiFunction<BlockPos, BlockState, MG08BlockEntity> getBlockEntityFactory() {
        return (pos, state) -> new MG08BlockEntity(null, pos, state, MG08Gun.class);
    }

    @Override
    public Function<Level, MG08Gun> getEntityFactory() {
        return level -> new MG08Gun(null, level);
    }
}
