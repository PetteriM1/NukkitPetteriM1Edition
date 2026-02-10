package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockCherryLog extends BlockWood {

    public BlockCherryLog() {
        this(0);
    }

    public BlockCherryLog(int meta) {
        super(meta);
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.LOG;
    }

    @Override
    public int getId() {
        return CHERRY_LOG;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public String getName() {
        return "Cherry Log";
    }

    public BlockFace.Axis getPillarAxis() {
        switch (this.getDamage() % 3) {
            case 2:
                return BlockFace.Axis.Z;
            case 1:
                return BlockFace.Axis.X;
            case 0:
            default:
                return BlockFace.Axis.Y;
        }
    }

    @Override
    protected int getStrippedDamage() {
        return getDamage();
    }

    @Override
    protected int getStrippedId() {
        return STRIPPED_CHERRY_LOG;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setPillarAxis(face.getAxis());
        return this.getLevel().setBlock(block, this, true, true);
    }

    public void setPillarAxis(BlockFace.Axis axis) {
        switch (axis) {
            case Y:
                this.setDamage(0);
                break;
            case X:
                this.setDamage(1);
                break;
            case Z:
                this.setDamage(2);
                break;
        }
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId(), 0), 0);
    }
}
