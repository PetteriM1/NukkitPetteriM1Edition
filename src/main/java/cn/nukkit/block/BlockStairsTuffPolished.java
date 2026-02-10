package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;

public class BlockStairsTuffPolished extends BlockStairs {

    public BlockStairsTuffPolished() {
        this(0);
    }

    public BlockStairsTuffPolished(int meta) {
        super(meta);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRAY_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public int getId() {
        return POLISHED_TUFF_STAIRS;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Polished Tuff Stairs";
    }

    @Override
    public double getResistance() {
        return 6;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol < ProtocolInfo.v1_17_0) {
            return BlockTypes.NORMAL_STONE_STAIRS;
        }
        return BlockTypes.POLISHED_DEEPSLATE_STAIRS;
    }
}
