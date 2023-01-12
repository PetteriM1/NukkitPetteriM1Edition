/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;
import cn.nukkit.item.Item;

public class FurnaceBurnEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList e = new HandlerList();
    private final BlockEntityFurnace c;
    private final Item b;
    private short f;
    private boolean d = true;

    public static HandlerList getHandlers() {
        return e;
    }

    public FurnaceBurnEvent(BlockEntityFurnace blockEntityFurnace, Item item, short s2) {
        super(blockEntityFurnace.getBlock());
        this.b = item;
        this.f = s2;
        this.c = blockEntityFurnace;
    }

    public BlockEntityFurnace getFurnace() {
        return this.c;
    }

    public Item getFuel() {
        return this.b;
    }

    public short getBurnTime() {
        return this.f;
    }

    public void setBurnTime(short s2) {
        this.f = s2;
    }

    public boolean isBurning() {
        return this.d;
    }

    public void setBurning(boolean bl) {
        this.d = bl;
    }
}

