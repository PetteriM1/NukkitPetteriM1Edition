package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockChain extends BlockTransparentMeta {

    public BlockChain() {
        this(0);
    }

    public BlockChain(int meta) {
        super(meta);
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public int getId() {
        return CHAIN_BLOCK;
    }

    @Override
    public double getMaxX() {
        return x + 9 / 16.0;
    }

    @Override
    public double getMaxZ() {
        return z + 9 / 16.0;
    }

    @Override
    public double getMinX() {
        return x + 7 / 16.0;
    }

    @Override
    public double getMinZ() {
        return z + 7 / 16.0;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_16_0;
    }

    @Override
    public String getName() {
        return "Iron Chain";
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
    public double getResistance() {
        return 6;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public WaterloggingType getWaterloggingType() {
        return WaterloggingType.WHEN_PLACED_IN_WATER;
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
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.GLASS_PANE;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setPillarAxis(face.getAxis());
        if (super.place(item, block, target, face, fx, fy, fz, player)) {
            return true;
        }
        return false;
    }

    @Override
    public Item toItem() {
        return Item.get(Item.CHAIN);
    }
}
