package com.semmelzahntiger.arms.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.semmelzahntiger.arms.Arms;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforgespi.language.ModFileScanData;

import java.util.Collection;
import java.util.Map;

public class CommandEvents {


    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        Collection<Map.Entry<String, Command>> commands = CommandRegister.getCommands();
        for (Map.Entry<String, Command> command : commands) {
            LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal(command.getKey());
            event.getDispatcher().register(command.getValue().buildCommand(builder));
        }
    }
}
