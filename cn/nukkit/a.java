/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.Achievement;
import java.util.HashMap;

final class a
extends HashMap<String, Achievement> {
    a() {
        this.put("openInventory", new Achievement("Taking Inventory", new String[0]));
        this.put("mineWood", new Achievement("Getting Wood", "openInventory"));
        this.put("buildWorkBench", new Achievement("Benchmarking", "mineWood"));
        this.put("buildPickaxe", new Achievement("Time to Mine!", "buildWorkBench"));
        this.put("buildFurnace", new Achievement("Hot Topic", "buildPickaxe"));
        this.put("acquireIron", new Achievement("Acquire Hardware", "buildFurnace"));
        this.put("buildHoe", new Achievement("Time to Farm!", "buildWorkBench"));
        this.put("makeBread", new Achievement("Bake Bread", "buildHoe"));
        this.put("bakeCake", new Achievement("The Lie", "buildHoe"));
        this.put("buildBetterPickaxe", new Achievement("Getting an Upgrade", "buildPickaxe"));
        this.put("cookFish", new Achievement("Delicious Fish", "buildFurnace"));
        this.put("onARail", new Achievement("On A Rail", "acquireIron"));
        this.put("buildSword", new Achievement("Time to Strike!", "buildWorkBench"));
        this.put("killEnemy", new Achievement("Monster Hunter", "buildSword"));
        this.put("killCow", new Achievement("Cow Tipper", "buildSword"));
        this.put("flyPig", new Achievement("When Pigs Fly", "killCow"));
        this.put("snipeSkeleton", new Achievement("Sniper Duel", "killEnemy"));
        this.put("diamonds", new Achievement("DIAMONDS!", "acquireIron"));
        this.put("portal", new Achievement("We Need to Go Deeper", "diamonds"));
        this.put("ghast", new Achievement("Return to Sender", "portal"));
        this.put("blazeRod", new Achievement("Into Fire", "portal"));
        this.put("potion", new Achievement("Local Brewery", "blazeRod"));
        this.put("theEnd", new Achievement("The End?", "blazeRod"));
        this.put("theEnd2", new Achievement("The End.", "theEnd"));
        this.put("enchantments", new Achievement("Enchanter", "diamonds"));
        this.put("overkill", new Achievement("Overkill", "enchantments"));
        this.put("bookcase", new Achievement("Librarian", "enchantments"));
        this.put("exploreAllBiomes", new Achievement("Adventuring Time", "theEnd"));
        this.put("spawnWither", new Achievement("The Beginning?", "theEnd"));
        this.put("killWither", new Achievement("The Beginning.", "spawnWither"));
        this.put("fullBeacon", new Achievement("Beaconator", "killWither"));
        this.put("breedCow", new Achievement("Repopulation", "killCow"));
        this.put("diamondsToYou", new Achievement("Diamonds to you!", "diamonds"));
        this.put("overpowered", new Achievement("Overpowered", "buildBetterPickaxe"));
    }
}

