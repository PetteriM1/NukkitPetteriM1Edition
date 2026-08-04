package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockDoubleMudBrickSlab extends BlockDoubleSlabBase {

    public BlockDoubleMudBrickSlab() {
        this(0);
    }

    public BlockDoubleMudBrickSlab(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return MUD_BRICK_DOUBLE_SLAB;
    }

    @Override
    public String getSlabName() {
        return "Double Mud Brick Slab";
    }

    @Override
    public int getSingleSlabId() {
        return MUD_BRICK_SLAB;
    }

    @Override
    public int getItemDamage() {
        return 0;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_19_0;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DOUBLE_STONE_SLAB;
    }

    @Override
    public int getAlternateMeta(int protocol) {
        return BlockSlabStone.SANDSTONE;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
}
