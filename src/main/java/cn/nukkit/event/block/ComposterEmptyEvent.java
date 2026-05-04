package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class ComposterEmptyEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private Item drop;
    private Item itemUsed;
    private int newLevel;
    private Vector3 motion;

    public ComposterEmptyEvent(Block block, Player player, Item itemUsed, Item drop, int newLevel) {
        super(block);
        this.player = player;
        this.drop = drop;
        this.itemUsed = itemUsed;
        this.newLevel = Math.max(0, Math.min(newLevel, 8));
    }

    public void setDrop(Item drop) {
        if (drop == null) {
            drop = Item.get(Item.AIR);
        } else {
            drop = drop.clone();
        }
        this.drop = drop;
    }

    public void setItemUsed(Item itemUsed) {
        this.itemUsed = itemUsed;
    }

    public void setMotion(Vector3 motion) {
        this.motion = motion;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = Math.max(0, Math.min(newLevel, 8));
    }

    public Item getDrop() {
        return drop.clone();
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Item getItemUsed() {
        return itemUsed;
    }

    public Vector3 getMotion() {
        return motion;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public Player getPlayer() {
        return player;
    }

}