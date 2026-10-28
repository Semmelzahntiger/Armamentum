package com.semmelzahntiger.arms.core.input;

import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface InputListener {
    UUID getInputListenerUUID();
    default boolean canPlayerAttachInput(Player player) {
        return false;
    }
    void onPlayerUnattach(Player player);
    
}
