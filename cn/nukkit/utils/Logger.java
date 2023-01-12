/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.LogLevel;

public interface Logger {
    public void emergency(String var1);

    public void alert(String var1);

    public void critical(String var1);

    public void error(String var1);

    public void warning(String var1);

    public void notice(String var1);

    public void info(String var1);

    public void debug(String var1);

    public void log(LogLevel var1, String var2);

    public void emergency(String var1, Throwable var2);

    public void alert(String var1, Throwable var2);

    public void critical(String var1, Throwable var2);

    public void error(String var1, Throwable var2);

    public void warning(String var1, Throwable var2);

    public void notice(String var1, Throwable var2);

    public void info(String var1, Throwable var2);

    public void debug(String var1, Throwable var2);

    public void log(LogLevel var1, String var2, Throwable var3);
}

