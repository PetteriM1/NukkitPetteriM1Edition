/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.item.Item;

public class EntityInventoryChangeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private final Item b;
    private Item d;
    private final int c;

    public static HandlerList getHandlers() {
        return e;
    }

    public EntityInventoryChangeEvent(Entity entity, Item item, Item item2, int n) {
        this.entity = entity;
        this.b = item;
        this.d = item2;
        this.c = n;
    }

    public int getSlot() {
        return this.c;
    }

    public Item getNewItem() {
        return this.d;
    }

    public void setNewItem(Item item) {
        this.d = item;
    }

    public Item getOldItem() {
        return this.b;
    }
}

