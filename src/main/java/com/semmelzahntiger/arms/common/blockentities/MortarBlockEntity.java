package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.blockentities.base.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.entities.Mortar;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MortarBlockEntity extends GunBaseBlockEntity<Mortar> {
    public MortarBlockEntity(BlockPos pos, BlockState blockState, Class<Mortar> entityClass, List<Vec3i> offsets) {
        super(BlockEntityRegister.MORTAR_BE.get(), pos, blockState, entityClass, offsets);
    }

    @Override
    protected Mortar createGunEntity() {
        return new Mortar(EntityRegister.MORTAR.get(), getLevel());
    }

    @Override
    protected void initializeHook(Level level, BlockPos blockPos, BlockState blockState) {

    }
}
