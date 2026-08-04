package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;

/**
 * @author Extollite
 * Nukkit Project
 */
public class PlayerLocallyInitializedEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public PlayerLocallyInitializedEvent(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
