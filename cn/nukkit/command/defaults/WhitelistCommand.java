/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class WhitelistCommand
extends VanillaCommand {
    public WhitelistCommand(String string) {
        super(string, "%nukkit.command.whitelist.description", "%commands.whitelist.usage");
        this.setPermission("nukkit.command.whitelist.reload;nukkit.command.whitelist.enable;nukkit.command.whitelist.disable;nukkit.command.whitelist.list;nukkit.command.whitelist.add;nukkit.command.whitelist.remove");
        this.commandParameters.clear();
        this.commandParameters.put("1arg", new CommandParameter[]{new CommandParameter("on|off|list|reload", CommandParamType.STRING, false)});
        this.commandParameters.put("2args", new CommandParameter[]{new CommandParameter("add|remove", CommandParamType.STRING, false), new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0 || stringArray.length > 2) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        if (stringArray.length == 1) {
            if (WhitelistCommand.a(commandSender, stringArray[0].toLowerCase())) {
                return false;
            }
            switch (stringArray[0].toLowerCase()) {
                case "reload": {
                    commandSender.getServer().reloadWhitelist();
                    Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.whitelist.reloaded"));
                    return true;
                }
                case "on": {
                    commandSender.getServer().setPropertyBoolean("white-list", true);
                    commandSender.getServer().whitelistEnabled = true;
                    Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.whitelist.enabled"));
                    return true;
                }
                case "off": {
                    commandSender.getServer().setPropertyBoolean("white-list", false);
                    commandSender.getServer().whitelistEnabled = false;
                    Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.whitelist.disabled"));
                    return true;
                }
                case "list": {
                    StringBuilder stringBuilder = new StringBuilder();
                    int n = 0;
                    for (String string2 : commandSender.getServer().getWhitelist().getAll().keySet()) {
                        stringBuilder.append(string2).append(", ");
                        ++n;
                    }
                    commandSender.sendMessage(new TranslationContainer("commands.whitelist.list", String.valueOf(n), String.valueOf(n)));
                    commandSender.sendMessage(stringBuilder.length() > 0 ? stringBuilder.substring(0, stringBuilder.length() - 2) : "");
                    return true;
                }
                case "add": {
                    commandSender.sendMessage(new TranslationContainer("commands.generic.usage", "%commands.whitelist.add.usage"));
                    return true;
                }
                case "remove": {
                    commandSender.sendMessage(new TranslationContainer("commands.generic.usage", "%commands.whitelist.remove.usage"));
                    return true;
                }
            }
        } else {
            if (WhitelistCommand.a(commandSender, stringArray[0].toLowerCase())) {
                return false;
            }
            switch (stringArray[0].toLowerCase()) {
                case "add": {
                    commandSender.getServer().getOfflinePlayer(stringArray[1]).setWhitelisted(true);
                    Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.whitelist.add.success", stringArray[1]));
                    return true;
                }
                case "remove": {
                    commandSender.getServer().getOfflinePlayer(stringArray[1]).setWhitelisted(false);
                    Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.whitelist.remove.success", stringArray[1]));
                    return true;
                }
            }
        }
        return true;
    }

    private static boolean a(CommandSender commandSender, String string) {
        if (!commandSender.hasPermission("nukkit.command.whitelist." + string)) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.permission"));
            return true;
        }
        return false;
    }
}

