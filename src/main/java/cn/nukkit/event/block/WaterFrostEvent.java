package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

/**
 * Event for Water freezing.
 */
public class WaterFrostEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Event called on water freezing (currently used for frost walker).
     * @param block Block frozen.
     */
    public WaterFrostEvent(Block block) {
        super(block);
    }
}
