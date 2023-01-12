/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import java.util.Objects;

public class TellCommand
extends VanillaCommand {
    public TellCommand(String string) {
        super(string, "%nukkit.command.tell.description", "%commands.message.usage", new String[]{"w", "msg"});
        this.setPermission("nukkit.command.tell");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("message", CommandParamType.TEXT, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length < 2) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        String string2 = stringArray[0].toLowerCase().replace("@s", commandSender.getName());
        Player player = commandSender.getServer().getPlayer(string2);
        if (player == null) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.player.notFound"));
            return true;
        }
        if (Objects.equals(player, commandSender)) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.message.sameTarget"));
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int k = 1; k < stringArray.length; ++k) {
            stringBuilder.append(stringArray[k]).append(' ');
        }
        if (stringBuilder.length() > 512) {
            commandSender.sendMessage((Object)((Object)TextFormat.RED) + "The message is too long");
            return true;
        }
        if (stringBuilder.length() > 0) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        String string3 = commandSender instanceof Player ? ((Player)commandSender).getDisplayName() : commandSender.getName();
        commandSender.sendMessage('[' + commandSender.getName() + " -> " + player.getDisplayName() + "] " + stringBuilder);
        player.sendMessage('[' + string3 + " -> " + player.getName() + "] " + stringBuilder);
        if (commandSender instanceof Player) {
            commandSender.getServer().getLogger().info('[' + commandSender.getName() + " -> " + player.getDisplayName() + "] " + stringBuilder);
        }
        return true;
    }
}

