/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;

public interface EntitySpawner {
    public void spawn();

    public void spawn(Player var1, Position var2, Level var3);

    public int getEntityNetworkId();
}

