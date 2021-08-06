package cn.nukkit.command.defaults;

import cn.nukkit.Nukkit;
import cn.nukkit.command.CommandMap;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.TextFormat;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on 2015/11/12 by xtypr.
 * Package cn.nukkit.command.defaults in project Nukkit .
 */
public class VersionCommand extends VanillaCommand {

    public VersionCommand(String name) {
        super(name,
                "%nukkit.command.version.description",
                "%nukkit.command.version.usage",
                new String[]{"ver", "about"}
        );
        this.setPermission("nukkit.command.version");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0 || !sender.hasPermission("nukkit.command.version.plugins")) {
            sender.sendMessage("§e###############################################\n§cNukkit §aPetteriM1 Edition\n§6Build: §b" + Nukkit.getBranch() + '/' + Nukkit.VERSION.substring(4) + "\n§6Multiversion: §bUp to version " + ProtocolInfo.MINECRAFT_VERSION_NETWORK + "\n§dhttps://github.com/PetteriM1/NukkitPetteriM1Edition\n§e###############################################");

            if (sender.isOp()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        URLConnection request = new URL(Nukkit.BRANCH).openConnection();
                        request.connect();
                        InputStreamReader content = new InputStreamReader((InputStream) request.getContent());
                        String latest = "git-" + new JsonParser().parse(content).getAsJsonObject().get("sha").getAsString().substring(0, 7);
                        content.close();

                        if (Nukkit.getBranch().equals("master")) {
                            if (!sender.getServer().getNukkitVersion().equals(latest) && !sender.getServer().getNukkitVersion().equals("git-null")) {
                                sender.sendMessage("\u00A7c[Update] \u00A7eThere is a new build of Nukkit PetteriM1 Edition available! Current: " + sender.getServer().getNukkitVersion() + " Latest: " + latest);
                            } else {
                                sender.sendMessage("\u00A7aYou are running the latest version.");
                            }
                        }
                    } catch (Exception ignore) {
                    }
                });
            }
        } else {
            StringBuilder pluginName = new StringBuilder();
            for (String arg : args) pluginName.append(arg).append(' ');
            pluginName = new StringBuilder(pluginName.toString().trim());
            final boolean[] found = {false};
            final Plugin[] exactPlugin = {sender.getServer().getPluginManager().getPlugin(pluginName.toString())};

            if (exactPlugin[0] == null) {
                pluginName = new StringBuilder(pluginName.toString().toLowerCase());
                final String finalPluginName = pluginName.toString();
                sender.getServer().getPluginManager().getPlugins().forEach((s, p) -> {
                    if (s.toLowerCase().contains(finalPluginName)) {
                        exactPlugin[0] = p;
                        found[0] = true;
                    }
                });
            } else {
                found[0] = true;
            }

            if (found[0]) {
                PluginDescription desc = exactPlugin[0].getDescription();
                sender.sendMessage(TextFormat.DARK_GREEN + desc.getName() + TextFormat.WHITE + " version " + TextFormat.DARK_GREEN + desc.getVersion());
                if (desc.getDescription() != null) {
                    sender.sendMessage(desc.getDescription());
                }
                if (desc.getWebsite() != null) {
                    sender.sendMessage("Website: " + desc.getWebsite());
                }
                List<String> authors = desc.getAuthors();
                final String[] authorsString = {""};
                authors.forEach((s) -> authorsString[0] += s);
                if (authors.size() == 1) {
                    sender.sendMessage("Author: " + authorsString[0]);
                } else if (authors.size() >= 2) {
                    sender.sendMessage("Authors: " + authorsString[0]);
                }
            } else {
                sender.sendMessage(new TranslationContainer("nukkit.command.version.noSuchPlugin"));
            }
        }
        return true;
    }

    @Override
    public boolean unregister(CommandMap commandMap) {
        return false;
    }
}
