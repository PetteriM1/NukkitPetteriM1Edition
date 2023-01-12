/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.FakeBlockUIComponent;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.PlayerUIInventory;
import cn.nukkit.level.Position;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class BeaconInventory
extends FakeBlockUIComponent {
    private static final IntSet f = new IntOpenHashSet(new int[]{0, 742, 388, 264, 266, 265});

    public BeaconInventory(PlayerUIInventory playerUIInventory, Position position) {
        super(playerUIInventory, InventoryType.BEACON, 27, position);
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        player.craftingType = 4;
    }

    @Override
    public boolean allowedToAdd(int n) {
        return f.contains(n);
    }
}

