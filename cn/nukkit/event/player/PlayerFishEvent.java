/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityFishingHook;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class PlayerFishEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final EntityFishingHook c;
    private Item b;
    private int f;
    private Vector3 e;

    public static HandlerList getHandlers() {
        return d;
    }

    public PlayerFishEvent(Player player, EntityFishingHook entityFishingHook, Item item, int n, Vector3 vector3) {
        this.player = player;
        this.c = entityFishingHook;
        this.b = item;
        this.f = n;
        this.e = vector3;
    }

    public EntityFishingHook getHook() {
        return this.c;
    }

    public Item getLoot() {
        return this.b;
    }

    public void setLoot(Item item) {
        this.b = item;
    }

    public int getExperience() {
        return this.f;
    }

    public void setExperience(int n) {
        this.f = n;
    }

    public Vector3 getMotion() {
        return this.e;
    }

    public void setMotion(Vector3 vector3) {
        this.e = vector3;
    }
}

