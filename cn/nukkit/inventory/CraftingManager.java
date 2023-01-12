/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Server;
import cn.nukkit.inventory.BrewingRecipe;
import cn.nukkit.inventory.CampfireRecipe;
import cn.nukkit.inventory.ContainerRecipe;
import cn.nukkit.inventory.CraftingRecipe;
import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.MultiRecipe;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.inventory.ShapelessRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Utils;
import io.netty.util.collection.CharObjectHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CraftingManager {
    public static BatchPacket packet313;
    public static BatchPacket packet340;
    public static BatchPacket packet361;
    public static BatchPacket packet354;
    public static BatchPacket packet388;
    public static BatchPacket packet407;
    public static DataPacket packet419;
    public static DataPacket packet431;
    public static DataPacket packet440;
    public static DataPacket packet448;
    public static DataPacket packet465;
    public static DataPacket packet471;
    public static DataPacket packet486;
    public static DataPacket packet503;
    public static DataPacket packet527;
    public static DataPacket packet544;
    public static DataPacket packet554;
    public static DataPacket packet560;
    private final Collection<Recipe> b = new ArrayDeque<Recipe>();
    private final Collection<Recipe> r = new ArrayDeque<Recipe>();
    private final Collection<Recipe> q = new ArrayDeque<Recipe>();
    private final Collection<Recipe> h = new ArrayDeque<Recipe>();
    public final Collection<Recipe> recipes = new ArrayDeque<Recipe>();
    private final Map<Integer, Map<UUID, ShapedRecipe>> k = new Int2ObjectOpenHashMap<Map<UUID, ShapedRecipe>>();
    private final Map<Integer, Map<UUID, ShapedRecipe>> j = new Int2ObjectOpenHashMap<Map<UUID, ShapedRecipe>>();
    private final Map<Integer, Map<UUID, ShapedRecipe>> t = new Int2ObjectOpenHashMap<Map<UUID, ShapedRecipe>>();
    private final Map<Integer, Map<UUID, ShapedRecipe>> i = new Int2ObjectOpenHashMap<Map<UUID, ShapedRecipe>>();
    protected final Map<Integer, Map<UUID, ShapedRecipe>> shapedRecipes = new Int2ObjectOpenHashMap<Map<UUID, ShapedRecipe>>();
    private final Map<Integer, Map<UUID, ShapelessRecipe>> g = new Int2ObjectOpenHashMap<Map<UUID, ShapelessRecipe>>();
    private final Map<Integer, Map<UUID, ShapelessRecipe>> o = new Int2ObjectOpenHashMap<Map<UUID, ShapelessRecipe>>();
    private final Map<Integer, Map<UUID, ShapelessRecipe>> e = new Int2ObjectOpenHashMap<Map<UUID, ShapelessRecipe>>();
    private final Map<Integer, Map<UUID, ShapelessRecipe>> d = new Int2ObjectOpenHashMap<Map<UUID, ShapelessRecipe>>();
    protected final Map<Integer, Map<UUID, ShapelessRecipe>> shapelessRecipes = new Int2ObjectOpenHashMap<Map<UUID, ShapelessRecipe>>();
    private final Map<Integer, ContainerRecipe> n = new Int2ObjectOpenHashMap<ContainerRecipe>();
    public final Map<Integer, ContainerRecipe> containerRecipes = new Int2ObjectOpenHashMap<ContainerRecipe>();
    public final Map<UUID, MultiRecipe> multiRecipes = new HashMap<UUID, MultiRecipe>();
    private final Map<Integer, FurnaceRecipe> w = new Int2ObjectOpenHashMap<FurnaceRecipe>();
    private final Map<Integer, FurnaceRecipe> s = new Int2ObjectOpenHashMap<FurnaceRecipe>();
    public final Map<Integer, FurnaceRecipe> furnaceRecipes = new Int2ObjectOpenHashMap<FurnaceRecipe>();
    private final Map<Integer, BrewingRecipe> m = new Int2ObjectOpenHashMap<BrewingRecipe>();
    public final Map<Integer, BrewingRecipe> brewingRecipes = new Int2ObjectOpenHashMap<BrewingRecipe>();
    public final Map<Integer, CampfireRecipe> campfireRecipes = new Int2ObjectOpenHashMap<CampfireRecipe>();
    private static int l;
    static int u;
    static final int v = 527;
    static final int a = 419;
    static final int p = 407;
    static final int c = 407;
    static final int f = 407;
    public static final Comparator<Item> recipeComparator;

    public CraftingManager() {
        this.a();
    }

    /*
     * WARNING - void declaration
     */
    private void a() {
        Item item;
        Item item2;
        ArrayList<Item> arrayList22;
        Object object;
        Object object222;
        Object object3;
        Object object4;
        Object object5;
        MainLogger.getLogger().debug("Loading recipes...");
        Config config = new Config(2).loadFromStream(Server.class.getClassLoader().getResourceAsStream("extras_407.json"));
        Config config2 = new Config(2).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes388.json"));
        List<Map> list = config2.getRootSection().getMapList("recipes");
        List<Map> list2 = new Config(2).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes332.json")).getMapList("recipes");
        List<Map> list3 = new Config(2).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes313.json")).getMapList("recipes");
        ConfigSection configSection = new Config(2).loadFromStream(Server.class.getClassLoader().getResourceAsStream("recipes419.json")).getRootSection();
        for (Map map : (List)configSection.get((Object)"shaped")) {
            if (!"crafting_table".equals(map.get("block"))) continue;
            object5 = (List)map.get("output");
            object4 = (Map)object5.remove(0);
            object3 = ((List)map.get("shape")).toArray(new String[0]);
            object222 = new CharObjectHashMap();
            object = new ArrayList();
            arrayList22 = (Map)map.get("input");
            for (Map.Entry entry : arrayList22.entrySet()) {
                char c2 = ((String)entry.getKey()).charAt(0);
                item2 = Item.fromJson((Map)entry.getValue());
                object222.put(Character.valueOf(c2), item2);
            }
            Iterator iterator3 = object5.iterator();
            while (iterator3.hasNext()) {
                Map map2 = (Map)((Object)iterator3.next());
                object.add(Item.fromJson(map2));
            }
            this.registerRecipe(419, new ShapedRecipe((String)map.get("id"), Utils.toInt(map.get("priority")), Item.fromJson(object4), (String[])object3, (Map<Character, Item>)object222, (List<Item>)object));
            this.registerRecipe(527, new ShapedRecipe((String)map.get("id"), Utils.toInt(map.get("priority")), Item.fromJson(object4), (String[])object3, (Map<Character, Item>)object222, (List<Item>)object));
        }
        for (Map map : (List)configSection.get((Object)"shapeless")) {
            if (!"crafting_table".equals(map.get("block")) || (object5 = (List)map.get("output")).size() > 1) continue;
            object4 = (Map)object5.get(0);
            object3 = Item.fromJson((Map<String, Object>)object4);
            if (((Item)object3).getId() == 401) {
                object222 = ((Item)object3).clone();
                if (object222 instanceof ItemFirework) {
                    object = new ArrayList();
                    boolean bl = false;
                    for (Map map3 : (List)map.get("input")) {
                        Item item3 = Item.fromJson(map3);
                        object.add(item3);
                        if (item3.getId() != 289) continue;
                        object.add(item3.clone());
                        bl = true;
                    }
                    if (!bl) {
                        throw new RuntimeException("Missing result item for " + map);
                    }
                } else {
                    throw new RuntimeException("Unexpected result item: " + ((Item)object222).toString());
                }
                object.sort(recipeComparator);
                ((ItemFirework)object222).setFlight(2);
                this.registerRecipe(419, new ShapelessRecipe((String)map.get("id"), Math.max(Utils.toInt(map.get("priority")) - 1, 0), (Item)object222, (Collection<Item>)object));
                this.registerRecipe(527, new ShapelessRecipe((String)map.get("id"), Math.max(Utils.toInt(map.get("priority")) - 1, 0), (Item)object222, (Collection<Item>)object));
                object = ((Item)object3).clone();
                if (object instanceof ItemFirework) {
                    arrayList22 = new ArrayList();
                    boolean bl = false;
                    for (Map map4 : (List)map.get("input")) {
                        item2 = Item.fromJson(map4);
                        arrayList22.add(item2);
                        if (item2.getId() != 289) continue;
                        arrayList22.add(item2.clone());
                        arrayList22.add(item2.clone());
                        bl = true;
                    }
                    if (!bl) {
                        throw new RuntimeException("Missing result item for " + map);
                    }
                    arrayList22.sort(recipeComparator);
                    ((ItemFirework)object).setFlight(3);
                    this.registerRecipe(419, new ShapelessRecipe((String)map.get("id"), Math.max(Utils.toInt(map.get("priority")) - 2, 0), (Item)object, arrayList22));
                    this.registerRecipe(527, new ShapelessRecipe((String)map.get("id"), Math.max(Utils.toInt(map.get("priority")) - 2, 0), (Item)object, arrayList22));
                } else {
                    throw new RuntimeException("Unexpected result item: " + ((Item)object).toString());
                }
            }
            object222 = new ArrayList();
            for (ArrayList<Item> arrayList22 : (List)map.get("input")) {
                object222.add(Item.fromJson((Map<String, Object>)((Object)arrayList22)));
            }
            object222.sort(recipeComparator);
            this.registerRecipe(419, new ShapelessRecipe((String)map.get("id"), Utils.toInt(map.get("priority")), (Item)object3, (Collection<Item>)object222));
            this.registerRecipe(527, new ShapelessRecipe((String)map.get("id"), Utils.toInt(map.get("priority")), (Item)object3, (Collection<Item>)object222));
        }
        for (Map map : (List)configSection.get((Object)"smelting")) {
            object5 = (String)map.get("block");
            if (!"furnace".equals(object5) && !"campfire".equals(object5)) continue;
            object4 = (Map)map.get("output");
            try {
                object3 = Item.fromJson((Map)map.get("input"));
            }
            catch (Exception exception) {
                object3 = Item.get(Utils.toInt(map.get("inputId")), map.containsKey("inputDamage") ? Utils.toInt(map.get("inputDamage")) : -1, 1);
            }
            switch (object5) {
                case "furnace": {
                    this.registerRecipe(419, new FurnaceRecipe(Item.fromJson(object4), (Item)object3));
                    break;
                }
                case "campfire": {
                    this.registerRecipe(419, new CampfireRecipe(Item.fromJson(object4), (Item)object3));
                }
            }
        }
        for (Map map : list) {
            try {
                switch (Utils.toInt(map.get("type"))) {
                    case 0: {
                        if (!"crafting_table".equals(map.get("block")) || (object5 = (List)map.get("output")).size() > 1) break;
                        object4 = (Map)object5.get(0);
                        object3 = new ArrayList();
                        for (Map map3 : (List)map.get("input")) {
                            object3.add(Item.fromJson(map3));
                        }
                        object3.sort(recipeComparator);
                        this.registerRecipe(388, new ShapelessRecipe((String)map.get("id"), Utils.toInt(map.get("priority")), Item.fromJson(object4), (Collection<Item>)object3));
                        break;
                    }
                    case 1: {
                        if (!"crafting_table".equals(map.get("block"))) break;
                        object5 = (List)map.get("output");
                        object4 = (Map)object5.remove(0);
                        object222 = ((List)map.get("shape")).toArray(new String[0]);
                        CharObjectHashMap<Item> charObjectHashMap = new CharObjectHashMap<Item>();
                        arrayList22 = new ArrayList();
                        Map map4 = (Map)map.get("input");
                        for (Map.Entry entry : map4.entrySet()) {
                            char c3 = ((String)entry.getKey()).charAt(0);
                            item = Item.fromJson((Map)entry.getValue());
                            charObjectHashMap.put(Character.valueOf(c3), item);
                        }
                        Iterator iterator = object5.iterator();
                        while (iterator.hasNext()) {
                            Map map5 = (Map)iterator.next();
                            arrayList22.add(Item.fromJson(map5));
                        }
                        this.registerRecipe(388, new ShapedRecipe((String)map.get("id"), Utils.toInt(map.get("priority")), Item.fromJson(object4), (String[])object222, charObjectHashMap, arrayList22));
                        break;
                    }
                    case 2: 
                    case 3: {
                        void var17_72;
                        if (!"furnace".equals(map.get("block"))) break;
                        Map map6 = (Map)map.get("output");
                        try {
                            Item item4 = Item.fromJson((Map)map.get("input"));
                        }
                        catch (Exception exception) {
                            Item item5 = Item.get(Utils.toInt(map.get("inputId")), map.containsKey("inputDamage") ? Utils.toInt(map.get("inputDamage")) : -1, 1);
                        }
                        this.registerRecipe(388, new FurnaceRecipe(Item.fromJson(map6), (Item)var17_72));
                    }
                }
            }
            catch (Exception exception) {
                MainLogger.getLogger().error("Exception while registering a protocol 388 recipe", exception);
            }
        }
        for (Map map : list2) {
            try {
                switch (Utils.toInt(map.get("type"))) {
                    case 0: {
                        object5 = (Map)((List)map.get("output")).get(0);
                        object4 = new ArrayList();
                        for (Object object222 : (List)map.get("input")) {
                            object4.add(Item.fromJsonOld((Map<String, Object>)object222));
                        }
                        object4.sort(recipeComparator);
                        this.registerRecipe(332, new ShapelessRecipe(Item.fromJsonOld((Map<String, Object>)object5), (Collection<Item>)object4));
                        break;
                    }
                    case 1: {
                        object3 = (List)map.get("output");
                        object5 = (Map)object3.remove(0);
                        object222 = ((List)map.get("shape")).toArray(new String[0]);
                        CharObjectHashMap<Item> charObjectHashMap = new CharObjectHashMap<Item>();
                        arrayList22 = new ArrayList<Item>();
                        Map map5 = (Map)map.get("input");
                        for (Map.Entry entry : map5.entrySet()) {
                            char c4 = ((String)entry.getKey()).charAt(0);
                            item = Item.fromJsonOld((Map)entry.getValue());
                            charObjectHashMap.put(Character.valueOf(c4), item);
                        }
                        Iterator iterator = object3.iterator();
                        while (iterator.hasNext()) {
                            Map map7 = (Map)iterator.next();
                            arrayList22.add(Item.fromJsonOld(map7));
                        }
                        this.registerRecipe(332, new ShapedRecipe(Item.fromJsonOld((Map<String, Object>)object5), (String[])object222, charObjectHashMap, arrayList22));
                    }
                }
            }
            catch (Exception exception) {
                MainLogger.getLogger().error("Exception while registering a protocol 332 recipe", exception);
            }
        }
        for (Map map : list3) {
            try {
                switch (Utils.toInt(map.get("type"))) {
                    case 0: {
                        object5 = (Map)((List)map.get("output")).get(0);
                        object4 = new ArrayList<Item>();
                        for (Object object222 : (List)map.get("input")) {
                            object4.add((Item)Item.fromJsonOld((Map<String, Object>)object222));
                        }
                        object4.sort(recipeComparator);
                        this.registerRecipe(313, new ShapelessRecipe(Item.fromJsonOld((Map<String, Object>)object5), (Collection<Item>)object4));
                        break;
                    }
                    case 1: {
                        object3 = (List)map.get("output");
                        object5 = (Map)object3.remove(0);
                        object222 = ((List)map.get("shape")).toArray(new String[0]);
                        CharObjectHashMap<Item> charObjectHashMap = new CharObjectHashMap<Item>();
                        arrayList22 = new ArrayList();
                        Map map6 = (Map)map.get("input");
                        for (Map.Entry entry : map6.entrySet()) {
                            char c5 = ((String)entry.getKey()).charAt(0);
                            item = Item.fromJsonOld((Map)entry.getValue());
                            charObjectHashMap.put(Character.valueOf(c5), item);
                        }
                        Iterator iterator = object3.iterator();
                        while (iterator.hasNext()) {
                            Map map8 = (Map)iterator.next();
                            arrayList22.add(Item.fromJsonOld(map8));
                        }
                        this.registerRecipe(313, new ShapedRecipe(Item.fromJsonOld((Map<String, Object>)object5), (String[])object222, charObjectHashMap, arrayList22));
                        break;
                    }
                    case 2: 
                    case 3: {
                        Item item6;
                        Map map9 = (Map)map.get("output");
                        Item item7 = Item.fromJsonOld(map9);
                        try {
                            item6 = Item.fromJsonOld((Map)map.get("input"));
                        }
                        catch (Exception exception) {
                            item6 = Item.get(Utils.toInt(map.get("inputId")), map.containsKey("inputDamage") ? Utils.toInt(map.get("inputDamage")) : -1, 1);
                        }
                        this.registerRecipe(313, new FurnaceRecipe(item7, item6));
                    }
                }
            }
            catch (Exception exception) {
                MainLogger.getLogger().error("Exception while registering a protocol 313 recipe", exception);
            }
        }
        for (Map map : config2.getMapList("potionMixes")) {
            int n = ((Number)map.get("fromPotionId")).intValue();
            int n2 = ((Number)map.get("ingredient")).intValue();
            int n3 = ((Number)map.get("toPotionId")).intValue();
            this.registerBrewingRecipe(388, new BrewingRecipe(Item.get(373, n), Item.get(n2), Item.get(373, n3)));
        }
        for (Map map : config2.getMapList("containerMixes")) {
            int n = ((Number)map.get("fromItemId")).intValue();
            int n4 = ((Number)map.get("ingredient")).intValue();
            int n5 = ((Number)map.get("toItemId")).intValue();
            this.registerContainerRecipe(388, new ContainerRecipe(Item.get(n), Item.get(n4), Item.get(n5)));
        }
        for (Map map : config.getMapList("potionMixes")) {
            int n = ((Number)map.get("inputId")).intValue();
            int n6 = ((Number)map.get("inputMeta")).intValue();
            int n7 = ((Number)map.get("reagentId")).intValue();
            int n8 = ((Number)map.get("reagentMeta")).intValue();
            int n9 = ((Number)map.get("outputId")).intValue();
            int n10 = ((Number)map.get("outputMeta")).intValue();
            this.registerBrewingRecipe(407, new BrewingRecipe(Item.get(n, n6), Item.get(n7, n8), Item.get(n9, n10)));
        }
        for (Map map : config.getMapList("containerMixes")) {
            int n = ((Number)map.get("inputId")).intValue();
            int n11 = ((Number)map.get("reagentId")).intValue();
            int n12 = ((Number)map.get("outputId")).intValue();
            this.registerContainerRecipe(407, new ContainerRecipe(Item.get(n), Item.get(n11), Item.get(n12)));
        }
        this.c(527);
    }

    private void c(int n) {
        List<Item> list = Arrays.asList(Item.get(54, 0, 1), Item.get(333, 0, 1));
        list.sort(recipeComparator);
        this.registerRecipe(n, new ShapelessRecipe("craft_oak_chest_boat", 0, Item.get(638, 0, 1), list));
        List<Item> list2 = Arrays.asList(Item.get(54, 0, 1), Item.get(333, 2, 1));
        list2.sort(recipeComparator);
        this.registerRecipe(n, new ShapelessRecipe("craft_birch_chest_boat", 0, Item.get(639, 0, 1), list2));
        List<Item> list3 = Arrays.asList(Item.get(54, 0, 1), Item.get(333, 3, 1));
        list3.sort(recipeComparator);
        this.registerRecipe(n, new ShapelessRecipe("craft_jungle_chest_boat", 0, Item.get(640, 0, 1), list3));
        List<Item> list4 = Arrays.asList(Item.get(54, 0, 1), Item.get(333, 1, 1));
        list4.sort(recipeComparator);
        this.registerRecipe(n, new ShapelessRecipe("craft_spruce_chest_boat", 0, Item.get(641, 0, 1), list4));
        List<Item> list5 = Arrays.asList(Item.get(54, 0, 1), Item.get(333, 4, 1));
        list5.sort(recipeComparator);
        this.registerRecipe(n, new ShapelessRecipe("craft_acacia_chest_boat", 0, Item.get(642, 0, 1), list5));
        List<Item> list6 = Arrays.asList(Item.get(54, 0, 1), Item.get(333, 5, 1));
        list6.sort(recipeComparator);
        this.registerRecipe(n, new ShapelessRecipe("craft_dark_oak_chest_boat", 0, Item.get(643, 0, 1), list6));
        CharObjectHashMap<Item> charObjectHashMap = new CharObjectHashMap<Item>();
        charObjectHashMap.put(Character.valueOf('A'), Item.get(637, 0, 1));
        this.registerRecipe(n, new ShapedRecipe("craft_record_5", 0, Item.get(636), new String[]{"AAA", "AAA", "AAA"}, charObjectHashMap, new ArrayList<Item>()));
    }

    public void rebuildPacket() {
        packet560 = this.b(560);
        packet554 = this.b(554);
        packet544 = this.b(544);
        packet527 = this.b(527);
        packet503 = this.b(503);
        packet486 = this.b(486);
        packet471 = this.b(471);
        packet465 = this.b(465);
        packet448 = this.b(448);
        packet440 = this.b(440);
        packet431 = this.b(431);
        packet419 = this.b(419);
        packet407 = this.b(407).compress(9);
        packet388 = this.b(388).compress(9);
        packet361 = this.b(361).compress(9);
        packet354 = this.b(354).compress(9);
        packet340 = this.b(340).compress(9);
        packet313 = this.b(313).compress(9);
    }

    private CraftingDataPacket b(int n) {
        CraftingDataPacket craftingDataPacket = new CraftingDataPacket();
        craftingDataPacket.protocol = n;
        for (Recipe recipe : this.getRecipes(n)) {
            if (recipe instanceof ShapedRecipe) {
                craftingDataPacket.addShapedRecipe((ShapedRecipe)recipe);
                continue;
            }
            if (!(recipe instanceof ShapelessRecipe)) continue;
            craftingDataPacket.addShapelessRecipe((ShapelessRecipe)recipe);
        }
        for (FurnaceRecipe furnaceRecipe : this.getFurnaceRecipes(n).values()) {
            craftingDataPacket.addFurnaceRecipe(furnaceRecipe);
        }
        if (n >= 388) {
            for (BrewingRecipe brewingRecipe : this.getBrewingRecipes(n).values()) {
                craftingDataPacket.addBrewingRecipe(brewingRecipe);
            }
            for (ContainerRecipe containerRecipe : this.getContainerRecipes(n).values()) {
                craftingDataPacket.addContainerRecipe(containerRecipe);
            }
            if (n >= 407) {
                for (MultiRecipe multiRecipe : this.getMultiRecipes(n).values()) {
                    craftingDataPacket.addMultiRecipe(multiRecipe);
                }
            }
        }
        craftingDataPacket.tryEncode();
        return craftingDataPacket;
    }

    public Collection<Recipe> getRecipes() {
        Server.mvw("CraftingManager#getRecipes()");
        return this.getRecipes(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public Collection<Recipe> getRecipes(int n) {
        if (n >= 524) {
            return this.recipes;
        }
        if (n >= 419) {
            return this.h;
        }
        if (n >= 354) {
            return this.q;
        }
        if (n >= 340) {
            return this.r;
        }
        return this.b;
    }

    private Collection<Recipe> a(int n) {
        if (n == 527) {
            return this.recipes;
        }
        if (n == 419) {
            return this.h;
        }
        if (n == 388) {
            return this.q;
        }
        if (n == 332) {
            return this.r;
        }
        if (n == 313) {
            return this.b;
        }
        throw new IllegalArgumentException("Invalid protocol: " + n + " Supported: 419, 388, 332, 313");
    }

    public Map<Integer, Map<UUID, ShapedRecipe>> getShapedRecipes(int n) {
        if (n >= 524) {
            return this.shapedRecipes;
        }
        if (n >= 419) {
            return this.i;
        }
        if (n >= 354) {
            return this.t;
        }
        if (n >= 340) {
            return this.j;
        }
        return this.k;
    }

    public Map<Integer, Map<UUID, ShapelessRecipe>> getShapelessRecipes(int n) {
        if (n >= 524) {
            return this.shapelessRecipes;
        }
        if (n >= 419) {
            return this.d;
        }
        if (n >= 354) {
            return this.e;
        }
        if (n >= 340) {
            return this.o;
        }
        return this.g;
    }

    public Map<Integer, FurnaceRecipe> getFurnaceRecipes() {
        Server.mvw("CraftingManager#getFurnaceRecipes()");
        return this.getFurnaceRecipes(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public Map<Integer, FurnaceRecipe> getFurnaceRecipes(int n) {
        if (n >= 419) {
            return this.furnaceRecipes;
        }
        if (n >= 340) {
            return this.s;
        }
        return this.w;
    }

    public Map<Integer, ContainerRecipe> getContainerRecipes(int n) {
        if (n >= 407) {
            return this.containerRecipes;
        }
        return this.n;
    }

    public Map<Integer, BrewingRecipe> getBrewingRecipes(int n) {
        if (n >= 407) {
            return this.brewingRecipes;
        }
        return this.m;
    }

    public Map<UUID, MultiRecipe> getMultiRecipes(int n) {
        if (n >= 407) {
            return this.multiRecipes;
        }
        throw new IllegalArgumentException("Multi recipes are not supported for protocol " + n + " (< 407)");
    }

    public void registerRecipe(Recipe recipe) {
        Server.mvw("CraftingManager#registerRecipe(Recipe)");
        this.registerRecipe(527, recipe);
    }

    public void registerRecipe(int n, Recipe recipe) {
        if (recipe instanceof CraftingRecipe) {
            UUID uUID = Utils.dataToUUID(String.valueOf(++l), String.valueOf(recipe.getResult().getId()), String.valueOf(recipe.getResult().getDamage()), String.valueOf(recipe.getResult().getCount()), Arrays.toString(recipe.getResult().getCompoundTag()));
            ((CraftingRecipe)recipe).setId(uUID);
            this.a(n).add(recipe);
        }
        recipe.registerToCraftingManager(n, this);
    }

    public void registerShapedRecipe(ShapedRecipe shapedRecipe) {
        Server.mvw("CraftingManager#registerShapedRecipe(ShapedRecipe)");
        this.registerShapedRecipe(313, shapedRecipe);
        this.registerShapedRecipe(332, shapedRecipe);
        this.registerShapedRecipe(388, shapedRecipe);
        this.registerShapedRecipe(419, shapedRecipe);
        this.registerShapedRecipe(527, shapedRecipe);
    }

    public void registerShapedRecipe(int n2, ShapedRecipe shapedRecipe) {
        Map map;
        int n3 = CraftingManager.b(shapedRecipe.getResult());
        switch (n2) {
            case 313: {
                map = this.k.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            case 332: {
                map = this.j.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            case 388: {
                map = this.t.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            case 419: {
                map = this.i.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            case 527: {
                map = this.shapedRecipes.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid protocol: " + n2 + " Supported: 527, 419, 388, 332, 313");
            }
        }
        map.put(CraftingManager.a(new LinkedList<Item>(shapedRecipe.getIngredientsAggregate())), shapedRecipe);
    }

    public void registerShapelessRecipe(ShapelessRecipe shapelessRecipe) {
        Server.mvw("CraftingManager#registerShapelessRecipe(ShapelessRecipe)");
        this.registerShapelessRecipe(313, shapelessRecipe);
        this.registerShapelessRecipe(332, shapelessRecipe);
        this.registerShapelessRecipe(388, shapelessRecipe);
        this.registerShapelessRecipe(419, shapelessRecipe);
        this.registerShapelessRecipe(527, shapelessRecipe);
    }

    public void registerShapelessRecipe(int n2, ShapelessRecipe shapelessRecipe) {
        Map map;
        int n3 = CraftingManager.b(shapelessRecipe.getResult());
        switch (n2) {
            case 313: {
                map = this.g.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            case 332: {
                map = this.o.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            case 388: {
                map = this.e.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            case 419: {
                map = this.d.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            case 527: {
                map = this.shapelessRecipes.computeIfAbsent(n3, n -> new HashMap());
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid protocol: " + n2 + " Supported: 527, 419, 388, 332, 313");
            }
        }
        map.put(CraftingManager.a(shapelessRecipe.getIngredientsAggregate()), shapelessRecipe);
    }

    public void registerFurnaceRecipe(FurnaceRecipe furnaceRecipe) {
        Server.mvw("CraftingManager#registerFurnaceRecipe(FurnaceRecipe)");
        this.registerFurnaceRecipe(313, furnaceRecipe);
        this.registerFurnaceRecipe(388, furnaceRecipe);
        this.registerFurnaceRecipe(419, furnaceRecipe);
    }

    public void registerFurnaceRecipe(int n, FurnaceRecipe furnaceRecipe) {
        if (n == 419) {
            this.furnaceRecipes.put(CraftingManager.b(furnaceRecipe.getInput()), furnaceRecipe);
        } else if (n == 388) {
            this.s.put(CraftingManager.b(furnaceRecipe.getInput()), furnaceRecipe);
        } else if (n == 313) {
            this.w.put(CraftingManager.b(furnaceRecipe.getInput()), furnaceRecipe);
        } else {
            throw new IllegalArgumentException("Invalid protocol: " + n + " Supported: 419, 388, 313");
        }
    }

    public void registerContainerRecipe(ContainerRecipe containerRecipe) {
        Server.mvw("CraftingManager#registerContainerRecipe(ContainerRecipe)");
        this.registerContainerRecipe(388, containerRecipe);
        this.registerContainerRecipe(407, containerRecipe);
    }

    public void registerContainerRecipe(int n, ContainerRecipe containerRecipe) {
        Item item = containerRecipe.getIngredient();
        Item item2 = containerRecipe.getInput();
        if (n == 407) {
            this.containerRecipes.put(CraftingManager.a(item.getId(), item2.getId()), containerRecipe);
        } else if (n == 388) {
            this.n.put(CraftingManager.a(item.getId(), item2.getId()), containerRecipe);
        } else {
            throw new IllegalArgumentException("Invalid protocol: " + n + " Supported: 407, 388");
        }
    }

    public void registerBrewingRecipe(BrewingRecipe brewingRecipe) {
        Server.mvw("CraftingManager#registerBrewingRecipe(BrewingRecipe)");
        this.registerBrewingRecipe(388, brewingRecipe);
        this.registerBrewingRecipe(407, brewingRecipe);
    }

    public void registerBrewingRecipe(int n, BrewingRecipe brewingRecipe) {
        Item item = brewingRecipe.getIngredient();
        Item item2 = brewingRecipe.getInput();
        if (n == 407) {
            this.brewingRecipes.put(CraftingManager.a(item, item2), brewingRecipe);
        } else if (n == 388) {
            this.m.put(CraftingManager.a(item, item2), brewingRecipe);
        } else {
            throw new IllegalArgumentException("Invalid protocol: " + n + " Supported: 407, 388");
        }
    }

    public void registerMultiRecipe(MultiRecipe multiRecipe) {
        this.registerMultiRecipe(407, multiRecipe);
    }

    public void registerMultiRecipe(int n, MultiRecipe multiRecipe) {
        if (n != 407) {
            throw new IllegalArgumentException("Invalid protocol: " + n + " Supported: 407");
        }
        this.multiRecipes.put(multiRecipe.getId(), multiRecipe);
    }

    public void registerCampfireRecipe(int n, CampfireRecipe campfireRecipe) {
        this.campfireRecipes.put(CraftingManager.b(campfireRecipe.getInput()), campfireRecipe);
    }

    public CraftingRecipe matchRecipe(List<Item> list, Item item, List<Item> list2) {
        Server.mvw("CraftingManager#matchRecipe(List<Item>, Item , List<Item>)");
        return this.matchRecipe(ProtocolInfo.CURRENT_PROTOCOL, list, item, list2);
    }

    public CraftingRecipe matchRecipe(int n, List<Item> list, Item item, List<Item> list2) {
        Object object;
        Object object2;
        Map<Comparable<Integer>, Object> map;
        int n2 = CraftingManager.b(item);
        Map<Integer, Map<UUID, ShapedRecipe>> map2 = this.getShapedRecipes(n);
        if (map2.containsKey(n2)) {
            list.sort(recipeComparator);
            map = map2.get(n2);
            if (map != null) {
                object2 = CraftingManager.a(list);
                object = (ShapedRecipe)map.get(object2);
                if (object != null && (((ShapedRecipe)object).matchItems(list, list2) || CraftingManager.a((CraftingRecipe)object, list, item, list2))) {
                    return object;
                }
                for (Object object3 : map.values()) {
                    if (!((ShapedRecipe)object3).matchItems(list, list2) && !CraftingManager.a((CraftingRecipe)object3, list, item, list2)) continue;
                    return object3;
                }
            }
        }
        if ((map = this.getShapelessRecipes(n)).containsKey(n2)) {
            list.sort(recipeComparator);
            object2 = (Map)map.get(n2);
            if (object2 == null) {
                return null;
            }
            object = CraftingManager.a(list);
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe)object2.get(object);
            if (shapelessRecipe != null && (shapelessRecipe.matchItems(list, list2) || CraftingManager.a(shapelessRecipe, list, item, list2))) {
                return shapelessRecipe;
            }
            for (ShapelessRecipe shapelessRecipe2 : object2.values()) {
                if (!shapelessRecipe2.matchItems(list, list2) && !CraftingManager.a(shapelessRecipe2, list, item, list2)) continue;
                return shapelessRecipe2;
            }
        }
        return null;
    }

    public FurnaceRecipe matchFurnaceRecipe(Item item) {
        Map<Integer, FurnaceRecipe> map = this.getFurnaceRecipes(ProtocolInfo.CURRENT_PROTOCOL);
        FurnaceRecipe furnaceRecipe = map.get(CraftingManager.b(item));
        if (furnaceRecipe == null) {
            furnaceRecipe = map.get(CraftingManager.b(item.getId(), 0));
        }
        return furnaceRecipe;
    }

    public ContainerRecipe matchContainerRecipe(Item item, Item item2) {
        return this.getContainerRecipes(ProtocolInfo.CURRENT_PROTOCOL).get(CraftingManager.a(item.getId(), item2.getId()));
    }

    public BrewingRecipe matchBrewingRecipe(Item item, Item item2) {
        return this.getBrewingRecipes(ProtocolInfo.CURRENT_PROTOCOL).get(CraftingManager.a(item, item2));
    }

    public CampfireRecipe matchCampfireRecipe(Item item) {
        CampfireRecipe campfireRecipe = this.campfireRecipes.get(CraftingManager.b(item));
        if (campfireRecipe == null) {
            campfireRecipe = this.campfireRecipes.get(CraftingManager.b(item.getId(), 0));
        }
        return campfireRecipe;
    }

    private static boolean a(CraftingRecipe craftingRecipe, List<Item> list, Item item, List<Item> list2) {
        Item item2 = craftingRecipe.getResult();
        if (item.equals(item2, item2.hasMeta(), item2.hasCompoundTag()) && item.getCount() % item2.getCount() == 0) {
            int n = item.getCount() / item2.getCount();
            return craftingRecipe.matchItems(list, list2, n);
        }
        return false;
    }

    private static int b(Item item) {
        return CraftingManager.b(item.getId(), item.getDamage());
    }

    private static int b(int n, int n2) {
        return n << 12 | n2 & 0xFFF;
    }

    private static int a(Item item) {
        return CraftingManager.b(item) << 6 | item.getCount() & 0x3F;
    }

    private static UUID a(Collection<Item> collection) {
        BinaryStream binaryStream = new BinaryStream();
        for (Item item : collection) {
            binaryStream.putVarInt(CraftingManager.a(item));
        }
        return UUID.nameUUIDFromBytes(binaryStream.getBuffer());
    }

    private static int a(int n, int n2) {
        return n << 15 | n2;
    }

    private static int a(Item item, Item item2) {
        int n = (item.getId() & 0x3FF) << 6 | item.getDamage() & 0x3F;
        int n2 = (item2.getId() & 0x3FF) << 6 | item2.getDamage() & 0x3F;
        return n << 16 | n2;
    }

    static {
        recipeComparator = (item, item2) -> {
            if (item.getId() > item2.getId()) {
                return 1;
            }
            if (item.getId() < item2.getId()) {
                return -1;
            }
            if (item.getDamage() > item2.getDamage()) {
                return 1;
            }
            if (item.getDamage() < item2.getDamage()) {
                return -1;
            }
            return Integer.compare(item.getCount(), item2.getCount());
        };
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    public static class Entry {
        final int f;
        final int a;
        final int c;
        final int d;
        final String b;
        final int e;

        public Entry(int n, int n2, int n3, int n4, String string, int n5) {
            this.f = n;
            this.a = n2;
            this.c = n3;
            this.d = n4;
            this.b = string;
            this.e = n5;
        }
    }
}

