package cn.nukkit.utils.bugreport;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.command.defaults.StatusCommand;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.TextFormat;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.Collection;

public class BugReportGenerator extends Thread {

    private final Throwable throwable;
    private final String message;
    private final long lastResponse;

    private static String lastReport;

    /**
     * Allow bug reports to be handled by a plugin
     */
    public static BugReportPlugin plugin;

    BugReportGenerator(Throwable throwable) {
        setName("BugReportGenerator - Throwable");
        this.throwable = throwable;
        this.message = null;
        this.lastResponse = -1;
    }

    public BugReportGenerator(String message, long lastResponse) {
        setName("BugReportGenerator - Watchdog");
        this.throwable = null;
        this.message = message;
        this.lastResponse = lastResponse;
    }

    @Override
    public void run() {
        if (plugin != null) {
            try {
                plugin.bugReport(throwable, message);
            } catch (Exception ex) {
                Server.getInstance().getLogger().error("[BugReport] External bug report failed", ex);
            }
        }
        if (ExceptionHandler.SENTRY != null) {
            try {
                sentry();
            } catch (Exception ex) {
                Server.getInstance().getLogger().error("[BugReport] Sentry bug report failed", ex);
            }
        }
    }

    /**
     * Send a bug report to Sentry
     */
    private void sentry() {
        long id = System.currentTimeMillis();
        Server.getInstance().getLogger().info("[BugReport] Creating a bug report (ID: " + id + ")...");

        ExceptionHandler.SENTRY.getContext().clear();

        if (throwable != null) {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            if (stackTrace.length > 0) {
                String thisReport = stackTrace[0].toString();
                if (lastReport != null && lastReport.equals(thisReport)) {
                    Server.getInstance().getLogger().debug("[BugReport] Report equals the last report");
                    return; // Try to filter error spam
                }
                lastReport = thisReport;
            } else {
                Server.getInstance().getLogger().debug("[BugReport] Empty stack trace");
                return; // Don't send empty stack traces
            }
        }

        StringBuilder plugins = new StringBuilder();
        Collection<Plugin> loadedPlugins = Server.getInstance().getPluginManager().getPlugins().values();
        try {
            for (Plugin plugin : loadedPlugins) {
                if (plugins.length() > 0) {
                    plugins.append(", ");
                }
                if (!plugin.isEnabled()) {
                    plugins.append('*');
                }
                plugins.append(plugin.getDescription().getName()).append(" ").append(plugin.getDescription().getVersion());
            }
        } catch (Exception ex) {
            Server.getInstance().getLogger().logException(ex);
        }

        String cpuType = System.getenv("PROCESSOR_IDENTIFIER");
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        ExceptionHandler.SENTRY.getContext().addExtra("Nukkit Version", Nukkit.getBranch() + '/' + Nukkit.VERSION.substring(4) + " (" + Nukkit.BUILD_VERSION_NUMBER + ')');
        ExceptionHandler.SENTRY.getContext().addExtra("Java Version", System.getProperty("java.vm.name") + " (" + System.getProperty("java.runtime.version") + ')');
        ExceptionHandler.SENTRY.getContext().addExtra("Host OS", osMXBean.getName() + '-' + osMXBean.getArch() + " [" + osMXBean.getVersion() + ']');
        Runtime runtime = Runtime.getRuntime();
        double usedMB = NukkitMath.round((double) (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024, 2);
        double maxMB = NukkitMath.round(((double) runtime.maxMemory()) / 1024 / 1024, 2);
        double usage = usedMB / maxMB * 100;
        ExceptionHandler.SENTRY.getContext().addExtra("Memory", usedMB + " MB (" + NukkitMath.round(usage, 2) + "%) of " + maxMB + " MB");
        ExceptionHandler.SENTRY.getContext().addExtra("CPU Type", cpuType == null ? "UNKNOWN" : cpuType);
        ExceptionHandler.SENTRY.getContext().addExtra("Available Cores", String.valueOf(osMXBean.getAvailableProcessors()));
        ExceptionHandler.SENTRY.getContext().addExtra("Uptime", TextFormat.clean(StatusCommand.formatUptime(System.currentTimeMillis() - Nukkit.START_TIME)));
        ExceptionHandler.SENTRY.getContext().addExtra("Players", Server.getInstance().getOnlinePlayersCount() + "/" + Server.getInstance().getMaxPlayers());
        ExceptionHandler.SENTRY.getContext().addExtra("Plugins (" + loadedPlugins.size() + ")", plugins.toString());
        if (lastResponse > -1) {
            ExceptionHandler.SENTRY.getContext().addExtra("Last Response", lastResponse + " seconds ago");
        }
        ExceptionHandler.SENTRY.getContext().addTag("nukkit_build", Nukkit.BUILD_VERSION_NUMBER);
        ExceptionHandler.SENTRY.getContext().addTag("branch", Nukkit.getBranch());
        ExceptionHandler.SENTRY.getContext().addTag("ID", String.valueOf(id));

        Server.getInstance().getLogger().debug("[BugReport] Sending a bug report to Sentry...");

        if (throwable != null) {
            ExceptionHandler.SENTRY.getContext().addTag("watchdog", String.valueOf(false));
            ExceptionHandler.SENTRY.sendException(throwable);
        } else if (message != null) {
            ExceptionHandler.SENTRY.getContext().addTag("watchdog", String.valueOf(true));
            ExceptionHandler.SENTRY.sendMessage(message);
        } else {
            Server.getInstance().getLogger().error("[BugReport] Failed to send a bug report: content cannot be null");
        }
    }
}
