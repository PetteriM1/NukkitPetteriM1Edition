/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.inventory.RecipeType;
import cn.nukkit.item.Item;
import java.util.UUID;

public class MultiRecipe
implements Recipe {
    private final UUID b;
    private final int a;

    public MultiRecipe(UUID uUID) {
        this.b = uUID;
        this.a = ++CraftingManager.u;
    }

    @Override
    public Item getResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerToCraftingManager(CraftingManager craftingManager) {
        this.registerToCraftingManager(407, craftingManager);
    }

    @Override
    public void registerToCraftingManager(int n, CraftingManager craftingManager) {
        craftingManager.registerMultiRecipe(n, this);
    }

    @Override
    public RecipeType getType() {
        return RecipeType.MULTI;
    }

    public UUID getId() {
        return this.b;
    }

    public int getNetworkId() {
        return this.a;
    }
}

