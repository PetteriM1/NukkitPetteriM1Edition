/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.LogLevel;
import cn.nukkit.utils.Logger;

public class PluginLogger
implements Logger {
    private final String a;

    public PluginLogger(Plugin plugin) {
        String string = plugin.getDescription().getPrefix();
        this.a = string != null ? '[' + string + "] " : '[' + plugin.getDescription().getName() + "] ";
    }

    @Override
    public void emergency(String string) {
        this.log(LogLevel.EMERGENCY, string);
    }

    @Override
    public void alert(String string) {
        this.log(LogLevel.ALERT, string);
    }

    @Override
    public void critical(String string) {
        this.log(LogLevel.CRITICAL, string);
    }

    @Override
    public void error(String string) {
        this.log(LogLevel.ERROR, string);
    }

    @Override
    public void warning(String string) {
        this.log(LogLevel.WARNING, string);
    }

    @Override
    public void notice(String string) {
        this.log(LogLevel.NOTICE, string);
    }

    @Override
    public void info(String string) {
        this.log(LogLevel.INFO, string);
    }

    @Override
    public void debug(String string) {
        this.log(LogLevel.DEBUG, string);
    }

    @Override
    public void log(LogLevel logLevel, String string) {
        Server.getInstance().getLogger().log(logLevel, this.a + string);
    }

    @Override
    public void emergency(String string, Throwable throwable) {
        this.log(LogLevel.EMERGENCY, string, throwable);
    }

    @Override
    public void alert(String string, Throwable throwable) {
        this.log(LogLevel.ALERT, string, throwable);
    }

    @Override
    public void critical(String string, Throwable throwable) {
        this.log(LogLevel.CRITICAL, string, throwable);
    }

    @Override
    public void error(String string, Throwable throwable) {
        this.log(LogLevel.ERROR, string, throwable);
    }

    @Override
    public void warning(String string, Throwable throwable) {
        this.log(LogLevel.WARNING, string, throwable);
    }

    @Override
    public void notice(String string, Throwable throwable) {
        this.log(LogLevel.NOTICE, string, throwable);
    }

    @Override
    public void info(String string, Throwable throwable) {
        this.log(LogLevel.INFO, string, throwable);
    }

    @Override
    public void debug(String string, Throwable throwable) {
        this.log(LogLevel.DEBUG, string, throwable);
    }

    @Override
    public void log(LogLevel logLevel, String string, Throwable throwable) {
        Server.getInstance().getLogger().log(logLevel, this.a + string, throwable);
    }
}

