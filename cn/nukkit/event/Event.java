/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event;

import cn.nukkit.event.Cancellable;
import cn.nukkit.utils.EventException;

public abstract class Event {
    protected String eventName = null;
    private boolean a = false;

    public final String getEventName() {
        return this.eventName == null ? this.getClass().getName() : this.eventName;
    }

    public boolean isCancelled() {
        if (!(this instanceof Cancellable)) {
            throw new EventException("Event is not Cancellable");
        }
        return this.a;
    }

    public void setCancelled() {
        this.setCancelled(true);
    }

    public void setCancelled(boolean bl) {
        if (!(this instanceof Cancellable)) {
            throw new EventException("Event is not Cancellable");
        }
        this.a = bl;
    }

    private static EventException b(EventException eventException) {
        return eventException;
    }
}

