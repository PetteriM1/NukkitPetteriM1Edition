package cn.nukkit;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.ServerKiller;
import com.google.common.base.Preconditions;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.*;
import java.util.Locale;
import java.util.Objects;
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
 */
@Log4j2
public class Nukkit {

    public static final String NUKKIT_PM1E = "Nukkit PetteriM1 Edition";
    public static final String MAIN_BRANCH = "release";
    static final String RELEASES = "https://api.github.com/repos/PetteriM1/NukkitPetteriM1Edition/releases";
    public final static Properties GIT_INFO = getGitInfo();
    public final static String VERSION = getVersion();
    public final static String PATH = System.getProperty("user.dir") + '/';
    public final static String DATA_PATH = System.getProperty("user.dir") + '/';
    public final static String PLUGIN_PATH = DATA_PATH + "plugins";
    public static String BUILD_VERSION_NUMBER;
    public static String BUILD_VERSION;
    /**
     * Server start time
     */
    public final static long START_TIME = System.currentTimeMillis();
    /**
     * Console title enabled
     */
    public static boolean TITLE = true;
    /**
     * Debug logging level
     */
    public static int DEBUG = 1;
    private static String branch;

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("log4j.skipJansi", "false");

        // Disable memory pooling unless specified
        System.getProperties().putIfAbsent("io.netty.allocator.type", "unpooled");

        // Force Mapped ByteBuffers for LevelDB till fixed
        System.setProperty("leveldb.mmap", "true");

        if (Runtime.getRuntime().maxMemory() < 1_000_000_000) {
            log.warn("Low memory allocation! The server may crash or freeze. Use higher -Xmx if memory is available.");
        }

        boolean loadPlugins = true;
        boolean debug = false;

        if (args.length > 0 && args[0].equalsIgnoreCase("-debug")) {
            debug = true;
            System.out.print("Debug stuff enabled!\nDo you want to skip loading plugins? (yes/no) ");
            loadPlugins = !new Scanner(System.in).nextLine().toLowerCase(Locale.ROOT).startsWith("y");
        }

        if (MAIN_BRANCH.equals(getBranch())) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Nukkit.class.getClassLoader().getResourceAsStream(".current_release"))))) {
                BUILD_VERSION_NUMBER = br.readLine();
                if (BUILD_VERSION_NUMBER != null) {
                    BUILD_VERSION_NUMBER = BUILD_VERSION_NUMBER.trim();
                }
            } catch (Exception ex) {
                //noinspection CallToPrintStackTrace
                ex.printStackTrace();
            }
        } else if (GIT_INFO != null) {
            BUILD_VERSION_NUMBER = GIT_INFO.getProperty("git.total.commit.count");
        }

        if (GIT_INFO == null || BUILD_VERSION_NUMBER == null) {
            debug = true;
            BUILD_VERSION_NUMBER = "dev";
        } else if ("git-null".equals(VERSION)) {
            debug = true;
        }

        BUILD_VERSION = ProtocolInfo.MINECRAFT_VERSION_NETWORK + '.' + BUILD_VERSION_NUMBER;

        if (debug) {
            InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
        }

        try {
            if (TITLE) {
                System.out.print("\u001B]0;Nukkit PM1E " + BUILD_VERSION + "\u0007");
            }
            new Server(PATH, DATA_PATH, PLUGIN_PATH, loadPlugins, debug);
        } catch (Throwable t) {
            log.throwing(t);
        }

        if (TITLE) {
            System.out.print("\u001B]0;Stopping Server...\u0007");
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

        ServerKiller killer = new ServerKiller(10);
        killer.start();

        if (TITLE) {
            System.out.print("\u001B]0;Server Stopped\u0007");
        }

        System.exit(0);
    }

    private static Properties getGitInfo() {
        InputStream gitFileStream = Nukkit.class.getClassLoader().getResourceAsStream("git.properties");
        if (gitFileStream == null) {
            log.debug("Unable to find git.properties");
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(gitFileStream);
        } catch (IOException e) {
            log.debug("Unable to load git.properties", e);
            return null;
        }
        return properties;
    }

    private static String getVersion() {
        StringBuilder version = new StringBuilder();
        version.append("git-");
        String commitId;
        if (GIT_INFO == null || (commitId = GIT_INFO.getProperty("git.commit.id.abbrev")) == null || commitId.isEmpty()) {
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
        if (branch != null) {
            return branch;
        }
        String branchTemp;
        if (GIT_INFO == null || (branchTemp = GIT_INFO.getProperty("git.branch")) == null) {
            branchTemp = "null";
        }
        branch = branchTemp;
        return branch;
    }
}
