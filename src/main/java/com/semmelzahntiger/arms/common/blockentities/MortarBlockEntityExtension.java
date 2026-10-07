package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.blockentities.base.GunBaseBlockExtensionBlockEntity;
import com.semmelzahntiger.arms.common.entities.Mortar;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MortarBlockEntityExtension extends GunBaseBlockExtensionBlockEntity<Mortar> {

    public MortarBlockEntityExtension(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegister.MORTAR_BE_EXTENSION.get(), pos, blockState);
    }
}
