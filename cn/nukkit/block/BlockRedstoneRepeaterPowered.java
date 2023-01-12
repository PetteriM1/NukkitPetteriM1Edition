/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRedstoneDiode;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class BlockRedstoneRepeaterPowered
extends BlockRedstoneDiode {
    public BlockRedstoneRepeaterPowered() {
        this(0);
    }

    public BlockRedstoneRepeaterPowered(int n) {
        super(n);
        this.isPowered = true;
    }

    @Override
    public int getId() {
        return 94;
    }

    @Override
    public String getName() {
        return "Powered Repeater";
    }

    @Override
    public BlockFace getFacing() {
        return BlockFace.fromHorizontalIndex(this.getDamage());
    }

    @Override
    protected boolean isAlternateInput(Block block) {
        return BlockRedstoneRepeaterPowered.isDiode(block);
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
        return this;
    }

    @Override
    protected Block getUnpowered() {
        return Block.get(93, this.getDamage());
    }

    @Override
    public int getLightLevel() {
        return 7;
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
    public boolean isLocked() {
        return this.getPowerOnSides() > 0;
    }
}

