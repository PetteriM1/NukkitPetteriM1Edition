package cn.nukkit.utils.bugreport;

public interface BugReportPlugin {

    void bugReport(Throwable throwable, String message);
}
