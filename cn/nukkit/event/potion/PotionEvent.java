/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.potion;

import cn.nukkit.event.Event;
import cn.nukkit.potion.Potion;

public abstract class PotionEvent
extends Event {
    private Potion b;

    public PotionEvent(Potion potion) {
        this.b = potion;
    }

    public Potion getPotion() {
        return this.b;
    }

    public void setPotion(Potion potion) {
        this.b = potion;
    }
}

