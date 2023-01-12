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

public class BanCommand
extends VanillaCommand {
    public BanCommand(String string) {
        super(string, "%nukkit.command.ban.player.description", "%commands.ban.usage");
        this.setPermission("nukkit.command.ban.player");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("reason", CommandParamType.STRING, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
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
        commandSender.getServer().getNameBans().addBan(string2, stringBuilder.toString(), null, commandSender.getName());
        Player player = commandSender.getServer().getPlayerExact(string2);
        if (player != null) {
            player.kick(PlayerKickEvent.Reason.NAME_BANNED, stringBuilder.length() > 0 ? "You are banned! Reason: " + stringBuilder : "You are banned!", true, "source=" + commandSender.getName() + ", reason=" + stringBuilder);
        }
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("%commands.ban.success", player != null ? player.getName() : string2));
        return true;
    }
}

