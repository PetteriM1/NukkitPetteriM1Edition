/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.MixRecipe;
import cn.nukkit.inventory.RecipeType;
import cn.nukkit.item.Item;

public class ContainerRecipe
extends MixRecipe {
    public ContainerRecipe(Item item, Item item2, Item item3) {
        super(item, item2, item3);
    }

    @Override
    public void registerToCraftingManager(CraftingManager craftingManager) {
        this.registerToCraftingManager(407, craftingManager);
    }

    @Override
    public void registerToCraftingManager(int n, CraftingManager craftingManager) {
        craftingManager.registerContainerRecipe(n, this);
    }

    @Override
    public RecipeType getType() {
        throw new UnsupportedOperationException();
    }
}

