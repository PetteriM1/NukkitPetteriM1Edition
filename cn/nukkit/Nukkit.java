/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.InterruptibleThread;
import cn.nukkit.Server;
import cn.nukkit.utils.ServerKiller;
import com.google.common.base.Preconditions;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class Nukkit {
    private static final Logger a;
    public static final String NUKKIT_PM1E;
    public static final String MAIN_BRANCH;
    static final String b;
    public static final Properties GIT_INFO;
    public static final String VERSION;
    public static final String PATH;
    public static final String DATA_PATH;
    public static final String PLUGIN_PATH;
    public static String BUILD_VERSION_NUMBER;
    public static String BUILD_VERSION;
    public static final long START_TIME;
    public static boolean TITLE;
    public static int DEBUG;

    public static void main(String[] stringArray) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("log4j.skipJansi", "false");
        System.getProperties().putIfAbsent("io.netty.allocator.type", "unpooled");
        System.setProperty("leveldb.mmap", "true");
        boolean bl = true;
        boolean bl2 = false;
        if (stringArray.length > 0 && stringArray[0].equalsIgnoreCase("-debug")) {
            bl2 = true;
            System.out.print("Debug stuff enabled!\n");
            System.out.print("Do you want to skip loading plugins? (yes/no) ");
            bl = !new Scanner(System.in).nextLine().toLowerCase().startsWith("y");
        }
        boolean bl3 = false;
        try {
            String[] stringArray2;
            boolean bl4 = System.getProperty("os.name").startsWith("Windows");
            if (bl4) {
                String[] stringArray3 = new String[3];
                stringArray3[0] = "reg";
                stringArray3[1] = "query";
                stringArray2 = stringArray3;
                stringArray3[2] = "reg query \"HKU\\S-1-5-19\"";
            } else {
                String[] stringArray4 = new String[2];
                stringArray4[0] = "id";
                stringArray2 = stringArray4;
                stringArray4[1] = "-u";
            }
            Process object = new ProcessBuilder(stringArray2).start();
            object.waitFor();
            if (bl4) {
                bl3 = object.exitValue() == 0;
            } else {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(object.getInputStream()));
                String string = bufferedReader.readLine();
                bl3 = string.equals("0");
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (bl3) {
            a.warn("****************************");
            a.warn("YOU ARE RUNNING THIS SERVER AS AN ADMINISTRATIVE OR ROOT USER. THIS IS NOT ADVISED.");
            a.warn("****************************");
        }
        if (GIT_INFO == null || (BUILD_VERSION_NUMBER = GIT_INFO.getProperty("git.total.commit.count")) == null) {
            bl2 = true;
            BUILD_VERSION_NUMBER = "dev";
        } else if ("git-null".equals(VERSION)) {
            bl2 = true;
        }
        BUILD_VERSION = "1.19.50." + BUILD_VERSION_NUMBER;
        if (bl2) {
            InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        }
        try {
            if (TITLE) {
                System.out.print("\u001b]0;Nukkit PetteriM1 Edition\u0007");
            }
            new Server(PATH, DATA_PATH, PLUGIN_PATH, bl, bl2);
        }
        catch (Throwable throwable) {
            a.throwing(throwable);
        }
        if (TITLE) {
            System.out.print("\u001b]0;Server shutting down...\u0007");
        }
        a.debug("Stopping other threads...");
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (!(thread instanceof InterruptibleThread)) continue;
            a.debug("Stopping {} thread", (Object)thread.getClass().getSimpleName());
            if (!thread.isAlive()) continue;
            thread.interrupt();
        }
        ServerKiller serverKiller = new ServerKiller(8L);
        serverKiller.start();
        if (TITLE) {
            System.out.print("\u001b]0;Server Stopped\u0007");
        }
        System.exit(0);
    }

    private static Properties a() {
        InputStream inputStream = Nukkit.class.getClassLoader().getResourceAsStream("git.properties");
        if (inputStream == null) {
            a.debug("Unable to find git.properties");
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        }
        catch (IOException iOException) {
            a.debug("Unable to load git.properties", (Throwable)iOException);
            return null;
        }
        return properties;
    }

    private static String b() {
        String string;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("git-");
        if (GIT_INFO == null || (string = GIT_INFO.getProperty("git.commit.id.abbrev")) == null || string.isEmpty()) {
            return stringBuilder.append("null").toString();
        }
        return stringBuilder.append(string).toString();
    }

    public static void setLogLevel(Level level) {
        Preconditions.checkNotNull(level, "level");
        LoggerContext loggerContext = (LoggerContext)LogManager.getContext(false);
        LoggerConfig loggerConfig = loggerContext.getConfiguration().getLoggerConfig("");
        loggerConfig.setLevel(level);
        loggerContext.updateLoggers();
    }

    public static Level getLogLevel() {
        return ((LoggerContext)LogManager.getContext(false)).getConfiguration().getLoggerConfig("").getLevel();
    }

    public static String getBranch() {
        String string;
        if (GIT_INFO == null || (string = GIT_INFO.getProperty("git.branch")) == null) {
            return "null";
        }
        return string;
    }

    static {
        MAIN_BRANCH = "private";
        b = "https://api.github.com/repos/PetteriM1/NukkitPetteriM1Edition/releases";
        NUKKIT_PM1E = "Nukkit PetteriM1 Edition";
        a = LogManager.getLogger(Nukkit.class);
        GIT_INFO = Nukkit.a();
        VERSION = Nukkit.b();
        PATH = System.getProperty("user.dir") + '/';
        DATA_PATH = System.getProperty("user.dir") + '/';
        PLUGIN_PATH = DATA_PATH + "plugins";
        START_TIME = System.currentTimeMillis();
        TITLE = true;
        DEBUG = 1;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

