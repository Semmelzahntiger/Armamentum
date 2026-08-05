package com.semmelzahntiger.arms.common.register;

import com.semmelzahntiger.arms.Arms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundRegister {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Arms.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> SHOT =
            SOUND_EVENTS.register(
                    "shot",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(Arms.MODID, "shot")
                    )
            );
}
