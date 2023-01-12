/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;

public class SaveOffCommand
extends VanillaCommand {
    public SaveOffCommand(String string) {
        super(string, "%nukkit.command.saveoff.description", "%commands.save-off.usage");
        this.setPermission("nukkit.command.save.disable");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        commandSender.getServer().setAutoSave(false);
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.save.disabled"));
        return true;
    }
}

