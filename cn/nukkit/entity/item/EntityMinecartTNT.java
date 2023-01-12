/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.item.EntityMinecartAbstract;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.MinecartType;
import cn.nukkit.utils.Utils;

public class EntityMinecartTNT
extends EntityMinecartAbstract
implements EntityExplosive {
    public static final int NETWORK_ID = 97;
    private int D;

    public EntityMinecartTNT(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
        this.setDisplayBlock(Block.get(46), false);
        this.setName("Minecart with TNT");
    }

    @Override
    public boolean isRideable() {
        return false;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.D = this.namedTag.contains("TNTFuse") ? this.namedTag.getByte("TNTFuse") : 80;
    }

    @Override
    public boolean onUpdate(int n) {
        int n2;
        if (this.timing != null) {
            this.timing.startTiming();
        }
        if (this.D < 80) {
            n2 = n - this.lastUpdate;
            this.lastUpdate = n;
            if (this.D % 5 == 0) {
                this.setDataProperty(new IntEntityData(55, this.D));
            }
            this.D -= n2;
            if (this.isAlive() && this.D <= 0) {
                if (this.level.getGameRules().getBoolean(GameRule.TNT_EXPLODES)) {
                    this.explode(Utils.random.nextInt(5));
                }
                this.close();
                return false;
            }
        }
        n2 = super.onUpdate(n);
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return n2 != 0;
    }

    @Override
    public void activate(int n, int n2, int n3, boolean bl) {
        this.level.addLevelSoundEvent(this, 50);
        this.D = 79;
    }

    @Override
    public void explode() {
        this.explode(0.0);
    }

    public void explode(double d2) {
        double d3 = Math.sqrt(d2);
        if (d3 > 5.0) {
            d3 = 5.0;
        }
        EntityExplosionPrimeEvent entityExplosionPrimeEvent = new EntityExplosionPrimeEvent(this, 4.0 + Utils.random.nextDouble() * 1.5 * d3);
        this.server.getPluginManager().callEvent(entityExplosionPrimeEvent);
        if (entityExplosionPrimeEvent.isCancelled()) {
            return;
        }
        Explosion explosion = new Explosion(this, entityExplosionPrimeEvent.getForce(), this);
        if (entityExplosionPrimeEvent.isBlockBreaking()) {
            explosion.explodeA();
        }
        explosion.explodeB();
        this.close();
    }

    @Override
    public void dropItem() {
        Entity entity;
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && (entity = ((EntityDamageByEntityEvent)this.lastDamageCause).getDamager()) instanceof Player && ((Player)entity).isCreative()) {
            return;
        }
        this.level.dropItem(this, Item.get(407));
    }

    @Override
    public MinecartType getType() {
        return MinecartType.valueOf(3);
    }

    @Override
    public int getNetworkId() {
        return 97;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("TNTFuse", this.D);
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (item.getId() == 259 || item.getId() == 385) {
            this.level.addLevelSoundEvent(this, 50);
            this.D = 79;
            return true;
        }
        return super.onInteract(player, item, vector3);
    }

    @Override
    public boolean mountEntity(Entity entity, byte by) {
        return false;
    }
}

