/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.dispenser.ProjectileDispenseBehavior;

final class d
extends ProjectileDispenseBehavior {
    d(String string) {
        super(string);
    }

    @Override
    protected float getAccuracy() {
        return super.getAccuracy() * 0.5f;
    }

    @Override
    protected double getMotion() {
        return super.getMotion() * 1.25;
    }
}

