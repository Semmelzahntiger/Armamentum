package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.MG08BlockEntity;
import com.semmelzahntiger.arms.common.entities.MG08Gun;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MG08Block extends GunBase<MG08Gun, MG08BlockEntity> {
    public MG08Block(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Class<MG08Gun> getGunEntityClass() {
        return MG08Gun.class;
    }

    @Override
    public @NotNull BiFunction<BlockPos, BlockState, MG08BlockEntity> getBlockEntityFactory() {
        return (pos, state) -> new MG08BlockEntity(pos, state, MG08Gun.class, getActiveOffsets(state));
    }

    @Override
    public @Nullable GunBaseExtension<MG08Gun> getExtensionType() {
        return null;
    }

    @Override
    public @NotNull Function<Level, MG08Gun> getEntityFactory() {
        return level -> new MG08Gun(null, level);
    }
}
