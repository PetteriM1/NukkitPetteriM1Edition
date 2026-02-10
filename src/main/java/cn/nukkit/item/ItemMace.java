package cn.nukkit.item;

import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemMace extends ItemTool {

    public ItemMace() {
        this(0, 1);
    }

    public ItemMace(Integer meta) {
        this(meta, 1);
    }

    public ItemMace(Integer meta, int count) {
        super(MACE, meta, count, "Mace");
    }

    @Override
    public int getAttackDamage() {
        return 6;
    }

    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_MACE;
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        return protocol >= ProtocolInfo.v1_21_0;
    }
}
