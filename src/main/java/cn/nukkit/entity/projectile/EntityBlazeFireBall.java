package cn.nukkit.entity.projectile;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFire;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.concurrent.ThreadLocalRandom;

public class EntityBlazeFireBall extends EntityProjectile {

    public static final int NETWORK_ID = 94;

    public EntityBlazeFireBall(FullChunk chunk, CompoundTag nbt) {
        this(chunk, nbt, null);
    }

    public EntityBlazeFireBall(FullChunk chunk, CompoundTag nbt, Entity shootingEntity) {
        super(chunk, nbt, shootingEntity);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.31f;
    }

    @Override
    public float getHeight() {
        return 0.31f;
    }

    @Override
    public float getGravity() {
        return 0.001f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    public double getBaseDamage() {
        return 5;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.age > 1200 || this.isCollided || this.hadCollision) {
            this.close();
            return false;
        } else {
            this.fireTicks = 2;
        }

        super.onUpdate(currentTick);
        return !this.closed;
    }


    @Override
    public void onHitGround(Vector3 moveVector) {
        Block block = level.getBlock(this.chunk, moveVector.getFloorX(), moveVector.getFloorY(), moveVector.getFloorZ(), false);

        if (block.getId() == BlockID.AIR) {
            BlockFire fire = (BlockFire) Block.get(BlockID.FIRE);
            fire.x = block.x;
            fire.y = block.y;
            fire.z = block.z;
            fire.level = this.level;

            if (fire.isBlockTopFacingSurfaceSolid(fire.down()) || fire.canNeighborBurn()) {
                BlockIgniteEvent e = new BlockIgniteEvent(block, null, this, BlockIgniteEvent.BlockIgniteCause.FIREBALL);
                getServer().getPluginManager().callEvent(e);

                if (!e.isCancelled()) {
                    level.setBlock(fire, fire, true);
                    level.scheduleUpdate(fire, fire.tickRate() + ThreadLocalRandom.current().nextInt(10));
                }
            }
        } else {
            block.onEntityCollide(this);
        }
    }
}
