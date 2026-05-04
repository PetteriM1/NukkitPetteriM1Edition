package cn.nukkit.block;

import cn.nukkit.block.properties.OxidizationLevel;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;


public class BlockCopperGrate extends BlockCopperBase {

    public BlockCopperGrate() {
        // Does nothing
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }

    @Override
    public int getId() {
        return COPPER_GRATE;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Copper Grate";
    }

    @Override
    public OxidizationLevel getOxidizationLevel() {
        return OxidizationLevel.UNAFFECTED;
    }

    @Override
    public WaterloggingType getWaterloggingType() {
        return WaterloggingType.WHEN_PLACED_IN_WATER;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.COPPER_BLOCK;
        }
        return BlockTypes.IRON_BLOCK;
    }

    @Override
    protected int getCopperId(boolean waxed, OxidizationLevel oxidizationLevel) {
        if (oxidizationLevel == null) {
            return this.getId();
        }

        switch (oxidizationLevel) {
            case UNAFFECTED:
                return waxed ? WAXED_COPPER_GRATE : COPPER_GRATE;
            case EXPOSED:
                return waxed ? WAXED_EXPOSED_COPPER_GRATE : EXPOSED_COPPER_GRATE;
            case WEATHERED:
                return waxed ? WAXED_WEATHERED_COPPER_GRATE : WEATHERED_COPPER_GRATE;
            case OXIDIZED:
                return waxed ? WAXED_OXIDIZED_COPPER_GRATE : OXIDIZED_COPPER_GRATE;
            default:
                return this.getId();
        }
    }
}
