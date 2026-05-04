package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemWindCharge extends ProjectileItem {

    public ItemWindCharge() {
        this(0, 1);
    }

    public ItemWindCharge(Integer meta) {
        this(meta, 1);
    }

    public ItemWindCharge(Integer meta, int count) {
        super(WIND_CHARGE, meta, count, "Wind Charge");
    }

    @Override
    public String getProjectileEntityType() {
        return "WindCharge";
    }

    @Override
    public float getThrowForce() {
        return 1.5f;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}
