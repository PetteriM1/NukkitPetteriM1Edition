package cn.nukkit.block.custom.container;

import cn.nukkit.level.GlobalBlockPalette;

public interface BlockContainer {

    default int getNukkitDamage() {
        return 0;
    }

    int getNukkitId();

    default int getRuntimeId(int protocol) {
        return GlobalBlockPalette.getOrCreateRuntimeId(protocol, this.getNukkitId(), this.getNukkitDamage());
    }
}
