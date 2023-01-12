/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class EntityCombustEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected int duration;

    public static HandlerList getHandlers() {
        return b;
    }

    public EntityCombustEvent(Entity entity, int n) {
        this.entity = entity;
        this.duration = n;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int n) {
        this.duration = n;
    }
}

