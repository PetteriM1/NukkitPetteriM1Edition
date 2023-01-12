/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.level.Position;

public class FakeBlockMenu
extends Position
implements InventoryHolder {
    private final Inventory a;

    public FakeBlockMenu(Inventory inventory, Position position) {
        super(position.x, position.y, position.z, position.level);
        this.a = inventory;
    }

    @Override
    public Inventory getInventory() {
        return this.a;
    }
}

