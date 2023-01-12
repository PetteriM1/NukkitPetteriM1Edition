/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class KickCommand
extends VanillaCommand {
    public KickCommand(String string) {
        super(string, "%nukkit.command.kick.description", "%commands.kick.usage");
        this.setPermission("nukkit.command.kick");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("reason", CommandParamType.STRING, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        Player player;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        String string2 = stringArray[0].replace("@s", commandSender.getName());
        StringBuilder stringBuilder = new StringBuilder();
        for (int k = 1; k < stringArray.length; ++k) {
            stringBuilder.append(stringArray[k]).append(' ');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        if ((player = commandSender.getServer().getPlayer(string2)) != null) {
            player.kick(PlayerKickEvent.Reason.KICKED_BY_ADMIN, stringBuilder.toString(), true, "source=" + commandSender.getName() + ", reason=" + stringBuilder.toString());
            if (stringBuilder.length() >= 1) {
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.kick.success.reason", player.getName(), stringBuilder.toString()));
            } else {
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.kick.success", player.getName()));
            }
        } else {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
        }
        return true;
    }
}

