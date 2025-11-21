package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

/**
 * Event for Block spread.
 *
 * @author MagicDroidX
 */
public class BlockSpreadEvent extends BlockFormEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Block source;

    /**
     * Event for block spread, such as grass or mycelium.
     *
     * @param block    Block that is being spread.
     * @param source   The source block.
     * @param newState New state of spread block.
     */
    public BlockSpreadEvent(Block block, Block source, Block newState) {
        super(block, newState);
        this.source = source;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Block getSource() {
        return source;
    }
}
