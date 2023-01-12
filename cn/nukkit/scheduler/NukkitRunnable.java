/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.TaskHandler;

public abstract class NukkitRunnable
implements Runnable {
    private TaskHandler a;

    public synchronized void cancel() throws IllegalStateException {
        this.a.cancel();
    }

    public synchronized Runnable runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.a();
        this.a = Server.getInstance().getScheduler().scheduleTask(plugin, this);
        return this.a.getTask();
    }

    public synchronized Runnable runTaskAsynchronously(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
        this.a();
        this.a = Server.getInstance().getScheduler().scheduleTask(plugin, this, true);
        return this.a.getTask();
    }

    public synchronized Runnable runTaskLater(Plugin plugin, int n) throws IllegalArgumentException, IllegalStateException {
        this.a();
        this.a = Server.getInstance().getScheduler().scheduleDelayedTask(plugin, this, n);
        return this.a.getTask();
    }

    public synchronized Runnable runTaskLaterAsynchronously(Plugin plugin, int n) throws IllegalArgumentException, IllegalStateException {
        this.a();
        this.a = Server.getInstance().getScheduler().scheduleDelayedTask(plugin, this, n, true);
        return this.a.getTask();
    }

    public synchronized Runnable runTaskTimer(Plugin plugin, int n, int n2) throws IllegalArgumentException, IllegalStateException {
        this.a();
        this.a = Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(plugin, this, n, n2);
        return this.a.getTask();
    }

    public synchronized Runnable runTaskTimerAsynchronously(Plugin plugin, int n, int n2) throws IllegalArgumentException, IllegalStateException {
        this.a();
        this.a = Server.getInstance().getScheduler().scheduleDelayedRepeatingTask(plugin, this, n, n2, true);
        return this.a.getTask();
    }

    public synchronized int getTaskId() throws IllegalStateException {
        if (this.a == null) {
            throw new IllegalStateException("Not scheduled yet");
        }
        return this.a.getTaskId();
    }

    private void a() {
        if (this.a != null) {
            throw new IllegalStateException("Already scheduled as " + this.a.getTaskId());
        }
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

