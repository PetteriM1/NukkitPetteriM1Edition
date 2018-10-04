package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.PortalParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.EntityUtils;

public class BlockDragonEgg extends BlockFallable {

    public BlockDragonEgg() {
    }

    @Override
    public String getName() {
        return "Dragon Egg";
    }

    @Override
    public int getId() {
        return DRAGON_EGG;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 45;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.OBSIDIAN_BLOCK_COLOR;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        int x = EntityUtils.rand((int) this.x - 8, (int) this.x + 8);
        int z = EntityUtils.rand((int) this.z - 8, (int) this.z + 8);
        Vector3 loc = new Vector3(x, this.level.getHeightMap(x, z) + 1, z);
        this.getLevel().setBlock(this, Block.get(AIR), true, true);
        this.getLevel().addParticle(new PortalParticle(this));
        this.getLevel().addParticle(new PortalParticle(this));
        this.getLevel().addParticle(new PortalParticle(this));
        this.getLevel().setBlock(loc, Block.get(DRAGON_EGG), true, true);
        this.getLevel().addParticle(new PortalParticle(loc));
        this.getLevel().addParticle(new PortalParticle(loc));
        this.getLevel().addParticle(new PortalParticle(loc));
        return true;
    }
}
