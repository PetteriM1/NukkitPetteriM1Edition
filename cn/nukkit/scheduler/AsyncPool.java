/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.scheduler.a;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncPool
extends ThreadPoolExecutor {
    private final Server a;

    public AsyncPool(Server server, int n) {
        super(n, Integer.MAX_VALUE, 60L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>());
        this.setThreadFactory(runnable -> new a(this, runnable));
        this.a = server;
    }

    @Override
    protected void afterExecute(Runnable runnable, Throwable throwable) {
        if (throwable != null) {
            this.a.getLogger().critical("Exception in asynchronous task", throwable);
        }
    }

    public Server getServer() {
        return this.a;
    }
}

