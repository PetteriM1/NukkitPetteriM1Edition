/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityEndCrystal
extends Entity
implements EntityExplosive {
    public static final int NETWORK_ID = 71;
    private boolean k = false;
    private String l;

    @Override
    public float getLength() {
        return 1.0f;
    }

    @Override
    public float getHeight() {
        return 1.5f;
    }

    @Override
    public float getWidth() {
        return 1.0f;
    }

    @Override
    public int getNetworkId() {
        return 71;
    }

    public EntityEndCrystal(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        if (this.namedTag.contains("ShowBottom")) {
            this.setShowBase(this.namedTag.getBoolean("ShowBottom"));
        }
        this.fireProof = true;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean("ShowBottom", this.showBase());
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        if (this.closed) {
            return false;
        }
        if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            return false;
        }
        this.explode();
        return true;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return true;
    }

    public boolean showBase() {
        return this.getDataFlag(0, 38);
    }

    public void setShowBase(boolean bl) {
        this.setDataFlag(0, 38, bl);
    }

    @Override
    public void explode() {
        this.close();
        if (!this.k && (this.level.getServer().suomiCraftPEMode() && this.level.getGameRules().getBoolean(GameRule.TNT_EXPLODES) || !this.level.getServer().suomiCraftPEMode() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING))) {
            this.k = true;
            EntityExplosionPrimeEvent entityExplosionPrimeEvent = new EntityExplosionPrimeEvent(this, 6.0);
            this.server.getPluginManager().callEvent(entityExplosionPrimeEvent);
            if (entityExplosionPrimeEvent.isCancelled()) {
                return;
            }
            Explosion explosion = new Explosion(this.add(0.0, this.getHeight() / 2.0f, 0.0), (float)entityExplosionPrimeEvent.getForce(), this);
            if (entityExplosionPrimeEvent.isBlockBreaking()) {
                explosion.explodeA();
            }
            explosion.explodeB();
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "End Crystal";
    }

    @Override
    public boolean goToNewChunk(FullChunk fullChunk) {
        if (fullChunk.getEntities().size() > 200) {
            this.close();
            return false;
        }
        return true;
    }

    @Override
    public void setNameTag(String string) {
        this.l = string;
        if (this.namedTag.contains("CustomNameVisible") || this.namedTag.contains("CustomNameAlwaysVisible")) {
            this.setDataProperty(new StringEntityData(4, string));
        }
    }

    @Override
    public boolean hasCustomName() {
        return this.l != null;
    }

    @Override
    public String getNameTag() {
        return this.l == null ? "" : this.l;
    }
}

