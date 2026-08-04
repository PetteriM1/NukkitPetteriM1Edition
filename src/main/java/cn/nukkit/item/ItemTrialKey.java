package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemTrialKey extends Item {

    public ItemTrialKey() {
        this(0, 1);
    }

    public ItemTrialKey(Integer meta) {
        this(meta, 1);
    }

    public ItemTrialKey(Integer meta, int count) {
        super(TRIAL_KEY, meta, count, "Trial Key");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}
