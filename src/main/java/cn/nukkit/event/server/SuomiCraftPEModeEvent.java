package cn.nukkit.event.server;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import lombok.Getter;

public class SuomiCraftPEModeEvent extends ServerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final EventType event;
    @Getter
    private final Player player;
    @Getter
    private final Object data;

    public SuomiCraftPEModeEvent(EventType event, Player player, Object data) {
        this.event = event;
        this.player = player;
        this.data = data;
    }

    public enum EventType {
        RECV_LOGIN_PACKET,
        RECV_INVENTORY_TRANSACTION_PACKET,
        SEND_SKIN_PACKET,
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
