package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.common.TestEntity;
import com.semmelzahntiger.arms.common.blockentities.BaseBlockEntity;
import com.semmelzahntiger.arms.common.entities.MG08;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Arms.MODID
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BaseBlockEntity>> TEST_ENTITY = BLOCK_ENTITY_REGISTER.register(
            "test_entity",
            () -> BlockEntityType.Builder.of(
                    BaseBlockEntity::new,
                    BlockRegister.BASE_BLOCK.get())
                    .build(null)
    );

}
