package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.level.Location;

public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Location from;
    private Location to;
    private boolean resetBlocksAround;

    public PlayerMoveEvent(Player player, Location from, Location to) {
        this(player, from, to, true);
    }

    public PlayerMoveEvent(Player player, Location from, Location to, boolean resetBlocks) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.resetBlocksAround = resetBlocks;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public void setResetBlocksAround(boolean value) {
        this.resetBlocksAround = value;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Location getTo() {
        return to;
    }

    public boolean isResetBlocksAround() {
        return resetBlocksAround;
    }
}
