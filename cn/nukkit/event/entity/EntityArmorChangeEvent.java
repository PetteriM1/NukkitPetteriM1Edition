/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.item.Item;

public class EntityArmorChangeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final Item d;
    private Item b;
    private final int e;

    public static HandlerList getHandlers() {
        return c;
    }

    public EntityArmorChangeEvent(Entity entity, Item item, Item item2, int n) {
        this.entity = entity;
        this.d = item;
        this.b = item2;
        this.e = n;
    }

    public int getSlot() {
        return this.e;
    }

    public Item getNewItem() {
        return this.b;
    }

    public void setNewItem(Item item) {
        this.b = item;
    }

    public Item getOldItem() {
        return this.d;
    }
}

