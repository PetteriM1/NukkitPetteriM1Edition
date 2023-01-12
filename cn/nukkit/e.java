/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.InterruptibleThread;
import cn.nukkit.Server;
import cn.nukkit.d;

class e
extends Thread
implements InterruptibleThread {
    final Server this$0;

    private e(Server server) {
        this.this$0 = server;
    }

    @Override
    public void run() {
        Server.access$500(this.this$0).start();
    }

    e(Server server, d d2) {
        this(server);
    }
}

