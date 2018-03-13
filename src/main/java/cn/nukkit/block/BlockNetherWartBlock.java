package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.utils.BlockColor;

public class BlockNetherWartBlock extends BlockSolid {

    public BlockNetherWartBlock() {
        this(0);
    }

    public BlockNetherWartBlock(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Nether Wart Block";
    }

    @Override
    public int getId() {
        return BLOCK_NETHER_WART_BLOCK;
    }

    @Override
    public double getResistance() {
        return 5;
    }

    @Override
    public double getHardness() {
        return 1;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{
                toItem()
        };
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }
}
