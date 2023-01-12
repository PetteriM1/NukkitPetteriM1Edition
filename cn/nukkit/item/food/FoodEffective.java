/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.food;

import cn.nukkit.Player;
import cn.nukkit.item.food.Food;
import cn.nukkit.item.food.a;
import cn.nukkit.potion.Effect;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class FoodEffective
extends Food {
    protected final Map<Effect, Float> effects = new LinkedHashMap<Effect, Float>();

    public FoodEffective(int n, float f2) {
        this.setRestoreFood(n);
        this.setRestoreSaturation(f2);
    }

    public FoodEffective addEffect(Effect effect) {
        return this.addChanceEffect(1.0f, effect);
    }

    public FoodEffective addChanceEffect(float f2, Effect effect) {
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        this.effects.put(effect, Float.valueOf(f2));
        return this;
    }

    @Override
    protected boolean onEatenBy(Player player) {
        super.onEatenBy(player);
        LinkedList linkedList = new LinkedList();
        this.effects.forEach((effect, f2) -> {
            if ((double)f2.floatValue() >= Math.random()) {
                linkedList.add(effect.clone());
            }
        });
        linkedList.forEach(player::addEffect);
        a a2 = (a)this.relativeIDs.get(0);
        if (a2 != null && a2.a == 466) {
            player.awardAchievement("overpowered");
        }
        return true;
    }
}

