package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.MG08BlockEntity;
import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MG08Block extends GunBaseBlock<MG08, MG08BlockEntity> {
    public MG08Block(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Class<MG08> getGunEntityClass() {
        return MG08.class;
    }

    @Override
    public @NotNull BiFunction<BlockPos, BlockState, MG08BlockEntity> getBlockEntityFactory() {
        return (pos, state) -> new MG08BlockEntity(pos, state, MG08.class, getActiveOffsets(state));
    }

    @Override
    public @Nullable GunBaseBlockExtension<MG08> getExtensionType() {
        return null;
    }

    @Override
    public @NotNull Function<Level, MG08> getEntityFactory() {
        return level -> new MG08(EntityRegister.MG08_GUN.get(), level);
    }
}
