/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.passive.EntityWaterAnimal;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public abstract class EntityFish
extends EntityWaterAnimal {
    public EntityFish(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 3);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 325 && (item.getDamage() == 0 || item.getDamage() == 8) && this.isInsideOfWater()) {
            this.close();
            if (item.getCount() <= 1) {
                player.getInventory().setItemInHand(Item.get(325, this.b(), 1));
                return false;
            }
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            player.getInventory().addItem(Item.get(325, this.b(), 1));
            return true;
        }
        return super.onInteract(player, item, vector3);
    }

    abstract int b();
}

