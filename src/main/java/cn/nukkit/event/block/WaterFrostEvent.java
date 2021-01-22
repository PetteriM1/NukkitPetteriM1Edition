package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

/**
 * Event for water freezing
 */
public class WaterFrostEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Event called on water frozen.
     * @param block Block affected. Contains Block data.
     */
    public WaterFrostEvent(Block block) {
        super(block);
    }
}
