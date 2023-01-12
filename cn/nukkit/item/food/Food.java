/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.food;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.player.PlayerEatFoodEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.food.FoodChorusFruit;
import cn.nukkit.item.food.FoodEffective;
import cn.nukkit.item.food.FoodEffectiveInBow;
import cn.nukkit.item.food.FoodInBowl;
import cn.nukkit.item.food.FoodMilk;
import cn.nukkit.item.food.FoodNormal;
import cn.nukkit.item.food.a;
import cn.nukkit.item.food.b;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.potion.Effect;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Food {
    private static final Map<b, Food> b = new LinkedHashMap<b, Food>();
    private static final Map<a, Food> a = new LinkedHashMap<a, Food>();
    public static final Food apple = Food.a(new FoodNormal(4, 2.4f).addRelative(260));
    public static final Food apple_golden = Food.a(new FoodEffective(4, 9.6f).addEffect(Effect.getEffect(10).setAmplifier(1).setDuration(100)).addEffect(Effect.getEffect(22).setDuration(2400)).addRelative(322));
    public static final Food apple_golden_enchanted = Food.a(new FoodEffective(4, 9.6f).addEffect(Effect.getEffect(10).setAmplifier(4).setDuration(600)).addEffect(Effect.getEffect(22).setDuration(2400).setAmplifier(3)).addEffect(Effect.getEffect(11).setDuration(6000)).addEffect(Effect.getEffect(12).setDuration(6000)).addRelative(466));
    public static final Food beef_raw = Food.a(new FoodNormal(3, 1.8f).addRelative(363));
    public static final Food beetroot = Food.a(new FoodNormal(1, 1.2f).addRelative(457));
    public static final Food beetroot_soup = Food.a(new FoodInBowl(6, 7.2f).addRelative(459));
    public static final Food bread = Food.a(new FoodNormal(5, 6.0f).addRelative(297));
    public static final Food cake_slice = Food.a(new FoodNormal(2, 0.4f).addRelative(92, 0).addRelative(92, 1).addRelative(92, 2).addRelative(92, 3).addRelative(92, 4).addRelative(92, 5).addRelative(92, 6));
    public static final Food carrot = Food.a(new FoodNormal(3, 4.8f).addRelative(391));
    public static final Food carrot_golden = Food.a(new FoodNormal(6, 14.4f).addRelative(396));
    public static final Food chicken_raw = Food.a(new FoodEffective(2, 1.2f).addChanceEffect(0.3f, Effect.getEffect(17).setDuration(600)).addRelative(365));
    public static final Food chicken_cooked = Food.a(new FoodNormal(6, 7.2f).addRelative(366));
    public static final Food chorus_fruit = Food.a(new FoodChorusFruit());
    public static final Food cookie = Food.a(new FoodNormal(2, 0.4f).addRelative(357));
    public static final Food melon_slice = Food.a(new FoodNormal(2, 1.2f).addRelative(360));
    public static final Food milk = Food.a(new FoodMilk().addRelative(325, 1));
    public static final Food mushroom_stew = Food.a(new FoodInBowl(6, 7.2f).addRelative(282));
    public static final Food mutton_cooked = Food.a(new FoodNormal(6, 9.6f).addRelative(424));
    public static final Food mutton_raw = Food.a(new FoodNormal(2, 1.2f).addRelative(423));
    public static final Food porkchop_cooked = Food.a(new FoodNormal(8, 12.8f).addRelative(320));
    public static final Food porkchop_raw = Food.a(new FoodNormal(3, 1.8f).addRelative(319));
    public static final Food potato_raw = Food.a(new FoodNormal(1, 0.6f).addRelative(392));
    public static final Food potato_baked = Food.a(new FoodNormal(5, 7.2f).addRelative(393));
    public static final Food potato_poisonous = Food.a(new FoodEffective(2, 1.2f).addChanceEffect(0.6f, Effect.getEffect(19).setDuration(80)).addRelative(394));
    public static final Food pumpkin_pie = Food.a(new FoodNormal(8, 4.8f).addRelative(400));
    public static final Food rabbit_cooked = Food.a(new FoodNormal(5, 6.0f).addRelative(412));
    public static final Food rabbit_raw = Food.a(new FoodNormal(3, 1.8f).addRelative(411));
    public static final Food rabbit_stew = Food.a(new FoodInBowl(10, 12.0f).addRelative(413));
    public static final Food rotten_flesh = Food.a(new FoodEffective(4, 0.8f).addChanceEffect(0.8f, Effect.getEffect(17).setDuration(600)).addRelative(367));
    public static final Food spider_eye = Food.a(new FoodEffective(2, 3.2f).addEffect(Effect.getEffect(19).setDuration(80)).addRelative(375));
    public static final Food steak = Food.a(new FoodNormal(8, 12.8f).addRelative(364));
    public static final Food clownfish = Food.a(new FoodNormal(1, 0.2f).addRelative(461));
    public static final Food fish_cooked = Food.a(new FoodNormal(5, 6.0f).addRelative(350));
    public static final Food fish_raw = Food.a(new FoodNormal(2, 0.4f).addRelative(349));
    public static final Food salmon_cooked = Food.a(new FoodNormal(6, 9.6f).addRelative(463));
    public static final Food salmon_raw = Food.a(new FoodNormal(2, 0.4f).addRelative(460));
    public static final Food pufferfish = Food.a(new FoodEffective(1, 0.2f).addEffect(Effect.getEffect(17).setAmplifier(2).setDuration(300)).addEffect(Effect.getEffect(9).setAmplifier(1).setDuration(300)).addEffect(Effect.getEffect(19).setAmplifier(3).setDuration(1200)).addRelative(462));
    public static final Food dried_kelp = Food.a(new FoodNormal(1, 0.6f).addRelative(464));
    public static final Food sweet_berries = Food.a(new FoodNormal(2, 0.4f).addRelative(477));
    public static final Food suspicious_stew_night_vision = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(16).setAmplifier(1).setDuration(80)).addRelative(734, 0));
    public static final Food suspicious_stew_jump = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(8).setAmplifier(1).setDuration(80)).addRelative(734, 1));
    public static final Food suspicious_stew_weakness = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(18).setAmplifier(1).setDuration(140)).addRelative(734, 2));
    public static final Food suspicious_stew_blindness = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(15).setAmplifier(1).setDuration(120)).addRelative(734, 3));
    public static final Food suspicious_stew_poison = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(19).setAmplifier(1).setDuration(220)).addRelative(734, 4));
    public static final Food suspicious_stew_saturation = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(23).setAmplifier(1).setDuration(7)).addRelative(734, 6));
    public static final Food suspicious_stew_fire_resistance = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(12).setAmplifier(1).setDuration(40)).addRelative(734, 7));
    public static final Food suspicious_stew_regeneration = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(10).setAmplifier(1).setDuration(120)).addRelative(734, 8));
    public static final Food suspicious_stew_wither = Food.a(new FoodEffectiveInBow(6, 7.2f).addEffect(Effect.getEffect(20).setAmplifier(1).setDuration(120)).addRelative(734, 9));
    public static final Food honey_bottle = Food.a(new FoodNormal(6, 1.2f).addRelative(737));
    protected int restoreFood = 0;
    protected float restoreSaturation = 0.0f;
    protected final List<a> relativeIDs = new ArrayList<a>();

    public static Food registerFood(Food food, Plugin plugin) {
        Objects.requireNonNull(food);
        Objects.requireNonNull(plugin);
        food.relativeIDs.forEach(a2 -> b.put(new b(a2.a, a2.b, plugin), food));
        return food;
    }

    private static Food a(Food food) {
        food.relativeIDs.forEach(a2 -> a.put((a)a2, food));
        return food;
    }

    public static Food getByRelative(Item item) {
        Objects.requireNonNull(item);
        return Food.getByRelative(item.getId(), item.getDamage());
    }

    public static Food getByRelative(Block block) {
        Objects.requireNonNull(block);
        return Food.getByRelative(block.getId(), block.getDamage());
    }

    public static Food getByRelative(int n, int n2) {
        Food[] foodArray = new Food[]{null};
        b.forEach((b2, food) -> {
            if (b2.a == n && b2.b == n2 && b2.c.isEnabled()) {
                foodArray[0] = food;
            }
        });
        if (foodArray[0] == null) {
            a.forEach((a2, food) -> {
                if (a2.a == n && a2.b == n2) {
                    foodArray[0] = food;
                }
            });
        }
        return foodArray[0];
    }

    public final boolean eatenBy(Player player) {
        PlayerEatFoodEvent playerEatFoodEvent = new PlayerEatFoodEvent(player, this);
        player.getServer().getPluginManager().callEvent(playerEatFoodEvent);
        if (playerEatFoodEvent.isCancelled()) {
            return false;
        }
        return playerEatFoodEvent.getFood().onEatenBy(player);
    }

    protected boolean onEatenBy(Player player) {
        player.getFoodData().addFoodLevel(this);
        return true;
    }

    public Food addRelative(int n) {
        return this.addRelative(n, 0);
    }

    public Food addRelative(int n, int n2) {
        a a2 = new a(n, n2);
        return this.a(a2);
    }

    private Food a(a a2) {
        if (!this.relativeIDs.contains(a2)) {
            this.relativeIDs.add(a2);
        }
        return this;
    }

    public int getRestoreFood() {
        return this.restoreFood;
    }

    public Food setRestoreFood(int n) {
        this.restoreFood = n;
        return this;
    }

    public float getRestoreSaturation() {
        return this.restoreSaturation;
    }

    public Food setRestoreSaturation(float f2) {
        this.restoreSaturation = f2;
        return this;
    }
}

