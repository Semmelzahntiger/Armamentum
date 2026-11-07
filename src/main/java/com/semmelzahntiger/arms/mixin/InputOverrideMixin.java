package com.semmelzahntiger.arms.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.semmelzahntiger.arms.client.input.InputPriorityHandler;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyMappingLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(KeyMappingLookup.class)
public class InputOverrideMixin {
    @Inject(method = "getAll(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void arms$filterPrioritized(InputConstants.Key keyCode, boolean releasing, CallbackInfoReturnable<List<KeyMapping>> cir) {
        cir.setReturnValue(InputPriorityHandler.filter(cir.getReturnValue(), releasing));
    }
}
