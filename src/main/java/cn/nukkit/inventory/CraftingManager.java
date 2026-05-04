package cn.nukkit.inventory;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.block.BlockID;
import cn.nukkit.item.*;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.*;
import cn.nukkit.utils.material.tags.MaterialTags;
import io.netty.util.collection.CharObjectHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

import java.util.*;
import java.util.zip.Deflater;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class CraftingManager {

    private static BatchPacket packet354;
    private static BatchPacket packet361;
    private static BatchPacket packet388;
    private static BatchPacket packet407;
    private static BatchPacket packet419;
    private static BatchPacket packet431;
    private static BatchPacket packet440;
    private static BatchPacket packet448;
    private static BatchPacket packet465;
    private static BatchPacket packet471; // 1.18 (475) doesn't have its own block palette or other changes so 1.17.40 (471) packet can be used
    private static BatchPacket packet486;
    private static BatchPacket packet503;
    private static BatchPacket packet527;
    private static BatchPacket packet544;
    private static BatchPacket packet554;
    private static BatchPacket packet560;
    private static BatchPacket packet567;
    private static BatchPacket packet575;
    private static BatchPacket packet582;
    private static BatchPacket packet589;
    private static BatchPacket packet594;
    private static BatchPacket packet618;
    private static BatchPacket packet622;
    private static BatchPacket packet630;
    private static BatchPacket packet649;
    private static BatchPacket packet662;
    private static BatchPacket packet671;
    private static BatchPacket packet685;
    private static BatchPacket packet712;
    private static BatchPacket packet729;
    private static BatchPacket packet748;
    private static BatchPacket packet766;
    private static BatchPacket packet776;
    private static BatchPacket packet786;
    private static BatchPacket packet800;
    private static BatchPacket packet818;
    private static BatchPacket packet827;
    private static BatchPacket packet843;
    private static BatchPacket packet859;
    private static BatchPacket packet897;
    private static BatchPacket packet944;
    private static BatchPacket packet975;
    private static BatchPacket packet1001;

    /* Keep these public for compatibility with official Nukkit */

    @Getter
    public final Collection<Recipe> recipes = new ArrayList<>();
    @Getter
    protected final Map<Integer, Map<UUID, ShapedRecipe>> shapedRecipes = new Int2ObjectOpenHashMap<>();
    @Getter
    protected final Map<Integer, Map<UUID, ShapelessRecipe>> shapelessRecipes = new Int2ObjectOpenHashMap<>();
    @Getter
    public final Map<Integer, ContainerRecipe> containerRecipes = new Int2ObjectOpenHashMap<>();
    @Getter
    public final Map<UUID, MultiRecipe> multiRecipes = new HashMap<>();
    @Getter
    public final Map<Integer, FurnaceRecipe> furnaceRecipes = new Int2ObjectOpenHashMap<>();
    @Getter
    public final Map<Integer, BlastFurnaceRecipe> blastFurnaceRecipes = new Int2ObjectOpenHashMap<>();
    @Getter
    public final Map<Integer, SmokerRecipe> smokerRecipes = new Int2ObjectOpenHashMap<>();
    @Getter
    public final Map<Integer, BrewingRecipe> brewingRecipes = new Int2ObjectOpenHashMap<>();
    @Getter
    private final Map<Integer, CampfireRecipe> campfireRecipes = new Int2ObjectOpenHashMap<>();
    @Getter
    private final Map<UUID, SmithingRecipe> smithingRecipes = new HashMap<>();

    private static int RECIPE_COUNT;
    static int NEXT_NETWORK_ID = 1; // Reserve 1 for smithing_armor_trim

    public static final Comparator<Item> recipeComparator = (i1, i2) -> {
        if (i1.getId() > i2.getId()) {
            return 1;
        } else if (i1.getId() < i2.getId()) {
            return -1;
        } else if (i1.getDamage() > i2.getDamage()) {
            return 1;
        } else if (i1.getDamage() < i2.getDamage()) {
            return -1;
        } else {
            return Integer.compare(i1.getCount(), i2.getCount());
        }
    };

    @SuppressWarnings("unchecked")
    public CraftingManager() {
        MainLogger.getLogger().debug("Loading recipes...");

        Map<String, Object> root = new Config(Config.YAML).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes.json")).getRootSection();

        RuntimeItemMapping itemMapping = RuntimeItems.getMapping((int) root.get("version"));

        for (Map recipe : (List<Map>) root.get("recipes")) {
            try {
                switch (Utils.toInt(recipe.get("type"))) {
                    case 0: // shapeless
                        if (!"crafting_table".equals(recipe.get("block"))) {
                            continue;
                        }

                        Map shapelessOutput = (Map) ((List) recipe.get("output")).get(0);
                        RuntimeItemMapping.LegacyEntry shapelessOutputEntry = itemMapping.fromRuntime((int) shapelessOutput.get("legacyId"));
                        top:
                        if (shapelessOutputEntry != null && shapelessOutputEntry.getLegacyId() != 0) {
                            int outputDamage = (int) shapelessOutput.getOrDefault("damage", 0);
                            if (outputDamage == 0) {
                                outputDamage = shapelessOutputEntry.getDamage();
                            }
                            String nbt = (String) shapelessOutput.get("nbt_b64");
                            byte[] nbtBytes = new byte[0];
                            if (nbt != null) {
                                nbtBytes = Base64.getDecoder().decode(nbt);
                            }
                            Item outputItem = Item.get(shapelessOutputEntry.getLegacyId(), outputDamage, (Integer) shapelessOutput.getOrDefault("count", 1), nbtBytes);
                            List<Map> input = ((List<Map>) recipe.get("input"));
                            List<Item> sorted = new ArrayList<>();

                            for (Map<String, Object> ingredient : input) {
                                String type = (String) ingredient.get("type");
                                if (!"default".equals(type)) {
                                    if ("item_tag".equals(type)) {
                                        buildShapelessRecipeItemTagOverrides(itemMapping, input, outputItem, (String) ingredient.get("itemTag"), null, null);
                                    } else if (Nukkit.DEBUG > 1) {
                                        MainLogger.getLogger().debug("Unknown shapeless ingredient type: " + recipe);
                                    }
                                    break top;
                                }
                                Item inputItem;
                                RuntimeItemMapping.LegacyEntry legacyEntry = itemMapping.fromRuntime((int) ingredient.get("itemId"));
                                if (legacyEntry == null || legacyEntry.getLegacyId() == 0) {
                                    if (Nukkit.DEBUG > 1) {
                                        MainLogger.getLogger().debug("Unknown shapeless input: " + recipe);
                                    }
                                    break top;
                                }
                                int aux = (int) ingredient.getOrDefault("auxValue", 0);
                                if (aux == 32767) {
                                    aux = legacyEntry.isHasDamage() ? legacyEntry.getDamage() : -1;
                                } else if (aux == 0) {
                                    aux = legacyEntry.getDamage();
                                }
                                inputItem = Item.get(legacyEntry.getLegacyId(), aux, (Integer) ingredient.getOrDefault("count", 1));
                                sorted.add(inputItem);
                            }

                            sorted.sort(recipeComparator);

                            int priority = (int) recipe.getOrDefault("priority", 0);
                            this.registerRecipe(new ShapelessRecipe((String) recipe.get("id"), priority, outputItem, sorted));

                            // Inject recipes for flight duration 2 and 3 fireworks
                            if (outputItem.getId() == Item.FIREWORKS && outputItem.getCount() == 3) {
                                sorted.add(Item.get(Item.GUNPOWDER, 0, 1));
                                sorted.sort(recipeComparator);
                                ((ItemFirework) outputItem).setFlight(2);
                                this.registerRecipe(new ShapelessRecipe(null, 0, outputItem, sorted));

                                sorted.add(Item.get(Item.GUNPOWDER, 0, 1)); // Re-using the list so only need to add one
                                sorted.sort(recipeComparator);
                                ((ItemFirework) outputItem).setFlight(3);
                                this.registerRecipe(new ShapelessRecipe(null, 0, outputItem, sorted));
                            }
                        } else {
                            if (Nukkit.DEBUG > 1) {
                                MainLogger.getLogger().debug("Unknown shapeless output: " + recipe);
                            }
                        }
                        break;
                    case 1: // shaped
                        if (!"crafting_table".equals(recipe.get("block"))) {
                            continue;
                        }

                        Map shapedOutput = (Map) ((List) recipe.get("output")).get(0);
                        RuntimeItemMapping.LegacyEntry shapedOutputEntry = itemMapping.fromRuntime((int) shapedOutput.get("legacyId"));
                        top:
                        if (shapedOutputEntry != null && shapedOutputEntry.getLegacyId() != 0) {
                            int outputDamage = (int) shapedOutput.getOrDefault("damage", 0);
                            if (outputDamage == 0) {
                                outputDamage = shapedOutputEntry.getDamage();
                            }
                            String nbt = (String) shapedOutput.get("nbt_b64");
                            byte[] nbtBytes = new byte[0];
                            if (nbt != null) {
                                nbtBytes = Base64.getDecoder().decode(nbt);
                            }
                            Item outputItem = Item.get(shapedOutputEntry.getLegacyId(), outputDamage, (Integer) shapedOutput.getOrDefault("count", 1), nbtBytes);
                            String[] shape = ((List<String>) recipe.get("shape")).toArray(new String[0]);
                            Map<Character, Item> ingredients = new CharObjectHashMap<>();
                            Map<String, Map<String, Object>> input = (Map) recipe.get("input");

                            for (Map.Entry<String, Map<String, Object>> ingredientEntry : input.entrySet()) {
                                Item inputItem = null;
                                String type = (String) ingredientEntry.getValue().get("type");
                                if (!"default".equals(type)) {
                                    if ("item_tag".equals(type)) {
                                        buildShapedRecipeItemTagOverrides(itemMapping, input, shape, outputItem, (String) ingredientEntry.getValue().get("itemTag"), null, null);
                                    } else if ("complex_alias".equals(type)) {
                                        // TODO: Variants. Maybe we could run this through buildShapedRecipeItemTagOverrides as a fake itemTag?
                                        // Currently handled by needExpandLegacy:
                                        // minecraft:Torch_recipeId
                                        // minecraft:smoker_from_log2
                                        // all campfires
                                        // soul torch
                                        switch ((String) recipe.get("id")) {
                                            case "minecraft:painting":
                                                inputItem = Item.get(BlockID.WOOL, 0, 1);
                                                break;
                                            case "minecraft:purpur_stairs":
                                                inputItem = Item.get(BlockID.PURPUR_BLOCK, 0, 1);
                                                break;
                                            case "minecraft:stonecutter":
                                                inputItem = Item.get(BlockID.STONE, 0, 1);
                                                break;
                                            case "minecraft:tnt":
                                                inputItem = Item.get(BlockID.SAND, 0, 1);
                                                break;
                                            // TODO: bedrock allows alternative materials for some trim duplication
                                            case "minecraft:dune_armor_trim_smithing_template_duplicate":
                                                inputItem = Item.get(BlockID.SANDSTONE, 0, 1);
                                                break;
                                            case "minecraft:spire_armor_trim_smithing_template_duplicate":
                                                inputItem = Item.get(BlockID.PURPUR_BLOCK, 0, 1);
                                                break;
                                            case "minecraft:tide_armor_trim_smithing_template_duplicate":
                                                inputItem = Item.get(BlockID.PRISMARINE, 0, 1);
                                                break;
                                        }
                                        if (Nukkit.DEBUG > 1 && inputItem == null) {
                                            MainLogger.getLogger().debug("Missing shaped ingredient complex_alias: " + recipe);
                                        }
                                    } else if (Nukkit.DEBUG > 1) {
                                        MainLogger.getLogger().debug("Unknown shaped ingredient type: " + recipe);
                                    }
                                    if (inputItem == null) {
                                        break top;
                                    }
                                }
                                if (inputItem == null) {
                                    RuntimeItemMapping.LegacyEntry legacyEntry = itemMapping.fromRuntime((int) ingredientEntry.getValue().get("itemId"));
                                    if (legacyEntry == null || legacyEntry.getLegacyId() == 0) {
                                        if (Nukkit.DEBUG > 1) {
                                            MainLogger.getLogger().debug("Unknown shaped input: " + recipe);
                                        }
                                        break top;
                                    }
                                    int aux = (int) ingredientEntry.getValue().getOrDefault("auxValue", 0);
                                    if (aux == 32767) {
                                        aux = legacyEntry.isHasDamage() ? legacyEntry.getDamage() : -1;
                                    } else if (aux == 0) {
                                        aux = legacyEntry.getDamage();
                                    }
                                    inputItem = Item.get(legacyEntry.getLegacyId(), aux, (Integer) ingredientEntry.getValue().getOrDefault("count", 1));
                                }
                                ingredients.put(ingredientEntry.getKey().charAt(0), inputItem);
                            }

                            int priority = (int) recipe.getOrDefault("priority", 0);
                            this.registerRecipe(new ShapedRecipe((String) recipe.get("id"), priority, outputItem, shape, ingredients, Collections.EMPTY_LIST));
                        } else {
                            if (Nukkit.DEBUG > 1) {
                                MainLogger.getLogger().debug("Unknown shaped output: " + recipe);
                            }
                        }
                        break;
                    case 3: // smelting
                        String smeltingBlock = (String) recipe.get("block");
                        if (!"furnace".equals(smeltingBlock) && !"blast_furnace".equals(smeltingBlock) && !"smoker".equals(smeltingBlock) && !"campfire".equals(smeltingBlock)) {
                            continue;
                        }

                        Map input = (Map) recipe.get("input");
                        Map output = (Map) recipe.get("output");
                        RuntimeItemMapping.LegacyEntry furnaceInputEntry = itemMapping.fromIdentifier((String) input.get("id"));
                        RuntimeItemMapping.LegacyEntry furnaceOutputEntry = itemMapping.fromIdentifier((String) output.get("id"));

                        if (furnaceInputEntry != null && furnaceOutputEntry != null && furnaceInputEntry.getLegacyId() != 0 && furnaceOutputEntry.getLegacyId() != 0) {
                            Item inputItem = Item.get(furnaceInputEntry.getLegacyId(), furnaceInputEntry.getDamage(), (Integer) input.getOrDefault("count", 1));
                            Item outputItem = Item.get(furnaceOutputEntry.getLegacyId(), furnaceOutputEntry.getDamage(), (Integer) output.getOrDefault("count", 1));

                            switch (smeltingBlock) {
                                case "furnace":
                                    this.registerRecipe(new FurnaceRecipe((String) recipe.get("id"), outputItem, inputItem));
                                    break;
                                case "blast_furnace":
                                    this.registerRecipe(new BlastFurnaceRecipe((String) recipe.get("id"), outputItem, inputItem));
                                    break;
                                case "smoker":
                                    this.registerRecipe(new SmokerRecipe((String) recipe.get("id"), outputItem, inputItem));
                                    break;
                                case "campfire":
                                    this.registerRecipe(new CampfireRecipe(outputItem, inputItem));
                                    break;
                            }
                        } else {
                            if (Nukkit.DEBUG > 1) {
                                MainLogger.getLogger().debug("Unknown smelting recipe: " + recipe);
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error while loading recipes", e);
            }
        }

        for (Map potionMix : (List<Map>) root.get("potionMixes")) {
            RuntimeItemMapping.LegacyEntry legacyEntry;

            legacyEntry = itemMapping.fromIdentifier(((String) potionMix.get("inputId")));
            if (legacyEntry == null) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown inputId: " + potionMix);
                }
                continue;
            }
            int fromPotionId = legacyEntry.getLegacyId();
            int fromPotionMeta = ((Integer) potionMix.get("inputMeta"));
            legacyEntry = itemMapping.fromIdentifier(((String) potionMix.get("reagentId")));
            if (legacyEntry == null) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown reagentId: " + potionMix);
                }
                continue;
            }
            int ingredient = legacyEntry.getLegacyId();
            int ingredientMeta = ((Integer) potionMix.get("reagentMeta"));
            legacyEntry = itemMapping.fromIdentifier(((String) potionMix.get("outputId")));
            if (legacyEntry == null) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown outputId: " + potionMix);
                }
                continue;
            }
            int toPotionId = legacyEntry.getLegacyId();
            int toPotionMeta = ((Integer) potionMix.get("outputMeta"));
            if (fromPotionId == 0 || ingredient == 0 || toPotionId == 0) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown potion mix: " + potionMix);
                }
                continue;
            }

            this.registerBrewingRecipe(new BrewingRecipe(Item.get(fromPotionId, fromPotionMeta), Item.get(ingredient, ingredientMeta), Item.get(toPotionId, toPotionMeta)));
        }

        for (Map containerMix : (List<Map>) root.get("containerMixes")) {
            RuntimeItemMapping.LegacyEntry legacyEntry;

            legacyEntry = itemMapping.fromIdentifier(((String) containerMix.get("inputId")));
            if (legacyEntry == null) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown inputId: " + containerMix);
                }
                continue;
            }
            int fromItemId = legacyEntry.getLegacyId();
            legacyEntry = itemMapping.fromIdentifier(((String) containerMix.get("reagentId")));
            if (legacyEntry == null) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown reagentId: " + containerMix);
                }
                continue;
            }
            int ingredient = legacyEntry.getLegacyId();
            legacyEntry = itemMapping.fromIdentifier(((String) containerMix.get("outputId")));
            if (legacyEntry == null) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown outputId: " + containerMix);
                }
                continue;
            }
            int toItemId = legacyEntry.getLegacyId();
            if (fromItemId == 0 || ingredient == 0 || toItemId == 0) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown container mix: " + containerMix);
                }
                continue;
            }

            this.registerContainerRecipe(new ContainerRecipe(Item.get(fromItemId), Item.get(ingredient), Item.get(toItemId)));
        }

        ConfigSection smithing = new Config(Config.YAML).loadFromStream(Server.class.getClassLoader().getResourceAsStream("smithing.json")).getRootSection();

        top:
        for (Map<String, Object> recipe : (List<Map<String, Object>>) smithing.get((Object) "smithing")) {
            String recipeId = (String) recipe.get("id");
            Map<String, Object> first = ((List<Map>) recipe.get("output")).get(0);
            RuntimeItemMapping.LegacyEntry legacyEntry;
            legacyEntry = itemMapping.fromIdentifier((String) first.get("id"));
            if (legacyEntry == null) {
                if (Nukkit.DEBUG > 1) {
                    MainLogger.getLogger().debug("Unknown smithing output: " + recipe);
                }
                continue;
            }

            Item item = Item.get(legacyEntry.getLegacyId(), 0, 1);

            List<Item> ingredients = new ArrayList<>();
            for (Map<String, Object> ingredient : ((List<Map>) recipe.get("input"))) {
                legacyEntry = itemMapping.fromIdentifier((String) ingredient.get("id"));
                if (legacyEntry == null) {
                    if (Nukkit.DEBUG > 1) {
                        MainLogger.getLogger().debug("Unknown smithing input: " + recipe);
                    }
                    continue top;
                }

                Item ing = Item.get(legacyEntry.getLegacyId(), 0, 1);
                ingredients.add(ing);
            }

            this.registerRecipe(new SmithingRecipe(recipeId, 0, ingredients, item));
        }
    }

    @SuppressWarnings("unchecked")
    private void buildShapedRecipeItemTagOverrides(RuntimeItemMapping itemMapping, Map<String, Map<String, Object>> input, String[] shape, Item outputItem, String toReplaceTag, String replaceOtherTagKey, String replaceOtherTagValue) {
        Set<String> tags = MaterialTags.getVanillaDefinitions(toReplaceTag);
        if (tags == null) {
            if (Nukkit.DEBUG > 1) {
                MainLogger.getLogger().debug("Unknown item tag: " + toReplaceTag);
            }
            return;
        }

        top:
        for (String material : tags) {
            Map<Character, Item> ingredients = new CharObjectHashMap<>();
            int expandLegacy = 0;

            for (Map.Entry<String, Map<String, Object>> ingredientEntry : input.entrySet()) {
                Item inputItem;
                String type = (String) ingredientEntry.getValue().get("type");

                if (!"default".equals(type)) {
                    if ("item_tag".equals(type)) {
                        String itemTag = (String) ingredientEntry.getValue().get("itemTag");
                        if (!itemTag.equals(toReplaceTag)) {
                            if (itemTag.equals(replaceOtherTagKey)) {
                                RuntimeItemMapping.LegacyEntry legacyEntry = itemMapping.fromIdentifier(replaceOtherTagValue);
                                if (legacyEntry == null || legacyEntry.getLegacyId() == 0) {
                                    if (Nukkit.DEBUG > 1) {
                                        MainLogger.getLogger().debug("Unknown multi item tag input: " + replaceOtherTagValue);
                                    }
                                    continue top;
                                }
                                inputItem = Item.get(legacyEntry.getLegacyId(), legacyEntry.getDamage(), (Integer) ingredientEntry.getValue().getOrDefault("count", 1));
                                if (needExpandLegacy(legacyEntry.getLegacyId())) {
                                    expandLegacy = legacyEntry.getLegacyId();
                                }
                            } else {
                                buildShapedRecipeItemTagOverrides(itemMapping, input, shape, outputItem, itemTag, toReplaceTag, material);
                                continue top;
                            }
                        } else {
                            RuntimeItemMapping.LegacyEntry legacyEntry = itemMapping.fromIdentifier(material);
                            if (legacyEntry == null || legacyEntry.getLegacyId() == 0) {
                                if (Nukkit.DEBUG > 1) {
                                    MainLogger.getLogger().debug("Unknown item tag input: " + material);
                                }
                                continue top;
                            }
                            inputItem = Item.get(legacyEntry.getLegacyId(), legacyEntry.getDamage(), (Integer) ingredientEntry.getValue().getOrDefault("count", 1));
                            if (needExpandLegacy(legacyEntry.getLegacyId())) {
                                expandLegacy = legacyEntry.getLegacyId();
                            }
                        }
                    } else {
                        throw new RuntimeException("Unsupported type: " + type);
                    }
                } else {
                    RuntimeItemMapping.LegacyEntry legacyEntry = itemMapping.fromRuntime((int) ingredientEntry.getValue().get("itemId"));
                    if (legacyEntry == null || legacyEntry.getLegacyId() == 0) {
                        if (Nukkit.DEBUG > 1) {
                            MainLogger.getLogger().debug("Unknown shaped input: " + input);
                        }
                        continue top;
                    }
                    int aux = (int) ingredientEntry.getValue().getOrDefault("auxValue", 0);
                    if (aux == 32767) {
                        aux = legacyEntry.isHasDamage() ? legacyEntry.getDamage() : -1;
                    } else if (aux == 0) {
                        aux = legacyEntry.getDamage();
                    }
                    inputItem = Item.get(legacyEntry.getLegacyId(), aux, (Integer) ingredientEntry.getValue().getOrDefault("count", 1));
                }

                ingredients.put(ingredientEntry.getKey().charAt(0), inputItem);
            }

            if (expandLegacy != 0) {
                int lastMeta = expandLegacy == ItemID.COAL ? 1 : expandLegacy == BlockID.PLANKS || expandLegacy == BlockID.WOODEN_SLAB ? 5 : expandLegacy == BlockID.STONE ? 6 : 15;

                for (int meta = 0; meta <= lastMeta; meta++) {
                    for (Item item : ingredients.values()) {
                        if (item.getId() == expandLegacy) {
                            item.setDamage(meta);
                        }
                    }

                    this.registerRecipe(new ShapedRecipe(null, 0, outputItem, shape, ingredients, Collections.EMPTY_LIST));
                }
            } else {
                this.registerRecipe(new ShapedRecipe(null, 0, outputItem, shape, ingredients, Collections.EMPTY_LIST));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void buildShapelessRecipeItemTagOverrides(RuntimeItemMapping itemMapping, List<Map> input, Item outputItem, String toReplaceTag, String replaceOtherTagKey, String replaceOtherTagValue) {
        Set<String> tags = MaterialTags.getVanillaDefinitions(toReplaceTag);
        if (tags == null) {
            if (Nukkit.DEBUG > 1) {
                MainLogger.getLogger().debug("Unknown item tag: " + toReplaceTag);
            }
            return;
        }

        top:
        for (String material : tags) {
            List<Item> sorted = new ArrayList<>();
            int expandLegacy = 0;

            for (Map<String, Object> ingredient : input) {
                Item inputItem;
                String type = (String) ingredient.get("type");

                if (!"default".equals(type)) {
                    if ("item_tag".equals(type)) {
                        String itemTag = (String) ingredient.get("itemTag");
                        if (!itemTag.equals(toReplaceTag)) {
                            if (itemTag.equals(replaceOtherTagKey)) {
                                RuntimeItemMapping.LegacyEntry legacyEntry = itemMapping.fromIdentifier(replaceOtherTagValue);
                                if (legacyEntry == null || legacyEntry.getLegacyId() == 0) {
                                    if (Nukkit.DEBUG > 1) {
                                        MainLogger.getLogger().debug("Unknown multi item tag input: " + replaceOtherTagValue);
                                    }
                                    continue top;
                                }
                                inputItem = Item.get(legacyEntry.getLegacyId(), legacyEntry.getDamage(), (Integer) ingredient.getOrDefault("count", 1));
                                if (needExpandLegacy(legacyEntry.getLegacyId())) {
                                    expandLegacy = legacyEntry.getLegacyId();
                                }
                            } else {
                                buildShapelessRecipeItemTagOverrides(itemMapping, input, outputItem, itemTag, toReplaceTag, material);
                                continue top;
                            }
                        } else {
                            RuntimeItemMapping.LegacyEntry legacyEntry = itemMapping.fromIdentifier(material);
                            if (legacyEntry == null || legacyEntry.getLegacyId() == 0) {
                                if (Nukkit.DEBUG > 1) {
                                    MainLogger.getLogger().debug("Unknown item tag input: " + material);
                                }
                                continue top;
                            }
                            inputItem = Item.get(legacyEntry.getLegacyId(), legacyEntry.getDamage(), (Integer) ingredient.getOrDefault("count", 1));
                            if (needExpandLegacy(legacyEntry.getLegacyId())) {
                                expandLegacy = legacyEntry.getLegacyId();
                            }
                        }
                    } else {
                        throw new RuntimeException("Unsupported type: " + type);
                    }
                } else {
                    RuntimeItemMapping.LegacyEntry legacyEntry = itemMapping.fromRuntime((int) ingredient.get("itemId"));
                    if (legacyEntry == null || legacyEntry.getLegacyId() == 0) {
                        if (Nukkit.DEBUG > 1) {
                            MainLogger.getLogger().debug("Unknown shapeless input: " + input);
                        }
                        continue top;
                    }
                    int aux = (int) ingredient.getOrDefault("auxValue", 0);
                    if (aux == 32767) {
                        aux = legacyEntry.isHasDamage() ? legacyEntry.getDamage() : -1;
                    } else if (aux == 0) {
                        aux = legacyEntry.getDamage();
                    }
                    inputItem = Item.get(legacyEntry.getLegacyId(), aux, (Integer) ingredient.getOrDefault("count", 1));
                }

                sorted.add(inputItem);
            }

            sorted.sort(recipeComparator);

            if (expandLegacy != 0) {
                int lastMeta = expandLegacy == ItemID.COAL ? 1 : expandLegacy == BlockID.PLANKS || expandLegacy == BlockID.WOODEN_SLAB ? 5 : expandLegacy == BlockID.STONE ? 6 : 15;

                for (int meta = 0; meta <= lastMeta; meta++) {
                    for (Item item : sorted) {
                        if (item.getId() == expandLegacy) {
                            item.setDamage(meta);
                        }
                    }

                    this.registerRecipe(new ShapelessRecipe(null, 0, outputItem, sorted));
                }
            } else {
                this.registerRecipe(new ShapelessRecipe(null, 0, outputItem, sorted));
            }
        }
    }

    public BatchPacket getCachedPacket(int protocol) { // Remember to update rebuildPacket
        if (protocol >= ProtocolInfo.v1_26_30) {
            if (packet1001 == null) packet1001 = packetFor(protocol);
            return packet1001;
        } else if (protocol >= ProtocolInfo.v1_26_20_26) {
            if (packet975 == null) packet975 = packetFor(protocol);
            return packet975;
        } else if (protocol >= ProtocolInfo.v1_26_10) {
            if (packet944 == null) packet944 = packetFor(protocol);
            return packet944;
        } else if (protocol >= ProtocolInfo.v1_21_130_28) {
            if (packet897 == null) packet897 = packetFor(protocol);
            return packet897;
        } else if (protocol >= ProtocolInfo.v1_21_120) {
            if (packet859 == null) packet859 = packetFor(protocol);
            return packet859;
        } else if (protocol >= ProtocolInfo.v1_21_110) {
            if (packet843 == null) packet843 = packetFor(protocol);
            return packet843;
        } else if (protocol >= ProtocolInfo.v1_21_100) {
            if (packet827 == null) packet827 = packetFor(protocol);
            return packet827;
        } else if (protocol >= ProtocolInfo.v1_21_90) {
            if (packet818 == null) packet818 = packetFor(protocol);
            return packet818;
        } else if (protocol >= ProtocolInfo.v1_21_80) {
            if (packet800 == null) packet800 = packetFor(protocol);
            return packet800;
        } else if (protocol >= ProtocolInfo.v1_21_70_24) {
            if (packet786 == null) packet786 = packetFor(protocol);
            return packet786;
        } else if (protocol >= ProtocolInfo.v1_21_60) {
            if (packet776 == null) packet776 = packetFor(protocol);
            return packet776;
        } else if (protocol >= ProtocolInfo.v1_21_50_28) {
            if (packet766 == null) packet766 = packetFor(protocol);
            return packet766;
        } else if (protocol >= ProtocolInfo.v1_21_40) {
            if (packet748 == null) packet748 = packetFor(protocol);
            return packet748;
        } else if (protocol >= ProtocolInfo.v1_21_30) {
            if (packet729 == null) packet729 = packetFor(protocol);
            return packet729;
        } else if (protocol >= ProtocolInfo.v1_21_20) {
            if (packet712 == null) packet712 = packetFor(protocol);
            return packet712;
        } else if (protocol >= ProtocolInfo.v1_21_0) {
            if (packet685 == null) packet685 = packetFor(protocol);
            return packet685;
        } else if (protocol >= ProtocolInfo.v1_20_80) {
            if (packet671 == null) packet671 = packetFor(protocol);
            return packet671;
        } else if (protocol >= ProtocolInfo.v1_20_70) {
            if (packet662 == null) packet662 = packetFor(protocol);
            return packet662;
        } else if (protocol >= ProtocolInfo.v1_20_60) {
            if (packet649 == null) packet649 = packetFor(protocol);
            return packet649;
        } else if (protocol >= ProtocolInfo.v1_20_50) {
            if (packet630 == null) packet630 = packetFor(protocol);
            return packet630;
        } else if (protocol >= ProtocolInfo.v1_20_40) {
            if (packet622 == null) packet622 = packetFor(protocol);
            return packet622;
        } else if (protocol >= ProtocolInfo.v1_20_30) {
            if (packet618 == null) packet618 = packetFor(protocol);
            return packet618;
        } else if (protocol >= ProtocolInfo.v1_20_10_21) {
            if (packet594 == null) packet594 = packetFor(protocol);
            return packet594;
        } else if (protocol >= ProtocolInfo.v1_20_0_23) {
            if (packet589 == null) packet589 = packetFor(protocol);
            return packet589;
        } else if (protocol >= ProtocolInfo.v1_19_80) {
            if (packet582 == null) packet582 = packetFor(protocol);
            return packet582;
        } else if (protocol >= ProtocolInfo.v1_19_70_24) {
            if (packet575 == null) packet575 = packetFor(protocol);
            return packet575;
        } else if (protocol >= ProtocolInfo.v1_19_60) {
            if (packet567 == null) packet567 = packetFor(protocol);
            return packet567;
        } else if (protocol >= ProtocolInfo.v1_19_50) {
            if (packet560 == null) packet560 = packetFor(protocol);
            return packet560;
        } else if (protocol >= ProtocolInfo.v1_19_30_23) {
            if (packet554 == null) packet554 = packetFor(protocol);
            return packet554;
        } else if (protocol >= ProtocolInfo.v1_19_20) {
            if (packet544 == null) packet544 = packetFor(protocol);
            return packet544;
        } else if (protocol >= ProtocolInfo.v1_19_0_29) {
            if (packet527 == null) packet527 = packetFor(protocol);
            return packet527;
        } else if (protocol >= ProtocolInfo.v1_18_30) {
            if (packet503 == null) packet503 = packetFor(protocol);
            return packet503;
        } else if (protocol >= ProtocolInfo.v1_18_10_26) {
            if (packet486 == null) packet486 = packetFor(protocol);
            return packet486;
        } else if (protocol >= ProtocolInfo.v1_17_40) {
            if (packet471 == null) packet471 = packetFor(protocol);
            return packet471;
        } else if (protocol >= ProtocolInfo.v1_17_30) {
            if (packet465 == null) packet465 = packetFor(protocol);
            return packet465;
        } else if (protocol >= ProtocolInfo.v1_17_10) {
            if (packet448 == null) packet448 = packetFor(protocol);
            return packet448;
        } else if (protocol >= ProtocolInfo.v1_17_0) {
            if (packet440 == null) packet440 = packetFor(protocol);
            return packet440;
        } else if (protocol >= ProtocolInfo.v1_16_220) {
            if (packet431 == null) packet431 = packetFor(protocol);
            return packet431;
        } else if (protocol >= ProtocolInfo.v1_16_100) {
            if (packet419 == null) packet419 = packetFor(protocol);
            return packet419;
        } else if (protocol >= ProtocolInfo.v1_16_0) {
            if (packet407 == null) packet407 = packetFor(protocol);
            return packet407;
        } else if (protocol >= ProtocolInfo.v1_13_0) {
            if (packet388 == null) packet388 = packetFor(protocol);
            return packet388;
        } else if (protocol >= ProtocolInfo.v1_12_0) {
            if (packet361 == null) packet361 = packetFor(protocol);
            return packet361;
        } else if (protocol >= ProtocolInfo.v1_11_0) {
            if (packet354 == null) packet354 = packetFor(protocol);
            return packet354;
        }
        return null;
    }

    private static int getContainerHash(int ingredientId, int containerId) {
        return (ingredientId << 15) | containerId;
    }

    private static int getFullItemHash(Item item) {
        return (getItemHash(item) << 6) | (item.getCount() & 0x3f);
    }

    private static int getItemHash(Item item) {
        return getItemHash(item.getId(), item.getDamage());
    }

    /* Register recipes start */

    private static int getItemHash(int id, int meta) {
        return (id << 12) | (meta & 0xfff);
    }

    private static UUID getMultiItemHash(Collection<Item> items) {
        BinaryStream stream = new BinaryStream(new byte[5 * items.size()]).reset();
        for (Item item : items) {
            stream.putVarInt(getFullItemHash(item));
        }
        return UUID.nameUUIDFromBytes(stream.getBuffer());
    }

    private static int getPotionHash(Item ingredient, Item potion) {
        int ingredientHash = ((ingredient.getId() & 0x3FF) << 6) | (ingredient.getDamage() & 0x3F);
        int potionHash = ((potion.getId() & 0x3FF) << 6) | (potion.getDamage() & 0x3F);
        return ingredientHash << 16 | potionHash;
    }

    public BlastFurnaceRecipe matchBlastFurnaceRecipe(Item input) {
        BlastFurnaceRecipe recipe = this.blastFurnaceRecipes.get(getItemHash(input));
        if (recipe == null) {
            recipe = this.blastFurnaceRecipes.get(getItemHash(input.getId(), 0));
        }
        return recipe;
    }

    public BrewingRecipe matchBrewingRecipe(Item input, Item potion) {
        return this.brewingRecipes.get(getPotionHash(input, potion));
    }

    public CampfireRecipe matchCampfireRecipe(Item input) {
        CampfireRecipe recipe = this.campfireRecipes.get(getItemHash(input));
        if (recipe == null) {
            recipe = this.campfireRecipes.get(getItemHash(input.getId(), 0));
        }
        return recipe;
    }

    public ContainerRecipe matchContainerRecipe(Item input, Item potion) {
        return this.containerRecipes.get(getContainerHash(input.getId(), potion.getId()));
    }

    public FurnaceRecipe matchFurnaceRecipe(Item input) {
        FurnaceRecipe recipe = this.furnaceRecipes.get(getItemHash(input));
        if (recipe == null) {
            recipe = this.furnaceRecipes.get(getItemHash(input.getId(), 0));
        }
        return recipe;
    }

    private static boolean matchItemsAccumulation(CraftingRecipe recipe, List<Item> inputList, Item primaryOutput, List<Item> extraOutputList) {
        Item recipeResult = recipe.getResult();
        if (primaryOutput.equals(recipeResult, recipeResult.hasMeta(), recipeResult.hasCompoundTag()) && primaryOutput.getCount() % recipeResult.getCount() == 0) {
            int multiplier = primaryOutput.getCount() / recipeResult.getCount();
            return recipe.matchItems(inputList, extraOutputList, multiplier);
        }
        return false;
    }

    public CraftingRecipe matchRecipe(List<Item> inputList, Item primaryOutput, List<Item> extraOutputList) {
        int outputHash = getItemHash(primaryOutput);


        Map<UUID, ShapedRecipe> shapedRecipeMap = this.shapedRecipes.get(outputHash);

        if (shapedRecipeMap != null) {
            inputList.sort(recipeComparator);
            UUID inputHash = getMultiItemHash(inputList);
            ShapedRecipe recipe = shapedRecipeMap.get(inputHash);
            if (recipe != null && (recipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(recipe, inputList, primaryOutput, extraOutputList))) {
                return recipe;
            }
            for (ShapedRecipe shapedRecipe : shapedRecipeMap.values()) {
                if (shapedRecipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(shapedRecipe, inputList, primaryOutput, extraOutputList)) {
                    return shapedRecipe;
                }
            }
        }

        Map<UUID, ShapelessRecipe> shapelessRecipeMap = this.shapelessRecipes.get(outputHash);

        if (shapelessRecipeMap != null) {
            inputList.sort(recipeComparator);
            UUID inputHash = getMultiItemHash(inputList);
            ShapelessRecipe recipe = shapelessRecipeMap.get(inputHash);
            if (recipe != null && (recipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(recipe, inputList, primaryOutput, extraOutputList))) {
                return recipe;
            }
            for (ShapelessRecipe shapelessRecipe : shapelessRecipeMap.values()) {
                if (shapelessRecipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(shapelessRecipe, inputList, primaryOutput, extraOutputList)) {
                    return shapelessRecipe;
                }
            }
        }

        return null;
    }

    public SmithingRecipe matchSmithingRecipe(List<Item> inputList) {
        inputList.sort(recipeComparator);

        SmithingRecipe recipe = this.smithingRecipes.get(getMultiItemHash(inputList));
        if (recipe != null && recipe.matchItems(inputList)) {
            return recipe;
        }

        ArrayList<Item> list = new ArrayList<>(inputList.size());
        for (Item item : inputList) {
            Item clone = item.clone();
            clone.setCount(1);
            if (item instanceof ItemDurable && item.getDamage() > 0) {
                clone.setDamage(0);
            }
            list.add(clone);
        }

        for (SmithingRecipe smithingRecipe : this.smithingRecipes.values()) {
            if (smithingRecipe.matchItems(list)) {
                return smithingRecipe;
            }
        }

        return null;
    }

    /* Match recipes start */

    public SmokerRecipe matchSmokerRecipe(Item input) {
        SmokerRecipe recipe = this.smokerRecipes.get(getItemHash(input));
        if (recipe == null) {
            recipe = this.smokerRecipes.get(getItemHash(input.getId(), 0));
        }
        return recipe;
    }

    private static boolean needExpandLegacy(int id) {
        return id == BlockID.WOOL || id == BlockID.STONE || id == BlockID.PLANKS || id == BlockID.WOODEN_SLAB || id == ItemID.COAL;
    }

    private BatchPacket packetFor(int protocol) {
        CraftingDataPacket pk = new CraftingDataPacket();
        pk.protocol = protocol;
        top:
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                for (Item item : ((ShapedRecipe) recipe).getAllResults()) {
                    if (!item.isSupportedOn(protocol)) {
                        continue top;
                    }
                }
                for (Item item : ((ShapedRecipe) recipe).getIngredientList()) {
                    if (!item.isSupportedOn(protocol)) {
                        continue top;
                    }
                }
                pk.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                if (!recipe.getResult().isSupportedOn(protocol)) {
                    continue;
                }
                for (Item item : ((ShapelessRecipe) recipe).getIngredientList()) {
                    if (!item.isSupportedOn(protocol)) {
                        continue top;
                    }
                }
                pk.addShapelessRecipe((ShapelessRecipe) recipe);
            } else if (recipe instanceof FurnaceRecipe) {
                if (!((FurnaceRecipe) recipe).getInput().isSupportedOn(protocol) || !recipe.getResult().isSupportedOn(protocol)) {
                    continue;
                }
                pk.addFurnaceRecipe((FurnaceRecipe) recipe);
            }
        }
        if (protocol >= ProtocolInfo.v1_13_0) {
            for (BrewingRecipe recipe : this.brewingRecipes.values()) {
                if (!recipe.getInput().isSupportedOn(protocol) || !recipe.getIngredient().isSupportedOn(protocol) || !recipe.getResult().isSupportedOn(protocol)) {
                    continue;
                }
                pk.addBrewingRecipe(recipe);
            }
            for (ContainerRecipe recipe : this.containerRecipes.values()) {
                if (!recipe.getInput().isSupportedOn(protocol) || !recipe.getIngredient().isSupportedOn(protocol) || !recipe.getResult().isSupportedOn(protocol)) {
                    continue;
                }
                pk.addContainerRecipe(recipe);
            }
            if (protocol >= ProtocolInfo.v1_16_0) {
                // Note: Currently not implemented
                for (MultiRecipe recipe : this.multiRecipes.values()) {
                    pk.addMultiRecipe(recipe);
                }
            }
        }
        pk.tryEncode();
        return pk.compress(Deflater.BEST_COMPRESSION);
    }

    /**
     * Rebuild cached CraftingDataPacket for all protocols after the recipe list has been changed
     */
    public void rebuildPacket() {
        packet1001 = null;
        packet975 = null;
        packet944 = null;
        packet897 = null;
        packet859 = null;
        packet843 = null;
        packet827 = null;
        packet818 = null;
        packet800 = null;
        packet786 = null;
        packet776 = null;
        packet766 = null;
        packet748 = null;
        packet729 = null;
        packet712 = null;
        packet685 = null;
        packet671 = null;
        packet662 = null;
        packet649 = null;
        packet630 = null;
        packet622 = null;
        packet618 = null;
        packet594 = null;
        packet589 = null;
        packet582 = null;
        packet575 = null;
        packet567 = null;
        packet560 = null;
        packet554 = null;
        packet544 = null;
        packet527 = null;
        packet503 = null;
        packet486 = null;
        packet471 = null;
        packet465 = null;
        packet448 = null;
        packet440 = null;
        packet431 = null;
        packet419 = null;
        packet407 = null;
        packet388 = null;
        packet361 = null;
        packet354 = null;
        this.getCachedPacket(ProtocolInfo.CURRENT_PROTOCOL); // Always cache the packet for the current protocol
    }

    public void registerBlastFurnaceRecipe(BlastFurnaceRecipe recipe) {
        this.blastFurnaceRecipes.put(getItemHash(recipe.getInput()), recipe);
    }

    public void registerBrewingRecipe(BrewingRecipe recipe) {
        this.brewingRecipes.put(getPotionHash(recipe.getIngredient(), recipe.getInput()), recipe);
    }

    public void registerCampfireRecipe(CampfireRecipe recipe) {
        this.campfireRecipes.put(getItemHash(recipe.getInput()), recipe);
    }

    public void registerContainerRecipe(ContainerRecipe recipe) {
        this.containerRecipes.put(getContainerHash(recipe.getIngredient().getId(), recipe.getInput().getId()), recipe);
    }

    public void registerFurnaceRecipe(FurnaceRecipe recipe) {
        this.furnaceRecipes.put(getItemHash(recipe.getInput()), recipe);
    }

    /* Hash items start */

    public void registerMultiRecipe(MultiRecipe recipe) {
        this.multiRecipes.put(recipe.getId(), recipe);
    }

    public void registerRecipe(Recipe recipe) {
        if (recipe instanceof CraftingRecipe) {
            UUID id = Utils.dataToUUID(String.valueOf(++RECIPE_COUNT), String.valueOf(recipe.getResult().getId()), String.valueOf(recipe.getResult().getDamage()), String.valueOf(recipe.getResult().getCount()), Arrays.toString(recipe.getResult().getCompoundTag()));
            ((CraftingRecipe) recipe).setId(id);
        } else if (recipe instanceof FurnaceRecipe) {
            ((FurnaceRecipe) recipe).setId(Utils.dataToUUID(String.valueOf(++RECIPE_COUNT), String.valueOf(recipe.getResult().getId()), String.valueOf(recipe.getResult().getDamage()), String.valueOf(recipe.getResult().getCount()), Arrays.toString(recipe.getResult().getCompoundTag())));
        }
        this.recipes.add(recipe);
        recipe.registerToCraftingManager(this);
    }

    public void registerShapedRecipe(ShapedRecipe recipe) {
        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, ShapedRecipe> map;
        map = this.shapedRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());
        map.put(getMultiItemHash(new LinkedList<>(recipe.getIngredientsAggregate())), recipe);
    }

    public void registerShapelessRecipe(ShapelessRecipe recipe) {
        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, ShapelessRecipe> map;
        map = this.shapelessRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());
        map.put(getMultiItemHash(recipe.getIngredientsAggregate()), recipe);
    }

    public void registerSmithingRecipe(SmithingRecipe recipe) {
        this.smithingRecipes.put(getMultiItemHash(recipe.getIngredientsAggregate()), recipe);
    }

    public void registerSmokerRecipe(SmokerRecipe recipe) {
        this.smokerRecipes.put(getItemHash(recipe.getInput()), recipe);
    }
}
