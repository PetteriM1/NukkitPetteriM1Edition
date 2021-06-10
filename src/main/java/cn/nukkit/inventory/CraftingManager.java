package cn.nukkit.inventory;

import cn.nukkit.Server;
import cn.nukkit.block.BlockID;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
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
    private final Collection<Recipe> recipes332 = new ArrayDeque<>();
    public final Collection<Recipe> recipes = new ArrayDeque<>();

    public static BatchPacket packet313 = null;
    public static BatchPacket packet340 = null;
    public static BatchPacket packet361 = null;
    public static BatchPacket packet354 = null;
    public static BatchPacket packet388 = null;
    public static DataPacket packet407 = null;
    public static DataPacket packet419 = null;
    public static DataPacket packet431 = null;
    public static DataPacket packet440 = null;

    private final Map<Integer, Map<UUID, ShapedRecipe>> shapedRecipes313 = new Int2ObjectOpenHashMap<>();
    private final Map<Integer, Map<UUID, ShapedRecipe>> shapedRecipes332 = new Int2ObjectOpenHashMap<>();
    protected final Map<Integer, Map<UUID, ShapedRecipe>> shapedRecipes = new Int2ObjectOpenHashMap<>();

    private final Map<Integer, Map<UUID, ShapelessRecipe>> shapelessRecipes313 = new Int2ObjectOpenHashMap<>();
    private final Map<Integer, Map<UUID, ShapelessRecipe>> shapelessRecipes332 = new Int2ObjectOpenHashMap<>();
    protected final Map<Integer, Map<UUID, ShapelessRecipe>> shapelessRecipes = new Int2ObjectOpenHashMap<>();

    public final Map<UUID, MultiRecipe> multiRecipes = new HashMap<>();
    public final Map<Integer, FurnaceRecipe> furnaceRecipes = new Int2ObjectOpenHashMap<>();
    private final Map<Integer, FurnaceRecipe> furnaceRecipesOld = new Int2ObjectOpenHashMap<>();
    public final Map<Integer, BrewingRecipe> brewingRecipes = new Int2ObjectOpenHashMap<>();
    private final Map<Integer, BrewingRecipe> brewingRecipesOld = new Int2ObjectOpenHashMap<>();
    public final Map<Integer, ContainerRecipe> containerRecipes = new Int2ObjectOpenHashMap<>();
    private final Map<Integer, ContainerRecipe> containerRecipesOld = new Int2ObjectOpenHashMap<>();

    private static int RECIPE_COUNT = 0;
    public static int NEXT_NETWORK_ID = 0;

    // Torch with charcoal recipe fix for 1.16.100+
    private static ShapedRecipe charcoalTorchRecipe419;

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
                    /*case 4:
                        this.registerRecipe(new MultiRecipe(UUID.fromString((String) recipe.get("uuid"))));
                        break;*/
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
                        ShapelessRecipe recipe_ = new ShapelessRecipe(Item.fromJsonOld(first), sorted);
                        recipes313.add(recipe_);
                        this.registerShapelessRecipe(313, recipe_);
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
                        ShapedRecipe recipe__ = new ShapedRecipe(Item.fromJsonOld(first), shape, ingredients, extraResults);
                        recipes313.add(recipe__);
                        this.registerShapedRecipe(313, recipe__);
                        break;
                    case 2:
                    case 3:
                        Map<String, Object> resultMap = (Map) recipe.get("output");
                        Item resultItem = Item.fromJsonOld(resultMap);
                        Item inputItem;
                        try {
                            Map<String, Object> inputMap = (Map) recipe.get("input");
                            inputItem = Item.fromJsonOld(inputMap);
                        } catch (Exception old) {
                            inputItem = Item.get(Utils.toInt(recipe.get("inputId")), recipe.containsKey("inputDamage") ? Utils.toInt(recipe.get("inputDamage")) : -1, 1);
                        }
                        this.furnaceRecipesOld.put(getItemHash(inputItem), new FurnaceRecipe(resultItem, inputItem));
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
                        ShapelessRecipe recipe_ = new ShapelessRecipe(Item.fromJsonOld(first), sorted);
                        recipes332.add(recipe_);
                        this.registerShapelessRecipe(332, recipe_);
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
                        ShapedRecipe recipe__ = new ShapedRecipe(Item.fromJsonOld(first), shape, ingredients, extraResults);
                        recipes332.add(recipe__);
                        this.registerShapedRecipe(332, recipe__);
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

        // Torch with charcoal recipe fix for 1.16.100+
        // TODO: Update recipes for 1.16+
        Map<Character, Item> ingredients = new HashMap<>();
        ingredients.put('A', Item.get(ItemID.COAL, 1)); // Make sure it's charcoal after converting to runtime ids
        ingredients.put('B', Item.get(ItemID.STICK, -1));
        charcoalTorchRecipe419 = new ShapedRecipe("Torch_from_charcoal_recipeId", 50, Item.get(BlockID.TORCH, 0, 4), new String[]{"A", "B"}, ingredients, new ArrayList<>());

        this.rebuildPacket();
        MainLogger.getLogger().debug("Loaded " + this.recipes.size() + " recipes");
    }

    public void rebuildPacket() {
        CraftingDataPacket pk440 = new CraftingDataPacket();
        pk440.protocol = 440;
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk440.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk440.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        // Torch with charcoal recipe fix for 1.16.100+
        pk440.addShapedRecipe(charcoalTorchRecipe419);
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk440.addFurnaceRecipe(recipe);
        }
        for (MultiRecipe recipe : this.multiRecipes.values()) {
            pk440.addMultiRecipe(recipe);
        }
        for (BrewingRecipe recipe : brewingRecipes.values()) {
            pk440.addBrewingRecipe(recipe);
        }
        for (ContainerRecipe recipe : containerRecipes.values()) {
            pk440.addContainerRecipe(recipe);
        }
        pk440.tryEncode();
        packet440 = pk440;//.compress(Deflater.BEST_COMPRESSION); //TODO: figure out why this doesn't work with batching
        CraftingDataPacket pk431 = new CraftingDataPacket();
        pk431.protocol = 431;
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk431.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk431.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        // Torch with charcoal recipe fix for 1.16.100+
        pk431.addShapedRecipe(charcoalTorchRecipe419);
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk431.addFurnaceRecipe(recipe);
        }
        for (MultiRecipe recipe : this.multiRecipes.values()) {
            pk431.addMultiRecipe(recipe);
        }
        for (BrewingRecipe recipe : brewingRecipes.values()) {
            pk431.addBrewingRecipe(recipe);
        }
        for (ContainerRecipe recipe : containerRecipes.values()) {
            pk431.addContainerRecipe(recipe);
        }
        pk431.tryEncode();
        packet431 = pk431;//.compress(Deflater.BEST_COMPRESSION); //TODO: figure out why this doesn't work with batching
        CraftingDataPacket pk419 = new CraftingDataPacket();
        pk419.protocol = 419;
        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk419.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk419.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        // Torch with charcoal recipe fix for 1.16.100+
        pk431.addShapedRecipe(charcoalTorchRecipe419);
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk419.addFurnaceRecipe(recipe);
        }
        for (MultiRecipe recipe : this.multiRecipes.values()) {
            pk419.addMultiRecipe(recipe);
        }
        for (BrewingRecipe recipe : brewingRecipes.values()) {
            pk419.addBrewingRecipe(recipe);
        }
        for (ContainerRecipe recipe : containerRecipes.values()) {
            pk419.addContainerRecipe(recipe);
        }
        pk419.tryEncode();
        packet419 = pk419;//.compress(Deflater.BEST_COMPRESSION); //TODO: figure out why this doesn't work with batching
        CraftingDataPacket pk407 = new CraftingDataPacket();
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
        pk407.tryEncode();
        packet407 = pk407;// .compress(Deflater.BEST_COMPRESSION);
        // 388
        CraftingDataPacket pk388 = new CraftingDataPacket();
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
        pk388.tryEncode();
        packet388 = pk388.compress(Deflater.BEST_COMPRESSION);
        // 361
        CraftingDataPacket pk361 = new CraftingDataPacket();
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
        pk361.tryEncode();
        packet361 = pk361.compress(Deflater.BEST_COMPRESSION);
        // 354
        CraftingDataPacket pk354 = new CraftingDataPacket();
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
        pk354.tryEncode();
        packet354 = pk354.compress(Deflater.BEST_COMPRESSION);
        // 340
        CraftingDataPacket pk340 = new CraftingDataPacket();
        pk340.protocol = 340;
        for (Recipe recipe : this.recipes332) {
            if (recipe instanceof ShapedRecipe) {
                pk340.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk340.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk340.addFurnaceRecipe(recipe);
        }
        pk340.tryEncode();
        packet340 = pk340.compress(Deflater.BEST_COMPRESSION);
        // 313
        CraftingDataPacket pk313 = new CraftingDataPacket();
        pk313.protocol = 313;
        for (Recipe recipe : this.recipes313) {
            if (recipe instanceof ShapedRecipe) {
                pk313.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk313.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }
        for (FurnaceRecipe recipe : this.furnaceRecipesOld.values()) {
            pk313.addFurnaceRecipe(recipe);
        }
        pk313.tryEncode();
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
        registerShapedRecipe(ProtocolInfo.CURRENT_PROTOCOL, recipe);
    }

    public void registerShapedRecipe(int protocol, ShapedRecipe recipe) {
        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, ShapedRecipe> map;
        switch (protocol) {
            case 313:
                map = shapedRecipes313.computeIfAbsent(resultHash, k -> new HashMap<>());
                return;
            case 332:
                map = shapedRecipes332.computeIfAbsent(resultHash, k -> new HashMap<>());
                return;
            default:
                map = shapedRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());
        }
        map.put(getMultiItemHash(new LinkedList<>(recipe.getIngredientsAggregate())), recipe);
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
        registerShapelessRecipe(ProtocolInfo.CURRENT_PROTOCOL, recipe);
    }

    public void registerShapelessRecipe(int protocol, ShapelessRecipe recipe) {
        List<Item> list = recipe.getIngredientsAggregate();

        UUID hash = getMultiItemHash(list);

        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, ShapelessRecipe> map;

        switch (protocol) {
            case 313:
                map = shapelessRecipes313.computeIfAbsent(resultHash, k -> new HashMap<>());
                return;
            case 332:
                map = shapelessRecipes332.computeIfAbsent(resultHash, k -> new HashMap<>());
                return;
            default:
                map = shapelessRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());
        }

        map.put(hash, recipe);
    }

    private static int getPotionHash(Item ingredient, Item potion) {
        int ingredientHash = ((ingredient.getId() & 0x3FF) << 6) | (ingredient.getDamage() & 0x3F);
        int potionHash = ((potion.getId() & 0x3FF) << 6) | (potion.getDamage() & 0x3F);
        return ingredientHash << 16 | potionHash;
    }

    private static int getPotionHashOld(int ingredientId, int potionType) {
        return (ingredientId << 6) | potionType;
    }

    private static int getContainerHash(int ingredientId, int containerId) {
        return (ingredientId << 9) | containerId;
    }

    public void registerBrewingRecipe(BrewingRecipe recipe) {
        Item input = recipe.getIngredient();
        Item potion = recipe.getInput();
        int potionHash = getPotionHash(input, potion);
        this.brewingRecipes.put(potionHash, recipe);
    }

    public void registerBrewingRecipeOld(BrewingRecipe recipe) {
        Item input = recipe.getIngredient();
        Item potion = recipe.getInput();
        this.brewingRecipesOld.put(getPotionHashOld(input.getId(), potion.getDamage()), recipe);
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
        return this.brewingRecipes.get(getPotionHash(input, potion));
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

            Map<UUID, ShapelessRecipe> recipes = shapelessRecipes.get(outputHash);

            if (recipes == null) {
                return null;
            }

            UUID inputHash = getMultiItemHash(inputList);
            ShapelessRecipe recipe = recipes.get(inputHash);

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

    public void registerMultiRecipe(MultiRecipe recipe) {
        this.multiRecipes.put(recipe.getId(), recipe);
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
