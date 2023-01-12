/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

public class PlayerExperienceChangeEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final int e;
    private final int d;
    private int f;
    private int b;

    public static HandlerList getHandlers() {
        return c;
    }

    public PlayerExperienceChangeEvent(Player player, int n, int n2, int n3, int n4) {
        this.player = player;
        this.e = n;
        this.d = n2;
        this.f = n3;
        this.b = n4;
    }

    public int getOldExperience() {
        return this.e;
    }

    public int getOldExperienceLevel() {
        return this.d;
    }

    public int getNewExperience() {
        return this.f;
    }

    public void setNewExperience(int n) {
        this.f = n;
    }

    public int getNewExperienceLevel() {
        return this.b;
    }

    public void setNewExperienceLevel(int n) {
        this.b = n;
    }
}

