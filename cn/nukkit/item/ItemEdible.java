/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.food.Food;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;

public abstract class ItemEdible
extends Item {
    public ItemEdible(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }

    public ItemEdible(int n) {
        super(n);
    }

    public ItemEdible(int n, Integer n2) {
        super(n, n2);
    }

    public ItemEdible(int n, Integer n2, int n3) {
        super(n, n2, n3);
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return player.canEat(true);
    }

    @Override
    public boolean onUse(Player player, int n) {
        if (n < 15) {
            return false;
        }
        PlayerItemConsumeEvent playerItemConsumeEvent = new PlayerItemConsumeEvent(player, this);
        player.getServer().getPluginManager().callEvent(playerItemConsumeEvent);
        if (playerItemConsumeEvent.isCancelled()) {
            return false;
        }
        Food food = Food.getByRelative(this);
        if (food != null && food.eatenBy(player)) {
            player.getLevel().addSound((Vector3)player, Sound.RANDOM_BURP);
            if (!player.isCreative() && !player.isSpectator()) {
                --this.count;
                player.getInventory().setItemInHand(this);
            }
        }
        return true;
    }
}

