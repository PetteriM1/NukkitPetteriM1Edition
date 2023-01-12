/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.EntityJumpingAnimal;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityRabbit
extends EntityJumpingAnimal {
    public static final int NETWORK_ID = 18;

    public EntityRabbit(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 18;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.2f;
        }
        return 0.4f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.25f;
        }
        return 0.5f;
    }

    @Override
    public double getSpeed() {
        return 1.2;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(3);
        super.initEntity();
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            int n = player.getInventory().getItemInHandFast().getId();
            return player.spawned && player.isAlive() && !player.closed && (n == 37 || n == 391 || n == 396) && d2 <= 40.0;
        }
        return super.targetOption(entityCreature, d2);
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            arrayList.add(Item.get(415, 0, Utils.rand(0, 1)));
            arrayList.add(Item.get(this.isOnFire() ? 412 : 411, 0, Utils.rand(0, 1)));
            for (int k = 0; k < (Utils.rand(0, 101) <= 9 ? 1 : 0); ++k) {
                arrayList.add(Item.get(414, 0, 1));
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
        if (item.getId() == 37 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(37)));
            this.setInLove();
            return true;
        }
        if (item.getId() == 391 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(391)));
            this.setInLove();
            return true;
        }
        if (item.getId() == 396 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(396)));
            this.setInLove();
            return true;
        }
        return super.onInteract(player, item, vector3);
    }
}

