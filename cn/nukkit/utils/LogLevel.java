/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.MainLogger;
import java.util.function.BiConsumer;
import org.apache.logging.log4j.util.TriConsumer;

public enum LogLevel implements Comparable<LogLevel>
{
    NONE((mainLogger, string) -> {}, (mainLogger, string, throwable) -> {}),
    EMERGENCY(MainLogger::emergency, MainLogger::emergency),
    ALERT(MainLogger::alert, MainLogger::alert),
    CRITICAL(MainLogger::critical, MainLogger::critical),
    ERROR(MainLogger::error, MainLogger::error),
    WARNING(MainLogger::warning, MainLogger::warning),
    NOTICE(MainLogger::notice, MainLogger::notice),
    INFO(MainLogger::info, MainLogger::info),
    DEBUG(MainLogger::debug, MainLogger::debug);

    public static final LogLevel DEFAULT_LEVEL;
    private final BiConsumer<MainLogger, String> c;
    private final TriConsumer<MainLogger, String, Throwable> b;

    private LogLevel(BiConsumer<MainLogger, String> biConsumer, TriConsumer<MainLogger, String, Throwable> triConsumer) {
        this.c = biConsumer;
        this.b = triConsumer;
    }

    public void log(MainLogger mainLogger, String string) {
        this.c.accept(mainLogger, string);
    }

    public void log(MainLogger mainLogger, String string, Throwable throwable) {
        this.b.accept(mainLogger, string, throwable);
    }

    public int getLevel() {
        return this.ordinal();
    }

    static {
        DEFAULT_LEVEL = INFO;
    }
}

