/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;

public class SeedCommand
extends VanillaCommand {
    public SeedCommand(String string) {
        super(string, "%nukkit.command.seed.description", "%commands.seed.usage");
        this.setPermission("nukkit.command.seed");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        long l = commandSender instanceof Player ? ((Player)commandSender).getLevel().getSeed() : commandSender.getServer().getDefaultLevel().getSeed();
        commandSender.sendMessage(new TranslationContainer("commands.seed.success", String.valueOf(l)));
        return true;
    }
}

