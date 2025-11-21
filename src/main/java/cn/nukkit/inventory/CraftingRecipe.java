package cn.nukkit.inventory;

import cn.nukkit.item.Item;

import java.util.List;
import java.util.UUID;

/**
 * @author CreeperFace
 */
public interface CraftingRecipe extends Recipe {

    List<Item> getAllResults();

    List<Item> getExtraResults();

    UUID getId();

    void setId(UUID id);

    List<Item> getIngredientsAggregate();

    int getPriority();

    String getRecipeId();

    boolean matchItems(List<Item> inputList, List<Item> extraOutputList, int multiplier);

    /**
     * Returns whether the specified list of crafting grid inputs and outputs matches this recipe. Outputs DO NOT
     * include the primary result item.
     *
     * @param inputList       list of items taken from the crafting grid
     * @param extraOutputList list of items put back into the crafting grid (secondary results)
     * @return bool
     */
    boolean matchItems(List<Item> inputList, List<Item> extraOutputList);

    boolean requiresCraftingTable();
}
