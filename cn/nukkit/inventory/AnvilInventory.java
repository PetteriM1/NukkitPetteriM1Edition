/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.FakeBlockUIComponent;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.PlayerUIInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;

public class AnvilInventory
extends FakeBlockUIComponent {
    public static final int ANVIL_INPUT_UI_SLOT = 1;
    public static final int ANVIL_MATERIAL_UI_SLOT = 2;
    public static final int ANVIL_OUTPUT_UI_SLOT = 50;
    public static final int TARGET = 0;
    public static final int SACRIFICE = 1;
    public static final int RESULT = 49;
    private int f;

    public AnvilInventory(PlayerUIInventory playerUIInventory, Position position) {
        super(playerUIInventory, InventoryType.ANVIL, 1, position);
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        player.craftingType = 2;
    }

    public Item getInputSlot() {
        return this.getItem(0);
    }

    public Item getMaterialSlot() {
        return this.getItem(1);
    }

    public Item getOutputSlot() {
        return this.getItem(49);
    }

    public int getCost() {
        return this.f;
    }

    public void setCost(int n) {
        this.f = n;
    }
}

