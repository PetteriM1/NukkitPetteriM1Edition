/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.Server;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.plugin.PluginLoader;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import java.io.File;
import java.io.InputStream;

public interface Plugin
extends CommandExecutor {
    public void onLoad();

    public void onEnable();

    public boolean isEnabled();

    public void onDisable();

    public boolean isDisabled();

    public File getDataFolder();

    public PluginDescription getDescription();

    public InputStream getResource(String var1);

    public boolean saveResource(String var1);

    public boolean saveResource(String var1, boolean var2);

    public boolean saveResource(String var1, String var2, boolean var3);

    public Config getConfig();

    public void saveConfig();

    public void saveDefaultConfig();

    public void reloadConfig();

    public Server getServer();

    public String getName();

    public PluginLogger getLogger();

    public PluginLoader getPluginLoader();
}

