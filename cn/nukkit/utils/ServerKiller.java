/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import java.util.concurrent.TimeUnit;

public class ServerKiller
extends Thread {
    public final long sleepTime;

    public ServerKiller(long l) {
        this(l, TimeUnit.SECONDS);
    }

    public ServerKiller(long l, TimeUnit timeUnit) {
        this.sleepTime = timeUnit.toMillis(l);
        this.setName("ServerKiller");
    }

    @Override
    public void run() {
        try {
            ServerKiller.sleep(this.sleepTime);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        System.exit(1);
    }
}

