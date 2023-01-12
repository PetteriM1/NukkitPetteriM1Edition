/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.inventory.LoomInventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockLoom
extends BlockSolidMeta {
    public BlockLoom() {
        this(0);
    }

    public BlockLoom(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Loom";
    }

    @Override
    public int getId() {
        return 459;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public double getResistance() {
        return 12.5;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            player.addWindow(new LoomInventory(player.getUIInventory(), this), 2);
        }
        return true;
    }
}

