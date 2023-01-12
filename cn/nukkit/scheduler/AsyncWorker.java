/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.InterruptibleThread;
import cn.nukkit.scheduler.AsyncTask;
import java.util.LinkedList;

public class AsyncWorker
extends Thread
implements InterruptibleThread {
    private final LinkedList<AsyncTask> a = new LinkedList();

    public AsyncWorker() {
        this.setName("Asynchronous Worker");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stack(AsyncTask asyncTask) {
        LinkedList<AsyncTask> linkedList = this.a;
        synchronized (linkedList) {
            this.a.addFirst(asyncTask);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void unstack() {
        LinkedList<AsyncTask> linkedList = this.a;
        synchronized (linkedList) {
            this.a.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void unstack(AsyncTask asyncTask) {
        LinkedList<AsyncTask> linkedList = this.a;
        synchronized (linkedList) {
            this.a.remove(asyncTask);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        while (true) {
            LinkedList<AsyncTask> linkedList = this.a;
            synchronized (linkedList) {
                for (AsyncTask asyncTask : this.a) {
                    if (asyncTask.isFinished()) continue;
                    asyncTask.run();
                }
            }
            try {
                AsyncWorker.sleep(5L);
            }
            catch (InterruptedException interruptedException) {
            }
        }
    }

    private static InterruptedException a(InterruptedException interruptedException) {
        return interruptedException;
    }
}

