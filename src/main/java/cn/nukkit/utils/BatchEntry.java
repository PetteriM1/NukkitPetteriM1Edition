package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.DataPacket;

public class BatchEntry {

    public final Player[] players;
    public final DataPacket[] packets;

    public BatchEntry(Player[] players, DataPacket[] packets) {
        this.players = players;
        this.packets = packets;
    }
}
