/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class AnvilDamageEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final int e;
    private int f;
    private final DamageCause b;
    private final Player d;

    public static HandlerList getHandlers() {
        return c;
    }

    public AnvilDamageEvent(Block block, int n, int n2, DamageCause damageCause, Player player) {
        super(block);
        this.e = n;
        this.f = n2;
        this.b = damageCause;
        this.d = player;
    }

    public int getOldDamage() {
        return this.e;
    }

    public int getNewDamage() {
        return this.f;
    }

    public void setNewDamage(int n) {
        this.f = n;
    }

    public DamageCause getCause() {
        return this.b;
    }

    public Player getPlayer() {
        return this.d;
    }

    public static enum DamageCause {
        USE,
        FALL;

    }
}

