package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemOminousTrialKey extends Item {

    public ItemOminousTrialKey() {
        this(0, 1);
    }

    public ItemOminousTrialKey(Integer meta) {
        this(meta, 1);
    }

    public ItemOminousTrialKey(Integer meta, int count) {
        super(OMINOUS_TRIAL_KEY, meta, count, "Ominous Trial Key");
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}
