/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.Task;

public abstract class PluginTask<T extends Plugin>
extends Task {
    protected final T owner;

    public PluginTask(T t) {
        this.owner = t;
    }

    public final T getOwner() {
        return this.owner;
    }
}

