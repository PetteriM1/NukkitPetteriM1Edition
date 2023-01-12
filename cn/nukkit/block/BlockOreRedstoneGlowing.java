/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockOreRedstone;
import cn.nukkit.event.block.BlockFadeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockOreRedstoneGlowing
extends BlockOreRedstone {
    @Override
    public String getName() {
        return "Glowing Redstone Ore";
    }

    @Override
    public int getId() {
        return 74;
    }

    @Override
    public int getLightLevel() {
        return 9;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(73));
    }

    @Override
    public int onUpdate(int n) {
        if (n == 3 || n == 2) {
            BlockFadeEvent blockFadeEvent = new BlockFadeEvent(this, BlockOreRedstoneGlowing.get(73));
            this.level.getServer().getPluginManager().callEvent(blockFadeEvent);
            if (!blockFadeEvent.isCancelled()) {
                this.level.setBlock(this, blockFadeEvent.getNewState(), false, false);
            }
            return 4;
        }
        return 0;
    }
}

