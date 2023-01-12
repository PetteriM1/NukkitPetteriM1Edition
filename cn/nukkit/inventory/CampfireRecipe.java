/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.inventory.RecipeType;
import cn.nukkit.item.Item;

public class CampfireRecipe
implements Recipe {
    private final Item b;
    private Item a;

    public CampfireRecipe(Item item, Item item2) {
        this.b = item.clone();
        this.a = item2.clone();
    }

    public void setInput(Item item) {
        this.a = item.clone();
    }

    public Item getInput() {
        return this.a.clone();
    }

    @Override
    public Item getResult() {
        return this.b.clone();
    }

    @Override
    public void registerToCraftingManager(CraftingManager craftingManager) {
        this.registerToCraftingManager(0, craftingManager);
    }

    @Override
    public void registerToCraftingManager(int n, CraftingManager craftingManager) {
        craftingManager.registerCampfireRecipe(n, this);
    }

    @Override
    public RecipeType getType() {
        return this.a.hasMeta() ? RecipeType.CAMPFIRE_DATA : RecipeType.CAMPFIRE;
    }
}

