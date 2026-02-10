package cn.nukkit.inventory;

import cn.nukkit.item.Item;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public interface Recipe {

    Item getResult();

    RecipeType getType();

    void registerToCraftingManager(CraftingManager manager);
}
