package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.utils.bugreport.BugReportGenerator;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

/**
 * Watchdog monitors the server's main thread and kills the server if it gets frozen.
 */
public class Watchdog extends Thread {

    private final Server server;
    private final long time;
    private boolean responding = true;
    /**
     * Watchdog running
     */
    public volatile boolean running;

    public Watchdog(Server server, long time) {
        this.server = server;
        this.time = time;
        this.running = true;
        this.setName("Watchdog");
        this.setDaemon(true);
    }

    /**
     * Shotdown Watchdog
     */
    public void kill() {
        running = false;
        interrupt();
    }

    @Override
    public void run() {
        while (this.running) {
            long current = server.getNextTick();
            if (current != 0) {
                long diff = System.currentTimeMillis() - current;
                if (!responding && diff > time << 1) {
                    System.exit(1); // Kill the server if it gets stuck on shutdown
                }

                if (diff <= time) {
                    responding = true;
                } else if (responding) {
                    MainLogger logger = this.server.getLogger();
                    StringBuilder log = new StringBuilder();

                    print("--------- Server stopped responding ---------", logger, log);
                    print(Math.round(diff / 1000d) + " s", logger, log);
                    print("---------------- Main thread ----------------", logger, log);

                    dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(this.server.getPrimaryThread().getId(), Integer.MAX_VALUE), logger, log);

                    print("---------------- All threads ----------------", logger, log);
                    ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                    for (int i = 0; i < threads.length; i++) {
                        if (i != 0) print("------------------------------", logger, log);
                        dumpThread(threads[i], logger, log);
                    }
                    print("---------------------------------------------", logger, log);
                    try {
                        new BugReportGenerator(log.toString()).start();
                        Thread.sleep(1000); // Wait for the report to be sent
                    } catch (Exception ignored) {}
                    responding = false;
                    this.server.forceShutdown("\u00A7cServer stopped responding");
                }
            }
            try {
                sleep(Math.max(time >> 2, 1000));
            } catch (InterruptedException ignore) {
                server.getLogger().emergency("The Watchdog thread has been interrupted and is no longer monitoring the server state");
                running = false;
                return;
            }
        }
        server.getLogger().warning("Watchdog has been stopped");
    }

    /**
     * Dump thread stack trace
     *
     * @param thread thread to dump
     * @param logger logger
     * @param log bug report generator input
     */
    private static void dumpThread(ThreadInfo thread, Logger logger, StringBuilder log) {
        print("Current Thread: " + thread.getThreadName(), logger, log);
        print("\tPID: " + thread.getThreadId() + " | Suspended: " + thread.isSuspended() + " | Native: " + thread.isInNative() + " | State: " + thread.getThreadState(), logger, log);

        if (thread.getLockedMonitors().length != 0) {
            print("\tThread is waiting on monitor(s):", logger, log);
            for (MonitorInfo monitor : thread.getLockedMonitors()) {
                print("\t\tLocked on:" + monitor.getLockedStackFrame(), logger, log);
            }
        }

        print("\tStack:", logger, log);
        for (StackTraceElement stack : thread.getStackTrace()) {
            print("\t\t" + stack, logger, log);
        }
    }

    /**
     * Print a line to log
     *
     * @param logger logger
     * @param log bug report generator input
     */
    private static void print(String text, Logger logger, StringBuilder log) {
        logger.emergency(text);
        log.append(text).append('\n');
    }
}
