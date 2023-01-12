/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils.bugreport;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.command.defaults.StatusCommand;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.bugreport.BugReportPlugin;
import cn.nukkit.utils.bugreport.ExceptionHandler;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class BugReportGenerator
extends Thread {
    private final Throwable b;
    private final String a;
    private final long d;
    private static String c;
    public static BugReportPlugin plugin;

    BugReportGenerator(Throwable throwable) {
        this.setName("BugReportGenerator - Throwable");
        this.b = throwable;
        this.a = null;
        this.d = -1L;
    }

    public BugReportGenerator(String string, long l) {
        this.setName("BugReportGenerator - Watchdog");
        this.b = null;
        this.a = string;
        this.d = l;
    }

    @Override
    public void run() {
        Server.getInstance().getLogger().info("[BugReport] Creating a bug report...");
        if (plugin != null) {
            try {
                plugin.bugReport(this.b, this.a);
            }
            catch (Exception exception) {
                Server.getInstance().getLogger().error("[BugReport] External bug report failed", exception);
            }
        }
        try {
            if (ExceptionHandler.a != null) {
                this.a();
            }
        }
        catch (Exception exception) {
            Server.getInstance().getLogger().error("[BugReport] Sentry bug report failed", exception);
        }
    }

    private void a() {
        Object object;
        Object object2;
        Object object3;
        ExceptionHandler.a.getContext().clear();
        if (this.b != null) {
            boolean bl = false;
            object3 = this.b.getStackTrace();
            if (((StackTraceElement[])object3).length > 0) {
                object2 = object3[0].toString();
                if (c != null && c.equals(object2)) {
                    Server.getInstance().getLogger().debug("[BugReport] Report equals last report, refusing to send");
                    return;
                }
                c = object2;
                object = ((StackTraceElement)object3[0]).getClassName();
                if (!((String)object).startsWith("cn.nukkit")) {
                    bl = true;
                }
            } else {
                Server.getInstance().getLogger().debug("[BugReport] Empty stack trace, refusing to send");
                return;
            }
            ExceptionHandler.a.getContext().addTag("plugin_error", String.valueOf(bl));
        }
        StringBuilder stringBuilder = new StringBuilder();
        object3 = Server.getInstance().getPluginManager().getPlugins().values();
        try {
            object2 = object3.iterator();
            while (object2.hasNext()) {
                object = (Plugin)object2.next();
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                if (!object.isEnabled()) {
                    stringBuilder.append('*');
                }
                stringBuilder.append(object.getDescription().getFullName());
            }
        }
        catch (Exception exception) {
            Server.getInstance().getLogger().logException(exception);
        }
        object2 = System.getenv("PROCESSOR_IDENTIFIER");
        object = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        ExceptionHandler.a.getContext().addExtra("Nukkit Version", Nukkit.getBranch() + '/' + Nukkit.VERSION.substring(4) + " (" + Nukkit.BUILD_VERSION_NUMBER + ')');
        ExceptionHandler.a.getContext().addExtra("Java Version", System.getProperty("java.vm.name") + " (" + System.getProperty("java.runtime.version") + ')');
        ExceptionHandler.a.getContext().addExtra("Host OS", object.getName() + '-' + object.getArch() + " [" + object.getVersion() + ']');
        Runtime runtime = Runtime.getRuntime();
        double d2 = NukkitMath.round((double)(runtime.totalMemory() - runtime.freeMemory()) / 1024.0 / 1024.0, 2);
        double d3 = NukkitMath.round((double)runtime.maxMemory() / 1024.0 / 1024.0, 2);
        double d4 = d2 / d3 * 100.0;
        ExceptionHandler.a.getContext().addExtra("Memory", d2 + " MB (" + NukkitMath.round(d4, 2) + "%) of " + d3 + " MB");
        ExceptionHandler.a.getContext().addExtra("CPU Type", object2 == null ? "UNKNOWN" : object2);
        ExceptionHandler.a.getContext().addExtra("Available Cores", String.valueOf(object.getAvailableProcessors()));
        ExceptionHandler.a.getContext().addExtra("Uptime", TextFormat.clean(StatusCommand.formatUptime(System.currentTimeMillis() - Nukkit.START_TIME)));
        ExceptionHandler.a.getContext().addExtra("Players", Server.getInstance().getOnlinePlayersCount() + "/" + Server.getInstance().getMaxPlayers());
        ExceptionHandler.a.getContext().addExtra("Plugins (" + object3.size() + ")", stringBuilder.toString());
        if (this.d > -1L) {
            ExceptionHandler.a.getContext().addExtra("Last Response", this.d + " seconds ago");
        }
        ExceptionHandler.a.getContext().addTag("nukkit_build", Nukkit.BUILD_VERSION_NUMBER);
        ExceptionHandler.a.getContext().addTag("branch", Nukkit.getBranch());
        Server.getInstance().getLogger().debug("[BugReport] Sending a bug report to Sentry...");
        if (this.b != null) {
            ExceptionHandler.a.getContext().addTag("watchdog", String.valueOf(false));
            ExceptionHandler.a.sendException(this.b);
        } else if (this.a != null) {
            ExceptionHandler.a.getContext().addTag("watchdog", String.valueOf(true));
            ExceptionHandler.a.sendMessage(this.a);
        } else {
            Server.getInstance().getLogger().error("[BugReport] Failed to send a bug report: content cannot be null");
        }
    }

    public static String getCount(long l, boolean bl) {
        int n;
        int n2 = n = bl ? 1000 : 1024;
        if (l < (long)n) {
            return l + " B";
        }
        int n3 = (int)(Math.log(l) / Math.log(n));
        String string = (bl ? "kMGTPE" : "KMGTPE").charAt(n3 - 1) + (bl ? "" : "i");
        return String.format("%.1f %sB", (double)l / Math.pow(n, n3), string);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

