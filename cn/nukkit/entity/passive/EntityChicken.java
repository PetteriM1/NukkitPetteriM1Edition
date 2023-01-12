/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityChicken
extends EntityWalkingAnimal {
    public static final int NETWORK_ID = 10;
    private int u = EntityChicken.b();
    private boolean v = false;

    public EntityChicken(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 10;
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
            return 0.35f;
        }
        return 0.7f;
    }

    @Override
    public float getDrag() {
        return 0.2f;
    }

    @Override
    public float getGravity() {
        return 0.08f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(4);
        super.initEntity();
        this.noFallDamage = true;
        this.u = this.namedTag.contains("EggLayTime") ? this.namedTag.getInt("EggLayTime") : EntityChicken.b();
        this.v = this.namedTag.contains("IsChickenJockey") ? this.namedTag.getBoolean("IsChickenJockey") : false;
    }

    @Override
    public boolean entityBaseTick(int n) {
        boolean bl = super.entityBaseTick(n);
        if (this.getServer().mobsFromBlocks && !this.isBaby()) {
            if (this.u > 0) {
                this.u -= n;
            } else {
                this.level.dropItem(this, Item.get(344, 0, 1));
                this.level.addLevelSoundEvent(this, 34);
                this.u = EntityChicken.b();
            }
        }
        return bl;
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            int n = player.getInventory().getItemInHandFast().getId();
            return player.isAlive() && !player.closed && (n == 295 || n == 458 || n == 362 || n == 361) && d2 <= 40.0;
        }
        return super.targetOption(entityCreature, d2);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 295 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)), Item.get(295)));
            this.setInLove();
        } else if (item.getId() == 458 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)), Item.get(458)));
            this.setInLove();
        } else if (item.getId() == 362 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)), Item.get(362)));
            this.setInLove();
        } else if (item.getId() == 361 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)), Item.get(361)));
            this.setInLove();
        }
        return super.onInteract(player, item, vector3);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("EggLayTime", this.u);
        this.namedTag.putBoolean("IsChickenJockey", this.v);
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            for (int k = 0; k < Utils.rand(0, 2); ++k) {
                arrayList.add(Item.get(288, 0, 1));
            }
            arrayList.add(Item.get(this.isOnFire() ? 366 : 365, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return super.attack(entityDamageEvent);
        }
        return false;
    }

    private static int b() {
        return Utils.rand(6000, 12000);
    }

    public boolean isChickenJockey() {
        return this.v;
    }

    public void setChickenJockey(boolean bl) {
        this.v = bl;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }
}

