package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.common.blockentities.BaseBlockEntity;
import com.semmelzahntiger.arms.common.blockentities.BaseBlockEntityExtension;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Arms.MODID
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BaseBlockEntity>> BASE_BE = BLOCK_ENTITY_REGISTER.register(
            "base_block_entity",
            () -> BlockEntityType.Builder.of(
                    BaseBlockEntity::new,
                    BlockRegister.BASE_BLOCK.get())
                    .build(null)
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BaseBlockEntityExtension>> BASE_BE_EXTENSION = BLOCK_ENTITY_REGISTER.register(
            "base_block_entity_extension",
            () -> BlockEntityType.Builder.of(
                    BaseBlockEntityExtension::new,
                    BlockRegister.EXTENSION_BLOCK.get()
            ).build(null)
    );

}
