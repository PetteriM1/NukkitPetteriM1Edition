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

public class MeCommand
extends VanillaCommand {
    public MeCommand(String string) {
        super(string, "%nukkit.command.me.description", "%nukkit.command.me.usage");
        this.setPermission("nukkit.command.me");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("action ...", CommandParamType.RAWTEXT, false)});
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
        String string2 = commandSender instanceof Player ? ((Player)commandSender).getDisplayName() : commandSender.getName();
        StringBuilder stringBuilder = new StringBuilder();
        for (String string3 : stringArray) {
            stringBuilder.append(string3).append(' ');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        commandSender.getServer().broadcastMessage(new TranslationContainer("chat.type.emote", string2, (Object)((Object)TextFormat.WHITE) + stringBuilder.toString()));
        return true;
    }
}

