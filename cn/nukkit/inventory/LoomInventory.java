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

public class LoomInventory
extends FakeBlockUIComponent {
    public static int OFFSET = 9;

    public LoomInventory(PlayerUIInventory playerUIInventory, Position position) {
        super(playerUIInventory, InventoryType.LOOM, OFFSET, position);
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        player.craftingType = 1004;
    }

    public Item getFirstItem() {
        return this.getItem(0);
    }

    public Item getSecondItem() {
        return this.getItem(1);
    }

    public void setFirstItem(Item item) {
        this.setItem(0, item);
    }

    public void setSecondItem(Item item) {
        this.setItem(1, item);
    }
}

