package cn.nukkit.block;

import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockVinesTwisting extends BlockVinesNether {

    public BlockVinesTwisting() {
        this(0);
    }

    public BlockVinesTwisting(int meta) {
        super(meta);
    }

    @Override
    public void setVineAge(int vineAge) {
        this.setDamage(vineAge & 0x19);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CYAN_BLOCK_COLOR;
    }

    @Override
    public BlockFace getGrowthDirection() {
        return BlockFace.UP;
    }

    @Override
    public int getId() {
        return TWISTING_VINES;
    }

    @Override
    public int getMaxVineAge() {
        return 25;
    }

    @Override
    public String getName() {
        return "Twisting Vines";
    }

    @Override
    public int getVineAge() {
        return this.getDamage();
    }
}
