/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.inventory.BrewingRecipe;
import cn.nukkit.inventory.ContainerRecipe;
import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.MultiRecipe;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.inventory.RecipeType;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.inventory.ShapelessRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.e;
import cn.nukkit.utils.BinaryStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CraftingDataPacket
extends DataPacket {
    public static final byte NETWORK_ID = 52;
    public static final String CRAFTING_TAG_CRAFTING_TABLE;
    public static final String CRAFTING_TAG_CARTOGRAPHY_TABLE;
    public static final String CRAFTING_TAG_STONECUTTER;
    public static final String CRAFTING_TAG_FURNACE;
    public static final String CRAFTING_TAG_CAMPFIRE;
    public static final String CRAFTING_TAG_BLAST_FURNACE;
    public static final String CRAFTING_TAG_SMOKER;
    private List<Recipe> f = new ArrayList<Recipe>();
    private final List<BrewingRecipe> e = new ArrayList<BrewingRecipe>();
    private final List<ContainerRecipe> g = new ArrayList<ContainerRecipe>();
    public boolean cleanRecipes = true;

    public void addShapelessRecipe(ShapelessRecipe ... shapelessRecipeArray) {
        Collections.addAll(this.f, shapelessRecipeArray);
    }

    public void addShapedRecipe(ShapedRecipe ... shapedRecipeArray) {
        Collections.addAll(this.f, shapedRecipeArray);
    }

    public void addFurnaceRecipe(FurnaceRecipe ... furnaceRecipeArray) {
        Collections.addAll(this.f, furnaceRecipeArray);
    }

    public void addBrewingRecipe(BrewingRecipe ... brewingRecipeArray) {
        Collections.addAll(this.e, brewingRecipeArray);
    }

    public void addMultiRecipe(MultiRecipe ... multiRecipeArray) {
        Collections.addAll(this.f, multiRecipeArray);
    }

    public void addContainerRecipe(ContainerRecipe ... containerRecipeArray) {
        Collections.addAll(this.g, containerRecipeArray);
    }

    @Override
    public DataPacket clean() {
        this.f = new ArrayList<Recipe>();
        return super.clean();
    }

    @Override
    public void decode() {
        this.b();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.f.size());
        if (this.protocol < 354) {
            BinaryStream binaryStream = new BinaryStream();
            for (Recipe recipe : this.f) {
                int n = this.a(recipe, binaryStream);
                if (n >= 0) {
                    this.putVarInt(n);
                    this.put(binaryStream.getBuffer());
                } else {
                    this.putVarInt(-1);
                }
                binaryStream.reset();
            }
        } else {
            for (Recipe recipe : this.f) {
                this.putVarInt(recipe.getType().ordinal());
                switch (cn.nukkit.network.protocol.e.a[recipe.getType().ordinal()]) {
                    case 1: {
                        ShapelessRecipe shapelessRecipe = (ShapelessRecipe)recipe;
                        if (this.protocol >= 361) {
                            this.putString(shapelessRecipe.getRecipeId());
                        }
                        List<Item> list = shapelessRecipe.getIngredientList();
                        this.putUnsignedVarInt(list.size());
                        for (Item item : list) {
                            if (this.protocol < 361) {
                                this.putSlot(this.protocol, item);
                                continue;
                            }
                            this.putRecipeIngredient(this.protocol, item);
                        }
                        this.putUnsignedVarInt(1L);
                        this.putSlot(this.protocol, shapelessRecipe.getResult(), this.protocol >= 419);
                        this.putUUID(shapelessRecipe.getId());
                        if (this.protocol < 354) break;
                        this.putString("crafting_table");
                        if (this.protocol < 361) break;
                        this.putVarInt(shapelessRecipe.getPriority());
                        if (this.protocol < 407) break;
                        this.putUnsignedVarInt(shapelessRecipe.getNetworkId());
                        break;
                    }
                    case 2: {
                        void var6_17;
                        ShapedRecipe shapedRecipe = (ShapedRecipe)recipe;
                        if (this.protocol >= 361) {
                            this.putString(shapedRecipe.getRecipeId());
                        }
                        this.putVarInt(shapedRecipe.getWidth());
                        this.putVarInt(shapedRecipe.getHeight());
                        boolean bl = false;
                        while (var6_17 < shapedRecipe.getHeight()) {
                            for (int k = 0; k < shapedRecipe.getWidth(); ++k) {
                                if (this.protocol < 361) {
                                    this.putSlot(this.protocol, shapedRecipe.getIngredient(k, (int)var6_17));
                                    continue;
                                }
                                this.putRecipeIngredient(this.protocol, shapedRecipe.getIngredient(k, (int)var6_17));
                            }
                            ++var6_17;
                        }
                        ArrayList<Item> arrayList = new ArrayList<Item>();
                        arrayList.add(shapedRecipe.getResult());
                        arrayList.addAll(shapedRecipe.getExtraResults());
                        this.putUnsignedVarInt(arrayList.size());
                        for (Item item : arrayList) {
                            this.putSlot(this.protocol, item, this.protocol >= 419);
                        }
                        this.putUUID(shapedRecipe.getId());
                        if (this.protocol < 354) break;
                        this.putString("crafting_table");
                        if (this.protocol < 361) break;
                        this.putVarInt(shapedRecipe.getPriority());
                        if (this.protocol < 407) break;
                        this.putUnsignedVarInt(shapedRecipe.getNetworkId());
                        break;
                    }
                    case 3: 
                    case 4: {
                        Item item;
                        FurnaceRecipe furnaceRecipe = (FurnaceRecipe)recipe;
                        item = furnaceRecipe.getInput();
                        this.putVarInt(item.getId());
                        if (recipe.getType() == RecipeType.FURNACE_DATA) {
                            this.putVarInt(item.getDamage());
                        }
                        this.putSlot(this.protocol, furnaceRecipe.getResult(), this.protocol >= 419);
                        if (this.protocol < 354) break;
                        this.putString("furnace");
                        break;
                    }
                    case 5: {
                        if (this.protocol < 407) break;
                        this.putUUID(((MultiRecipe)recipe).getId());
                        this.putUnsignedVarInt(((MultiRecipe)recipe).getNetworkId());
                    }
                }
            }
            if (this.protocol >= 388) {
                this.putUnsignedVarInt(this.e.size());
                for (BrewingRecipe brewingRecipe : this.e) {
                    if (this.protocol >= 407) {
                        this.putVarInt(brewingRecipe.getInput().getNetworkId(this.protocol));
                    }
                    this.putVarInt(brewingRecipe.getInput().getDamage());
                    this.putVarInt(brewingRecipe.getIngredient().getNetworkId(this.protocol));
                    if (this.protocol >= 407) {
                        this.putVarInt(brewingRecipe.getIngredient().getDamage());
                        this.putVarInt(brewingRecipe.getResult().getNetworkId(this.protocol));
                    }
                    this.putVarInt(brewingRecipe.getResult().getDamage());
                }
                this.putUnsignedVarInt(this.g.size());
                for (ContainerRecipe containerRecipe : this.g) {
                    this.putVarInt(containerRecipe.getInput().getNetworkId(this.protocol));
                    this.putVarInt(containerRecipe.getIngredient().getNetworkId(this.protocol));
                    this.putVarInt(containerRecipe.getResult().getNetworkId(this.protocol));
                }
                if (this.protocol >= 465) {
                    this.putUnsignedVarInt(0L);
                }
            }
        }
        this.putBoolean(this.cleanRecipes);
    }

    @Override
    public byte pid() {
        return 52;
    }

    private int a(Object object, BinaryStream binaryStream) {
        if (object instanceof ShapelessRecipe) {
            return this.a((ShapelessRecipe)object, binaryStream);
        }
        if (object instanceof ShapedRecipe) {
            return this.a((ShapedRecipe)object, binaryStream);
        }
        if (object instanceof FurnaceRecipe) {
            return this.a((FurnaceRecipe)object, binaryStream);
        }
        return -1;
    }

    private int a(ShapelessRecipe shapelessRecipe, BinaryStream binaryStream) {
        binaryStream.putUnsignedVarInt(shapelessRecipe.getIngredientCount());
        for (Item item : shapelessRecipe.getIngredientList()) {
            binaryStream.putSlot(0, item);
        }
        binaryStream.putUnsignedVarInt(1L);
        binaryStream.putSlot(0, shapelessRecipe.getResult());
        binaryStream.putUUID(shapelessRecipe.getId());
        return 0;
    }

    private int a(ShapedRecipe shapedRecipe, BinaryStream binaryStream) {
        binaryStream.putVarInt(shapedRecipe.getWidth());
        binaryStream.putVarInt(shapedRecipe.getHeight());
        for (int k = 0; k < shapedRecipe.getHeight(); ++k) {
            for (int i2 = 0; i2 < shapedRecipe.getWidth(); ++i2) {
                binaryStream.putSlot(0, shapedRecipe.getIngredient(i2, k));
            }
        }
        binaryStream.putUnsignedVarInt(1L);
        binaryStream.putSlot(0, shapedRecipe.getResult());
        binaryStream.putUUID(shapedRecipe.getId());
        return 1;
    }

    private int a(FurnaceRecipe furnaceRecipe, BinaryStream binaryStream) {
        if (furnaceRecipe.getInput().hasMeta()) {
            binaryStream.putVarInt(furnaceRecipe.getInput().getId());
            binaryStream.putVarInt(furnaceRecipe.getInput().getDamage());
            binaryStream.putSlot(0, furnaceRecipe.getResult());
            return 3;
        }
        binaryStream.putVarInt(furnaceRecipe.getInput().getId());
        binaryStream.putSlot(0, furnaceRecipe.getResult());
        return 2;
    }

    public String toString() {
        return "CraftingDataPacket(entries=" + this.f + ", brewingEntries=" + this.e + ", containerEntries=" + this.g + ", cleanRecipes=" + this.cleanRecipes + ")";
    }

    static {
        CRAFTING_TAG_SMOKER = "smoker";
        CRAFTING_TAG_CAMPFIRE = "campfire";
        CRAFTING_TAG_FURNACE = "furnace";
        CRAFTING_TAG_CRAFTING_TABLE = "crafting_table";
        CRAFTING_TAG_STONECUTTER = "stonecutter";
        CRAFTING_TAG_CARTOGRAPHY_TABLE = "cartography_table";
        CRAFTING_TAG_BLAST_FURNACE = "blast_furnace";
    }
}

