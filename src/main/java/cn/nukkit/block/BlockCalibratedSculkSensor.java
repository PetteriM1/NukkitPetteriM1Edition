package cn.nukkit.block;

import cn.nukkit.network.protocol.ProtocolInfo;

public class BlockCalibratedSculkSensor extends BlockSculkSensor {

    @Override
    public int getId() {
        return CALIBRATED_SCULK_SENSOR;
    }

    @Override
    public String getName() {
        return "Calibrated Sculk Sensor";
    }

    @Override
    public int getMinimumVersion() {
        return ProtocolInfo.v1_20_0_23;
    }
}
