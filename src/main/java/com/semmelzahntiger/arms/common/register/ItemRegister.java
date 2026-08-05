package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.common.items.BaseBlockItem;
import com.semmelzahntiger.arms.common.items.MG08DualItem;
import com.semmelzahntiger.arms.common.items.MG08Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.createItems(Arms.MODID);

    public static final DeferredHolder<Item, BaseBlockItem> BASE_BLOCK_ITEM = ITEM_REGISTER.register(
      "small_base",
            () -> new BaseBlockItem(BlockRegister.BASE_BLOCK.get(), new Item.Properties())
    );
    public static final DeferredHolder<Item, MG08Item> MG_08_ITEM = ITEM_REGISTER.register(
            "mg_08",
            () -> new MG08Item(new Item.Properties())
    );
    public static final DeferredHolder<Item, MG08DualItem> MG_08_DUAL_ITEM = ITEM_REGISTER.register(
            "mg_08_dual",
            () -> new MG08DualItem(new Item.Properties())
    );
}
