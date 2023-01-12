/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;

public interface EntityControllable {
    public void onPlayerInput(Player var1, double var2, double var4);

    default public void onJump(Player player, int n) {
    }
}

