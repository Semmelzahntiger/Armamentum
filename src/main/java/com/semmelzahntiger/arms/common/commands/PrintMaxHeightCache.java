package com.semmelzahntiger.arms.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.semmelzahntiger.arms.core.simulate.chunks.ChunkMaxHeightCache;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

import java.util.Collection;

public class PrintMaxHeightCache implements Command {

    @Override
    public String getCommandName() {
        return "printmaxheightcache";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> buildCommand(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder.requires(source -> source.hasPermission(2))
                .executes(ctx -> {
                    Collection<ChunkMaxHeightCache> caches = ChunkMaxHeightCache.getCaches();
                    if(caches.isEmpty()) {
                        ServerPlayer player = (ServerPlayer) ctx.getSource().getPlayer();
                        if(player != null) {
                            player.sendSystemMessage(Component.literal("No caches present."));
                        }
                    }
                    caches.forEach(chunkMaxHeightCache -> {
                        chunkMaxHeightCache.getSectionHeights().forEach((longPos, height) -> {
                            CommandSourceStack stack = ctx.getSource();
                            ServerPlayer player = stack.getPlayer();
                            if(player != null) {
                                player.sendSystemMessage(Component.literal("ChunkPos: " + new ChunkPos(longPos) + ": " + height));
                            }
                        });
                    });
                    return 1;
                });
    }
}
