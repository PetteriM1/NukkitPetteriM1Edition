/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.event.Event;
import cn.nukkit.event.Listener;
import cn.nukkit.utils.EventException;

public interface EventExecutor {
    public void execute(Listener var1, Event var2) throws EventException;
}

