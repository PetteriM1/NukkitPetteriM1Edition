/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.CraftingRecipe;
import cn.nukkit.inventory.RecipeType;
import cn.nukkit.item.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ShapelessRecipe
implements CraftingRecipe {
    private String g;
    private final Item c;
    private long d;
    private long e;
    private final List<Item> f;
    private final List<Item> b;
    private final int a;
    private final int h;

    public ShapelessRecipe(Item item, Collection<Item> collection) {
        this(null, 10, item, collection);
    }

    public ShapelessRecipe(String string, int n, Item item, Collection<Item> collection) {
        this(string, n, item, collection, null);
    }

    public ShapelessRecipe(String string, int n, Item item, Collection<Item> collection, Integer n2) {
        this.g = string;
        this.a = n;
        this.c = item.clone();
        if (collection.size() > 9) {
            throw new IllegalArgumentException("Shapeless recipes cannot have more than 9 ingredients");
        }
        this.f = new ArrayList<Item>();
        this.b = new ArrayList<Item>();
        for (Item item2 : collection) {
            if (item2.getCount() < 1) {
                throw new IllegalArgumentException("Recipe '" + string + "' Ingredient amount was not 1 (value: " + item2.getCount() + ')');
            }
            boolean bl = false;
            for (Item item3 : this.b) {
                if (!item3.equals(item2, item2.hasMeta(), item2.hasCompoundTag())) continue;
                item3.setCount(item3.getCount() + item2.getCount());
                bl = true;
                break;
            }
            if (!bl) {
                this.b.add(item2.clone());
            }
            this.f.add(item2.clone());
        }
        this.b.sort(CraftingManager.recipeComparator);
        this.h = n2 != null ? n2 : (CraftingManager.u = CraftingManager.u + 1);
    }

    @Override
    public Item getResult() {
        return this.c.clone();
    }

    @Override
    public String getRecipeId() {
        return this.g;
    }

    @Override
    public UUID getId() {
        return new UUID(this.d, this.e);
    }

    @Override
    public void setId(UUID uUID) {
        block0: {
            this.d = uUID.getLeastSignificantBits();
            this.e = uUID.getMostSignificantBits();
            if (this.g != null) break block0;
            this.g = this.getId().toString();
        }
    }

    public List<Item> getIngredientList() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        for (Item item : this.f) {
            arrayList.add(item.clone());
        }
        return arrayList;
    }

    public int getIngredientCount() {
        return this.f.size();
    }

    @Override
    public void registerToCraftingManager(CraftingManager craftingManager) {
        this.registerToCraftingManager(527, craftingManager);
    }

    @Override
    public void registerToCraftingManager(int n, CraftingManager craftingManager) {
        craftingManager.registerShapelessRecipe(n, this);
    }

    @Override
    public RecipeType getType() {
        return RecipeType.SHAPELESS;
    }

    @Override
    public boolean requiresCraftingTable() {
        return this.f.size() > 4;
    }

    @Override
    public List<Item> getExtraResults() {
        return new ArrayList<Item>();
    }

    @Override
    public List<Item> getAllResults() {
        return null;
    }

    @Override
    public int getPriority() {
        return this.a;
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
            for (Item item : this.b) {
                if (item.isNull()) continue;
                Item item2 = item.clone();
                item2.setCount(item2.getCount() * n);
                arrayList2.add(item2);
            }
        } else {
            for (Item item : this.b) {
                if (item.isNull()) continue;
                arrayList2.add(item.clone());
            }
        }
        if (!ShapelessRecipe.a(arrayList, arrayList2)) {
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
        return ShapelessRecipe.a(arrayList3, arrayList4);
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

    @Override
    public List<Item> getIngredientsAggregate() {
        return this.b;
    }

    public int getNetworkId() {
        return this.h;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

