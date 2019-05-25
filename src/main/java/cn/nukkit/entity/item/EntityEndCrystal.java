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

    @Override
    public float getLength() {
        return 1f;
    }

    @Override
    public float getHeight() {
        return 1f;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityEndCrystal(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (this.closed) {
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
        this.kill();
        if (this.level.getGameRules().getBoolean(GameRule.TNT_EXPLODES)) {
            ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 5);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) return;
            Explosion explode = new Explosion(this, (float) ev.getForce(), this);
            explode.explodeA();
            explode.explodeB();
        }
    }
}
