package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created by PetteriM1
 */
public class EntityEndCrystal extends Entity implements EntityExplosive {

    public static final int NETWORK_ID = 71;

    protected boolean detonated = false;

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
        return NETWORK_ID;
    }

    public EntityEndCrystal(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
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
    public boolean attack(EntityDamageEvent source) {
        if (this.closed) {
            return false;
        }

        if (source.getCause() == EntityDamageEvent.DamageCause.FIRE || source.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || source.getCause() == EntityDamageEvent.DamageCause.LAVA) {
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
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_SHOWBASE);
    }

    public void setShowBase(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SHOWBASE, value);
    }

    @Override
    public void explode() {
        this.close();
        if (!detonated && ((level.getServer().suomiCraftPEMode() && this.level.getGameRules().getBoolean(GameRule.TNT_EXPLODES)) || (!level.getServer().suomiCraftPEMode() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)))) {
            this.detonated = true;
            ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 6);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) return;
            Explosion explode = new Explosion(this, (float) ev.getForce(), this);
            explode.explodeA();
            explode.explodeB();
        }
    }
}
