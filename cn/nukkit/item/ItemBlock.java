/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.item.Item;

public class ItemBlock
extends Item {
    public ItemBlock(Block block) {
        this(block, 0, 1);
    }

    public ItemBlock(Block block, Integer n) {
        this(block, n, 1);
    }

    public ItemBlock(Block block, int n) {
        this(block, n, 1);
    }

    public ItemBlock(Block block, Integer n, int n2) {
        super(block.getId() > 255 ? 255 - block.getId() : block.getId(), n, n2, block.getName());
        this.block = block;
    }

    public ItemBlock(Block block, int n, int n2) {
        super(block.getId() > 255 ? 255 - block.getId() : block.getId(), n, n2, block.getName());
        this.block = block;
    }

    @Override
    public void setDamage(Integer n) {
        if (n != null) {
            this.meta = n & 0xFFFF;
        } else {
            this.hasMeta = false;
        }
        this.block.setDamage(n);
    }

    @Override
    public ItemBlock clone() {
        ItemBlock itemBlock = (ItemBlock)super.clone();
        itemBlock.block = this.block.clone();
        return itemBlock;
    }

    @Override
    public Block getBlock() {
        return this.block.clone();
    }

    @Override
    public int getMaxStackSize() {
        if (this.block.getId() == 218 || this.block.getId() == 205) {
            return 1;
        }
        return super.getMaxStackSize();
    }
}

