package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.BEMountedEntity;
import com.semmelzahntiger.arms.common.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BaseBlockEntity extends BlockEntity {
    private boolean powered = false;
    private Entity mountedEntity = null;
    public BaseBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegister.BASE_BE.get(), pos, blockState);
    }

    public <E extends Entity & BEMountedEntity> void setMountedEntity(E mountedEntity, Vec3 offsetFromCenter) {
        this.mountedEntity = mountedEntity;
        if(getLevel() == null) {
            return;
        }
        getLevel().addFreshEntity(mountedEntity);
        mountedEntity.setPos(getBlockPos().getCenter().add(mountedEntity.getInitialOffset()).add(offsetFromCenter));
    }
    public void unmountEntity() {
        if(mountedEntity != null) {
            mountedEntity.discard();
        }
        mountedEntity = null;
    }

    public void setEnabled(boolean enabled) {

    }
}
