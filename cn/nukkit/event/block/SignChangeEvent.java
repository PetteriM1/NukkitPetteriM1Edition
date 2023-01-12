/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;

public class SignChangeEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    private final Player d;
    private final String[] c;

    public static HandlerList getHandlers() {
        return b;
    }

    public SignChangeEvent(Block block, Player player, String[] stringArray) {
        super(block);
        this.d = player;
        this.c = stringArray;
    }

    public Player getPlayer() {
        return this.d;
    }

    public String[] getLines() {
        return this.c;
    }

    public String getLine(int n) {
        return this.c[n];
    }

    public void setLine(int n, String string) {
        this.c[n] = string;
    }
}

