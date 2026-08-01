package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.BEMountedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseBlockEntity extends BlockEntity {
    private Entity mountedEntity = null;
    public BaseBlockEntity(BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(mountedEntity != null) {
            mountedEntity.discard();
            mountedEntity = null;
        }
    }

    public <E extends Entity & BEMountedEntity> void setMountedEntity(E mountedEntity) {
        this.mountedEntity = mountedEntity;
        if(getLevel() == null) {
            return;
        }
        getLevel().addFreshEntity(mountedEntity);
        mountedEntity.setPos(getBlockPos().getCenter().add(mountedEntity.getInitialOffset()));
    }
    public void unmountEntity() {
        if(mountedEntity != null) {
            mountedEntity.discard();
        }
        mountedEntity = null;
    }
}
