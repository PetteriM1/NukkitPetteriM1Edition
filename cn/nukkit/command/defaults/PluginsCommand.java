/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.TextFormat;
import java.util.Map;

public class PluginsCommand
extends VanillaCommand {
    public PluginsCommand(String string) {
        super(string, "%nukkit.command.plugins.description", "%nukkit.command.plugins.usage");
        this.setPermission("nukkit.command.plugins");
        this.setAliases(new String[]{"pl"});
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        PluginsCommand.a(commandSender);
        return true;
    }

    private static void a(CommandSender commandSender) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, Plugin> map = commandSender.getServer().getPluginManager().getPlugins();
        for (Plugin plugin : map.values()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append((Object)((Object)TextFormat.WHITE) + ", ");
            }
            stringBuilder.append((Object)(plugin.isEnabled() ? TextFormat.GREEN : TextFormat.RED));
            stringBuilder.append(plugin.getDescription().getFullName());
        }
        commandSender.sendMessage(new TranslationContainer("nukkit.command.plugins.success", String.valueOf(map.size()), stringBuilder.toString()));
    }
}

