package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.common.blocks.MG08Block;
import com.semmelzahntiger.arms.common.blocks.MortarBlock;
import com.semmelzahntiger.arms.common.blocks.MortarBlockExtension;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.createBlocks(Arms.MODID);

    public static final DeferredHolder<Block, MG08Block> MG_08_BLOCK = BLOCK_REGISTER.register("mg_08", () -> new MG08Block(
            BlockBehaviour.Properties.of().noOcclusion()
    ));
    public static final DeferredHolder<Block, MortarBlock> MORTAR_BLOCK = BLOCK_REGISTER.register("mortar", () -> new MortarBlock(
            BlockBehaviour.Properties.of().noOcclusion()
    ));
    public static final DeferredHolder<Block, MortarBlockExtension> MORTAR_BLOCK_EXTENSION = BLOCK_REGISTER.register("mortar_extension", () -> new MortarBlockExtension(
            BlockBehaviour.Properties.of().noOcclusion()
    ));
}
