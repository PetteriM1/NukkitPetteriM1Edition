package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.transaction.CraftingTransaction;

/**
 * Event for anvils being damaged.
 * @author GameModsBR
 */
public class AnvilDamageEvent extends BlockFadeEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private final Player player;
    private final CraftingTransaction transaction;
    private final Cause cause;
    /**
     * This event is called when an anvil is damaged.
     * @param block The block (anvil) that has been damaged.
     * @param newState The new state of the anvil when damaged.
     * @param player The player who used the anvil.
     * @param transaction Crafting (anvil use) transaction.
     * @param cause Cause of the anvil being damaged.
     */
    public AnvilDamageEvent(Block block, Block newState, Player player, CraftingTransaction transaction, Cause cause) {
        super(block, newState);
        this.player = player;
        this.transaction = transaction;
        this.cause = cause;
    }

    public Player getPlayer() {
        return player;
    }

    public CraftingTransaction getTransaction() {
        return transaction;
    }

    public Cause getCause() {
        return cause;
    }

    public enum Cause {
        USE,
        IMPACT
    }
}
