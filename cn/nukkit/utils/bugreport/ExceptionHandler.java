/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils.bugreport;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.utils.bugreport.BugReportGenerator;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;

public class ExceptionHandler
implements Thread.UncaughtExceptionHandler {
    static SentryClient a;

    public static void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        if (a == null) {
            a = SentryClientFactory.sentryClient("https://11f68489b7bc46149984237a72d45908@o381665.ingest.sentry.io/6069405");
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        this.handle(throwable);
    }

    public void handle(Throwable throwable) {
        block2: {
            throwable.printStackTrace();
            try {
                new BugReportGenerator(throwable).start();
            }
            catch (Exception exception) {
                if (Nukkit.DEBUG <= 1) break block2;
                Server.getInstance().getLogger().debug("Exception in ExceptionHandler", exception);
            }
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

