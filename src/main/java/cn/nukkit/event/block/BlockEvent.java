package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.Event;

/**
 * Generic Block event.
 * @author MagicDroidX
 */
public abstract class BlockEvent extends Event {

    protected final Block block;

    /**
     * Standard block Event.
     * @param block Block.
     */
    public BlockEvent(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
