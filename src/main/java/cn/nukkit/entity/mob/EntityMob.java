package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;

import java.util.HashMap;

public interface EntityMob {

    void attackEntity(Entity player);

    int getDamage();

    int getDamage(Integer difficulty);

    int getMinDamage();

    int getMinDamage(Integer difficulty);

    int getMaxDamage();

    int getMaxDamage(Integer difficulty);

    void setDamage(int damage);

    void setDamage(int[] damage);

    void setDamage(int damage, int difficulty);

    void setMinDamage(int damage);

    void setMinDamage(int[] damage);

    void setMinDamage(int damage, int difficulty);

    void setMaxDamage(int damage);

    void setMaxDamage(int[] damage);

    void setMaxDamage(int damage, int difficulty);

    final class ArmorPoints extends HashMap<Integer, Float> {
        {
            put(Item.LEATHER_CAP, 1f);
            put(Item.LEATHER_TUNIC, 3f);
            put(Item.LEATHER_PANTS, 2f);
            put(Item.LEATHER_BOOTS, 1f);
            put(Item.CHAIN_HELMET, 1f);
            put(Item.CHAIN_CHESTPLATE, 5f);
            put(Item.CHAIN_LEGGINGS, 4f);
            put(Item.CHAIN_BOOTS, 1f);
            put(Item.GOLD_HELMET, 1f);
            put(Item.GOLD_CHESTPLATE, 5f);
            put(Item.GOLD_LEGGINGS, 3f);
            put(Item.GOLD_BOOTS, 1f);
            put(Item.IRON_HELMET, 2f);
            put(Item.IRON_CHESTPLATE, 6f);
            put(Item.IRON_LEGGINGS, 5f);
            put(Item.IRON_BOOTS, 2f);
            put(Item.DIAMOND_HELMET, 3f);
            put(Item.DIAMOND_CHESTPLATE, 8f);
            put(Item.DIAMOND_LEGGINGS, 6f);
            put(Item.DIAMOND_BOOTS, 3f);
        }
    }
}
