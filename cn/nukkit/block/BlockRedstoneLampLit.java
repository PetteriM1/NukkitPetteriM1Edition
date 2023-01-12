/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRedstoneLamp;
import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockRedstoneLampLit
extends BlockRedstoneLamp {
    @Override
    public String getName() {
        return "Lit Redstone Lamp";
    }

    @Override
    public int getId() {
        return 124;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(123));
    }

    @Override
    public int onUpdate(int n) {
        if (!(n != 1 && n != 6 || this.level.isBlockPowered(this.getLocation()))) {
            RedstoneUpdateEvent redstoneUpdateEvent = new RedstoneUpdateEvent(this);
            this.getLevel().getServer().getPluginManager().callEvent(redstoneUpdateEvent);
            if (redstoneUpdateEvent.isCancelled()) {
                return 0;
            }
            this.level.scheduleUpdate(this, 4);
            return 1;
        }
        if (n == 3 && !this.level.isBlockPowered(this.getLocation())) {
            this.level.setBlock(this, Block.get(123), false, false);
        }
        return 0;
    }
}

