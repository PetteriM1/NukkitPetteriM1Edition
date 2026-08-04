package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

/**
 * Event for Block Update
 *
 * @author MagicDroidX
 */
public class BlockUpdateEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    /**
     * Event called on a block being updated.
     *
     * @param block Block updated.
     */
    public BlockUpdateEvent(Block block) {
        super(block);
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
