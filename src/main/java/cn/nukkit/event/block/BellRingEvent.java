package cn.nukkit.event.block;

import cn.nukkit.block.BlockBell;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class BellRingEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final RingCause cause;
    private final Entity entity;
    public BellRingEvent(BlockBell bell, RingCause cause, Entity entity) {
        super(bell);
        this.cause = cause;
        this.entity = entity;
    }

    public enum RingCause {
        HUMAN_INTERACTION,
        REDSTONE,
        PROJECTILE,
        DROPPED_ITEM,
        UNKNOWN
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public BlockBell getBlock() {
        return (BlockBell) super.getBlock();
    }

    public Entity getEntity() {
        return entity;
    }

    public RingCause getCause() {
        return cause;
    }

}