package cn.nukkit.entity.item;

import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Explosion;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created by PetteriM1
 */
public class EntityEndCrystal extends Entity {

    public static final int NETWORK_ID = 71;

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
    }

    public boolean attack(EntityDamageEvent source){
        if (this.closed) return false;
        close();
        kill();
        ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 5);
        this.server.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) return true;
		Explosion explode = new Explosion(this, (float) ev.getForce(), this);
		explode.explodeA();
		explode.explodeB();
        return true;
	}

    @Override
	public boolean canCollideWith(Entity entity) {
		return false;
	}
    
    public boolean showBase() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_SHOWBASE);
    }

    public void setShowBase(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SHOWBASE, value);
    }
}
