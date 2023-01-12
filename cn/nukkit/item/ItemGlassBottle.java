/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;

public class ItemGlassBottle
extends Item {
    public ItemGlassBottle() {
        this((Integer)0, 1);
    }

    public ItemGlassBottle(Integer n) {
        this(n, 1);
    }

    public ItemGlassBottle(Integer n, int n2) {
        super(374, n, n2, "Glass Bottle");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (block2.getId() == 8 || block2.getId() == 9) {
            Item item = Item.get(373);
            if (this.count == 1) {
                player.getInventory().setItemInHand(item);
            } else if (this.count > 1) {
                --this.count;
                player.getInventory().setItemInHand(this);
                if (player.getInventory().canAddItem(item)) {
                    player.getInventory().addItem(item);
                } else {
                    player.getLevel().dropItem(player.add(0.0, 1.3, 0.0), item, player.getDirectionVector().multiply(0.4));
                }
            }
        }
        return false;
    }
}

