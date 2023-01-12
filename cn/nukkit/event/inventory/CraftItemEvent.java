/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.inventory;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.inventory.transaction.CraftingTransaction;
import cn.nukkit.item.Item;

public class CraftItemEvent
extends Event
implements Cancellable {
    private static final HandlerList f = new HandlerList();
    private final Item[] c;
    private final Recipe b;
    private final Player d;
    private CraftingTransaction e;

    public static HandlerList getHandlers() {
        return f;
    }

    public CraftItemEvent(CraftingTransaction craftingTransaction) {
        this.e = craftingTransaction;
        this.d = craftingTransaction.getSource();
        this.c = craftingTransaction.getInputList().toArray(new Item[0]);
        this.b = craftingTransaction.getRecipe();
    }

    public CraftItemEvent(Player player, Item[] itemArray, Recipe recipe) {
        this.d = player;
        this.c = itemArray;
        this.b = recipe;
    }

    public CraftingTransaction getTransaction() {
        return this.e;
    }

    public Item[] getInput() {
        return this.c;
    }

    public Recipe getRecipe() {
        return this.b;
    }

    public Player getPlayer() {
        return this.d;
    }
}

