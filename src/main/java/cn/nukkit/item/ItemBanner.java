package cn.nukkit.item;

import cn.nukkit.block.BlockBanner;

/**
 * Created by PetteriM1
 */
public class ItemBanner extends Item {

    public ItemBanner() {
        this(0, 1);
    }

    public ItemBanner(Integer meta) {
        this(meta, 1);
    }

    public ItemBanner(Integer meta, int count) {
        super(BANNER, meta, count, "Banner");
        this.block = new BlockBanner();
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }
}
