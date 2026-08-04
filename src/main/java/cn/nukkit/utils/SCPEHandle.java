package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.DataPacket;

public interface SCPEHandle {

    boolean handleMove(Player p, DataPacket pk);
}
