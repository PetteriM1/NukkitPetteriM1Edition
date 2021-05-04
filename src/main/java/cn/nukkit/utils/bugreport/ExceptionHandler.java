package cn.nukkit.utils.bugreport;

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
        } catch (Exception ignored) {}
    }
}
