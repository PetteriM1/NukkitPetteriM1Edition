/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.entity.EntityEvent;
import cn.nukkit.level.Position;
import java.util.List;

public class EntityExplodeEvent
extends EntityEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected final Position position;
    protected List<Block> blocks;
    protected double yield;

    public static HandlerList getHandlers() {
        return b;
    }

    public EntityExplodeEvent(Entity entity, Position position, List<Block> list, double d2) {
        this.entity = entity;
        this.position = position;
        this.blocks = list;
        this.yield = d2;
    }

    public Position getPosition() {
        return this.position;
    }

    public List<Block> getBlockList() {
        return this.blocks;
    }

    public void setBlockList(List<Block> list) {
        this.blocks = list;
    }

    public double getYield() {
        return this.yield;
    }

    public void setYield(double d2) {
        this.yield = d2;
    }
}

