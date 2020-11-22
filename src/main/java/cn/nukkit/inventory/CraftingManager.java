package cn.nukkit.inventory;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Utils;
import io.netty.util.collection.CharObjectHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.*;
import java.util.zip.Deflater;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class CraftingManager {

    private final Collection<Recipe> recipes313 = new ArrayDeque<>();
    private final Collection<Recipe> recipes340 = new ArrayDeque<>();
    public final Collection<Recipe> recipes = new ArrayDeque<>();

    public static BatchPacket packet313 = null;
    public static BatchPacket packet340 = null;
    public static BatchPacket packet361 = null;
    public static BatchPacket packet354 = null;
    public static BatchPacket packet338 = null;
    public static BatchPacket packet407 = null;
    public static BatchPacket packet419 = null;

    protected final Map<Integer, Map<UUID, ShapedRecipe>> shapedRecipes = new Int2ObjectOpenHashMap<>();
    public final Map<Integer, FurnaceRecipe> furnaceRecipes = new Int2ObjectOpenHashMap<>();
    public final Map<Integer, BrewingRecipe> brewingRecipes = new Int2ObjectOpenHashMap<>();
    public final Map<Integer, BrewingRecipe> brewingRecipesOld = new Int2ObjectOpenHashMap<>();
    public final Map<Integer, ContainerRecipe> containerRecipes = new Int2ObjectOpenHashMap<>();
    public final Map<Integer, ContainerRecipe> containerRecipesOld = new Int2ObjectOpenHashMap<>();
    protected final Map<Integer, Map<UUID, ShapelessRecipe>> shapelessRecipes = new Int2ObjectOpenHashMap<>();

    private static int RECIPE_COUNT = 0;
    public static int NEXT_NETWORK_ID = 0;

    public static final Comparator<Item> recipeComparator = (i1, i2) -> {
        if (i1.getId() > i2.getId()) {
            return 1;
        } else if (i1.getId() < i2.getId()) {
            return -1;
        } else if (i1.getDamage() > i2.getDamage()) {
            return 1;
        } else if (i1.getDamage() < i2.getDamage()) {
            return -1;
        } else return Integer.compare(i1.getCount(), i2.getCount());
    };

    @SuppressWarnings("unchecked")
    public CraftingManager() {
        MainLogger.getLogger().debug("Loading recipes...");
        List<Map> recipes_388 = new Config(Config.YAML).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes388.json")).getRootSection().getMapList("recipes");
        List<Map> recipes_332 = new Config(Config.YAML).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes332.json")).getMapList("recipes");
        List<Map> recipes_313 = new Config(Config.YAML).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes313.json")).getMapList("recipes");

        for (Map<String, Object> recipe : recipes_388) {
            try {
                switch (Utils.toInt(recipe.get("type"))) {
                    case 0:
                        String craftingBlock = (String) recipe.get("block");
                        if (!"crafting_table".equals(craftingBlock)) {
                            // Ignore other recipes than crafting table ones
                            continue;
                        }
                        List<Map> outputs = ((List<Map>) recipe.get("output"));
                        if (outputs.size() > 1) {
                            continue;
                        }
                        Map<String, Object> first = outputs.get(0);
                        List<Item> sorted = new ArrayList<>();
                        for (Map<String, Object> ingredient : ((List<Map>) recipe.get("input"))) {
                            sorted.add(Item.fromJson(ingredient));
                        }
                        sorted.sort(recipeComparator);

                        String recipeId = (String) recipe.get("id");
                        int priority = Utils.toInt(recipe.get("priority"));

                        ShapelessRecipe result = new ShapelessRecipe(recipeId, priority, Item.fromJson(first), sorted);

                        this.registerRecipe(result);
                        break;
                    case 1:
                        craftingBlock = (String) recipe.get("block");
                        if (!"crafting_table".equals(craftingBlock)) {
                            // Ignore other recipes than crafting table ones
                            continue;
                        }
                        outputs = (List<Map>) recipe.get("output");

                        first = outputs.remove(0);
                        String[] shape = ((List<String>) recipe.get("shape")).toArray(new String[0]);
                        Map<Character, Item> ingredients = new CharObjectHashMap<>();
                        List<Item> extraResults = new ArrayList<>();

                        Map<String, Map<String, Object>> input = (Map) recipe.get("input");
                        for (Map.Entry<String, Map<String, Object>> ingredientEntry : input.entrySet()) {
                            char ingredientChar = ingredientEntry.getKey().charAt(0);
                            Item ingredient = Item.fromJson(ingredientEntry.getValue());

                            ingredients.put(ingredientChar, ingredient);
                        }

                        for (Map<String, Object> data : outputs) {
                            extraResults.add(Item.fromJson(data));
                        }

                        recipeId = (String) recipe.get("id");
                        priority = Utils.toInt(recipe.get("priority"));

                        this.registerRecipe(new ShapedRecipe(recipeId, priority, Item.fromJson(first), shape, ingredients, extraResults));
                        break;
                    case 2:
                    case 3:
                        craftingBlock = (String) recipe.get("block");
                        if (!"furnace".equals(craftingBlock)) {
                            // Ignore other recipes than furnaces
                            continue;
                        }
                        Map<String, Object> resultMap = (Map) recipe.get("output");
                        Item resultItem = Item.fromJson(resultMap);
                        Item inputItem;
                        try {
                            Map<String, Object> inputMap = (Map) recipe.get("input");
                            inputItem = Item.fromJson(inputMap);
                        } catch (Exception old) {
                            inputItem = Item.get(Utils.toInt(recipe.get("inputId")), recipe.containsKey("inputDamage") ? Utils.toInt(recipe.get("inputDamage")) : -1, 1);
                        }
                        this.registerRecipe(new FurnaceRecipe(resultItem, inputItem));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                MainLogger.getLogger().error("Exception during registering (protocol 388) recipe", e);
            }
        }

        for (Map<String, Object> recipe : recipes_313) {
            try {
                switch (Utils.toInt(recipe.get("type"))) {
                    case 0:
                        Map<String, Object> first = ((List<Map>) recipe.get("output")).get(0);
                        List<Item> sorted = new ArrayList<>();
                        for (Map<String, Object> ingredient : ((List<Map>) recipe.get("input"))) {
                            sorted.add(Item.fromJsonOld(ingredient));
                        }
                        sorted.sort(recipeComparator);
                        recipes313.add(new ShapelessRecipe(Item.fromJsonOld(first), sorted));
                        break;
                    case 1:
                        List<Map> output = (List<Map>) recipe.get("output");
                        first = output.remove(0);
                        String[] shape = ((List<String>) recipe.get("shape")).toArray(new String[0]);
                        Map<Character, Item> ingredients = new CharObjectHashMap<>();
                        List<Item> extraResults = new ArrayList<>();
                        Map<String, Map<String, Object>> input = (Map) recipe.get("input");
                        for (Map.Entry<String, Map<String, Object>> ingredientEntry : input.entrySet()) {
                            char ingredientChar = ingredientEntry.getKey().charAt(0);
                            Item ingredient = Item.fromJsonOld(ingredientEntry.getValue());
                            ingredients.put(ingredientChar, ingredient);
                        }
                        for (Map<String, Object> data : output) {
                            extraResults.add(Item.fromJsonOld(data));
                        }
                        recipes313.add(new ShapedRecipe(Item.fromJsonOld(first), shape, ingredients, extraResults));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                MainLogger.getLogger().error("Exception during registering (protocol 313) recipe", e);
            }
        }

        for (Map<String, Object> recipe : recipes_332) {
            try {
                switch (Utils.toInt(recipe.get("type"))) {
                    case 0:
                        Map<String, Object> first = ((List<Map>) recipe.get("output")).get(0);
                        List<Item> sorted = new ArrayList<>();
                        for (Map<String, Object> ingredient : ((List<Map>) recipe.get("input"))) {
                            sorted.add(Item.fromJsonOld(ingredient));
                        }
                        sorted.sort(recipeComparator);
                        recipes340.add(new ShapelessRecipe(Item.fromJsonOld(first), sorted));
                        break;
                    case 1:
                        List<Map> output = (List<Map>) recipe.get("output");
                        first = output.remove(0);
                        String[] shape = ((List<String>) recipe.get("shape")).toArray(new String[0]);
                        Map<Character, Item> ingredients = new CharObjectHashMap<>();
                        List<Item> extraResults = new ArrayList<>();
                        Map<String, Map<String, Object>> input = (Map) recipe.get("input");
                        for (Map.Entry<String, Map<String, Object>> ingredientEntry : input.entrySet()) {
                            char ingredientChar = ingredientEntry.getKey().charAt(0);
                            Item ingredient = Item.fromJsonOld(ingredientEntry.getValue());
                            ingredients.put(ingredientChar, ingredient);
                        }
                        for (Map<String, Object> data : output) {
                            extraResults.add(Item.fromJsonOld(data));
                        }
                        recipes340.add(new ShapedRecipe(Item.fromJsonOld(first), shape, ingredients, extraResults));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                MainLogger.getLogger().error("Exception during registering (protocol 332) recipe", e);
            }
        }

        Config extras = new Config(Config.YAML).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes388.json"));
        List<Map> potionMixes = extras.getMapList("potionMixes");
        for (Map potionMix : potionMixes) {
            int fromPotionId = ((Number) potionMix.get("fromPotionId")).intValue();
            int ingredient = ((Number) potionMix.get("ingredient")).intValue();
            int toPotionId = ((Number) potionMix.get("toPotionId")).intValue();
            registerBrewingRecipeOld(new BrewingRecipe(Item.get(ItemID.POTION, fromPotionId), Item.get(ingredient), Item.get(ItemID.POTION, toPotionId)));
        }

        List<Map> containerMixes = extras.getMapList("containerMixes");
        for (Map containerMix : containerMixes) {
            int fromItemId = ((Number) containerMix.get("fromItemId")).intValue();
            int ingredient = ((Number) containerMix.get("ingredient")).intValue();
            int toItemId = ((Number) containerMix.get("toItemId")).intValue();
            registerContainerRecipeOld(new ContainerRecipe(Item.get(fromItemId), Item.get(ingredient), Item.get(toItemId)));
        }

        Config extras407 = new Config(Config.YAML).loadFromStream(Server.class.getClassLoader().getResourceAsStream("extras_407.json"));
        List<Map> potionMixes407 = extras407.getMapList("potionMixes");
        for (Map potionMix : potionMixes407) {
            int fromPotionId = ((Number) potionMix.get("inputId")).intValue();
            int fromPotionMeta = ((Number) potionMix.get("inputMeta")).intValue();
            int ingredient = ((Number) potionMix.get("reagentId")).intValue();
            int ingredientMeta = ((Number) potionMix.get("reagentMeta")).intValue();
            int toPotionId = ((Number) potionMix.get("outputId")).intValue();
            int toPotionMeta = ((Number) potionMix.get("outputMeta")).intValue();
            registerBrewingRecipe(new BrewingRecipe(Item.get(fromPotionId, fromPotionMeta), Item.get(ingredient, ingredientMeta), Item.get(toPotionId, toPotionMeta)));
        }

        List<Map> containerMixes407 = extras407.getMapList("containerMixes");
        for (Map containerMix : containerMixes407) {
            int fromItemId = ((Number) containerMix.get("inputId")).intValue();
            int ingredient = ((Number) containerMix.get("reagentId")).intValue();
            int toItemId = ((Number) containerMix.get("outputId")).intValue();
            registerContainerRecipe(new ContainerRecipe(Item.get(fromItemId), Item.get(ingredient), Item.get(toItemId)));
        }

        this.rebuildPacket();
        MainLogger.getLogger().debug("Loaded " + this.recipes.size() + " recipes");
    }

    public void rebuildPacket() {
        CraftingDataPacket pk419 = new CraftingDataPacket();
        pk419.cleanRecipes = true;
        pk419.protocol = 419;
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk419.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk419.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk419.addFurnaceRecipe(recipe);
        }
        for (BrewingRecipe recipe : brewingRecipes.values()) {
            pk419.addBrewingRecipe(recipe);
        }
        for (ContainerRecipe recipe : containerRecipes.values()) {
            pk419.addContainerRecipe(recipe);
        }
        pk419.encode();
        packet419 = pk419.compress(Deflater.BEST_COMPRESSION);
        CraftingDataPacket pk407 = new CraftingDataPacket();
        pk407.cleanRecipes = true;
        pk407.protocol = 407;
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk407.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk407.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk407.addFurnaceRecipe(recipe);
        }
        for (BrewingRecipe recipe : brewingRecipes.values()) {
            pk407.addBrewingRecipe(recipe);
        }
        for (ContainerRecipe recipe : containerRecipes.values()) {
            pk407.addContainerRecipe(recipe);
        }
        pk407.encode();
        packet407 = pk407.compress(Deflater.BEST_COMPRESSION);
        // 388
        CraftingDataPacket pk388 = new CraftingDataPacket();
        pk388.cleanRecipes = true;
        pk388.protocol = 388;
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk388.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk388.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk388.addFurnaceRecipe(recipe);
        }
        for (BrewingRecipe recipe : brewingRecipesOld.values()) {
            pk388.addBrewingRecipe(recipe);
        }
        for (ContainerRecipe recipe : containerRecipesOld.values()) {
            pk388.addContainerRecipe(recipe);
        }
        pk388.encode();
        packet338 = pk388.compress(Deflater.BEST_COMPRESSION);
        // 361
        CraftingDataPacket pk361 = new CraftingDataPacket();
        pk361.cleanRecipes = true;
        pk361.protocol = 361;
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk361.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk361.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk361.addFurnaceRecipe(recipe);
        }
        pk361.encode();
        packet361 = pk361.compress(Deflater.BEST_COMPRESSION);
        // 354
        CraftingDataPacket pk354 = new CraftingDataPacket();
        pk354.cleanRecipes = true;
        pk354.protocol = 354;
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk354.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk354.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk354.addFurnaceRecipe(recipe);
        }
        pk354.encode();
        packet354 = pk354.compress(Deflater.BEST_COMPRESSION);
        // 340
        CraftingDataPacket pk340 = new CraftingDataPacket();
        pk340.cleanRecipes = true;
        pk340.protocol = 340;
        for (Recipe recipe : this.recipes340) {
            if (recipe instanceof ShapedRecipe) {
                pk340.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk340.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk340.addFurnaceRecipe(recipe);
        }
        pk340.encode();
        packet340 = pk340.compress(Deflater.BEST_COMPRESSION);
        // 313
        CraftingDataPacket pk313 = new CraftingDataPacket();
        pk313.cleanRecipes = true;
        pk313.protocol = 313;
        for (Recipe recipe : this.recipes313) {
            if (recipe instanceof ShapedRecipe) {
                pk313.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk313.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        //TODO: furnace recipes?
        /*for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk313.addFurnaceRecipe(recipe);
        }*/
        pk313.encode();
        packet313 = pk313.compress(Deflater.BEST_COMPRESSION);
    }

    public Collection<Recipe> getRecipes() {
        return recipes;
    }

    public Map<Integer, FurnaceRecipe> getFurnaceRecipes() {
        return furnaceRecipes;
    }

    public FurnaceRecipe matchFurnaceRecipe(Item input) {
        FurnaceRecipe recipe = this.furnaceRecipes.get(getItemHash(input));
        if (recipe == null) recipe = this.furnaceRecipes.get(getItemHash(input.getId(), 0));
        return recipe;
    }

    private static UUID getMultiItemHash(Collection<Item> items) {
        BinaryStream stream = new BinaryStream();
        for (Item item : items) {
            stream.putVarInt(getFullItemHash(item));
        }
        return UUID.nameUUIDFromBytes(stream.getBuffer());
    }

    private static int getFullItemHash(Item item) {
        return 31 * getItemHash(item) + item.getCount();
    }

    public void registerFurnaceRecipe(FurnaceRecipe recipe) {
        this.furnaceRecipes.put(getItemHash(recipe.getInput()), recipe);
    }

    private static int getItemHash(Item item) {
        return getItemHash(item.getId(), item.getDamage());
    }

    private static int getItemHash(int id, int meta) {
        return (id << 4) | (meta & 0xf);
    }

    public void registerShapedRecipe(ShapedRecipe recipe) {
        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, ShapedRecipe> map = shapedRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());
        List<Item> inputList = new LinkedList<>(recipe.getIngredientsAggregate());
        map.put(getMultiItemHash(inputList), recipe);
    }

    public void registerRecipe(Recipe recipe) {
        if (recipe instanceof CraftingRecipe) {
            UUID id = Utils.dataToUUID(String.valueOf(++RECIPE_COUNT), String.valueOf(recipe.getResult().getId()), String.valueOf(recipe.getResult().getDamage()), String.valueOf(recipe.getResult().getCount()), Arrays.toString(recipe.getResult().getCompoundTag()));
            ((CraftingRecipe) recipe).setId(id);
            this.recipes.add(recipe);
        }
        recipe.registerToCraftingManager(this);
    }

    public void registerShapelessRecipe(ShapelessRecipe recipe) {
        List<Item> list = recipe.getIngredientsAggregate();

        UUID hash = getMultiItemHash(list);

        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, ShapelessRecipe> map = shapelessRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());

        map.put(hash, recipe);
    }

    private static int getPotionHash(int ingredientId, int potionType) {
        return (ingredientId << 6) | potionType;
    }

    private static int getContainerHash(int ingredientId, int containerId) {
        return (ingredientId << 9) | containerId;
    }

    public void registerBrewingRecipe(BrewingRecipe recipe) {
        Item input = recipe.getIngredient();
        Item potion = recipe.getInput();
        this.brewingRecipes.put(getPotionHash(input.getId(), potion.getDamage()), recipe);
    }

    public void registerBrewingRecipeOld(BrewingRecipe recipe) {
        Item input = recipe.getIngredient();
        Item potion = recipe.getInput();
        this.brewingRecipesOld.put(getPotionHash(input.getId(), potion.getDamage()), recipe);
    }

    public void registerContainerRecipe(ContainerRecipe recipe) {
        Item input = recipe.getIngredient();
        Item potion = recipe.getInput();
        this.containerRecipes.put(getContainerHash(input.getId(), potion.getId()), recipe);
    }

    public void registerContainerRecipeOld(ContainerRecipe recipe) {
        Item input = recipe.getIngredient();
        Item potion = recipe.getInput();
        this.containerRecipesOld.put(getContainerHash(input.getId(), potion.getId()), recipe);
    }

    public BrewingRecipe matchBrewingRecipe(Item input, Item potion) {
        int id = potion.getId();
        if (id == Item.POTION || id == Item.SPLASH_POTION || id == Item.LINGERING_POTION) {
            return this.brewingRecipes.get(getPotionHash(input.getId(), potion.getDamage()));
        }
        return null;
    }

    public ContainerRecipe matchContainerRecipe(Item input, Item potion) {
        return this.containerRecipes.get(getContainerHash(input.getId(), potion.getId()));
    }

    public CraftingRecipe matchRecipe(List<Item> inputList, Item primaryOutput, List<Item> extraOutputList) {
        int outputHash = getItemHash(primaryOutput);
        if (this.shapedRecipes.containsKey(outputHash)) {
            inputList.sort(recipeComparator);

            UUID inputHash = getMultiItemHash(inputList);

            Map<UUID, ShapedRecipe> recipeMap = shapedRecipes.get(outputHash);

            if (recipeMap != null) {
                ShapedRecipe recipe = recipeMap.get(inputHash);

                if (recipe != null && (recipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(recipe, inputList, primaryOutput, extraOutputList))) {
                    return recipe;
                }

                for (ShapedRecipe shapedRecipe : recipeMap.values()) {
                    if (shapedRecipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(shapedRecipe, inputList, primaryOutput, extraOutputList)) {
                        return shapedRecipe;
                    }
                }
            }
        }

        if (shapelessRecipes.containsKey(outputHash)) {
            inputList.sort(recipeComparator);

            UUID inputHash = getMultiItemHash(inputList);

            Map<UUID, ShapelessRecipe> recipes = shapelessRecipes.get(outputHash);

            if (recipes == null) {
                return null;
            }

            ShapelessRecipe recipe = recipes.get(inputList);

            if (recipe != null && (recipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(recipe, inputList, primaryOutput, extraOutputList))) {
                return recipe;
            }

            for (ShapelessRecipe shapelessRecipe : recipes.values()) {
                if (shapelessRecipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(shapelessRecipe, inputList, primaryOutput, extraOutputList)) {
                    return shapelessRecipe;
                }
            }
        }

        return null;
    }

    private static boolean matchItemsAccumulation(CraftingRecipe recipe, List<Item> inputList, Item primaryOutput, List<Item> extraOutputList) {
        Item recipeResult = recipe.getResult();
        if (primaryOutput.equals(recipeResult, recipeResult.hasMeta(), recipeResult.hasCompoundTag()) && primaryOutput.getCount() % recipeResult.getCount() == 0) {
            int multiplier = primaryOutput.getCount() / recipeResult.getCount();
            return recipe.matchItems(inputList, extraOutputList, multiplier);
        }
        return false;
    }

    public static class Entry {
        final int resultItemId;
        final int resultMeta;
        final int ingredientItemId;
        final int ingredientMeta;
        final String recipeShape;
        final int resultAmount;

        public Entry(int resultItemId, int resultMeta, int ingredientItemId, int ingredientMeta, String recipeShape, int resultAmount) {
            this.resultItemId = resultItemId;
            this.resultMeta = resultMeta;
            this.ingredientItemId = ingredientItemId;
            this.ingredientMeta = ingredientMeta;
            this.recipeShape = recipeShape;
            this.resultAmount = resultAmount;
        }
    }
}
