package com.semmelzahntiger.arms.common.items;

import com.semmelzahntiger.arms.common.entities.MG08Dual;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import net.minecraft.world.level.Level;

public class MG08DualItem extends MountableWeaponItem<MG08Dual> {
    public MG08DualItem(Properties properties) {
        super(properties);
    }

    @Override
    public MG08Dual getNewMountedEntity(Level level) {
        return new MG08Dual(EntityRegister.MG08DUAL_ENTITY.get(), level);
    }

    @Override
    public int requiredSize() {
        return 1;
    }
}
