/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.EventExecutor;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.EventException;
import co.aikar.timings.Timing;

public class RegisteredListener {
    private final Listener c;
    private final EventPriority d;
    private final Plugin f;
    private final EventExecutor a;
    private final boolean b;
    private final Timing e;

    public RegisteredListener(Listener listener, EventExecutor eventExecutor, EventPriority eventPriority, Plugin plugin, boolean bl, Timing timing) {
        this.c = listener;
        this.d = eventPriority;
        this.f = plugin;
        this.a = eventExecutor;
        this.b = bl;
        this.e = timing;
    }

    public Listener getListener() {
        return this.c;
    }

    public Plugin getPlugin() {
        return this.f;
    }

    public EventPriority getPriority() {
        return this.d;
    }

    public void callEvent(Event event) throws EventException {
        block2: {
            if (event instanceof Cancellable && event.isCancelled() && this.b) {
                return;
            }
            if (this.e != null) {
                this.e.startTiming();
            }
            this.a.execute(this.c, event);
            if (this.e == null) break block2;
            this.e.stopTiming();
        }
    }

    public boolean isIgnoringCancelled() {
        return this.b;
    }

    private static EventException a(EventException eventException) {
        return eventException;
    }
}

