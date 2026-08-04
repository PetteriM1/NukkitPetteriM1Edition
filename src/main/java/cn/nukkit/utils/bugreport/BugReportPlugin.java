package cn.nukkit.utils.bugreport;

public interface BugReportPlugin {

    /**
     * Handle a bug report
     *
     * @param throwable exception or null
     * @param message   watchdog log or null
     */
    void bugReport(Throwable throwable, String message);
}
