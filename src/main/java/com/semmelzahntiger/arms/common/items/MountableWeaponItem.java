package com.semmelzahntiger.arms.common.items;

import com.semmelzahntiger.arms.common.entities.BEMountedEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public abstract class MountableWeaponItem<E extends Entity & BEMountedEntity> extends Item {
    public MountableWeaponItem(Properties properties) {
        super(properties);
    }

    public abstract E getNewMountedEntity(Level level);
    public abstract int requiredSize();
}
