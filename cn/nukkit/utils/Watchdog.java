/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.utils.Logger;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.bugreport.BugReportGenerator;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

public class Watchdog
extends Thread {
    private final Server a;
    public volatile long time;
    public volatile boolean running;

    public Watchdog(Server server, long l) {
        this.a = server;
        this.time = l;
        this.running = true;
        this.setName("Watchdog");
        this.setDaemon(true);
    }

    public void kill() {
        this.running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        while (this.running) {
            long l;
            long l2 = this.a.getNextTick();
            if (l2 != 0L && (l = System.currentTimeMillis() - l2) > this.time) {
                if (this.a.isRunning()) {
                    block12: {
                        MainLogger mainLogger = this.a.getLogger();
                        StringBuilder stringBuilder = new StringBuilder();
                        long l3 = Math.round((double)l / 1000.0);
                        Watchdog.a("--------- Server stopped responding ---------", (Logger)mainLogger, stringBuilder);
                        Watchdog.a("Last response " + l3 + " seconds ago", (Logger)mainLogger, null);
                        Watchdog.a("---------------- Main thread ----------------", (Logger)mainLogger, null);
                        Watchdog.a(ManagementFactory.getThreadMXBean().getThreadInfo(this.a.getPrimaryThread().getId(), Integer.MAX_VALUE), (Logger)mainLogger, stringBuilder);
                        Watchdog.a("---------------- All threads ----------------", (Logger)mainLogger, stringBuilder);
                        ThreadInfo[] threadInfoArray = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                        for (int k = 0; k < threadInfoArray.length; ++k) {
                            if (k != 0) {
                                Watchdog.a("------------------------------", (Logger)mainLogger, stringBuilder);
                            }
                            Watchdog.a(threadInfoArray[k], (Logger)mainLogger, stringBuilder);
                        }
                        Watchdog.a("---------------------------------------------", (Logger)mainLogger, null);
                        try {
                            new BugReportGenerator(stringBuilder.toString(), l3).start();
                            Thread.sleep(1000L);
                        }
                        catch (Exception exception) {
                            if (Nukkit.DEBUG <= 1) break block12;
                            mainLogger.debug("Exception in Watchdog", exception);
                        }
                    }
                    this.a.forceShutdown("\u00a7cServer stopped responding");
                } else if (l > this.time << 1) {
                    System.out.println("\nTook too long to stop, server was killed forcefully!\n");
                    System.exit(1);
                    return;
                }
            }
            try {
                Thread.sleep(Math.max(this.time >> 2, 1000L));
            }
            catch (InterruptedException interruptedException) {
                if (this.running) {
                    this.running = false;
                    this.a.getLogger().emergency("The Watchdog thread has been interrupted and is no longer monitoring the server state");
                }
                return;
            }
        }
    }

    private static void a(ThreadInfo threadInfo, Logger logger, StringBuilder stringBuilder) {
        Watchdog.a("Thread: " + threadInfo.getThreadName(), logger, stringBuilder);
        Watchdog.a("\tPID: " + threadInfo.getThreadId() + " | Suspended: " + threadInfo.isSuspended() + " | Native: " + threadInfo.isInNative() + " | State: " + (Object)((Object)threadInfo.getThreadState()), logger, stringBuilder);
        if (threadInfo.getLockedMonitors().length != 0) {
            Watchdog.a("\tThread is waiting on monitor(s):", logger, stringBuilder);
            for (MonitorInfo object : threadInfo.getLockedMonitors()) {
                Watchdog.a("\t\tLocked on:" + object.getLockedStackFrame(), logger, stringBuilder);
            }
        }
        Watchdog.a("\tStack:", logger, stringBuilder);
        for (StackTraceElement stackTraceElement : threadInfo.getStackTrace()) {
            Watchdog.a("\t\t" + stackTraceElement, logger, stringBuilder);
        }
    }

    private static void a(String string, Logger logger, StringBuilder stringBuilder) {
        logger.emergency(string);
        if (stringBuilder != null) {
            stringBuilder.append(string).append('\n');
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

