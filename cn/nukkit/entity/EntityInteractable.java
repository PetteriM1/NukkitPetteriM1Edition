/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;

public interface EntityInteractable {
    public String getInteractButtonText();

    default public String getInteractButtonText(Player player) {
        return this.getInteractButtonText();
    }

    public boolean canDoInteraction();
}

