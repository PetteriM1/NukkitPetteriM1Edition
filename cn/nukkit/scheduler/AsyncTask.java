/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.utils.ThreadStore;
import co.aikar.timings.Timings;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AsyncTask
implements Runnable {
    public static final Queue<AsyncTask> FINISHED_LIST = new ConcurrentLinkedQueue<AsyncTask>();
    private Object a;
    private int c;
    private boolean b = false;

    @Override
    public void run() {
        this.a = null;
        this.onRun();
        this.b = true;
        FINISHED_LIST.offer(this);
    }

    public boolean isFinished() {
        return this.b;
    }

    public Object getResult() {
        return this.a;
    }

    public boolean hasResult() {
        return this.a != null;
    }

    public void setResult(Object object) {
        this.a = object;
    }

    public void setTaskId(int n) {
        this.c = n;
    }

    public int getTaskId() {
        return this.c;
    }

    public Object getFromThreadStore(String string) {
        return this.b ? null : ThreadStore.store.get(string);
    }

    public void saveToThreadStore(String string, Object object) {
        if (!this.b) {
            if (object == null) {
                ThreadStore.store.remove(string);
            } else {
                ThreadStore.store.put(string, object);
            }
        }
    }

    public abstract void onRun();

    public void onCompletion(Server server) {
    }

    public void cleanObject() {
        this.a = null;
        this.c = 0;
        this.b = false;
    }

    public static void collectTask() {
        block4: {
            if (Timings.schedulerAsyncTimer != null) {
                Timings.schedulerAsyncTimer.startTiming();
            }
            while (!FINISHED_LIST.isEmpty()) {
                AsyncTask asyncTask = FINISHED_LIST.poll();
                try {
                    asyncTask.onCompletion(Server.getInstance());
                }
                catch (Exception exception) {
                    Server.getInstance().getLogger().critical("Exception while async task " + asyncTask.c + " invoking onCompletion", exception);
                }
            }
            if (Timings.schedulerAsyncTimer == null) break block4;
            Timings.schedulerAsyncTimer.stopTiming();
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

