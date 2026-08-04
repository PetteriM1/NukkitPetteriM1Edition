package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.material.BlockType;

import java.util.concurrent.ThreadLocalRandom;

public class BlockOreCopper extends BlockOre {

    public BlockOreCopper() {
        // Does nothing
    }

    @Override
    public int getDropExp() {
        return Utils.rand(0, 2);
    }

    @Override
    protected float getDropMultiplier() {
        return 3;
    }

    @Override
    public int getId() {
        return COPPER_ORE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_17_0;
    }

    @Override
    public String getName() {
        return "Copper Ore";
    }

    @Override
    protected int getRawMaterial() {
        return ItemID.RAW_COPPER;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.IRON_ORE;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= this.getToolTier()) {
            if (item.hasEnchantment(Enchantment.ID_SILK_TOUCH)) {
                return new Item[]{this.toItem()};
            }

            float multiplier = this.getDropMultiplier();
            int amount = (int) multiplier;
            if (amount > 1) {
                amount = 2 + ThreadLocalRandom.current().nextInt(amount + 1);
            }
            int fortuneLevel = NukkitMath.clamp(item.getEnchantmentLevel(Enchantment.ID_FORTUNE_DIGGING), 0, 3);
            if (fortuneLevel > 0) {
                int increase = ThreadLocalRandom.current().nextInt((int) (multiplier * fortuneLevel) + 1);
                amount += increase;
            }
            return new Item[]{Item.get(this.getRawMaterial(), this.getRawMaterialMeta(), amount)};
        } else {
            return new Item[0];
        }
    }
}
