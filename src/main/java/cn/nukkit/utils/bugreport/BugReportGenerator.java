package cn.nukkit.utils.bugreport;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class BugReportGenerator extends Thread {

    private final Throwable throwable;
    private final String message;

    /**
     * Allow bug reports to be handled by a plugin
     */
    public static BugReportPlugin plugin;

    BugReportGenerator(Throwable throwable) {
        setName("BugReportGenerator");
        this.throwable = throwable;
        this.message = null;
    }

    public BugReportGenerator(String message) {
        setName("BugReportGenerator");
        this.throwable = null;
        this.message = message;
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
        try {
            if (Server.getInstance().sentry != null) {
                sentry();
            }
        } catch (Exception ignored) {
            Server.getInstance().getLogger().error("[BugReport] Sending a bug report failed!");
        }
    }

    /**
     * Send a bug report to Sentry
     */
    private void sentry() {
        Server.getInstance().getLogger().info("[BugReport] Sending a bug report ...");

        Server.getInstance().sentry.getContext().clear();

        if (throwable != null) {
            boolean pluginError = false;
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            if (stackTrace.length > 0) {
                String className = throwable.getStackTrace()[0].getClassName();
                if (!className.startsWith("cn.nukkit")) {
                    pluginError = true;
                    if (className.startsWith("org.hibernate")) {
                        return;
                    }
                }
            }
            Server.getInstance().sentry.getContext().addTag("plugin_error", String.valueOf(pluginError));
        }

        StringBuilder plugins = new StringBuilder();
        try {
            for (Plugin plugin : Server.getInstance().getPluginManager().getPlugins().values()) {
                if (plugins.length() > 0) {
                    plugins.append(", ");
                }
                if (!plugin.isEnabled()) {
                    plugins.append("*");
                }
                plugins.append(plugin.getDescription().getFullName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String cpuType = System.getenv("PROCESSOR_IDENTIFIER");
        OperatingSystemMXBean osMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Server.getInstance().sentry.getContext().addExtra("Nukkit Version", Nukkit.getBranch() + '/' + Nukkit.VERSION.substring(4));
        Server.getInstance().sentry.getContext().addExtra("Java Version", System.getProperty("java.vm.name") + " (" + System.getProperty("java.runtime.version") + ')');
        Server.getInstance().sentry.getContext().addExtra("Host OS", osMXBean.getName() + '-' + osMXBean.getArch() + " [" + osMXBean.getVersion() + ']');
        Server.getInstance().sentry.getContext().addExtra("Memory", getCount(osMXBean.getTotalPhysicalMemorySize(), true));
        Server.getInstance().sentry.getContext().addExtra("CPU Type", cpuType == null ? "UNKNOWN" : cpuType);
        Server.getInstance().sentry.getContext().addExtra("Available Cores", String.valueOf(osMXBean.getAvailableProcessors()));
        Server.getInstance().sentry.getContext().addExtra("Players", Server.getInstance().getOnlinePlayersCount() + "/" + Server.getInstance().getMaxPlayers());
        Server.getInstance().sentry.getContext().addExtra("Plugins", plugins.toString());
        Server.getInstance().sentry.getContext().addTag("nukkit_version", Nukkit.VERSION);
        Server.getInstance().sentry.getContext().addTag("branch", Nukkit.getBranch());

        if (throwable != null) {
            Server.getInstance().sentry.getContext().addTag("watchdog", String.valueOf(false));
            Server.getInstance().sentry.sendException(throwable);
        } else if (message != null) {
            Server.getInstance().sentry.getContext().addTag("watchdog", String.valueOf(true));
            Server.getInstance().sentry.sendMessage(message);
        } else {
            Server.getInstance().getLogger().error("[BugReport] Sending a bug report failed!");
        }
    }

    public static String getCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
