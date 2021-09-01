package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockChorusPlant extends BlockTransparent {

    @Override
    public int getId() {
        return CHORUS_PLANT;
    }

    @Override
    public String getName() {
        return "Chorus Plant";
    }

    @Override
    public double getHardness() {
        return 0.4;
    }

    @Override
    public double getResistance() {
        return 0.4;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        Block down = block.down();
        if (!(down instanceof BlockEndStone) &&
        !(down instanceof BlockChorusPlant) &&
        !(block.north() instanceof BlockChorusPlant) &&
        !(block.east() instanceof BlockChorusPlant) &&
        !(block.south() instanceof BlockChorusPlant) &&
        !(block.west() instanceof BlockChorusPlant)) {
            return false;
        }

        this.level.setBlock(block, this, true, true);
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.down().isTransparent() &&
            !(this.down() instanceof BlockChorusPlant) &&
            !(this.north() instanceof BlockChorusPlant) &&
            !(this.east() instanceof BlockChorusPlant) &&
            !(this.south() instanceof BlockChorusPlant) &&
            !(this.west() instanceof BlockChorusPlant)) {
                this.getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.hasEnchantment(Enchantment.ID_SILK_TOUCH)) {
            return new Item[]{this.toItem()};
        }
        return Utils.rand() ? new Item[]{Item.get(ItemID.CHORUS_FRUIT, 0, 1)} : new Item[0];
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }
}
