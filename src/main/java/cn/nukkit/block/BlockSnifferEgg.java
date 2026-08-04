package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.material.BlockType;

public class BlockSnifferEgg extends BlockTransparentMeta {

    public BlockSnifferEgg() {
        this(0);
    }

    public BlockSnifferEgg(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Sniffer Egg";
    }

    @Override
    public int getId() {
        return SNIFFER_EGG;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public WaterloggingType getWaterloggingType() {
        return WaterloggingType.WHEN_PLACED_IN_WATER;
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }

    @Override
    public BlockType getAlternateBlock(int protocol) {
        return BlockTypes.DRAGON_EGG;
    }
}
