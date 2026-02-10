package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockSlabTuff extends BlockSlab {

    public BlockSlabTuff() {
        this(0);
    }

    public BlockSlabTuff(int meta) {
        super(meta, TUFF_DOUBLE_SLAB);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.STONE_SLAB;
        }
        return BlockTypes.COBBLED_DEEPSLATE_SLAB;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRAY_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{
                this.toItem()
        };
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public int getId() {
        return TUFF_SLAB;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getSlabName() {
        return "Tuff Slab";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean hasTopBit() {
        return (this.getDamage() & 0x01) == 1;
    }

    @Override
    public void setTopBit(boolean topBit) {
        this.setDamage(topBit ? 1 : 0);
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId(), 0), 0);
    }
}
