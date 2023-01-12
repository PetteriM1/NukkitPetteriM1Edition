/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;

public interface EntityOwnable {
    public String getOwnerName();

    public void setOwnerName(String var1);

    public Player getOwner();
}

