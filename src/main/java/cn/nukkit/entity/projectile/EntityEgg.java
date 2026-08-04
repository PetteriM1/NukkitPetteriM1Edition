package cn.nukkit.entity.projectile;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityChicken;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class EntityEgg extends EntityProjectile {

    public static final int NETWORK_ID = 82;

    public EntityEgg(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityEgg(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    protected float getDrag() {
        return 0.01f;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.age > 1200) {
            this.close();
        } else if (this.isCollided) {
            this.close();

            if (Server.getInstance().mobsFromBlocks) {
                if (ThreadLocalRandom.current().nextInt(256) < 35) {
                    CreatureSpawnEvent ev = new CreatureSpawnEvent(NETWORK_ID, this, CreatureSpawnEvent.SpawnReason.EGG);
                    level.getServer().getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        return false;
                    }

                    EntityChicken entity = (EntityChicken) Entity.createEntity("Chicken", this.add(0.5, 1, 0.5));
                    if (entity != null) {
                        entity.setBaby(true);
                        entity.spawnToAll();
                    }
                }
            }
        }

        super.onUpdate(currentTick);
        return !this.closed;
    }

    @Override
    public void onHit() {
        level.addParticle(new ItemBreakParticle(this, Item.get(Item.EGG)), null, 5);
    }
}
