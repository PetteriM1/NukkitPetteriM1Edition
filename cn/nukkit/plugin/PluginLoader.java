/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import java.io.File;
import java.util.regex.Pattern;

public interface PluginLoader {
    public Plugin loadPlugin(String var1) throws Exception;

    public Plugin loadPlugin(File var1) throws Exception;

    public PluginDescription getPluginDescription(String var1);

    public PluginDescription getPluginDescription(File var1);

    public Pattern[] getPluginFilters();

    public void enablePlugin(Plugin var1);

    public void disablePlugin(Plugin var1);
}

