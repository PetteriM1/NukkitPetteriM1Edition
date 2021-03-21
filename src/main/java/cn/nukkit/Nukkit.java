package cn.nukkit;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.ServerKiller;
import com.google.common.base.Preconditions;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/*
 * `_   _       _    _    _ _
 * | \ | |     | |  | |  (_) |
 * |  \| |_   _| | _| | ___| |_
 * | . ` | | | | |/ / |/ / | __|
 * | |\  | |_| |   <|   <| | |_
 * |_| \_|\__,_|_|\_\_|\_\_|\__|
 */

/**
 * The launcher class of Nukkit, including the {@code main} function
 *
 * @author MagicDroidX(code) @ Nukkit Project
 * @author 粉鞋大妈(javadoc) @ Nukkit Project
 * @since Nukkit 1.0 | Nukkit API 1.0.0
 */
@Log4j2
public class Nukkit {

    public static final String NUKKIT = "Nukkit PetteriM1 Edition";
    public final static Properties GIT_INFO = getGitInfo();
    public final static String VERSION = getVersion();
    public final static String API_VERSION = "CUSTOM";
    public final static String CODENAME = "On My Way";
    public final static String MINECRAFT_VERSION = ProtocolInfo.MINECRAFT_VERSION;
    public final static String MINECRAFT_VERSION_NETWORK = ProtocolInfo.MINECRAFT_VERSION_NETWORK;
    public final static String PATH = System.getProperty("user.dir") + '/';
    public final static String DATA_PATH = System.getProperty("user.dir") + '/';
    public final static String PLUGIN_PATH = DATA_PATH + "plugins";
    public final static long START_TIME = System.currentTimeMillis();
    public static boolean TITLE = true;
    public static int DEBUG = 1;

    public static void main(String[] args) {

        System.setProperty("java.net.preferIPv4Stack" , "true");
        System.setProperty("log4j.skipJansi", "false");
        System.getProperties().putIfAbsent("io.netty.allocator.type", "unpooled"); // Disable memory pooling unless specified

        // Force Mapped ByteBuffers for LevelDB till fixed
        System.setProperty("leveldb.mmap", "true");

        boolean loadPlugins = true;
        boolean debug = false;

        if (args.length > 0 && args[0].equalsIgnoreCase("-debug")) {
            debug = true;
            InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
            //ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
            System.out.print("Debug stuff enabled!\n");
            System.out.print("Do you want to skip loading plugins? (yes/no) ");
            loadPlugins = !new Scanner(System.in).nextLine().toLowerCase().startsWith("y");
        }

        try {
            if (TITLE) {
                System.out.print("\u001B]0;Nukkit PetteriM1 Edition\u0007");
            }
            new Server(PATH, DATA_PATH, PLUGIN_PATH, loadPlugins, debug);
        } catch (Throwable t) {
            log.throwing(t);
        }

        if (TITLE) {
            System.out.print("\u001B]0;Server shutting down...\u0007");
        }

        log.debug("Stopping other threads...");

        for (Thread thread : java.lang.Thread.getAllStackTraces().keySet()) {
            if (!(thread instanceof InterruptibleThread)) {
                continue;
            }
            log.debug("Stopping {} thread", thread.getClass().getSimpleName());
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }

        ServerKiller killer = new ServerKiller(8);
        killer.start();

        if (TITLE) {
            System.out.print("\u001B]0;Server Stopped\u0007");
        }

        System.exit(0);
    }

    private static Properties getGitInfo() {
        InputStream gitFileStream = Nukkit.class.getClassLoader().getResourceAsStream("git.properties");
        if (gitFileStream == null) {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(gitFileStream);
        } catch (IOException e) {
            return null;
        }
        return properties;
    }

    private static String getVersion() {
        StringBuilder version = new StringBuilder();
        version.append("git-");
        String commitId;
        if (GIT_INFO == null || (commitId = GIT_INFO.getProperty("git.commit.id.abbrev")) == null) {
            return version.append("null").toString();
        }
        return version.append(commitId).toString();
    }

    public static void setLogLevel(Level level) {
        Preconditions.checkNotNull(level, "level");
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        LoggerConfig loggerConfig = ctx.getConfiguration().getLoggerConfig(org.apache.logging.log4j.LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(level);
        ctx.updateLoggers();
    }

    public static Level getLogLevel() {
        return ((LoggerContext) LogManager.getContext(false)).getConfiguration().getLoggerConfig(org.apache.logging.log4j.LogManager.ROOT_LOGGER_NAME).getLevel();
    }

    public static String getBranch() {
        String branch;
        if (GIT_INFO == null || (branch = GIT_INFO.getProperty("git.branch")) == null) {
            return "null";
        }
        return branch;
    }
}
