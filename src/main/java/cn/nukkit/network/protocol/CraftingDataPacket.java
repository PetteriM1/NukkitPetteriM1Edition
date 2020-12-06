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

    public static final String CRAFTING_TAG_CRAFTING_TABLE = "crafting_table";
    public static final String CRAFTING_TAG_CARTOGRAPHY_TABLE = "cartography_table";
    public static final String CRAFTING_TAG_STONECUTTER = "stonecutter";
    public static final String CRAFTING_TAG_FURNACE = "furnace";
    public static final String CRAFTING_TAG_CAMPFIRE = "campfire";
    public static final String CRAFTING_TAG_BLAST_FURNACE = "blast_furnace";
    public static final String CRAFTING_TAG_SMOKER = "smoker";

    private List<Recipe> entries = new ArrayList<>();
    private final List<BrewingRecipe> brewingEntries = new ArrayList<>();
    private final List<ContainerRecipe> containerEntries = new ArrayList<>();
    public boolean cleanRecipes;

    public void addShapelessRecipe(ShapelessRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    public void addShapedRecipe(ShapedRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    public void addFurnaceRecipe(FurnaceRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    public void addBrewingRecipe(BrewingRecipe... recipe) {
        Collections.addAll(brewingEntries, recipe);
    }

    public void addContainerRecipe(ContainerRecipe... recipe) {
        Collections.addAll(containerEntries, recipe);
    }

    @Override
    public DataPacket clean() {
        entries = new ArrayList<>();
        return super.clean();
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putUnsignedVarInt(entries.size());

        if (protocolId < 354) {
            BinaryStream writer = new BinaryStream();
            for (Object entry : entries) {
                int entryType = writeEntryLegacy(entry, writer);
                if (entryType >= 0) {
                    this.putVarInt(entryType);
                    this.put(writer.getBuffer());
                } else {
                    this.putVarInt(-1);
                }
                writer.reset();
            }
        } else {
            for (Recipe recipe : entries) {
                this.putVarInt(recipe.getType().ordinal());
                switch (recipe.getType()) {
                    case SHAPELESS:
                        ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
                        if (protocolId >= 361) {
                            this.putString(shapeless.getRecipeId());
                        }
                        List<Item> ingredients = shapeless.getIngredientList();
                        this.putUnsignedVarInt(ingredients.size());
                        for (Item ingredient : ingredients) {
                            if (protocolId < 361) {
                                this.putSlot(protocolId, ingredient);
                            } else {
                                this.putRecipeIngredient(protocolId, ingredient);
                            }
                        }
                        this.putUnsignedVarInt(1);
                        this.putSlot(protocolId, shapeless.getResult());
                        this.putUUID(shapeless.getId());
                        if (protocolId >= 354) {
                            this.putString(CRAFTING_TAG_CRAFTING_TABLE);
                            if (protocolId >= 361) {
                                this.putVarInt(shapeless.getPriority());
                                if (protocolId >= 407) {
                                    this.putUnsignedVarInt(shapeless.getNetworkId());
                                }
                            }
                        }
                        break;
                    case SHAPED:
                        ShapedRecipe shaped = (ShapedRecipe) recipe;
                        if (protocolId >= 361) {
                            this.putString(shaped.getRecipeId());
                        }
                        this.putVarInt(shaped.getWidth());
                        this.putVarInt(shaped.getHeight());

                        for (int z = 0; z < shaped.getHeight(); ++z) {
                            for (int x = 0; x < shaped.getWidth(); ++x) {
                                if (protocolId < 361) {
                                    this.putSlot(protocolId, shaped.getIngredient(x, z));
                                } else {
                                    this.putRecipeIngredient(protocolId, shaped.getIngredient(x, z));
                                }
                            }
                        }
                        List<Item> outputs = new ArrayList<>();
                        outputs.add(shaped.getResult());
                        outputs.addAll(shaped.getExtraResults());
                        this.putUnsignedVarInt(outputs.size());
                        for (Item output : outputs) {
                            this.putSlot(protocolId, output);
                        }
                        this.putUUID(shaped.getId());
                        if (protocolId >= 354) {
                            this.putString(CRAFTING_TAG_CRAFTING_TABLE);
                            if (protocolId >= 361) {
                                this.putVarInt(shaped.getPriority());
                                if (protocolId >= 407) {
                                    this.putUnsignedVarInt(shaped.getNetworkId());
                                }
                            }
                        }
                        break;
                    case FURNACE:
                    case FURNACE_DATA:
                        FurnaceRecipe furnace = (FurnaceRecipe) recipe;
                        Item input = furnace.getInput();
                        this.putVarInt(input.getId());
                        if (recipe.getType() == RecipeType.FURNACE_DATA) {
                            this.putVarInt(input.getDamage());
                        }
                        this.putSlot(protocolId, furnace.getResult());
                        if (protocolId >= 354) {
                            this.putString(CRAFTING_TAG_FURNACE);
                        }
                        break;
                }
            }

            if (protocolId >= 388) {
                this.putUnsignedVarInt(this.brewingEntries.size());
                for (BrewingRecipe recipe : brewingEntries) {
                    if (protocolId >= 407) {
                        this.putVarInt(recipe.getInput().getId());
                    }
                    this.putVarInt(recipe.getInput().getDamage());
                    this.putVarInt(recipe.getIngredient().getId());
                    if (protocolId >= 407) {
                        this.putVarInt(recipe.getIngredient().getDamage());
                        this.putVarInt(recipe.getResult().getId());
                    }
                    this.putVarInt(recipe.getResult().getDamage());
                }

                this.putUnsignedVarInt(this.containerEntries.size());
                for (ContainerRecipe recipe : containerEntries) {
                    this.putVarInt(recipe.getInput().getId());
                    this.putVarInt(recipe.getIngredient().getId());
                    this.putVarInt(recipe.getResult().getId());
                }
            }
        }

        this.putBoolean(cleanRecipes);
    }

    @Override
    public byte pid() {
        return ProtocolInfo.CRAFTING_DATA_PACKET;
    }

    private int writeEntryLegacy(Object entry, BinaryStream stream) {
        if (entry instanceof ShapelessRecipe) {
            return writeShapelessRecipeLegacy(((ShapelessRecipe) entry), stream);
        } else if (entry instanceof ShapedRecipe) {
            return writeShapedRecipeLegacy(((ShapedRecipe) entry), stream);
        } else if (entry instanceof FurnaceRecipe) {
            return writeFurnaceRecipeLegacy(((FurnaceRecipe) entry), stream);
        }
        return -1;
    }

    private int writeShapelessRecipeLegacy(ShapelessRecipe recipe, BinaryStream stream) {
        stream.putUnsignedVarInt(recipe.getIngredientCount());
        for (Item item : recipe.getIngredientList()) {
            stream.putSlot(0, item);
        }
        stream.putUnsignedVarInt(1);
        stream.putSlot(0, recipe.getResult());
        stream.putUUID(recipe.getId());
        return 0;
    }

    private int writeShapedRecipeLegacy(ShapedRecipe recipe, BinaryStream stream) {
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
    }

    private int writeFurnaceRecipeLegacy(FurnaceRecipe recipe, BinaryStream stream) {
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
}
