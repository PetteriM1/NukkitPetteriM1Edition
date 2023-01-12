/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCrops;
import cn.nukkit.event.Event;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Utils;

public class BlockStemPumpkin
extends BlockCrops {
    public BlockStemPumpkin() {
        this(0);
    }

    public BlockStemPumpkin(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 104;
    }

    @Override
    public String getName() {
        return "Pumpkin Stem";
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (this.down().getId() != 60) {
                this.getLevel().useBreakOn(this);
                return 1;
            }
        } else if (n == 2) {
            if (Utils.rand()) {
                Object object;
                Object object22;
                if (this.getDamage() < 7) {
                    Block block = this.clone();
                    block.setDamage(block.getDamage() + 1);
                    BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, block);
                    Server.getInstance().getPluginManager().callEvent(blockGrowEvent);
                    if (!blockGrowEvent.isCancelled()) {
                        this.getLevel().setBlock(this, blockGrowEvent.getNewState(), true);
                    }
                    return 2;
                }
                for (Object object22 : BlockFace.Plane.HORIZONTAL) {
                    object = this.getSide((BlockFace)((Object)object22));
                    if (((Block)object).getId() != 86) continue;
                    return 2;
                }
                Block block = this.getSide(BlockFace.Plane.HORIZONTAL.random(Utils.nukkitRandom));
                if (block.getId() == 0 && (((Block)(object22 = block.down())).getId() == 60 || ((Block)object22).getId() == 2 || ((Block)object22).getId() == 3)) {
                    object = new BlockGrowEvent(block, Block.get(86));
                    Server.getInstance().getPluginManager().callEvent((Event)object);
                    if (!((Event)object).isCancelled()) {
                        this.getLevel().setBlock(block, ((BlockGrowEvent)object).getNewState(), true);
                    }
                }
            }
            return 2;
        }
        return 0;
    }

    @Override
    public Item toItem() {
        return Item.get(361);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (this.getDamage() < 4) {
            return new Item[0];
        }
        return new Item[]{Item.get(361, 0, Utils.rand(0, 48) >> 4)};
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

