/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Nukkit;
import cn.nukkit.command.CommandMap;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.TextFormat;
import java.util.List;

public class VersionCommand
extends VanillaCommand {
    public VersionCommand(String string) {
        super(string, "%nukkit.command.version.description", "%nukkit.command.version.usage", new String[]{"ver", "about"});
        this.setPermission("nukkit.command.version");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string22, String[] stringArray) {
        if (stringArray.length == 0 || !commandSender.hasPermission("nukkit.command.version.plugins")) {
            commandSender.sendMessage("\u00a7e###############################################\n\u00a7cNukkit \u00a7aPetteriM1 Edition\n\u00a76Build: \u00a7b" + Nukkit.getBranch() + '/' + Nukkit.VERSION.substring(4) + " (" + Nukkit.BUILD_VERSION_NUMBER + ")\n\u00a76Multiversion: \u00a7bUp to version " + "1.19.50" + "\n\u00a7dhttps://github.com/PetteriM1/NukkitPetteriM1Edition\n\u00a7e###############################################");
            if (commandSender.isOp() && Nukkit.getBranch().equals("private")) {
                commandSender.getServer().updateNotification(commandSender, true);
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String string3 : stringArray) {
                stringBuilder.append(string3).append(' ');
            }
            stringBuilder = new StringBuilder(stringBuilder.toString().trim());
            boolean[] blArray = new boolean[]{false};
            Plugin[] pluginArray = new Plugin[]{commandSender.getServer().getPluginManager().getPlugin(stringBuilder.toString())};
            if (pluginArray[0] == null) {
                stringBuilder = new StringBuilder(stringBuilder.toString().toLowerCase());
                String pluginDescription = stringBuilder.toString();
                commandSender.getServer().getPluginManager().getPlugins().forEach((string2, plugin) -> {
                    if (string2.toLowerCase().contains(pluginDescription)) {
                        pluginArray[0] = plugin;
                        blArray[0] = true;
                    }
                });
            } else {
                blArray[0] = true;
            }
            if (blArray[0]) {
                PluginDescription pluginDescription = pluginArray[0].getDescription();
                commandSender.sendMessage((Object)((Object)TextFormat.DARK_GREEN) + pluginDescription.getName() + (Object)((Object)TextFormat.WHITE) + " version " + (Object)((Object)TextFormat.DARK_GREEN) + pluginDescription.getVersion());
                if (pluginDescription.getDescription() != null) {
                    commandSender.sendMessage(pluginDescription.getDescription());
                }
                if (pluginDescription.getWebsite() != null) {
                    commandSender.sendMessage("Website: " + pluginDescription.getWebsite());
                }
                List<String> list = pluginDescription.getAuthors();
                String[] stringArray2 = new String[]{""};
                list.forEach(string -> {
                    stringArray[0] = stringArray2[0] + string;
                });
                if (list.size() == 1) {
                    commandSender.sendMessage("Author: " + stringArray2[0]);
                } else if (list.size() >= 2) {
                    commandSender.sendMessage("Authors: " + stringArray2[0]);
                }
            } else {
                commandSender.sendMessage(new TranslationContainer("nukkit.command.version.noSuchPlugin"));
            }
        }
        return true;
    }

    @Override
    public boolean unregister(CommandMap commandMap) {
        return false;
    }
}

