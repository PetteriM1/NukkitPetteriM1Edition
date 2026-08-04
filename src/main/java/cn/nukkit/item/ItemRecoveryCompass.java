package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemRecoveryCompass extends Item {

    public ItemRecoveryCompass() {
        this(0, 1);
    }

    public ItemRecoveryCompass(Integer meta) {
        this(meta, 1);
    }

    public ItemRecoveryCompass(Integer meta, int count) {
        super(RECOVERY_COMPASS, meta, count, "Recovery Compass");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_19_0_29;
    }
}
