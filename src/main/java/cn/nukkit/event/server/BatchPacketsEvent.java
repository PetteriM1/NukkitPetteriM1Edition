package cn.nukkit.event.server;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.network.protocol.DataPacket;

@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class BatchPacketsEvent extends ServerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player[] players;
    private final DataPacket[] packets;
    public BatchPacketsEvent(Player[] players, DataPacket[] packets, boolean forceSync) {
        this.players = players;
        this.packets = packets;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public DataPacket[] getPackets() {
        return packets;
    }

    public Player[] getPlayers() {
        return players;
    }

    @Deprecated
    public boolean isForceSync() {
        return true;
    }
}
