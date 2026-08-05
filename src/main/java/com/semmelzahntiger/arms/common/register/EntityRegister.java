package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.common.TestEntity;
import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.common.entities.MG08Dual;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_REGISTER = DeferredRegister.create(
            BuiltInRegistries.ENTITY_TYPE,
            Arms.MODID
    );
    public static final DeferredHolder<EntityType<?>, EntityType<TestEntity>> TEST_ENTITY = ENTITY_REGISTER.register(
            "test_entity",
            () -> EntityType.Builder.of(TestEntity::new, MobCategory.MISC)
                    .eyeHeight(0.0f)
                    .build("test_entity")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<MG08>> MG08_ENTITY = ENTITY_REGISTER.register(
            "mg_08",
            () -> EntityType.Builder.of(MG08::new, MobCategory.MISC)
                    .build("mg_08")
    );
    public static final DeferredHolder<EntityType<?>, EntityType<MG08Dual>> MG08DUAL_ENTITY = ENTITY_REGISTER.register(
            "mg_08_dual",
            () -> EntityType.Builder.of(MG08Dual::new, MobCategory.MISC)
                    .build("mg_08_dual")
    );
}
