package cn.nukkit.utils.bugreport;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * Register exception handler
     */
    public static void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        handle(throwable);
    }

    /**
     * Handle exception, run BugReportGenerator
     */
    public void handle(Throwable throwable) {
        throwable.printStackTrace();

        try {
            new BugReportGenerator(throwable).start();
        } catch (Exception ex) {
            if (Nukkit.DEBUG > 1) {
                Server.getInstance().getLogger().logException(ex);
            }
        }
    }
}
