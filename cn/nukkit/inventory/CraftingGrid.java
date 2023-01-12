/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.PlayerUIComponent;
import cn.nukkit.inventory.PlayerUIInventory;

public class CraftingGrid
extends PlayerUIComponent {
    CraftingGrid(PlayerUIInventory playerUIInventory) {
        this(playerUIInventory, 28, 4);
    }

    CraftingGrid(PlayerUIInventory playerUIInventory, int n, int n2) {
        super(playerUIInventory, n, n2);
    }
}

