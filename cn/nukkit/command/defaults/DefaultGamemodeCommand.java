/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;

public class DefaultGamemodeCommand
extends VanillaCommand {
    public DefaultGamemodeCommand(String string) {
        super(string, "%nukkit.command.defaultgamemode.description", "%commands.defaultgamemode.usage");
        this.setPermission("nukkit.command.defaultgamemode");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("mode", CommandParamType.INT, false)});
        this.commandParameters.put("byString", new CommandParameter[]{new CommandParameter("mode", new String[]{"survival", "creative", "s", "c", "adventure", "a", "spectator", "view", "v"})});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", new String[]{this.usageMessage}));
            return false;
        }
        int n = Server.getGamemodeFromString(stringArray[0]);
        if (n != -1) {
            commandSender.getServer().setPropertyInt("gamemode", n);
            commandSender.sendMessage(new TranslationContainer("commands.defaultgamemode.success", new String[]{Server.getGamemodeString(n)}));
        } else {
            commandSender.sendMessage("Unknown game mode");
        }
        return true;
    }
}

