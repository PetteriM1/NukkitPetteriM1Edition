/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.projectile;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.List;

public class EntityEnderPearl
extends EntityProjectile {
    public static final int NETWORK_ID = 87;
    private int k;

    @Override
    public int getNetworkId() {
        return 87;
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
        return 0.03f;
    }

    @Override
    protected float getDrag() {
        return 0.01f;
    }

    public EntityEnderPearl(FullChunk fullChunk, CompoundTag compoundTag) {
        this(fullChunk, compoundTag, null);
    }

    public EntityEnderPearl(FullChunk fullChunk, CompoundTag compoundTag, Entity entity) {
        super(fullChunk, compoundTag, entity);
        if (this.shootingEntity != null) {
            this.k = this.shootingEntity.getLevel().getId();
        }
    }

    @Override
    public boolean onUpdate(int n) {
        if (this.closed) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        if (this.isCollided && this.shootingEntity instanceof Player) {
            List<Block> list = this.getCollisionBlocks();
            boolean bl = false;
            for (Block position : list) {
                if (position.getId() != 90) continue;
                bl = true;
            }
            this.close();
            if (!bl) {
                this.a();
                if (Server.getInstance().mobsFromBlocks && Utils.rand(1, 20) == 5) {
                    CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(87, CreatureSpawnEvent.SpawnReason.ENDER_PEARL);
                    this.level.getServer().getPluginManager().callEvent(creatureSpawnEvent);
                    if (creatureSpawnEvent.isCancelled()) {
                        return false;
                    }
                    Entity entity = Entity.createEntity("Endermite", (Position)this.add(0.5, 1.0, 0.5), new Object[0]);
                    if (entity != null) {
                        entity.spawnToAll();
                    }
                }
            }
            return false;
        }
        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            this.close();
            return false;
        }
        boolean bl = super.onUpdate(n);
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl;
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        if (this.shootingEntity instanceof Player) {
            this.a();
        }
        super.onCollideWithEntity(entity);
    }

    private void a() {
        if (this.shootingEntity == null || this.shootingEntity.getLevel().getId() != this.k) {
            return;
        }
        this.shootingEntity.teleport(new Vector3((double)NukkitMath.floorDouble(this.x) + 0.5, this.y, (double)NukkitMath.floorDouble(this.z) + 0.5), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
        int n = ((Player)this.shootingEntity).getGamemode();
        if (n == 0 || n == 2) {
            this.shootingEntity.attack(new EntityDamageByEntityEvent((Entity)this, this.shootingEntity, EntityDamageEvent.DamageCause.FALL, 5.0f, 0.0f));
        }
        this.level.addLevelSoundEvent(this, 118);
    }
}

