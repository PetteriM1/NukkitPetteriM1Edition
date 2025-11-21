package cn.nukkit.utils;

import cn.nukkit.Nukkit;
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
    /**
     * Watchdog threshold
     */
    public volatile long time;
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
     * Dump thread stack trace
     *
     * @param thread thread to dump
     * @param logger logger
     * @param log    bug report generator input
     */
    private static void dumpThread(ThreadInfo thread, Logger logger, StringBuilder log) {
        print("Thread: " + thread.getThreadName(), logger, log);
        print("\tPID: " + thread.getThreadId() + " | Suspended: " + thread.isSuspended() + " | Native: " + thread.isInNative() + " | State: " + thread.getThreadState(), logger, log);

        if (thread.getLockedMonitors().length != 0) {
            print("\tThread is waiting on monitor(s):", logger, log);
            for (MonitorInfo monitor : thread.getLockedMonitors()) {
                print("\t\tLocked on:" + monitor.getLockedStackFrame(), logger, log);
            }
        }

        print("\tStack:", logger, null);
        for (StackTraceElement stack : thread.getStackTrace()) {
            print("\t\t" + stack, logger, log);
        }
    }

    /**
     * Disable Watchdog
     */
    public void kill() {
        this.running = false;
        this.interrupt();
    }

    /**
     * Print a line to log
     *
     * @param logger logger
     * @param log    bug report generator input
     */
    private static void print(String text, Logger logger, StringBuilder log) {
        logger.emergency(text);
        if (log != null) log.append(text).append('\n');
    }

    @Override
    public void run() {
        while (this.running) {
            long current = this.server.getNextTick();
            if (current != 0) {
                long diff = System.currentTimeMillis() - current;
                if (diff > this.time) {
                    if (this.server.isRunning()) {
                        MainLogger logger = this.server.getLogger();
                        StringBuilder reporter = new StringBuilder();
                        long lastResponse = Math.round(diff / 1000d);

                        print("--------- Server stopped responding ---------", logger, reporter);
                        print("Last response " + lastResponse + " seconds ago", logger, null);
                        print("---------------- Main thread ----------------", logger, null);

                        ThreadInfo mainThread = ManagementFactory.getThreadMXBean().getThreadInfo(this.server.getPrimaryThread().getId(), Integer.MAX_VALUE);
                        dumpThread(mainThread, logger, reporter);

                        print("---------------- All threads ----------------", logger, reporter);
                        ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                        for (int i = 0; i < threads.length; i++) {
                            if (i != 0) print("------------------------------", logger, reporter);
                            dumpThread(threads[i], logger, reporter);
                        }
                        print("---------------------------------------------", logger, null);

                        if ("TIMED_WAITING".equals(mainThread.getThreadState().toString())) {
                            logger.warning("Make sure your plugins are not calling sleep() on main thread and that your terminal doesn't suspend server process when not focused");
                        }

                        try {
                            new BugReportGenerator(reporter.toString(), lastResponse).start();
                            Thread.sleep(1000); // Wait for the report to be sent
                        } catch (Exception ex) {
                            if (Nukkit.DEBUG > 1) {
                                logger.debug("Exception in Watchdog", ex);
                            }
                            // Fail safe
                        }
                        this.server.forceShutdown("§cServer stopped responding");
                    } else if (diff > time << 1) {
                        System.out.println("\nTook too long to stop, server was killed forcefully!\n");
                        System.exit(1);
                        return;
                    }
                }
            }
            try {
                Thread.sleep(Math.max(this.time >> 2, 1000));
            } catch (InterruptedException ignore) {
                if (this.running) {
                    this.running = false;
                    this.server.getLogger().emergency("The Watchdog thread has been interrupted and is no longer monitoring the server state");
                }
                return;
            }
        }
    }
}
