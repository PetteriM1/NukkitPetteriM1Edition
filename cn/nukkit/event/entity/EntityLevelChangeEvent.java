/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.level.Level;

public class EntityLevelChangeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Level c;
    private final Level d;

    public static HandlerList getHandlers() {
        return b;
    }

    public EntityLevelChangeEvent(Entity entity, Level level, Level level2) {
        this.entity = entity;
        this.c = level;
        this.d = level2;
    }

    public Level getOrigin() {
        return this.c;
    }

    public Level getTarget() {
        return this.d;
    }
}

