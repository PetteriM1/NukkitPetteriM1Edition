package cn.nukkit.utils.bugreport;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.utils.MainLogger;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    static SentryClient SENTRY;
    private static ExceptionHandler INSTANCE;

    /**
     * Register the default exception handler for current thread
     */
    public static void registerExceptionHandler() {
        if (INSTANCE == null) {
            INSTANCE = new ExceptionHandler();
        }
        Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
    }

    /**
     * Register the default exception handler for a ThreadFactoryBuilder
     */
    public static void registerExceptionHandler(ThreadFactoryBuilder builder) {
        if (INSTANCE == null) {
            INSTANCE = new ExceptionHandler();
        }
        builder.setUncaughtExceptionHandler(INSTANCE);
    }

    /**
     * Internal: Initialize Sentry client
     */
    public static void initSentry() {
        SENTRY = SentryClientFactory.sentryClient("https://11f68489b7bc46149984237a72d45908@o381665.ingest.sentry.io/6069405");
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handle(thread, throwable);
    }

    /**
     * Print stack trace, run BugReportGenerator
     */
    public void handle(Thread thread, Throwable throwable) {
        try {
            MainLogger.getLogger().logException(throwable);
        } catch (Exception ignore) {
            //noinspection CallToPrintStackTrace
            throwable.printStackTrace();
        }

        try {
            new BugReportGenerator(throwable).start();
        } catch (Exception ex) {
            if (Nukkit.DEBUG > 1) {
                Server.getInstance().getLogger().debug("Exception in ExceptionHandler", ex);
            }
            // Fail safe
        }
    }

    /**
     * Only run BugReportGenerator
     */
    public static void handleSilently(Throwable throwable) {
        try {
            new BugReportGenerator(throwable).start();
        } catch (Exception ex) {
            if (Nukkit.DEBUG > 1) {
                Server.getInstance().getLogger().debug("Exception in ExceptionHandler", ex);
            }
            // Fail safe
        }
    }
}
