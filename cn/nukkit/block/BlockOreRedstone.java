/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.Utils;

public class BlockOreRedstone
extends BlockSolid {
    @Override
    public int getId() {
        return 73;
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public double getResistance() {
        return 15.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        return "Redstone Ore";
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= 4) {
            if (item.hasEnchantment(16)) {
                return new Item[]{this.toItem()};
            }
            int n = Utils.random.nextInt(2) + 4;
            Enchantment enchantment = item.getEnchantment(18);
            if (enchantment != null && enchantment.getLevel() >= 1) {
                n += Utils.random.nextInt(enchantment.getLevel() + 1);
            }
            return new Item[]{Item.get(331, 0, n)};
        }
        return new Item[0];
    }

    @Override
    public int onUpdate(int n) {
        if (n == 5) {
            this.getLevel().setBlock(this, Block.get(74), false, false);
            this.getLevel().scheduleUpdate(this, 600);
            return 4;
        }
        return 0;
    }

    @Override
    public int getDropExp() {
        return Utils.rand(1, 5);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}

