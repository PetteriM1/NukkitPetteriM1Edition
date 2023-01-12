/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

public class TimeCommand
extends VanillaCommand {
    public TimeCommand(String string) {
        super(string, "%nukkit.command.time.description", "%nukkit.command.time.usage");
        this.setPermission("nukkit.command.time.add;nukkit.command.time.set;nukkit.command.time.start;nukkit.command.time.stop");
        this.commandParameters.clear();
        this.commandParameters.put("1arg", new CommandParameter[]{new CommandParameter("start|stop", CommandParamType.STRING, false)});
        this.commandParameters.put("2args", new CommandParameter[]{new CommandParameter("add|set", CommandParamType.STRING, false), new CommandParameter("value", CommandParamType.INT, false)});
        this.commandParameters.put("2args_", new CommandParameter[]{new CommandParameter("add|set", CommandParamType.STRING, false), new CommandParameter("value", CommandParamType.STRING, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (stringArray.length < 1) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        if ("start".equals(stringArray[0])) {
            if (!commandSender.hasPermission("nukkit.command.time.start")) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                return true;
            }
            for (Level level : commandSender.getServer().getLevels().values()) {
                level.checkTime();
                level.startTime();
                level.checkTime();
            }
            Command.broadcastCommandMessage(commandSender, "Restarted the time");
            return true;
        }
        if ("stop".equals(stringArray[0])) {
            if (!commandSender.hasPermission("nukkit.command.time.stop")) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                return true;
            }
            for (Level level : commandSender.getServer().getLevels().values()) {
                level.checkTime();
                level.stopTime();
                level.checkTime();
            }
            Command.broadcastCommandMessage(commandSender, "Stopped the time");
            return true;
        }
        if ("query".equals(stringArray[0])) {
            if (!commandSender.hasPermission("nukkit.command.time.query")) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                return true;
            }
            Level level = commandSender instanceof Player ? ((Player)commandSender).getLevel() : commandSender.getServer().getDefaultLevel();
            commandSender.sendMessage(new TranslationContainer("commands.time.query.gametime", String.valueOf(level.getTime())));
            return true;
        }
        if (stringArray.length < 2) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        if ("set".equals(stringArray[0])) {
            int n;
            if (!commandSender.hasPermission("nukkit.command.time.set")) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                return true;
            }
            if ("day".equals(stringArray[1])) {
                n = 0;
            } else if ("night".equals(stringArray[1])) {
                n = 14000;
            } else if ("midnight".equals(stringArray[1])) {
                n = 18000;
            } else if ("noon".equals(stringArray[1])) {
                n = 6000;
            } else if ("sunrise".equals(stringArray[1])) {
                n = 23000;
            } else if ("sunset".equals(stringArray[1])) {
                n = 12000;
            } else {
                try {
                    n = Math.max(0, Integer.parseInt(stringArray[1]));
                }
                catch (Exception exception) {
                    commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
                }
            }
            for (Level level : commandSender.getServer().getLevels().values()) {
                level.checkTime();
                level.setTime(n);
                level.checkTime();
            }
            Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.time.set", String.valueOf(n)));
        } else if ("add".equals(stringArray[0])) {
            int n;
            if (!commandSender.hasPermission("nukkit.command.time.add")) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                return true;
            }
            try {
                n = Math.max(0, Integer.parseInt(stringArray[1]));
            }
            catch (Exception exception) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
            for (Level level : commandSender.getServer().getLevels().values()) {
                level.checkTime();
                level.setTime(level.getTime() + n);
                level.checkTime();
            }
            Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.time.added", String.valueOf(n)));
        } else {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        }
        return true;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

