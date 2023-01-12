/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class ItemSpawnEvent
extends EntityEvent {
    private static final HandlerList b = new HandlerList();

    public static HandlerList getHandlers() {
        return b;
    }

    public ItemSpawnEvent(EntityItem entityItem) {
        this.entity = entityItem;
    }

    @Override
    public EntityItem getEntity() {
        return (EntityItem)this.entity;
    }
}

