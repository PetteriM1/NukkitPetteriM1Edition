/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.PlayerUIInventory;
import cn.nukkit.item.Item;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class PlayerUIComponent
extends BaseInventory {
    public static final int CREATED_ITEM_OUTPUT_UI_SLOT = 50;
    private final PlayerUIInventory d;
    private final int b;
    private final int c;

    PlayerUIComponent(PlayerUIInventory playerUIInventory, int n, int n2) {
        super(playerUIInventory.holder, InventoryType.UI, Collections.emptyMap(), n2);
        this.d = playerUIInventory;
        this.b = n;
        this.c = n2;
    }

    @Override
    public int getSize() {
        return this.c;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setMaxStackSize(int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item getItem(int n) {
        return this.d.getItem(n + this.b);
    }

    @Override
    public Item getItemFast(int n) {
        return this.d.getItemFast(n + this.b);
    }

    @Override
    public boolean setItem(int n, Item item, boolean bl) {
        return this.d.setItem(n + this.b, item, bl);
    }

    @Override
    public Map<Integer, Item> getContents() {
        Map<Integer, Item> map = this.d.getContents();
        map.keySet().removeIf(n -> n < this.b || n > this.b + this.c);
        return map;
    }

    @Override
    public void sendContents(Player ... playerArray) {
        this.d.sendContents(playerArray);
    }

    @Override
    public void sendSlot(int n, Player ... playerArray) {
        this.d.sendSlot(n + this.b, playerArray);
    }

    @Override
    public Set<Player> getViewers() {
        return this.d.viewers;
    }

    @Override
    public InventoryType getType() {
        return this.d.type;
    }

    @Override
    public void onOpen(Player player) {
    }

    @Override
    public boolean open(Player player) {
        return false;
    }

    @Override
    public void close(Player player) {
    }

    @Override
    public void onClose(Player player) {
    }

    @Override
    public void onSlotChange(int n, Item item, boolean bl) {
        this.d.onSlotChange(n + this.b, item, bl);
    }

    private static UnsupportedOperationException a(UnsupportedOperationException unsupportedOperationException) {
        return unsupportedOperationException;
    }
}

