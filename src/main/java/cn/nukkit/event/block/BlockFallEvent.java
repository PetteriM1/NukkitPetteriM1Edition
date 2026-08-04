package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

/**
 * Event for Block falling
 */
public class BlockFallEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    /**
     * This event is called when a block is falling.
     *
     * @param block Block that has fallen.
     */
    public BlockFallEvent(Block block) {
        super(block);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
