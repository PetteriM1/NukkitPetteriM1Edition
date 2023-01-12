/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class EntityRegainHealthEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    public static final int CAUSE_REGEN = 0;
    public static final int CAUSE_EATING = 1;
    public static final int CAUSE_MAGIC = 2;
    public static final int CAUSE_CUSTOM = 3;
    private float c;
    private final int b;

    public static HandlerList getHandlers() {
        return d;
    }

    public EntityRegainHealthEvent(Entity entity, float f2, int n) {
        this.entity = entity;
        this.c = f2;
        this.b = n;
    }

    public float getAmount() {
        return this.c;
    }

    public void setAmount(float f2) {
        this.c = f2;
    }

    public int getRegainReason() {
        return this.b;
    }
}

