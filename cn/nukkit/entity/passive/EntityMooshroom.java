/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityMooshroom
extends EntityWalkingAnimal {
    public static final int NETWORK_ID = 16;

    public EntityMooshroom(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 16;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.7f;
        }
        return 1.4f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(10);
        super.initEntity();
        if (this.namedTag.contains("Variant")) {
            this.setBrown(this.namedTag.getInt("Variant") == 1);
        }
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return player.spawned && player.isAlive() && !player.closed && player.getInventory().getItemInHandFast().getId() == 296 && d2 <= 40.0;
        }
        return super.targetOption(entityCreature, d2);
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            int n;
            for (n = 0; n < Utils.rand(0, 2); ++n) {
                arrayList.add(Item.get(334, 0, 1));
            }
            for (n = 0; n < Utils.rand(1, 3); ++n) {
                arrayList.add(Item.get(this.isOnFire() ? 364 : 363, 0, 1));
            }
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 281) {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            player.getInventory().addItem(Item.get(282, 0, 1));
            this.level.addSound((Vector3)this, Sound.MOB_MOOSHROOM_SUSPICIOUS_MILK);
            return false;
        }
        if (item.getId() == 325) {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            Item item2 = Item.get(325, 1, 1);
            if (player.getInventory().getItemFast((int)player.getInventory().getHeldItemIndex()).count > 0) {
                if (player.getInventory().canAddItem(item2)) {
                    player.getInventory().addItem(item2);
                } else {
                    player.dropItem(item2);
                }
            } else {
                player.getInventory().setItemInHand(item2);
            }
            this.level.addLevelSoundEvent(this, 46);
            return false;
        }
        if (item.getId() == 296 && !this.isBaby() && !this.isInLoveCooldown()) {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(296)));
            this.setInLove();
            return false;
        }
        return super.onInteract(player, item, vector3);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Variant", this.isBrown() ? 1 : 0);
    }

    @Override
    public void onStruckByLightning(Entity entity) {
        this.setBrown(!this.isBrown());
        super.onStruckByLightning(entity);
    }

    public boolean isBrown() {
        return this.getDataPropertyInt(2) == 1;
    }

    public void setBrown(boolean bl) {
        this.setDataProperty(new IntEntityData(2, bl ? 1 : 0));
    }
}

