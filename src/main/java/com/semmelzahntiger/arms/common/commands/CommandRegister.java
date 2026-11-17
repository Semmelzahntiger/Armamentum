package com.semmelzahntiger.arms.common.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegister {

    private static final HashMap<String, Command> commands = new HashMap<>();

    public static void registerCommand(Command command) {
        commands.put(command.getCommandName(), command);
    }
    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }
    public static Collection<Map.Entry<String, Command>> getCommands() {
        return commands.entrySet();
    }
    static {
        registerCommand(new PrintMaxHeightCache());
        registerCommand(new PrintBlockHeightCache());
    }
}
