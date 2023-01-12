/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Nukkit;
import cn.nukkit.utils.LogLevel;
import cn.nukkit.utils.Logger;
import org.apache.logging.log4j.LogManager;

public class MainLogger
extends Thread
implements Logger {
    private static final org.apache.logging.log4j.Logger b = LogManager.getLogger(MainLogger.class);
    private static final MainLogger a = new MainLogger();

    public static MainLogger getLogger() {
        return a;
    }

    @Override
    public void emergency(String string) {
        b.fatal(string);
    }

    @Override
    public void alert(String string) {
        b.warn(string);
    }

    @Override
    public void critical(String string) {
        b.fatal(string);
    }

    @Override
    public void error(String string) {
        b.error(string);
    }

    @Override
    public void warning(String string) {
        b.warn(string);
    }

    @Override
    public void notice(String string) {
        b.warn(string);
    }

    @Override
    public void info(String string) {
        b.info(string);
    }

    @Override
    public void debug(String string) {
        if (Nukkit.DEBUG > 1) {
            b.debug(string);
        }
    }

    public void logException(Throwable throwable) {
        b.throwing(throwable);
    }

    @Override
    public void log(LogLevel logLevel, String string) {
        logLevel.log(this, string);
    }

    @Override
    public void emergency(String string, Throwable throwable) {
        b.fatal(string, throwable);
    }

    @Override
    public void alert(String string, Throwable throwable) {
        b.warn(string, throwable);
    }

    @Override
    public void critical(String string, Throwable throwable) {
        b.fatal(string, throwable);
    }

    @Override
    public void error(String string, Throwable throwable) {
        b.error(string, throwable);
    }

    @Override
    public void warning(String string, Throwable throwable) {
        b.warn(string, throwable);
    }

    @Override
    public void notice(String string, Throwable throwable) {
        b.warn(string, throwable);
    }

    @Override
    public void info(String string, Throwable throwable) {
        b.info(string, throwable);
    }

    @Override
    public void debug(String string, Throwable throwable) {
        b.debug(string, throwable);
    }

    @Override
    public void log(LogLevel logLevel, String string, Throwable throwable) {
        logLevel.log(this, string, throwable);
    }

    private MainLogger() {
    }
}

