package com.semmelzahntiger.arms.common.items;

import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.common.register.EntityRegister;
import net.minecraft.world.level.Level;

public class MG08Item extends MountableWeaponItem<MG08> {
    public MG08Item(Properties properties) {
        super(properties);
    }

    @Override
    public MG08 getNewMountedEntity(Level level) {
        return new MG08(EntityRegister.MG08_ENTITY.get(), level);
    }

    @Override
    public int requiredSize() {
        return 1;
    }
}
