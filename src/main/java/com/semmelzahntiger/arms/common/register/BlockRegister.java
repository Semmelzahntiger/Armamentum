package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.common.blocks.mountablebases.BaseExtension;
import com.semmelzahntiger.arms.common.blocks.mountablebases.LargeBase;
import com.semmelzahntiger.arms.common.blocks.mountablebases.MediumBase;
import com.semmelzahntiger.arms.common.blocks.mountablebases.SmallBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.createBlocks(Arms.MODID);


    public static final DeferredHolder<Block, SmallBase> BASE_BLOCK = BLOCK_REGISTER.register("small_base", () -> new SmallBase(
            BlockBehaviour.Properties.of().noOcclusion()
    ));
    public static final DeferredHolder<Block, MediumBase> MEDIUM_BASE_BLOCK = BLOCK_REGISTER.register("medium_base", () -> new MediumBase(
            BlockBehaviour.Properties.of().noOcclusion()
    ));
    public static final DeferredHolder<Block, LargeBase> LARGE_BASE_BLOCK = BLOCK_REGISTER.register("large_base", () -> new LargeBase(
            BlockBehaviour.Properties.of().noOcclusion()
    ));
    public static final DeferredHolder<Block, BaseExtension> EXTENSION_BLOCK = BLOCK_REGISTER.register("base_extension", () -> new BaseExtension(
            BlockBehaviour.Properties.of().noOcclusion()
    ));
}
