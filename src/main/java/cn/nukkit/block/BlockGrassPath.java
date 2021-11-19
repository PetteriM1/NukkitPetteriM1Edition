package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2015/11/22 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockGrassPath extends BlockGrass {

    @Override
    public int getId() {
        return GRASS_PATH;
    }

    @Override
    public String getName() {
        return "Grass Path";
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new SimpleAxisAlignedBB(this.x, this.y, this.z, this.x + 1, this.y + 1, this.z + 1);
    }

    @Override
    public double getResistance() {
        return 3.25;
    }

    @Override
    public int onUpdate(int type) {
        return 0;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.isHoe()) {
            Block up = this.up();
            if (up instanceof BlockAir || up instanceof BlockFlowable) {
                item.useOn(this);
                this.getLevel().setBlock(this, get(FARMLAND), true);
                if (player != null) {
                    player.getLevel().addSoundToViewers(player, Sound.STEP_GRASS);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}
