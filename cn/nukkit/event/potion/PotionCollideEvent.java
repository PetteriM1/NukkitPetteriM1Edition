/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.potion;

import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.potion.PotionEvent;
import cn.nukkit.potion.Potion;

public class PotionCollideEvent
extends PotionEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private final EntityPotion c;

    public static HandlerList getHandlers() {
        return d;
    }

    public PotionCollideEvent(Potion potion, EntityPotion entityPotion) {
        super(potion);
        this.c = entityPotion;
    }

    public EntityPotion getThrownPotion() {
        return this.c;
    }
}

