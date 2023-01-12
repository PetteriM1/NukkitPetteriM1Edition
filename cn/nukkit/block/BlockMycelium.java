/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.event.block.BlockSpreadEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockMycelium
extends BlockSolid {
    @Override
    public String getName() {
        return "Mycelium";
    }

    @Override
    public int getId() {
        return 110;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{new ItemBlock(Block.get(3))};
    }

    @Override
    public int onUpdate(int n) {
        if (n == 2) {
            int n2 = Utils.rand((int)this.x - 1, (int)this.x + 1);
            int n3 = Utils.rand((int)this.y - 1, (int)this.y + 1);
            int n4 = Utils.rand((int)this.z - 1, (int)this.z + 1);
            Block block = this.getLevel().getBlock(n2, n3, n4);
            if (block.getId() == 3 && block.getDamage() == 0 && block.up() instanceof BlockAir) {
                BlockSpreadEvent blockSpreadEvent = new BlockSpreadEvent(block, this, Block.get(110));
                Server.getInstance().getPluginManager().callEvent(blockSpreadEvent);
                if (!blockSpreadEvent.isCancelled()) {
                    this.getLevel().setBlock(block, blockSpreadEvent.getNewState());
                }
            }
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        Block block;
        if (item.isShovel() && ((block = this.up()) instanceof BlockAir || block instanceof BlockFlowable)) {
            item.useOn(this);
            this.getLevel().setBlock(this, Block.get(198));
            if (player != null) {
                player.getLevel().addSound((Vector3)player, Sound.STEP_GRASS);
            }
            return true;
        }
        return false;
    }
}

