package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemWolfArmor extends Item {

    public ItemWolfArmor() {
        this(0, 1);
    }

    public ItemWolfArmor(Integer meta) {
        this(meta, 1);
    }

    public ItemWolfArmor(Integer meta, int count) {
        super(WOLF_ARMOR, meta, count, "Wolf Armor");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_20_80;
    }
}
