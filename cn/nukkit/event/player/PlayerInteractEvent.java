/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

public class PlayerInteractEvent
extends PlayerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected final Block blockTouched;
    protected final Vector3 touchVector;
    protected final BlockFace blockFace;
    protected final Item item;
    protected final Action action;

    public static HandlerList getHandlers() {
        return b;
    }

    public PlayerInteractEvent(Player player, Item item, Vector3 vector3, BlockFace blockFace) {
        this(player, item, vector3, blockFace, Action.RIGHT_CLICK_BLOCK);
    }

    public PlayerInteractEvent(Player player, Item item, Vector3 vector3, BlockFace blockFace, Action action) {
        if (vector3 instanceof Block) {
            this.blockTouched = (Block)vector3;
            this.touchVector = new Vector3(0.0, 0.0, 0.0);
        } else {
            this.touchVector = vector3;
            this.blockTouched = Block.get(0, 0, new Position(0.0, 0.0, 0.0, player.level));
        }
        this.player = player;
        this.item = item;
        this.blockFace = blockFace;
        this.action = action;
    }

    public Action getAction() {
        return this.action;
    }

    public Item getItem() {
        return this.item;
    }

    public Block getBlock() {
        return this.blockTouched;
    }

    public Vector3 getTouchVector() {
        return this.touchVector;
    }

    public BlockFace getFace() {
        return this.blockFace;
    }

    public static enum Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL;

    }
}

