/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;

public class StopCommand
extends VanillaCommand {
    public StopCommand(String string) {
        super(string, "%nukkit.command.stop.description", "%commands.stop.usage");
        this.setPermission("nukkit.command.stop");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (commandSender instanceof Player && !Server.getInstance().stopInGame) {
            commandSender.sendMessage("\u00a7cCan't use this command in game");
            return true;
        }
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.stop.start"));
        commandSender.getServer().shutdown();
        return true;
    }
}

