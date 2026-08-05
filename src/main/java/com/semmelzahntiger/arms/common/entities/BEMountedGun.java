package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.common.items.AmmoItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface BEMountedGun extends BEMountedEntity {
    void fire();
}
