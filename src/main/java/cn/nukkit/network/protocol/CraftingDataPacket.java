package cn.nukkit.network.protocol;

import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.utils.BinaryStream;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Nukkit Project Team
 */
@ToString
public class CraftingDataPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CRAFTING_DATA_PACKET;

    public static final String CRAFTING_TAG_CRAFTING_TABLE = "crafting_table";
    public static final String CRAFTING_TAG_CARTOGRAPHY_TABLE = "cartography_table";
    public static final String CRAFTING_TAG_STONECUTTER = "stonecutter";
    public static final String CRAFTING_TAG_FURNACE = "furnace";
    public static final String CRAFTING_TAG_CAMPFIRE = "campfire";
    public static final String CRAFTING_TAG_BLAST_FURNACE = "blast_furnace";
    public static final String CRAFTING_TAG_SMOKER = "smoker";
    public static final String CRAFTING_TAG_SMITHING_TABLE = "smithing_table";

    private final List<ShapedRecipe> shapedData = new ArrayList<>();
    private final List<Recipe> shapelessData = new ArrayList<>();
    private final List<MultiRecipe> multiData = new ArrayList<>();
    private final List<SmithingRecipe> smithingTransformData = new ArrayList<>();
    private final List<BrewingRecipe> brewingEntries = new ArrayList<>();
    private final List<ContainerRecipe> containerEntries = new ArrayList<>();
    public boolean cleanRecipes = true;

    public void addShapelessRecipe(ShapelessRecipe... recipe) {
        Collections.addAll(shapelessData, recipe);
    }

    public void addSmithingRecipe(SmithingRecipe... recipe) {
        Collections.addAll(smithingTransformData, recipe);
    }

    public void addShapedRecipe(ShapedRecipe... recipe) {
        Collections.addAll(shapedData, recipe);
    }

    public void addFurnaceRecipe(FurnaceRecipe... recipe) {
        Collections.addAll(shapelessData, recipe);
    }

    public void addBrewingRecipe(BrewingRecipe... recipe) {
        Collections.addAll(brewingEntries, recipe);
    }

    public void addMultiRecipe(MultiRecipe... recipe) {
        Collections.addAll(multiData, recipe);
    }

    public void addContainerRecipe(ContainerRecipe... recipe) {
        Collections.addAll(containerEntries, recipe);
    }

    @Override
    public DataPacket clean() {
        shapedData.clear();
        shapelessData.clear();
        multiData.clear();
        smithingTransformData.clear();
        brewingEntries.clear();
        containerEntries.clear();
        return super.clean();
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();

        if (protocol < ProtocolInfo.v1_11_0) {
            this.putUnsignedVarInt(shapedData.size() + shapelessData.size());

            BinaryStream writer = new BinaryStream();
            for (Recipe entry : shapedData) {
                int entryType = writeEntryLegacy(entry, writer);
                if (entryType >= 0) {
                    this.putVarInt(entryType);
                    this.put(writer.getBuffer());
                } else {
                    this.putVarInt(-1);
                }
                writer.reset();
            }

            for (Recipe entry : shapelessData) {
                int entryType = writeEntryLegacy(entry, writer);
                if (entryType >= 0) {
                    this.putVarInt(entryType);
                    this.put(writer.getBuffer());
                } else {
                    this.putVarInt(-1);
                }
                writer.reset();
            }

            return;
        }

        if (protocol < ProtocolInfo.v1_26_40) {
            this.putUnsignedVarInt(shapedData.size() + shapelessData.size() + multiData.size() + smithingTransformData.size() + (protocol >= ProtocolInfo.v1_20_0_23 ? 1 : 0)); // + hardcoded smithing recipe

            for (ShapedRecipe shaped : shapedData) {
                this.putVarInt(shaped.getType().ordinal());

                if (protocol >= 361) {
                    this.putString(shaped.getRecipeId());
                }

                this.putVarInt(shaped.getWidth());
                this.putVarInt(shaped.getHeight());

                for (int z = 0; z < shaped.getHeight(); ++z) {
                    for (int x = 0; x < shaped.getWidth(); ++x) {
                        if (protocol < 361) {
                            this.putSlot(protocol, shaped.getIngredient(x, z));
                        } else {
                            this.putRecipeIngredient(this.protocol, shaped.getIngredient(x, z));
                        }
                    }
                }
                List<Item> outputs = new ArrayList<>();
                outputs.add(shaped.getResult());
                outputs.addAll(shaped.getExtraResults());
                this.putUnsignedVarInt(outputs.size());
                for (Item output : outputs) {
                    this.putSlot(protocol, output, protocol >= ProtocolInfo.v1_16_100);
                }
                this.putUUID(shaped.getId());
                if (protocol >= 354) {
                    this.putString(CRAFTING_TAG_CRAFTING_TABLE);
                    if (protocol >= 361) {
                        this.putVarInt(shaped.getPriority());
                        if (protocol >= ProtocolInfo.v1_20_80) {
                            this.putBoolean(true); // Assume Symmetry
                            if (protocol >= ProtocolInfo.v1_21_0) {
                                this.putByte((byte) 1); // Requirement ordinal, 1 = ALWAYS_UNLOCKED
                            }
                        }
                        if (protocol >= 407) {
                            this.putUnsignedVarInt(shaped.getNetworkId());
                        }
                    }
                }
            }

            for (Recipe recipe : shapelessData) {
                RecipeType networkType = recipe.getType();
                if ((networkType == RecipeType.FURNACE || networkType == RecipeType.FURNACE_DATA) && protocol >= ProtocolInfo.v1_26_20_26) {
                    networkType = RecipeType.SHAPELESS;
                }
                this.putVarInt(networkType.ordinal());

                switch (recipe.getType()) {
                    case SHAPELESS:
                        ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
                        if (protocol >= 361) {
                            this.putString(shapeless.getRecipeId());
                        }
                        List<Item> ingredients = shapeless.getIngredientList();
                        this.putUnsignedVarInt(ingredients.size());
                        for (Item ingredient : ingredients) {
                            if (protocol < 361) {
                                this.putSlot(protocol, ingredient);
                            } else {
                                this.putRecipeIngredient(this.protocol, ingredient);
                            }
                        }
                        this.putUnsignedVarInt(1); // Results length
                        this.putSlot(protocol, shapeless.getResult(), protocol >= ProtocolInfo.v1_16_100);
                        this.putUUID(shapeless.getId());
                        if (protocol >= 354) {
                            this.putString(CRAFTING_TAG_CRAFTING_TABLE);
                            if (protocol >= 361) {
                                this.putVarInt(shapeless.getPriority());
                                if (protocol >= 407) {
                                    if (protocol >= ProtocolInfo.v1_21_0) {
                                        this.putByte((byte) 1); // Requirement ordinal, 1 = ALWAYS_UNLOCKED
                                    }
                                    this.putUnsignedVarInt(shapeless.getNetworkId());
                                }
                            }
                        }
                        break;
                    case FURNACE:
                    case FURNACE_DATA:
                        FurnaceRecipe furnace = (FurnaceRecipe) recipe;
                        Item input = furnace.getInput();
                        if (protocol >= ProtocolInfo.v1_26_20_26) {
                            this.putString(furnace.getId().toString());
                            this.putUnsignedVarInt(1); // Ingredients length
                            this.putRecipeIngredient(this.protocol, input);
                            this.putUnsignedVarInt(1); // Results length
                            this.putSlot(protocol, furnace.getResult(), true);
                            this.putUUID(furnace.getId());
                            this.putString(recipe instanceof SmokerRecipe ? CRAFTING_TAG_SMOKER : recipe instanceof BlastFurnaceRecipe ? CRAFTING_TAG_BLAST_FURNACE : CRAFTING_TAG_FURNACE);
                            this.putVarInt(0); // priority
                            this.putByte((byte) 1); // Requirement ordinal, 1 = ALWAYS_UNLOCKED
                            this.putUnsignedVarInt(furnace.getNetworkId());
                        } else {
                            this.putVarInt(input.getId());
                            if (recipe.getType() == RecipeType.FURNACE_DATA) {
                                this.putVarInt(input.getDamage());
                            }
                            this.putSlot(protocol, furnace.getResult(), protocol >= ProtocolInfo.v1_16_100);
                            if (protocol >= 354) {
                                this.putString(CRAFTING_TAG_FURNACE);
                            }
                        }
                        break;
                }
            }

            for (MultiRecipe recipe : multiData) {
                this.putVarInt(recipe.getType().ordinal());
                this.putUUID(recipe.getId());
                this.putUnsignedVarInt(recipe.getNetworkId());
            }

            for (SmithingRecipe smithing : smithingTransformData) {
                RecipeType networkType = smithing.getType();
                if (networkType == RecipeType.SMITHING_TRANSFORM) {
                    networkType = (protocol >= ProtocolInfo.v1_19_60) ? RecipeType.REPAIR : RecipeType.SHAPELESS;
                }
                this.putVarInt(networkType.ordinal());

                if (protocol < ProtocolInfo.v1_19_60) {
                    this.putString(smithing.getRecipeId());
                    List<Item> ingredients = smithing.getIngredientList();
                    this.putUnsignedVarInt(ingredients.size());
                    for (Item ingredient : ingredients) {
                        this.putRecipeIngredient(protocol, ingredient);
                    }
                    this.putUnsignedVarInt(1);
                    this.putSlot(protocol, smithing.getResult(), true);
                    this.putUUID(smithing.getId());
                    this.putString(CRAFTING_TAG_SMITHING_TABLE);
                    this.putVarInt(smithing.getPriority());
                    this.putUnsignedVarInt(smithing.getNetworkId());
                    continue;
                }

                this.putString(smithing.getRecipeId());
                if (protocol >= ProtocolInfo.v1_19_80) {
                    this.putRecipeIngredient(protocol, smithing.getTemplate());
                }
                this.putRecipeIngredient(protocol, smithing.getEquipment());
                this.putRecipeIngredient(protocol, smithing.getIngredient());
                this.putSlot(protocol, smithing.getResult(), true);
                this.putString(CRAFTING_TAG_SMITHING_TABLE);
                this.putUnsignedVarInt(smithing.getNetworkId());
            }

            if (protocol >= ProtocolInfo.v1_20_0_23) {
                this.putVarInt(9); // Type SMITHING_TRIM
                this.putString("minecraft:smithing_armor_trim"); // Recipe
                this.putTrimRecipeIngredient(protocol, "minecraft:trim_templates");
                this.putTrimRecipeIngredient(protocol, "minecraft:trimmable_armors");
                this.putTrimRecipeIngredient(protocol, "minecraft:trim_materials");
                this.putString(CRAFTING_TAG_SMITHING_TABLE);
                this.putUnsignedVarInt(1); // Network ID (hardcoded in CraftingManager)
            }
        } else {
            this.putUnsignedVarInt(shapedData.size());
            for (ShapedRecipe shaped : shapedData) {
                this.putString(shaped.getRecipeId());
                this.putVarInt(shaped.getWidth());
                this.putVarInt(shaped.getHeight());
                this.putUnsignedVarInt((long) shaped.getWidth() * shaped.getHeight());
                for (int z = 0; z < shaped.getHeight(); ++z) {
                    for (int x = 0; x < shaped.getWidth(); ++x) {
                        this.putRecipeIngredient(this.protocol, shaped.getIngredient(x, z));
                    }
                }
                List<Item> outputs = new ArrayList<>();
                outputs.add(shaped.getResult());
                outputs.addAll(shaped.getExtraResults());
                this.putUnsignedVarInt(outputs.size());
                for (Item output : outputs) {
                    this.putSlot(protocol, output, true);
                }
                this.putUUID(shaped.getId());
                this.putString(CRAFTING_TAG_CRAFTING_TABLE);
                this.putVarInt(shaped.getPriority());
                this.putBoolean(true); // Assume symmetry
                this.putBoolean(false); // No unlock requirement
                this.putUnsignedVarInt(shaped.getNetworkId());
            }

            this.putUnsignedVarInt(shapelessData.size());
            for (Recipe recipe : shapelessData) {
                switch (recipe.getType()) {
                    case SHAPELESS:
                        ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
                        this.putString(shapeless.getRecipeId());
                        List<Item> ingredients = shapeless.getIngredientList();
                        this.putUnsignedVarInt(ingredients.size());
                        for (Item ingredient : ingredients) {
                            this.putRecipeIngredient(this.protocol, ingredient);
                        }
                        this.putUnsignedVarInt(1); // Results length
                        this.putSlot(protocol, shapeless.getResult(), true);
                        this.putUUID(shapeless.getId());
                        this.putString(CRAFTING_TAG_CRAFTING_TABLE);
                        this.putVarInt(shapeless.getPriority());
                        this.putBoolean(false); // No unlock requirement
                        this.putUnsignedVarInt(shapeless.getNetworkId());
                        break;
                    case FURNACE:
                    case FURNACE_DATA:
                        FurnaceRecipe furnace = (FurnaceRecipe) recipe;
                        this.putString(furnace.getId().toString());
                        this.putUnsignedVarInt(1); // Ingredients length
                        this.putRecipeIngredient(this.protocol, furnace.getInput());
                        this.putUnsignedVarInt(1); // Results length
                        this.putSlot(protocol, furnace.getResult(), true);
                        this.putUUID(furnace.getId());
                        this.putString(recipe instanceof SmokerRecipe ? CRAFTING_TAG_SMOKER : recipe instanceof BlastFurnaceRecipe ? CRAFTING_TAG_BLAST_FURNACE : CRAFTING_TAG_FURNACE);
                        this.putVarInt(0); // priority
                        this.putBoolean(false); // No unlock requirement
                        this.putUnsignedVarInt(furnace.getNetworkId());
                        break;
                }
            }

            this.putUnsignedVarInt(multiData.size());
            for (MultiRecipe recipe : multiData) {
                this.putUUID(recipe.getId());
                this.putUnsignedVarInt(recipe.getNetworkId());
            }

            this.putUnsignedVarInt(0); // user
            this.putUnsignedVarInt(0); // chemistry shapeless
            this.putUnsignedVarInt(0); // chemistry shaped

            this.putUnsignedVarInt(smithingTransformData.size());
            for (SmithingRecipe smithing : smithingTransformData) {
                this.putString(smithing.getRecipeId());
                this.putRecipeIngredient(protocol, smithing.getTemplate());
                this.putRecipeIngredient(protocol, smithing.getEquipment());
                this.putRecipeIngredient(protocol, smithing.getIngredient());
                this.putSlot(protocol, smithing.getResult(), true);
                this.putString(CRAFTING_TAG_SMITHING_TABLE);
                this.putUnsignedVarInt(smithing.getNetworkId());
            }

            this.putUnsignedVarInt(1);
            this.putString("minecraft:smithing_armor_trim"); // Recipe
            this.putTrimRecipeIngredient(protocol, "minecraft:trim_templates");
            this.putTrimRecipeIngredient(protocol, "minecraft:trimmable_armors");
            this.putTrimRecipeIngredient(protocol, "minecraft:trim_materials");
            this.putString(CRAFTING_TAG_SMITHING_TABLE);
            this.putUnsignedVarInt(1); // Network ID (hardcoded in CraftingManager)
        }

        if (protocol >= ProtocolInfo.v1_13_0) {
            this.putUnsignedVarInt(this.brewingEntries.size());
            for (BrewingRecipe recipe : brewingEntries) {
                if (protocol >= 407) {
                    this.putVarInt(recipe.getInput().getNetworkId(protocol));
                }
                this.putVarInt(recipe.getInput().getDamage());
                this.putVarInt(recipe.getIngredient().getNetworkId(protocol));
                if (protocol >= 407) {
                    this.putVarInt(recipe.getIngredient().getDamage());
                    this.putVarInt(recipe.getResult().getNetworkId(protocol));
                }
                this.putVarInt(recipe.getResult().getDamage());
            }

            this.putUnsignedVarInt(this.containerEntries.size());
            for (ContainerRecipe recipe : containerEntries) {
                this.putVarInt(recipe.getInput().getNetworkId(protocol));
                this.putVarInt(recipe.getIngredient().getNetworkId(protocol));
                this.putVarInt(recipe.getResult().getNetworkId(protocol));
            }

            if (protocol >= ProtocolInfo.v1_17_30) {
                this.putUnsignedVarInt(0); // Material reducers size
            }
        }

        this.putBoolean(cleanRecipes);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    private static int writeEntryLegacy(Recipe entry, BinaryStream stream) {
        if (entry instanceof ShapelessRecipe) {
            ShapelessRecipe recipe = (ShapelessRecipe) entry;
            stream.putUnsignedVarInt(recipe.getIngredientCount());
            for (Item item : recipe.getIngredientList()) {
                stream.putSlot(0, item);
            }
            stream.putUnsignedVarInt(1);
            stream.putSlot(0, recipe.getResult());
            stream.putUUID(recipe.getId());
            return 0;
        } else if (entry instanceof ShapedRecipe) {
            ShapedRecipe recipe = (ShapedRecipe) entry;
            stream.putVarInt(recipe.getWidth());
            stream.putVarInt(recipe.getHeight());
            for (int z = 0; z < recipe.getHeight(); ++z) {
                for (int x = 0; x < recipe.getWidth(); ++x) {
                    stream.putSlot(0, recipe.getIngredient(x, z));
                }
            }
            stream.putUnsignedVarInt(1);
            stream.putSlot(0, recipe.getResult());
            stream.putUUID(recipe.getId());
            return 1;
        } else if (entry instanceof FurnaceRecipe) {
            FurnaceRecipe recipe = (FurnaceRecipe) entry;
            if (recipe.getInput().hasMeta()) {
                stream.putVarInt(recipe.getInput().getId());
                stream.putVarInt(recipe.getInput().getDamage());
                stream.putSlot(0, recipe.getResult());
                return 3;
            } else {
                stream.putVarInt(recipe.getInput().getId());
                stream.putSlot(0, recipe.getResult());
                return 2;
            }
        }
        return -1;
    }

    private void putTrimRecipeIngredient(int protocol, String itemTag) {
        if (protocol >= ProtocolInfo.v1_26_40) {
            this.putUnsignedVarInt(1); // type
            this.putString("item_tag"); // type
            this.putString(itemTag);
            this.putVarInt(0); // meta
            this.putVarInt(1); // count
            return;
        }

        this.putByte((byte) 3);
        this.putString(itemTag);
        this.putVarInt(1);
    }
}
