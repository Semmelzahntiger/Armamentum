package com.semmelzahntiger.arms.common.blocks;

import com.semmelzahntiger.arms.common.blockentities.GunBaseBlockEntity;
import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class GunBaseExtension<G extends Entity & GunEntity, B extends GunBaseBlockEntity<G>> extends Block implements EntityBlock {
    public GunBaseExtension(Properties properties) {
        super(properties);
    }

}
