package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.common.blockentities.MG08BlockEntity;
import com.semmelzahntiger.arms.common.blockentities.MortarBlockEntity;
import com.semmelzahntiger.arms.common.blockentities.MortarBlockEntityExtension;
import com.semmelzahntiger.arms.common.blocks.base.GunBaseBlock;
import com.semmelzahntiger.arms.common.entities.MG08;
import com.semmelzahntiger.arms.common.entities.Mortar;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Arms.MODID
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MG08BlockEntity>> MG_08_BE = BLOCK_ENTITY_REGISTER.register(
            "mg_08_block_entity",
            () -> BlockEntityType.Builder.of(
                    (pos, state) -> new MG08BlockEntity(pos, state, MG08.class, BlockRegister.MG_08_BLOCK.get().getActiveOffsets(state)),
                    BlockRegister.MG_08_BLOCK.get()
            ).build(null)
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MortarBlockEntity>> MORTAR_BE = BLOCK_ENTITY_REGISTER.register(
            "mortar_block_entity",
            () -> BlockEntityType.Builder.of(
                    (pos,state) -> new MortarBlockEntity(pos, state, Mortar.class, BlockRegister.MORTAR_BLOCK.get().getActiveOffsets(state)),
                    BlockRegister.MORTAR_BLOCK.get()
            ).build(null)
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MortarBlockEntityExtension>> MORTAR_BE_EXTENSION = BLOCK_ENTITY_REGISTER.register(
            "mortar_block_entity_extension",
            () -> BlockEntityType.Builder.of(
                    MortarBlockEntityExtension::new,BlockRegister.MORTAR_BLOCK_EXTENSION.get()
            ).build(null)
    );
}
