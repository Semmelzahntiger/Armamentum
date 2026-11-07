package com.semmelzahntiger.arms.mixin;

import com.semmelzahntiger.arms.client.input.InputPriorityHandler;
import com.semmelzahntiger.arms.client.input.InputPriorityProfile;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {
    @Inject(method = "setAll", at = @At("TAIL"))
    private static void arms$suppressAfterSetAll(CallbackInfo ci) {
        InputPriorityProfile profile = InputPriorityHandler.getActiveProfile();
        if (profile != null) {
            InputPriorityHandler.suppressOverlapping(profile);
        }
    }
}
