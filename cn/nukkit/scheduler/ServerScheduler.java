/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.AsyncPool;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.PluginException;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerScheduler {
    public static int WORKERS = 4;
    private final AsyncPool a;
    private final Queue<TaskHandler> d = new ConcurrentLinkedQueue<TaskHandler>();
    private final Queue<TaskHandler> e;
    private final Map<Integer, TaskHandler> b;
    private final AtomicInteger c = new AtomicInteger();
    private volatile int f;

    public ServerScheduler() {
        this.e = new PriorityQueue<TaskHandler>(11, (taskHandler, taskHandler2) -> {
            int n = taskHandler.getNextRunTick() - taskHandler2.getNextRunTick();
            if (n == 0) {
                return taskHandler.getTaskId() - taskHandler2.getTaskId();
            }
            return n;
        });
        this.b = new ConcurrentHashMap<Integer, TaskHandler>();
        this.a = new AsyncPool(Server.getInstance(), WORKERS);
    }

    public TaskHandler scheduleTask(Task task) {
        return this.a(task, 0, 0, false);
    }

    public TaskHandler scheduleTask(Runnable runnable) {
        return this.a(null, runnable, 0, 0, false);
    }

    public TaskHandler scheduleTask(Plugin plugin, Runnable runnable) {
        return this.a(plugin, runnable, 0, 0, false);
    }

    public TaskHandler scheduleTask(Runnable runnable, boolean bl) {
        return this.a(null, runnable, 0, 0, bl);
    }

    public TaskHandler scheduleTask(Plugin plugin, Runnable runnable, boolean bl) {
        return this.a(plugin, runnable, 0, 0, bl);
    }

    public TaskHandler scheduleAsyncTask(AsyncTask asyncTask) {
        return this.a(null, asyncTask, 0, 0, true);
    }

    public TaskHandler scheduleAsyncTask(Plugin plugin, AsyncTask asyncTask) {
        return this.a(plugin, asyncTask, 0, 0, true);
    }

    public void scheduleAsyncTaskToWorker(AsyncTask asyncTask, int n) {
        this.scheduleAsyncTask(asyncTask);
    }

    public int getAsyncTaskPoolSize() {
        return this.a.getCorePoolSize();
    }

    public TaskHandler scheduleDelayedTask(Task task, int n) {
        return this.a(task, n, 0, false);
    }

    public TaskHandler scheduleDelayedTask(Task task, int n, boolean bl) {
        return this.a(task, n, 0, bl);
    }

    public TaskHandler scheduleDelayedTask(Runnable runnable, int n) {
        return this.a(null, runnable, n, 0, false);
    }

    public TaskHandler scheduleDelayedTask(Plugin plugin, Runnable runnable, int n) {
        return this.a(plugin, runnable, n, 0, false);
    }

    public TaskHandler scheduleDelayedTask(Runnable runnable, int n, boolean bl) {
        return this.a(null, runnable, n, 0, bl);
    }

    public TaskHandler scheduleDelayedTask(Plugin plugin, Runnable runnable, int n, boolean bl) {
        return this.a(plugin, runnable, n, 0, bl);
    }

    public TaskHandler scheduleRepeatingTask(Runnable runnable, int n) {
        return this.a(null, runnable, 0, n, false);
    }

    public TaskHandler scheduleRepeatingTask(Plugin plugin, Runnable runnable, int n) {
        return this.a(plugin, runnable, 0, n, false);
    }

    public TaskHandler scheduleRepeatingTask(Runnable runnable, int n, boolean bl) {
        return this.a(null, runnable, 0, n, bl);
    }

    public TaskHandler scheduleRepeatingTask(Plugin plugin, Runnable runnable, int n, boolean bl) {
        return this.a(plugin, runnable, 0, n, bl);
    }

    public TaskHandler scheduleRepeatingTask(Task task, int n) {
        return this.a(task, 0, n, false);
    }

    public TaskHandler scheduleRepeatingTask(Task task, int n, boolean bl) {
        return this.a(task, 0, n, bl);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Task task, int n, int n2) {
        return this.a(task, n, n2, false);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Task task, int n, int n2, boolean bl) {
        return this.a(task, n, n2, bl);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Runnable runnable, int n, int n2) {
        return this.a(null, runnable, n, n2, false);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Plugin plugin, Runnable runnable, int n, int n2) {
        return this.a(plugin, runnable, n, n2, false);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Runnable runnable, int n, int n2, boolean bl) {
        return this.a(null, runnable, n, n2, bl);
    }

    public TaskHandler scheduleDelayedRepeatingTask(Plugin plugin, Runnable runnable, int n, int n2, boolean bl) {
        return this.a(plugin, runnable, n, n2, bl);
    }

    public void cancelTask(int n) {
        if (this.b.containsKey(n)) {
            try {
                this.b.remove(n).cancel();
            }
            catch (RuntimeException runtimeException) {
                Server.getInstance().getLogger().critical("Exception while invoking onCancel", runtimeException);
            }
        }
    }

    public void cancelTask(Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("Plugin cannot be null!");
        }
        for (Map.Entry<Integer, TaskHandler> entry : this.b.entrySet()) {
            TaskHandler taskHandler = entry.getValue();
            if (taskHandler.getPlugin() != null && !plugin.equals(taskHandler.getPlugin())) continue;
            try {
                taskHandler.cancel();
            }
            catch (RuntimeException runtimeException) {
                Server.getInstance().getLogger().critical("Exception while invoking onCancel", runtimeException);
            }
        }
    }

    public void cancelAllTasks() {
        for (Map.Entry<Integer, TaskHandler> entry : this.b.entrySet()) {
            try {
                entry.getValue().cancel();
            }
            catch (RuntimeException runtimeException) {
                Server.getInstance().getLogger().critical("Exception while invoking onCancel", runtimeException);
            }
        }
        this.b.clear();
        this.e.clear();
        this.c.set(0);
    }

    public boolean isQueued(int n) {
        return this.b.containsKey(n);
    }

    private TaskHandler a(Task task, int n, int n2, boolean bl) {
        return this.a(task instanceof PluginTask ? (Plugin)((PluginTask)task).getOwner() : null, task, n, n2, bl);
    }

    private TaskHandler a(Plugin plugin, Runnable runnable, int n, int n2, boolean bl) {
        if (plugin != null && plugin.isDisabled()) {
            throw new PluginException("Plugin '" + plugin.getName() + "' attempted to register a task while disabled.");
        }
        if (n < 0 || n2 < 0) {
            throw new PluginException("Attempted to register a task with negative delay or period.");
        }
        TaskHandler taskHandler = new TaskHandler(plugin, runnable, this.a(), bl);
        taskHandler.setDelay(n);
        taskHandler.setPeriod(n2);
        taskHandler.setNextRunTick(taskHandler.isDelayed() ? this.f + taskHandler.getDelay() : this.f);
        if (runnable instanceof Task) {
            ((Task)runnable).setHandler(taskHandler);
        }
        this.d.offer(taskHandler);
        this.b.put(taskHandler.getTaskId(), taskHandler);
        return taskHandler;
    }

    public void mainThreadHeartbeat(int n) {
        this.f = n;
        while (!this.d.isEmpty()) {
            this.e.offer(this.d.poll());
        }
        while (this.a(n)) {
            TaskHandler taskHandler = this.e.poll();
            if (taskHandler.isCancelled()) {
                this.b.remove(taskHandler.getTaskId());
                continue;
            }
            if (taskHandler.isAsynchronous()) {
                this.a.execute(taskHandler.getTask());
            } else {
                if (taskHandler.timing != null) {
                    taskHandler.timing.startTiming();
                }
                try {
                    taskHandler.run(n);
                }
                catch (Throwable throwable) {
                    Server.getInstance().getLogger().critical("Could not execute taskHandler " + taskHandler.getTaskId() + ": " + throwable.getMessage());
                    Server.getInstance().getLogger().logException(throwable instanceof Exception ? throwable : new RuntimeException(throwable));
                }
                if (taskHandler.timing != null) {
                    taskHandler.timing.stopTiming();
                }
            }
            if (taskHandler.isRepeating()) {
                taskHandler.setNextRunTick(n + taskHandler.getPeriod());
                this.d.offer(taskHandler);
                continue;
            }
            try {
                Optional.ofNullable(this.b.remove(taskHandler.getTaskId())).ifPresent(TaskHandler::cancel);
            }
            catch (RuntimeException runtimeException) {
                Server.getInstance().getLogger().critical("Exception while invoking onCancel", runtimeException);
            }
        }
        AsyncTask.collectTask();
    }

    public int getQueueSize() {
        return this.e.size() + this.d.size();
    }

    private boolean a(int n) {
        return this.e.peek() != null && this.e.peek().getNextRunTick() <= n;
    }

    private int a() {
        return this.c.incrementAndGet();
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

