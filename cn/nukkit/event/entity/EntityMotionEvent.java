/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.math.Vector3;

public class EntityMotionEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Vector3 c;

    public static HandlerList getHandlers() {
        return b;
    }

    public EntityMotionEvent(Entity entity, Vector3 vector3) {
        this.entity = entity;
        this.c = vector3;
    }

    public Vector3 getVector() {
        return this.c;
    }

    public Vector3 getMotion() {
        return this.c;
    }
}

