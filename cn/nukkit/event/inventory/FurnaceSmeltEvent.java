/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;
import cn.nukkit.item.Item;

public class FurnaceSmeltEvent
extends BlockEvent
implements Cancellable {
    private static final HandlerList c = new HandlerList();
    private final BlockEntityFurnace b;
    private final Item d;
    private Item e;

    public static HandlerList getHandlers() {
        return c;
    }

    public FurnaceSmeltEvent(BlockEntityFurnace blockEntityFurnace, Item item, Item item2) {
        super(blockEntityFurnace.getBlock());
        this.d = item.clone();
        this.d.setCount(1);
        this.e = item2;
        this.b = blockEntityFurnace;
    }

    public BlockEntityFurnace getFurnace() {
        return this.b;
    }

    public Item getSource() {
        return this.d;
    }

    public Item getResult() {
        return this.e;
    }

    public void setResult(Item item) {
        this.e = item;
    }
}

