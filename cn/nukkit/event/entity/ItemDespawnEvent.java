/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;

public class ItemDespawnEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();

    public static HandlerList getHandlers() {
        return b;
    }

    public ItemDespawnEvent(EntityItem entityItem) {
        this.entity = entityItem;
    }

    @Override
    public EntityItem getEntity() {
        return (EntityItem)this.entity;
    }
}

