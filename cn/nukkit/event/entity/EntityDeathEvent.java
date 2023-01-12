/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.item.Item;

public class EntityDeathEvent
extends EntityEvent {
    private static final HandlerList c = new HandlerList();
    private Item[] b;

    public static HandlerList getHandlers() {
        return c;
    }

    public EntityDeathEvent(EntityLiving entityLiving) {
        this(entityLiving, new Item[0]);
    }

    public EntityDeathEvent(EntityLiving entityLiving, Item[] itemArray) {
        this.entity = entityLiving;
        this.b = itemArray;
    }

    public Item[] getDrops() {
        return this.b;
    }

    public void setDrops(Item[] itemArray) {
        if (itemArray == null) {
            itemArray = new Item[]{};
        }
        this.b = itemArray;
    }
}

