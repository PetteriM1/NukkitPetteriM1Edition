/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.weather;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFire;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.weather.EntityLightningStrike;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityLightning
extends Entity
implements EntityLightningStrike {
    public static final int NETWORK_ID = 93;
    private boolean k = true;
    public int state;
    public int liveTime;

    @Override
    public int getNetworkId() {
        return 93;
    }

    public EntityLightning(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initEntity() {
        Block block;
        super.initEntity();
        this.state = 2;
        this.liveTime = Utils.random.nextInt(3) + 1;
        if (this.k && this.level.gameRules.getBoolean(GameRule.DO_FIRE_TICK) && this.server.getDifficulty() >= 2 && ((block = this.getLevelBlock()).getId() == 0 || block.getId() == 31)) {
            BlockFire blockFire = (BlockFire)Block.get(51);
            blockFire.x = block.x;
            blockFire.y = block.y;
            blockFire.z = block.z;
            blockFire.level = this.level;
            this.getLevel().setBlock(blockFire, blockFire, true);
            if (blockFire.isBlockTopFacingSurfaceSolid(blockFire.down()) || blockFire.canNeighborBurn()) {
                BlockIgniteEvent blockIgniteEvent = new BlockIgniteEvent(block, null, this, BlockIgniteEvent.BlockIgniteCause.LIGHTNING);
                this.getServer().getPluginManager().callEvent(blockIgniteEvent);
                if (!blockIgniteEvent.isCancelled()) {
                    this.level.setBlock(blockFire, blockFire, true);
                    this.level.scheduleUpdate(blockFire, blockFire.tickRate() + Utils.random.nextInt(10));
                }
            }
        }
    }

    @Override
    public boolean isEffect() {
        return this.k;
    }

    @Override
    public void setEffect(boolean bl) {
        this.k = bl;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        entityDamageEvent.setDamage(0.0f);
        return super.attack(entityDamageEvent);
    }

    @Override
    public boolean onUpdate(int n) {
        Cloneable cloneable;
        if (this.closed) {
            return false;
        }
        int n2 = n - this.lastUpdate;
        if (n2 <= 0 && !this.justCreated) {
            return true;
        }
        this.lastUpdate = n;
        this.entityBaseTick(n2);
        if (this.state == 2) {
            this.level.addLevelSoundEvent(this, 47);
            this.level.addLevelSoundEvent(this, 48);
        }
        --this.state;
        if (this.state < 0) {
            if (this.liveTime == 0) {
                this.close();
                return false;
            }
            if (this.state < -Utils.random.nextInt(10)) {
                --this.liveTime;
                this.state = 1;
                if (this.k && this.level.gameRules.getBoolean(GameRule.DO_FIRE_TICK) && (((Block)(cloneable = this.getLevelBlock())).getId() == 0 || ((Block)cloneable).getId() == 31)) {
                    BlockIgniteEvent blockIgniteEvent = new BlockIgniteEvent((Block)cloneable, null, this, BlockIgniteEvent.BlockIgniteCause.LIGHTNING);
                    this.getServer().getPluginManager().callEvent(blockIgniteEvent);
                    if (!blockIgniteEvent.isCancelled()) {
                        Block block = Block.get(51);
                        this.level.setBlock((Vector3)cloneable, block);
                        this.getLevel().scheduleUpdate(block, block.tickRate());
                    }
                }
            }
        }
        if (this.state >= 0 && this.k) {
            cloneable = this.getBoundingBox().grow(3.0, 3.0, 3.0);
            ((AxisAlignedBB)cloneable).maxX += 6.0;
            for (Entity entity : this.level.getCollidingEntities((AxisAlignedBB)cloneable, this)) {
                entity.onStruckByLightning(this);
            }
        }
        return true;
    }
}

