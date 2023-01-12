/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntitySheep
extends EntityWalkingAnimal {
    public static final int NETWORK_ID = 13;
    private boolean v = false;
    private int u;
    private int w = -1;

    public EntitySheep(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 13;
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
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(8);
        super.initEntity();
        if (!this.namedTag.contains("Color")) {
            this.setColor(this.b());
        } else {
            this.setColor(this.namedTag.getByte("Color"));
        }
        if (!this.namedTag.contains("Sheared")) {
            this.namedTag.putBoolean("Sheared", false);
        } else if (this.namedTag.getBoolean("Sheared")) {
            this.v = true;
            this.w = Utils.rand(2400, 4800);
            this.setDataFlag(0, 31, true);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("Color", this.u);
        this.namedTag.putBoolean("Sheared", this.isSheared());
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 351) {
            this.setColor(((ItemDye)item).getDyeColor().getWoolData());
            return true;
        }
        if (item.getId() == 296 && !this.isBaby() && !this.isInLoveCooldown()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, 31);
            this.level.addParticle(new ItemBreakParticle(this.add(0.0, this.getMountedYOffset(), 0.0), Item.get(296)));
            this.setInLove();
            return true;
        }
        if (item.getId() == 359 && !this.isBaby() && !this.v) {
            this.shear(true);
            this.level.addLevelSoundEvent(this, 45);
            player.getInventory().getItemInHand().setDamage(item.getDamage() + 1);
            return true;
        }
        return super.onInteract(player, item, vector3);
    }

    public void shear(boolean bl) {
        this.v = bl;
        this.setDataFlag(0, 31, bl);
        if (bl) {
            this.level.dropItem(this, Item.get(35, this.getColor(), Utils.rand(1, 3)));
            this.w = Utils.rand(2400, 4800);
        } else {
            this.w = -1;
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
            if (!this.v) {
                arrayList.add(Item.get(35, this.getColor(), 1));
            }
            for (int k = 0; k < Utils.rand(1, 2); ++k) {
                arrayList.add(Item.get(this.isOnFire() ? 424 : 423, 0, 1));
            }
        }
        return arrayList.toArray(new Item[0]);
    }

    public void setColor(int n) {
        this.u = n;
        this.namedTag.putByte("Color", n);
        this.setDataProperty(new ByteEntityData(3, n));
    }

    public int getColor() {
        return this.u;
    }

    private int b() {
        int n = Utils.rand(1, 200);
        if (n == 1) {
            return DyeColor.PINK.getWoolData();
        }
        if (n < 8) {
            return DyeColor.BROWN.getWoolData();
        }
        if (n < 18) {
            return DyeColor.GRAY.getWoolData();
        }
        if (n < 28) {
            return DyeColor.LIGHT_GRAY.getWoolData();
        }
        if (n < 38) {
            return DyeColor.BLACK.getWoolData();
        }
        return DyeColor.WHITE.getWoolData();
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public boolean entityBaseTick(int n) {
        if (this.v && this.w > 0) {
            if (this.w == 40) {
                if (this.stayTime <= 0) {
                    this.stayTime = 50;
                }
                EntityEventPacket entityEventPacket = new EntityEventPacket();
                entityEventPacket.eid = this.getId();
                entityEventPacket.event = 10;
                Server.broadcastPacket(this.getViewers().values(), (DataPacket)entityEventPacket);
            }
            --this.w;
        } else if (this.v && this.w == 0) {
            this.shear(false);
        }
        return super.entityBaseTick(n);
    }

    public boolean isSheared() {
        return this.v;
    }
}

