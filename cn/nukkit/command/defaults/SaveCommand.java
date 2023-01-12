/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;

public class SaveCommand
extends VanillaCommand {
    public SaveCommand(String string) {
        super(string, "%nukkit.command.save.description", "%commands.save.usage");
        this.setPermission("nukkit.command.save.perform");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.save.start"));
        for (Player metadatable : commandSender.getServer().getOnlinePlayers().values()) {
            metadatable.save();
        }
        for (Level level : commandSender.getServer().getLevels().values()) {
            level.save(true);
        }
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.save.success"));
        return true;
    }
}

