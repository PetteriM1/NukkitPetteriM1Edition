/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;

public abstract class EntityEvent
extends Event {
    protected Entity entity;

    public Entity getEntity() {
        return this.entity;
    }
}

