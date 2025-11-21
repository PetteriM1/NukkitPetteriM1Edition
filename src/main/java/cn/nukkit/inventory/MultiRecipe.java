package cn.nukkit.inventory;

import cn.nukkit.item.Item;

import java.util.UUID;

public class MultiRecipe implements Recipe {

    private final UUID id;

    private final int networkId;

    public MultiRecipe(UUID id) {
        this.id = id;
        this.networkId = ++CraftingManager.NEXT_NETWORK_ID;
    }

    public UUID getId() {
        return this.id;
    }

    public int getNetworkId() {
        return this.networkId;
    }

    @Override
    public Item getResult() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RecipeType getType() {
        return RecipeType.MULTI;
    }

    @Override
    public void registerToCraftingManager(CraftingManager manager) {
        manager.registerMultiRecipe(this);
    }
}
