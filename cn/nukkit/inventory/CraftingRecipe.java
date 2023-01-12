/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.Recipe;
import cn.nukkit.item.Item;
import java.util.List;
import java.util.UUID;

public interface CraftingRecipe
extends Recipe {
    public String getRecipeId();

    public UUID getId();

    public void setId(UUID var1);

    public boolean requiresCraftingTable();

    public List<Item> getExtraResults();

    public List<Item> getAllResults();

    public int getPriority();

    public boolean matchItems(List<Item> var1, List<Item> var2);

    public boolean matchItems(List<Item> var1, List<Item> var2, int var3);

    public List<Item> getIngredientsAggregate();
}

