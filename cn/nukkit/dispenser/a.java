/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.dispenser.ProjectileDispenseBehavior;

final class a
extends ProjectileDispenseBehavior {
    a(String string) {
        super(string);
    }

    @Override
    protected double getMotion() {
        return super.getMotion() * 1.5;
    }
}

