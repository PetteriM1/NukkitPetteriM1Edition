/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class XpCommand
extends Command {
    public XpCommand(String string) {
        super(string, "%nukkit.command.xp.description", "%commands.xp.usage");
        this.setPermission("nukkit.command.xp");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("amount|level", CommandParamType.INT, false), new CommandParameter("player", CommandParamType.TARGET, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        int n;
        Player player;
        String string2;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (!(commandSender instanceof Player)) {
            if (stringArray.length != 2) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
            string2 = stringArray[0];
            String string3 = stringArray[1];
            player = commandSender.getServer().getPlayer(string3);
        } else if (stringArray.length == 1) {
            string2 = stringArray[0];
            player = (Player)commandSender;
        } else if (stringArray.length == 2) {
            string2 = stringArray[0];
            String string4 = stringArray[1].replace("@s", commandSender.getName());
            player = commandSender.getServer().getPlayer(string4);
        } else {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        if (player == null) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
            return true;
        }
        boolean bl = false;
        if (string2.endsWith("l") || string2.endsWith("L")) {
            string2 = string2.substring(0, string2.length() - 1);
            bl = true;
        }
        try {
            n = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        if (bl) {
            int n2 = player.getExperienceLevel();
            if ((n2 += n) > 24791) {
                n2 = 24791;
            }
            if (n2 < 0) {
                player.setExperience(0, 0);
            } else {
                player.setExperience(player.getExperience(), n2);
            }
            if (n > 0) {
                commandSender.sendMessage(new TranslationContainer("commands.xp.success.levels", String.valueOf(n), player.getName()));
            } else {
                commandSender.sendMessage(new TranslationContainer("commands.xp.success.levels.minus", String.valueOf(-n), player.getName()));
            }
            return true;
        }
        if (n < 0) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        player.addExperience(n);
        commandSender.sendMessage(new TranslationContainer("commands.xp.success", String.valueOf(n), player.getName()));
        return true;
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

