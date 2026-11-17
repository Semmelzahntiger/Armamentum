package com.semmelzahntiger.arms.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.semmelzahntiger.arms.core.simulate.chunks.ChunkBlockHeightCache;
import com.semmelzahntiger.arms.core.simulate.chunks.ChunkMaxHeightCache;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

import java.util.Arrays;
import java.util.Collection;

public class PrintBlockHeightCache implements Command {
    @Override
    public String getCommandName() {
        return "printblockheightcache";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> buildCommand(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    Collection<ChunkBlockHeightCache> caches = ChunkBlockHeightCache.getCaches();
                    if(caches.isEmpty()) {
                        ServerPlayer player = (ServerPlayer) ctx.getSource().getPlayer();
                        if(player != null) {
                            player.sendSystemMessage(Component.literal("No caches present."));
                        }
                    }
                    ServerPlayer player = (ServerPlayer) ctx.getSource().getPlayer();
                    if(player != null) {
                        player.sendSystemMessage(Component.literal("Printing Caches..."));
                    }
                    caches.forEach(chunkMaxHeightCache -> {
                        chunkMaxHeightCache.getCacheEntries().forEach(entry -> {
                            CommandSourceStack stack = ctx.getSource();
                            if(player != null) {
                                player.sendSystemMessage(Component.literal("ChunkPos: " + entry.getKey() + ": " + Arrays.toString(entry.getValue())));
                            }
                        });
                    });
                    return 1;
                });
    }
}
