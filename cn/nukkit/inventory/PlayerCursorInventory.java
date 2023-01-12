/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerUIComponent;
import cn.nukkit.inventory.PlayerUIInventory;

public class PlayerCursorInventory
extends PlayerUIComponent {
    private final PlayerUIInventory e;

    PlayerCursorInventory(PlayerUIInventory playerUIInventory) {
        super(playerUIInventory, 0, 1);
        this.e = playerUIInventory;
    }

    @Override
    public Player getHolder() {
        return this.e.getHolder();
    }
}

