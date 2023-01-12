/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.plugin.PluginLoader;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Utils;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public abstract class PluginBase
implements Plugin {
    private PluginLoader c;
    private Server b;
    private boolean a = false;
    private boolean f = false;
    private PluginDescription i;
    private File e;
    private Config g;
    private File h;
    private File j;
    private PluginLogger d;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public final boolean isEnabled() {
        return this.a;
    }

    public final void setEnabled() {
        this.setEnabled(true);
    }

    public final void setEnabled(boolean bl) {
        if (this.a != bl) {
            this.a = bl;
            if (this.a) {
                this.onEnable();
            } else {
                this.onDisable();
            }
        }
    }

    @Override
    public final boolean isDisabled() {
        return !this.a;
    }

    @Override
    public final File getDataFolder() {
        return this.e;
    }

    @Override
    public final PluginDescription getDescription() {
        return this.i;
    }

    public final void init(PluginLoader pluginLoader, Server server, PluginDescription pluginDescription, File file, File file2) {
        if (!this.f) {
            this.f = true;
            this.c = pluginLoader;
            this.b = server;
            this.i = pluginDescription;
            this.e = file;
            this.j = file2;
            this.h = new File(this.e, "config.yml");
            this.d = new PluginLogger(this);
        }
    }

    @Override
    public PluginLogger getLogger() {
        return this.d;
    }

    public final boolean isInitialized() {
        return this.f;
    }

    public PluginIdentifiableCommand getCommand(String string) {
        PluginIdentifiableCommand pluginIdentifiableCommand = this.b.getPluginCommand(string);
        if (pluginIdentifiableCommand == null || !pluginIdentifiableCommand.getPlugin().equals(this)) {
            pluginIdentifiableCommand = this.b.getPluginCommand(this.i.getName().toLowerCase() + ':' + string);
        }
        if (pluginIdentifiableCommand != null && pluginIdentifiableCommand.getPlugin().equals(this)) {
            return pluginIdentifiableCommand;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] stringArray) {
        return false;
    }

    @Override
    public InputStream getResource(String string) {
        return this.getClass().getClassLoader().getResourceAsStream(string);
    }

    @Override
    public boolean saveResource(String string) {
        return this.saveResource(string, false);
    }

    @Override
    public boolean saveResource(String string, boolean bl) {
        return this.saveResource(string, string, bl);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean saveResource(String string, String string2, boolean bl) {
        Preconditions.checkArgument(string != null && string2 != null, "Filename can not be null!");
        Preconditions.checkArgument(!string.trim().isEmpty() && !string2.trim().isEmpty(), "Filename can not be empty!");
        File file = new File(this.e, string2);
        if (file.exists()) {
            if (!bl) return false;
        }
        try (InputStream inputStream = this.getResource(string);){
            if (inputStream == null) return false;
            File file2 = file.getParentFile();
            if (!file2.exists()) {
                file2.mkdirs();
            }
            Utils.writeFile(file, inputStream);
            boolean bl2 = true;
            return bl2;
        }
        catch (IOException iOException) {
            Server.getInstance().getLogger().logException(iOException);
        }
        return false;
    }

    @Override
    public Config getConfig() {
        if (this.g == null) {
            this.reloadConfig();
        }
        return this.g;
    }

    @Override
    public void saveConfig() {
        if (!this.getConfig().save()) {
            this.d.critical("Could not save config to " + this.h.toString());
        }
    }

    @Override
    public void saveDefaultConfig() {
        if (!this.h.exists()) {
            this.saveResource("config.yml", false);
        }
    }

    @Override
    public void reloadConfig() {
        this.g = new Config(this.h);
        InputStream inputStream = this.getResource("config.yml");
        if (inputStream != null) {
            DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(dumperOptions);
            try {
                this.g.setDefault(yaml.loadAs(Utils.readFile(this.h), LinkedHashMap.class));
            }
            catch (IOException iOException) {
                Server.getInstance().getLogger().logException(iOException);
            }
        }
    }

    @Override
    public Server getServer() {
        return this.b;
    }

    @Override
    public String getName() {
        return this.i.getName();
    }

    public final String getFullName() {
        return this.i.getFullName();
    }

    public File getFile() {
        return this.j;
    }

    @Override
    public PluginLoader getPluginLoader() {
        return this.c;
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

