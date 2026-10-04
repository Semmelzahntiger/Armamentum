package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import com.semmelzahntiger.arms.core.obb.Pose;
import com.semmelzahntiger.arms.helper.PoseUtil;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class EntityDataSerializerRegister {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZER_REGISTER = DeferredRegister.create(
            NeoForgeRegistries.ENTITY_DATA_SERIALIZERS,
            Arms.MODID
    );
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Pose.Snapshot>> POSE = ENTITY_DATA_SERIALIZER_REGISTER.register(
            "pose",
            () -> PoseUtil.POSE_SERIALIZER
    );
}
