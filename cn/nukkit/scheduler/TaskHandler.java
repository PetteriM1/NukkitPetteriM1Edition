/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.Task;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;

public class TaskHandler {
    private final int i;
    private final boolean b;
    private final Plugin f;
    private final Runnable g;
    private int e;
    private int h;
    private int c;
    private int d;
    private boolean a;
    public final Timing timing;

    public TaskHandler(Plugin plugin, Runnable runnable, int n, boolean bl) {
        this.b = bl;
        this.f = plugin;
        this.g = runnable;
        this.i = n;
        this.timing = Timings.getTaskTiming(this, this.h);
    }

    public boolean isCancelled() {
        return this.a;
    }

    public int getNextRunTick() {
        return this.d;
    }

    public void setNextRunTick(int n) {
        this.d = n;
    }

    public int getTaskId() {
        return this.i;
    }

    public Runnable getTask() {
        return this.g;
    }

    public int getDelay() {
        return this.e;
    }

    public boolean isDelayed() {
        return this.e > 0;
    }

    public boolean isRepeating() {
        return this.h > 0;
    }

    public int getPeriod() {
        return this.h;
    }

    public Plugin getPlugin() {
        return this.f;
    }

    public int getLastRunTick() {
        return this.c;
    }

    public void setLastRunTick(int n) {
        this.c = n;
    }

    public void cancel() {
        if (!this.a && this.g instanceof Task) {
            ((Task)this.g).onCancel();
        }
        this.a = true;
    }

    public void remove() {
        this.a = true;
    }

    public void run(int n) {
        try {
            this.setLastRunTick(n);
            this.g.run();
        }
        catch (RuntimeException runtimeException) {
            Server.getInstance().getLogger().critical("Exception while invoking run", runtimeException);
        }
    }

    public boolean isAsynchronous() {
        return this.b;
    }

    public void setDelay(int n) {
        this.e = n;
    }

    public void setPeriod(int n) {
        this.h = n;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

