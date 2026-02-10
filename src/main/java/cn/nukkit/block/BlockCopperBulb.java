package cn.nukkit.block;

import cn.nukkit.block.properties.OxidizationLevel;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.material.BlockType;


public class BlockCopperBulb extends BlockCopperBase {

    public BlockCopperBulb() {
        // Does nothing
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        if (protocol >= ProtocolInfo.v1_17_0) {
            return BlockTypes.COPPER_BLOCK;
        }
        return BlockTypes.IRON_BLOCK;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }

    @Override
    protected int getCopperId(boolean waxed, OxidizationLevel oxidizationLevel) {
        if (oxidizationLevel == null) {
            return this.getId();
        }

        switch (oxidizationLevel) {
            case UNAFFECTED:
                return waxed ? WAXED_COPPER_BULB : COPPER_BULB;
            case EXPOSED:
                return waxed ? WAXED_EXPOSED_COPPER_BULB : EXPOSED_COPPER_BULB;
            case WEATHERED:
                return waxed ? WAXED_WEATHERED_COPPER_BULB : WEATHERED_COPPER_BULB;
            case OXIDIZED:
                return waxed ? WAXED_OXIDIZED_COPPER_BULB : OXIDIZED_COPPER_BULB;
            default:
                return this.getId();
        }
    }

    @Override
    public int getId() {
        return COPPER_BULB;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_21_0;
    }

    @Override
    public String getName() {
        return "Copper Bulb";
    }

    @Override
    public OxidizationLevel getOxidizationLevel() {
        return OxidizationLevel.UNAFFECTED;
    }
}
