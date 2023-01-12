/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.lang.TranslationContainer;

public class SpawnCommand
extends VanillaCommand {
    public SpawnCommand(String string) {
        super(string, "%nukkit.command.spawn.description", "%commands.spawn.usage");
        this.setPermission("nukkit.command.spawn");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (commandSender instanceof Player) {
            ((Player)commandSender).teleport(((Player)commandSender).getLevel().getSafeSpawn(), PlayerTeleportEvent.TeleportCause.COMMAND);
        } else {
            commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
        }
        return true;
    }
}

