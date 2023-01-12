/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.dispenser.ProjectileDispenseBehavior;

final class e
extends ProjectileDispenseBehavior {
    e(String string) {
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

