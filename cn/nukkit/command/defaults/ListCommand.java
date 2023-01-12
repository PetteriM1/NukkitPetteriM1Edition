/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;

public class ListCommand
extends VanillaCommand {
    public ListCommand(String string) {
        super(string, "%nukkit.command.list.description", "%commands.players.usage");
        this.setPermission("nukkit.command.list");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        for (Player player : commandSender.getServer().getOnlinePlayers().values()) {
            if (!player.isOnline() || commandSender instanceof Player && !((Player)commandSender).canSee(player)) continue;
            stringBuilder.append(player.getDisplayName()).append(", ");
            ++n;
        }
        if (stringBuilder.length() > 0) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 2));
        }
        commandSender.sendMessage(new TranslationContainer("commands.players.list", String.valueOf(n), String.valueOf(commandSender.getServer().getMaxPlayers())));
        commandSender.sendMessage(stringBuilder.toString());
        return true;
    }
}

