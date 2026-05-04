package cn.nukkit.block;

import cn.nukkit.Server;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemTool;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class BlockOreGold extends BlockOre {

    @Override
    public int getId() {
        return GOLD_ORE;
    }

    @Override
    public String getName() {
        return "Gold Ore";
    }

    @Override
    protected int getRawMaterial() {
        return Server.getInstance().useRawOres ? ItemID.RAW_GOLD : AIR;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_IRON;
    }
}
