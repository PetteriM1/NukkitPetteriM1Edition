/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRedstoneDiode;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class BlockRedstoneRepeaterUnpowered
extends BlockRedstoneDiode {
    public BlockRedstoneRepeaterUnpowered() {
        this(0);
    }

    public BlockRedstoneRepeaterUnpowered(int n) {
        super(n);
        this.isPowered = false;
    }

    @Override
    public int getId() {
        return 93;
    }

    @Override
    public String getName() {
        return "Unpowered Repeater";
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        this.setDamage(this.getDamage() + 4);
        if (this.getDamage() > 15) {
            this.setDamage(this.getDamage() % 4);
        }
        this.level.setBlock(this, this, true, false);
        return true;
    }

    @Override
    public BlockFace getFacing() {
        return BlockFace.fromHorizontalIndex(this.getDamage());
    }

    @Override
    protected boolean isAlternateInput(Block block) {
        return BlockRedstoneRepeaterUnpowered.isDiode(block);
    }

    @Override
    public Item toItem() {
        return Item.get(356);
    }

    @Override
    protected int getDelay() {
        return 1 + (this.getDamage() >> 2) << 1;
    }

    @Override
    protected Block getPowered() {
        return Block.get(94, this.getDamage());
    }

    @Override
    protected Block getUnpowered() {
        return this;
    }

    @Override
    public boolean isLocked() {
        return this.getPowerOnSides() > 0;
    }
}

