/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.inventory.Recipe;
import cn.nukkit.item.Item;

public abstract class MixRecipe
implements Recipe {
    private final Item a;
    private final Item c;
    private final Item b;

    public MixRecipe(Item item, Item item2, Item item3) {
        this.a = item.clone();
        this.c = item2.clone();
        this.b = item3.clone();
    }

    public Item getIngredient() {
        return this.c.clone();
    }

    public Item getInput() {
        return this.a.clone();
    }

    @Override
    public Item getResult() {
        return this.b.clone();
    }

    public String toString() {
        return "MixRecipe(input=" + this.getInput() + ", ingredient=" + this.getIngredient() + ", output=" + this.b + ")";
    }
}

