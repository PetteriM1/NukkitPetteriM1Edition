/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class TitleCommand
extends VanillaCommand {
    public TitleCommand(String string) {
        super(string, "%nukkit.command.title.description", "%nukkit.command.title.usage");
        this.setPermission("nukkit.command.title");
        this.commandParameters.clear();
        this.commandParameters.put("clear", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("clear", new String[]{"clear"})});
        this.commandParameters.put("reset", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("reset", new String[]{"reset"})});
        this.commandParameters.put("title", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("title", new String[]{"title"}), new CommandParameter("titleText", CommandParamType.STRING, false)});
        this.commandParameters.put("subtitle", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("subtitle", new String[]{"subtitle"}), new CommandParameter("titleText", CommandParamType.STRING, false)});
        this.commandParameters.put("actionbar", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("actionbar", new String[]{"actionbar"}), new CommandParameter("titleText", CommandParamType.STRING, false)});
        this.commandParameters.put("times", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("times", new String[]{"times"}), new CommandParameter("fadeIn", CommandParamType.INT, false), new CommandParameter("stay", CommandParamType.INT, false), new CommandParameter("fadeOut", CommandParamType.INT, false)});
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length < 2) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        Player player = Server.getInstance().getPlayer(stringArray[0]);
        if (player == null) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
            return true;
        }
        if (stringArray.length == 2) {
            switch (stringArray[1].toLowerCase()) {
                case "clear": {
                    player.clearTitle();
                    commandSender.sendMessage(new TranslationContainer("nukkit.command.title.clear", player.getName()));
                    return true;
                }
                case "reset": {
                    player.resetTitleSettings();
                    commandSender.sendMessage(new TranslationContainer("nukkit.command.title.reset", player.getName()));
                    return true;
                }
            }
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        if (stringArray.length == 3) {
            switch (stringArray[1].toLowerCase()) {
                case "title": {
                    player.sendTitle(stringArray[2]);
                    commandSender.sendMessage(new TranslationContainer("nukkit.command.title.title", TextFormat.clean(stringArray[2]), player.getName()));
                    return true;
                }
                case "subtitle": {
                    player.setSubtitle(stringArray[2]);
                    commandSender.sendMessage(new TranslationContainer("nukkit.command.title.subtitle", TextFormat.clean(stringArray[2]), player.getName()));
                    return true;
                }
                case "actionbar": {
                    player.sendActionBar(stringArray[2]);
                    commandSender.sendMessage(new TranslationContainer("nukkit.command.title.actionbar", TextFormat.clean(stringArray[2]), player.getName()));
                    return true;
                }
            }
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        if (stringArray.length != 5) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        if (!stringArray[1].toLowerCase().equals("times")) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        try {
            commandSender.sendMessage(new TranslationContainer("nukkit.command.title.times.success", stringArray[2], stringArray[3], stringArray[4], player.getName()));
            return true;
        }
        catch (NumberFormatException numberFormatException) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%nukkit.command.title.times.fail"));
            return true;
        }
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

