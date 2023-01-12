/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.potion;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.potion.PotionEvent;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;

public class PotionApplyEvent
extends PotionEvent
implements Cancellable {
    private static final HandlerList d = new HandlerList();
    private Effect e;
    private final Entity c;

    public static HandlerList getHandlers() {
        return d;
    }

    public PotionApplyEvent(Potion potion, Effect effect, Entity entity) {
        super(potion);
        this.e = effect;
        this.c = entity;
    }

    public Entity getEntity() {
        return this.c;
    }

    public Effect getApplyEffect() {
        return this.e;
    }

    public void setApplyEffect(Effect effect) {
        this.e = effect;
    }
}

