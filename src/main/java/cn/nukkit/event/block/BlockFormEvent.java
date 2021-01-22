package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

/**
 * Event for Block form State.
 * @author MagicDroidX
 */
public class BlockFormEvent extends BlockGrowEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Block Form Event is called on block form state.
     * @param block Block affected by event.
     * @param newState New state of block.
     */
    public BlockFormEvent(Block block, Block newState) {
        super(block, newState);
    }
}
