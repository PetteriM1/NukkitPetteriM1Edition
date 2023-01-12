/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.scheduler.TaskHandler;

public abstract class Task
implements Runnable {
    private TaskHandler a = null;

    public final TaskHandler getHandler() {
        return this.a;
    }

    public final int getTaskId() {
        return this.a != null ? this.a.getTaskId() : -1;
    }

    public final void setHandler(TaskHandler taskHandler) {
        block0: {
            if (this.a != null && taskHandler != null) break block0;
            this.a = taskHandler;
        }
    }

    public abstract void onRun(int var1);

    @Override
    public final void run() {
        this.onRun(this.a.getLastRunTick());
    }

    public void onCancel() {
    }

    public void cancel() {
        try {
            this.a.cancel();
        }
        catch (RuntimeException runtimeException) {
            Server.getInstance().getLogger().critical("Exception while invoking onCancel", runtimeException);
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

