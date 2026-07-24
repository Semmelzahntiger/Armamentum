package com.semmelzahntiger.arms.core.cts;

import net.minecraft.world.entity.Entity;

public abstract class CTSData<E extends Entity> {
    private final E entity;
    public CTSData(E entity) {
        this.entity = entity;
    }

    public E getEntity() {
        return entity;
    }
    public void updateData() {

    }
}
