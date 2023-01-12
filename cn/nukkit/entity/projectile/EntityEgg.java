/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityChicken;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityEgg
extends EntityProjectile {
    public static final int NETWORK_ID = 82;

    @Override
    public int getNetworkId() {
        return 82;
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

    public EntityEgg(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityEgg(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.age > 1200) {
            this.close();
        } else if (this.isCollided) {
            this.close();
            if (Server.getInstance().mobsFromBlocks && Utils.rand(1, 20) == 5) {
                CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(82, CreatureSpawnEvent.SpawnReason.EGG);
                this.level.getServer().getPluginManager().callEvent(creatureSpawnEvent);
                if (creatureSpawnEvent.isCancelled()) {
                    return false;
                }
                EntityChicken entityChicken = (EntityChicken)Entity.createEntity("Chicken", (Position)this.add(0.5, 1.0, 0.5), new Object[0]);
                if (entityChicken != null) {
                    entityChicken.spawnToAll();
                    entityChicken.setBaby(true);
                }
            }
        }
        super.onUpdate(n);
        return !this.closed;
    }

    @Override
    public void onHit() {
        this.level.addParticle(new ItemBreakParticle(this, Item.get(344)), null, 5);
    }
}

