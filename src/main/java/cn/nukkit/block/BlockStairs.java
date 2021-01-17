package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class BlockStairs extends BlockSolidMeta implements Faceable {

    private static final short[] faces = new short[]{2, 1, 3, 0};

    protected BlockStairs(int meta) {
        super(meta);
    }

    @Override
    protected AxisAlignedBB[] recalculateCollisionBoxes() {
        double minYSlab = (this.getDamage() & 0x04) == 0? 0 : 0.5;
        double maxYSlab = minYSlab + 0.5;

        AxisAlignedBB bb1 = new AxisAlignedBB(
                this.x,
                this.y + minYSlab,
                this.z,
                this.x + 1,
                this.y + maxYSlab,
                this.z + 1
        );

        int rotationMeta  = this.getDamage() & 0x03;
        double minY = (this.getDamage() & 0x04) == 0? 0.5 : 0;
        double maxY = minY + 0.5;
        double minX = 0;
        double maxX = 1;
        double minZ = 0;
        double maxZ = 1;

        switch (rotationMeta) {
            case 0:
                minX = 0.5;
                break;
            case 1:
                maxX = 0.5;
                break;
            case 2:
                minZ = 0.5;
                break;
            case 3:
                maxZ = 0.5;
                break;
        }

        AxisAlignedBB bb2 = new AxisAlignedBB(
                this.x + minX,
                this.y + minY,
                this.z + minZ,
                this.x + maxX,
                this.y + maxY,
                this.z + maxZ
        );
        return new AxisAlignedBB[]{bb1, bb2};
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setDamage(faces[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        if ((fy > 0.5 && face != BlockFace.UP) || face == BlockFace.DOWN) {
            this.setDamage(this.getDamage() | 0x04); //Upside-down stairs
        }
        this.getLevel().setBlock(block, this, true, true);

        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{
                  toItem()
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public Item toItem() {
        Item item = super.toItem();
        item.setDamage(0);
        return item;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }
}
