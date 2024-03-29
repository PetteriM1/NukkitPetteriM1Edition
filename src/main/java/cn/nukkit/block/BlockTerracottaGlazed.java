package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;

/**
 * Created by CreeperFace on 2.6.2017.
 */
public abstract class BlockTerracottaGlazed extends BlockSolidMeta {

    public BlockTerracottaGlazed() {
        this(0);
    }

    public BlockTerracottaGlazed(int meta) {
        super(meta);
    }

    @Override
    public double getResistance() {
        return 7;
    }

    @Override
    public double getHardness() {
        return 1.4;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public Item[] getDrops(Item item) {
        return item.getTier() >= ItemTool.TIER_WOODEN ? new Item[]{this.toItem()} : new Item[0];
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setDamage(Block.FACES2534[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        return this.getLevel().setBlock(block, this, true, true);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return DyeColor.getByDyeData(getDyeColor().getDyeData()).getColor();
    }

    public DyeColor getDyeColor() {
        return DyeColor.BLACK;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId(), 0), 0);
    }
}
