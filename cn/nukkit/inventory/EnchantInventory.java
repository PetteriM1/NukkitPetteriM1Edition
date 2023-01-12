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

public class EnchantInventory
extends FakeBlockUIComponent {
    public static final int ENCHANT_INPUT_ITEM_UI_SLOT = 14;
    public static final int ENCHANT_REAGENT_UI_SLOT = 15;

    public EnchantInventory(PlayerUIInventory playerUIInventory, Position position) {
        super(playerUIInventory, InventoryType.ENCHANT_TABLE, 14, position);
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        player.craftingType = 3;
    }

    public Item getInputSlot() {
        return this.getItem(0);
    }

    public Item getOutputSlot() {
        return this.getItem(0);
    }

    public Item getReagentSlot() {
        return this.getItem(1);
    }
}

