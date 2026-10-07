package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.MortarBlockEntityExtension;
import com.semmelzahntiger.arms.common.blockentities.base.GunBaseBlockExtensionBlockEntity;
import com.semmelzahntiger.arms.common.blocks.base.GunBaseBlockExtension;
import com.semmelzahntiger.arms.common.entities.Mortar;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class MortarBlockExtension extends GunBaseBlockExtension<Mortar> {
    public MortarBlockExtension(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BiFunction<BlockPos, BlockState, BlockEntity> getBlockEntityFactory() {
        return MortarBlockEntityExtension::new;
    }

    @Override
    public BlockEntityType<? extends GunBaseBlockExtensionBlockEntity<?>> getCheckedBlockEntityType() {
        return BlockEntityRegister.MORTAR_BE_EXTENSION.get();
    }


}
