/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Server;
import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.RecipeType;
import cn.nukkit.item.Item;

public interface Recipe {
    public static final String k = "Recipe#registerToCraftingManager(CraftingManager)";

    public Item getResult();

    public void registerToCraftingManager(CraftingManager var1);

    default public void registerToCraftingManager(int n, CraftingManager craftingManager) {
        Server.mvw(k);
        this.registerToCraftingManager(craftingManager);
    }

    public RecipeType getType();
}

