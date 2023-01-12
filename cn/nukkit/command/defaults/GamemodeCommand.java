/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class GamemodeCommand
extends VanillaCommand {
    public GamemodeCommand(String string) {
        super(string, "%nukkit.command.gamemode.description", "%commands.gamemode.usage", new String[]{"gm"});
        this.setPermission("nukkit.command.gamemode.survival;nukkit.command.gamemode.creative;nukkit.command.gamemode.adventure;nukkit.command.gamemode.spectator;nukkit.command.gamemode.other");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{CommandParameter.newType("gameMode", CommandParamType.INT), new CommandParameter("player", CommandParamType.TARGET, true)});
        this.commandParameters.put("byString", new CommandParameter[]{CommandParameter.newEnum("gameMode", CommandEnum.ENUM_GAMEMODE), new CommandParameter("player", CommandParamType.TARGET, true)});
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (stringArray.length == 0) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        int n = Server.getGamemodeFromString(stringArray[0]);
        if (n == -1) {
            commandSender.sendMessage("Unknown game mode");
            return true;
        }
        CommandSender commandSender2 = commandSender;
        if (stringArray.length > 1) {
            if (!commandSender.hasPermission("nukkit.command.gamemode.other")) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
                return true;
            }
            commandSender2 = commandSender.getServer().getPlayer(stringArray[1].replace("@s", commandSender.getName()));
            if (commandSender2 == null) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
                return true;
            }
        } else if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        if (n == 0 && !commandSender.hasPermission("nukkit.command.gamemode.survival") || n == 1 && !commandSender.hasPermission("nukkit.command.gamemode.creative") || n == 2 && !commandSender.hasPermission("nukkit.command.gamemode.adventure") || n == 3 && !commandSender.hasPermission("nukkit.command.gamemode.spectator")) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
            return true;
        }
        if (!((Player)commandSender2).setGamemode(n)) {
            commandSender.sendMessage("Game mode update for " + commandSender2.getName() + " failed");
            return true;
        }
        if (commandSender2.equals(commandSender)) {
            Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.gamemode.success.self", Server.getGamemodeString(n)));
            return true;
        }
        commandSender2.sendMessage(new TranslationContainer("gameMode.changed"));
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.gamemode.success.other", commandSender2.getName(), Server.getGamemodeString(n)));
        return true;
    }
}

