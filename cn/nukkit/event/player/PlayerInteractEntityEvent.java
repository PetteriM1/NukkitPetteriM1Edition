/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class PlayerInteractEntityEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected final Entity entity;
    protected final Item item;
    protected final Vector3 clickedPos;

    public PlayerInteractEntityEvent(Player player, Entity entity, Item item, Vector3 vector3) {
        this.player = player;
        this.entity = entity;
        this.item = item;
        this.clickedPos = vector3;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Item getItem() {
        return this.item;
    }

    public Vector3 getClickedPos() {
        return this.clickedPos;
    }

    public static HandlerList getHandlers() {
        return b;
    }
}

