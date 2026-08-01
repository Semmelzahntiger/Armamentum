package com.semmelzahntiger.arms.common.entities;

import com.semmelzahntiger.arms.common.items.AmmoItem;
import net.minecraft.world.item.ItemStack;

public interface BEMountedGun extends BEMountedEntity {
    void fire();
    boolean validAmmo(AmmoItem ammo);
    void setAmmoItem(ItemStack ammoItemStack);
    void toggleAutoFire(boolean autoFire);
}
