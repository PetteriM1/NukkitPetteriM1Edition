/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.CraftingRecipe;
import cn.nukkit.inventory.RecipeType;
import cn.nukkit.item.Item;
import io.netty.util.collection.CharObjectHashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

public class ShapedRecipe
implements CraftingRecipe {
    private String a;
    private final Item c;
    private final List<Item> e = new ArrayList<Item>();
    private final List<Item> i;
    private long d;
    private long h;
    private final String[] b;
    private final int g;
    private final CharObjectHashMap<Item> j = new CharObjectHashMap();
    private final int f;

    public ShapedRecipe(Item item, String[] stringArray, Map<Character, Item> map, List<Item> list) {
        this(null, 1, item, stringArray, map, list);
    }

    public ShapedRecipe(String string, int n, Item item, String[] stringArray, Map<Character, Item> map, List<Item> list) {
        this(string, n, item, stringArray, map, list, null);
    }

    public ShapedRecipe(String string, int n, Item item, String[] stringArray, Map<Character, Item> map, List<Item> list, Integer n2) {
        this.a = string;
        this.g = n;
        int n3 = stringArray.length;
        if (n3 > 3 || n3 <= 0) {
            throw new RuntimeException("Shaped recipes may only have 1, 2 or 3 rows, not " + n3);
        }
        int n4 = stringArray[0].length();
        if (n4 > 3 || n4 <= 0) {
            throw new RuntimeException("Shaped recipes may only have 1, 2 or 3 columns, not " + n4);
        }
        String[] stringArray2 = stringArray;
        int n5 = stringArray2.length;
        for (int k = 0; k < n5; ++k) {
            String string2 = stringArray2[k];
            if (string2.length() != n4) {
                throw new RuntimeException("Shaped recipe rows must all have the same length (expected " + n4 + ", got " + string2.length() + ')');
            }
            for (int i2 = 0; i2 < n4; ++i2) {
                char c2 = string2.charAt(i2);
                if (c2 == ' ' || map.containsKey(Character.valueOf(c2))) continue;
                throw new RuntimeException("No item specified for symbol '" + c2 + '\'');
            }
        }
        this.c = item.clone();
        this.e.addAll(list);
        this.b = stringArray;
        for (Map.Entry<Character, Item> entry : map.entrySet()) {
            this.setIngredient(entry.getKey().charValue(), entry.getValue());
        }
        this.i = new ArrayList<Item>();
        for (char c3 : String.join((CharSequence)"", this.b).toCharArray()) {
            if (c3 == ' ') continue;
            Item item2 = this.j.get(c3).clone();
            for (Item item3 : this.i) {
                if (!item3.equals(item2, item2.hasMeta(), item2.hasCompoundTag())) continue;
                item3.setCount(item3.getCount() + item2.getCount());
                item2 = null;
                break;
            }
            if (item2 == null) continue;
            this.i.add(item2);
        }
        this.i.sort(CraftingManager.recipeComparator);
        this.f = n2 != null ? n2 : (CraftingManager.u = CraftingManager.u + 1);
    }

    public int getWidth() {
        return this.b[0].length();
    }

    public int getHeight() {
        return this.b.length;
    }

    @Override
    public Item getResult() {
        return this.c;
    }

    @Override
    public String getRecipeId() {
        return this.a;
    }

    @Override
    public UUID getId() {
        return new UUID(this.d, this.h);
    }

    @Override
    public void setId(UUID uUID) {
        block0: {
            this.d = uUID.getLeastSignificantBits();
            this.h = uUID.getMostSignificantBits();
            if (this.a != null) break block0;
            this.a = this.getId().toString();
        }
    }

    public ShapedRecipe setIngredient(String string, Item item) {
        return this.setIngredient(string.charAt(0), item);
    }

    public ShapedRecipe setIngredient(char c2, Item item) {
        if (String.join((CharSequence)"", this.b).indexOf(c2) < 0) {
            throw new RuntimeException("Symbol does not appear in the shape: " + c2);
        }
        this.j.put(c2, item);
        return this;
    }

    public List<Item> getIngredientList() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        int n = this.getHeight();
        for (int k = 0; k < n; ++k) {
            int n2 = this.getWidth();
            for (int i2 = 0; i2 < n2; ++i2) {
                arrayList.add(this.getIngredient(i2, k));
            }
        }
        return arrayList;
    }

    public Map<Integer, Map<Integer, Item>> getIngredientMap() {
        LinkedHashMap<Integer, Map<Integer, Item>> linkedHashMap = new LinkedHashMap<Integer, Map<Integer, Item>>();
        int n = this.getHeight();
        for (int k = 0; k < n; ++k) {
            LinkedHashMap<Integer, Item> linkedHashMap2 = new LinkedHashMap<Integer, Item>();
            int n2 = this.getWidth();
            for (int i2 = 0; i2 < n2; ++i2) {
                linkedHashMap2.put(i2, this.getIngredient(i2, k));
            }
            linkedHashMap.put(k, linkedHashMap2);
        }
        return linkedHashMap;
    }

    public Item getIngredient(int n, int n2) {
        Item item = this.j.get(this.b[n2].charAt(n));
        return item != null ? item.clone() : Item.get(0);
    }

    public String[] getShape() {
        return this.b;
    }

    @Override
    public void registerToCraftingManager(CraftingManager craftingManager) {
        this.registerToCraftingManager(527, craftingManager);
    }

    @Override
    public void registerToCraftingManager(int n, CraftingManager craftingManager) {
        craftingManager.registerShapedRecipe(n, this);
    }

    @Override
    public RecipeType getType() {
        return RecipeType.SHAPED;
    }

    @Override
    public List<Item> getExtraResults() {
        return this.e;
    }

    @Override
    public List<Item> getAllResults() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        arrayList.add(this.c);
        arrayList.addAll(this.e);
        return arrayList;
    }

    @Override
    public int getPriority() {
        return this.g;
    }

    @Override
    public boolean matchItems(List<Item> list, List<Item> list2, int n) {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (Item object2 : list) {
            if (object2.isNull()) continue;
            arrayList.add(object2.clone());
        }
        ArrayList arrayList2 = new ArrayList();
        if (n != 1) {
            for (Item item : this.i) {
                if (item.isNull()) continue;
                Item item2 = item.clone();
                item2.setCount(item2.getCount() * n);
                arrayList2.add(item2);
            }
        } else {
            for (Item item : this.i) {
                if (item.isNull()) continue;
                arrayList2.add(item.clone());
            }
        }
        if (!ShapedRecipe.a(arrayList, arrayList2)) {
            return false;
        }
        ArrayList<Item> arrayList3 = new ArrayList<Item>();
        for (Item item : list2) {
            if (item.isNull()) continue;
            arrayList3.add(item.clone());
        }
        arrayList3.sort(CraftingManager.recipeComparator);
        ArrayList<Item> arrayList4 = new ArrayList<Item>();
        if (n != 1) {
            for (Item item : this.getExtraResults()) {
                if (item.isNull()) continue;
                Item item3 = item.clone();
                item3.setCount(item3.getCount() * n);
                arrayList4.add(item3);
            }
        } else {
            for (Item item : this.getExtraResults()) {
                if (item.isNull()) continue;
                arrayList4.add(item.clone());
            }
        }
        arrayList4.sort(CraftingManager.recipeComparator);
        return ShapedRecipe.a(arrayList3, arrayList4);
    }

    @Override
    public boolean matchItems(List<Item> list, List<Item> list2) {
        return this.matchItems(list, list2, 1);
    }

    private static boolean a(List<Item> list, List<Item> list2) {
        block0: for (Item item : new ArrayList<Item>(list2)) {
            for (Item item2 : new ArrayList<Item>(list)) {
                if (!item.equals(item2, item.hasMeta(), item.hasCompoundTag())) continue;
                int n = Math.min(item2.getCount(), item.getCount());
                item.setCount(item.getCount() - n);
                item2.setCount(item2.getCount() - n);
                if (item2.getCount() == 0) {
                    list.remove(item2);
                }
                if (item.getCount() != 0) continue;
                list2.remove(item);
                continue block0;
            }
        }
        return list.isEmpty() && list2.isEmpty();
    }

    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        this.j.forEach((c2, item) -> stringJoiner.add(item.getName() + ':' + item.getDamage()));
        return stringJoiner.toString();
    }

    @Override
    public boolean requiresCraftingTable() {
        return this.getHeight() > 2 || this.getWidth() > 2;
    }

    @Override
    public List<Item> getIngredientsAggregate() {
        return this.i;
    }

    public int getNetworkId() {
        return this.f;
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public static class Entry {
        public final int x;
        public final int y;

        public Entry(int n, int n2) {
            this.x = n;
            this.y = n2;
        }
    }
}

