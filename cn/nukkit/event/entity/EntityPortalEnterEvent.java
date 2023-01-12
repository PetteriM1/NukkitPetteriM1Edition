/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class EntityPortalEnterEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final PortalType c;

    public static HandlerList getHandlers() {
        return b;
    }

    public EntityPortalEnterEvent(Entity entity, PortalType portalType) {
        this.entity = entity;
        this.c = portalType;
    }

    public PortalType getPortalType() {
        return this.c;
    }

    public static enum PortalType {
        NETHER,
        END;

    }
}

