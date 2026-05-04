package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class PlayerBedEnterEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Block bed;
    private final boolean onlySetSpawn;

    public PlayerBedEnterEvent(Player player, Block bed) {
        this(player, bed, false);
    }

    public PlayerBedEnterEvent(Player player, Block bed, boolean onlySetSpawn) {
        this.player = player;
        this.bed = bed;
        this.onlySetSpawn = onlySetSpawn;
    }

    public Block getBed() {
        return bed;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Whether the event is called when a player is trying to only set spawn point and not actually sleep on the bed (on day).
     *
     * @return is only setting spawn point
     */
    public boolean isOnlySetSpawn() {
        return onlySetSpawn;
    }
}
