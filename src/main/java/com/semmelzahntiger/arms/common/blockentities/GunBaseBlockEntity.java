package com.semmelzahntiger.arms.common.blockentities;

import com.semmelzahntiger.arms.common.entities.GunEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GunBaseBlockEntity<T extends Entity & GunEntity> extends BlockEntity {
    protected boolean pendingLink = false;
    protected final Class<T> entityClass;
    protected T entity;

    public GunBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, Class<T> entityClass) {
        super(type, pos, blockState);
        this.entityClass = entityClass;
    }
    public Class<T> getEntityClass() {
        return entityClass;
    }
    public void setManagedEntity(T entity) {
        this.entity = entity;
    }
    public T getManagedEntity() {
        return entity;
    }
    public void onBlockRemove() {
        T entity = getManagedEntity();
        if(entity != null) {
            entity.discard();
            setManagedEntity(null);
        }
    }


}
