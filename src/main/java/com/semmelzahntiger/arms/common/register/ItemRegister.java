package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.common.items.GunBlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.createItems(Arms.MODID);

    public static final DeferredHolder<Item, GunBlockItem> MG08_ITEM = ITEM_REGISTER.register(
            "mg_08_item",
            () -> new GunBlockItem(BlockRegister.MG_08_BLOCK.get(), new Item.Properties())
    );
    public static final DeferredHolder<Item, GunBlockItem> MORTAR_ITEM = ITEM_REGISTER.register(
            "mortar_item",
            () -> new GunBlockItem(BlockRegister.MORTAR_BLOCK.get(), new Item.Properties())
    );
}
