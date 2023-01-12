/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory;

public enum InventoryType {
    CHEST(27, "Chest", 0),
    ENDER_CHEST(27, "Ender Chest", 0),
    DOUBLE_CHEST(54, "Double Chest", 0),
    PLAYER(40, "Player", -1),
    FURNACE(3, "Furnace", 2),
    BLAST_FURNACE(3, "Blast Furnace", 27),
    SMOKER(3, "Smoker", 28),
    CAMPFIRE(4, "Campfire", -1),
    CRAFTING(5, "Crafting", 1),
    WORKBENCH(10, "Crafting", 1),
    BREWING_STAND(5, "Brewing", 4),
    ANVIL(3, "Anvil", 5),
    ENCHANT_TABLE(2, "Enchantment Table", 3),
    DISPENSER(9, "Dispenser", 6),
    DROPPER(9, "Dropper", 7),
    HOPPER(5, "Hopper", 8),
    UI(1, "UI", -1),
    SHULKER_BOX(27, "Shulker Box", 0),
    BEACON(1, "Beacon", 13),
    ENTITY_ARMOR(4, "Entity Armor", -1),
    ENTITY_EQUIPMENT(36, "Entity Equipment", -1),
    MINECART_CHEST(27, "Minecart with Chest", 0),
    MINECART_HOPPER(5, "Minecart with Hopper", 8),
    OFFHAND(1, "Offhand", -1),
    TRADING(3, "Villager Trade", 15),
    BARREL(27, "Barrel", 0),
    LOOM(4, "Loom", 24),
    CHEST_BOAT(27, "Boat with Chest", 0);

    private final int c;
    private final String a;
    private final int d;

    private InventoryType(int n2, String string2, int n3) {
        this.c = n2;
        this.a = string2;
        this.d = n3;
    }

    public int getDefaultSize() {
        return this.c;
    }

    public String getDefaultTitle() {
        return this.a;
    }

    public int getNetworkType() {
        return this.d;
    }
}

