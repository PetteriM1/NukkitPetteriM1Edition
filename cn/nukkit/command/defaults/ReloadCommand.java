/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class ReloadCommand
extends VanillaCommand {
    public ReloadCommand(String string) {
        super(string, "%nukkit.command.reload.description", "%commands.reload.usage");
        this.setPermission("nukkit.command.reload");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        Command.broadcastCommandMessage(commandSender, new TranslationContainer((Object)((Object)TextFormat.YELLOW) + "%nukkit.command.reload.reloading" + (Object)((Object)TextFormat.WHITE)));
        commandSender.getServer().reload();
        Command.broadcastCommandMessage(commandSender, new TranslationContainer((Object)((Object)TextFormat.YELLOW) + "%nukkit.command.reload.reloaded" + (Object)((Object)TextFormat.WHITE)));
        return true;
    }
}

