package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;

/**
 * Created by PetteriM1
 */
public class BlockIceFrosted extends BlockIce {

    public BlockIceFrosted() {
    }

    @Override
    public int getId() {
        return FROSTED_ICE;
    }

    @Override
    public String getName() {
        return "Frosted Ice";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_NONE;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(new BlockAir());
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (this.getLevel().getBlockLightAt((int) this.x, (int) this.y, (int) this.z) >= 12 || (level.getTime() % Level.TIME_FULL < 13184 || level.getTime() % Level.TIME_FULL > 22800)) {
                this.getLevel().setBlock(this, new BlockWater(), true);
            }
            return Level.BLOCK_UPDATE_RANDOM;
        }
        return 0;
    }
}
