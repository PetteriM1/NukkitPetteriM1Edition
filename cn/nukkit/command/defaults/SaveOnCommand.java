/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;

public class SaveOnCommand
extends VanillaCommand {
    public SaveOnCommand(String string) {
        super(string, "%nukkit.command.saveon.description", "%commands.save-on.usage");
        this.setPermission("nukkit.command.save.enable");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        commandSender.getServer().setAutoSave(true);
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.save.enabled"));
        return true;
    }
}

