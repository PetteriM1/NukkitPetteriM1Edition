/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityBlockChangeEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

public class EntityFallingBlock
extends Entity {
    public static final int NETWORK_ID = 66;
    protected int blockId;
    protected int damage;

    @Override
    public float getWidth() {
        return 0.98f;
    }

    @Override
    public float getLength() {
        return 0.98f;
    }

    @Override
    public float getHeight() {
        return 0.98f;
    }

    @Override
    protected float getGravity() {
        return 0.04f;
    }

    @Override
    protected float getDrag() {
        return 0.02f;
    }

    @Override
    protected float getBaseOffset() {
        return 0.49f;
    }

    @Override
    public boolean canCollide() {
        return this.blockId == 145;
    }

    public EntityFallingBlock(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        if (this.namedTag != null) {
            if (this.namedTag.contains("TileID")) {
                this.blockId = this.namedTag.getInt("TileID");
            } else if (this.namedTag.contains("Tile")) {
                this.blockId = this.namedTag.getInt("Tile");
                this.namedTag.putInt("TileID", this.blockId);
            }
            if (this.namedTag.contains("Data")) {
                this.damage = this.namedTag.getByte("Data");
            }
        }
        if (this.blockId == 0) {
            this.close();
            return;
        }
        this.fireProof = true;
    }

    @Override
    public void spawnTo(Player player) {
        Boolean bl;
        if (!this.hasSpawned.containsKey(player.getLoaderId()) && (bl = player.usedChunks.get(Level.chunkHash(this.chunk.getX(), this.chunk.getZ()))) != null && bl.booleanValue()) {
            AddEntityPacket addEntityPacket = new AddEntityPacket();
            addEntityPacket.type = this.getNetworkId();
            addEntityPacket.entityUniqueId = this.id;
            addEntityPacket.entityRuntimeId = this.id;
            addEntityPacket.yaw = (float)this.yaw;
            addEntityPacket.headYaw = (float)this.yaw;
            addEntityPacket.pitch = (float)this.pitch;
            addEntityPacket.x = (float)this.x;
            addEntityPacket.y = (float)this.y;
            addEntityPacket.z = (float)this.z;
            addEntityPacket.speedX = (float)this.motionX;
            addEntityPacket.speedY = (float)this.motionY;
            addEntityPacket.speedZ = (float)this.motionZ;
            addEntityPacket.metadata = this.dataProperties.clone().put(new IntEntityData(2, GlobalBlockPalette.getOrCreateRuntimeId(player.protocol, this.blockId, this.damage)));
            player.dataPacket(addEntityPacket);
            this.hasSpawned.put(player.getLoaderId(), player);
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return this.blockId == 145;
    }

    @Override
    public boolean attack(EntityDamageEvent entityDamageEvent) {
        return entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.VOID && super.attack(entityDamageEvent);
    }

    @Override
    public boolean onUpdate(int n) {
        boolean bl;
        block10: {
            block11: {
                block14: {
                    Block block;
                    Vector3 vector3;
                    block12: {
                        int n2;
                        Block block2;
                        block13: {
                            int n3;
                            if (this.closed) {
                                return false;
                            }
                            if (this.timing != null) {
                                this.timing.startTiming();
                            }
                            if ((n3 = n - this.lastUpdate) <= 0 && !this.justCreated) {
                                return true;
                            }
                            this.lastUpdate = n;
                            bl = this.entityBaseTick(n3);
                            if (!this.isAlive()) break block10;
                            this.motionY -= (double)this.getGravity();
                            this.move(this.motionX, this.motionY, this.motionZ);
                            float f2 = 1.0f - this.getDrag();
                            this.motionX *= (double)f2;
                            this.motionY *= (double)(1.0f - this.getDrag());
                            this.motionZ *= (double)f2;
                            vector3 = new Vector3(this.x - 0.5, this.y, this.z - 0.5).round();
                            if (!this.onGround) break block11;
                            this.close();
                            block = this.level.getBlock(vector3);
                            block2 = this.level.getBlock(vector3);
                            if (this.getBlock() != 78 || block2.getId() != 78 || (block2.getDamage() & 7) == 7) break block12;
                            n2 = (block2.getDamage() & 7) + 1 + (this.getDamage() & 7) + 1;
                            if (n2 <= 8) break block13;
                            EntityBlockChangeEvent entityBlockChangeEvent = new EntityBlockChangeEvent(this, block2, Block.get(78, 7));
                            this.server.getPluginManager().callEvent(entityBlockChangeEvent);
                            if (entityBlockChangeEvent.isCancelled()) break block14;
                            this.level.setBlock(vector3, entityBlockChangeEvent.getTo(), true);
                            Vector3 vector32 = vector3.getSideVec(BlockFace.UP);
                            Block block3 = this.level.getBlock(vector32);
                            if (block3.getId() != 0) break block14;
                            EntityBlockChangeEvent entityBlockChangeEvent2 = new EntityBlockChangeEvent(this, block3, Block.get(78, n2 - 9));
                            this.server.getPluginManager().callEvent(entityBlockChangeEvent2);
                            if (entityBlockChangeEvent2.isCancelled()) break block14;
                            this.level.setBlock(vector32, entityBlockChangeEvent2.getTo(), true);
                            break block14;
                        }
                        EntityBlockChangeEvent entityBlockChangeEvent = new EntityBlockChangeEvent(this, block2, Block.get(78, n2 - 1));
                        this.server.getPluginManager().callEvent(entityBlockChangeEvent);
                        if (entityBlockChangeEvent.isCancelled()) break block14;
                        this.level.setBlock(vector3, entityBlockChangeEvent.getTo(), true);
                        break block14;
                    }
                    if (block.isTransparent() && !block.canBeReplaced() || this.getBlock() == 78 && block instanceof BlockLiquid) {
                        if (this.getBlock() != 78 ? this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS) : this.level.getGameRules().getBoolean(GameRule.DO_TILE_DROPS)) {
                            this.getLevel().dropItem(this, Item.get(this.blockId, this.damage, 1));
                        }
                    } else {
                        EntityBlockChangeEvent entityBlockChangeEvent = new EntityBlockChangeEvent(this, block, Block.get(this.blockId, this.damage));
                        this.server.getPluginManager().callEvent(entityBlockChangeEvent);
                        if (!entityBlockChangeEvent.isCancelled()) {
                            this.getLevel().setBlock(vector3, entityBlockChangeEvent.getTo(), true, true);
                            this.getLevel().scheduleUpdate(this.getLevel().getBlock(vector3), 1);
                            if (entityBlockChangeEvent.getTo().getId() == 145) {
                                Entity[] entityArray;
                                this.getLevel().addLevelEvent(this, 1022);
                                for (Entity entity : entityArray = this.level.getCollidingEntities(this.getBoundingBox(), this)) {
                                    if (!(entity instanceof EntityLiving) || !(this.highestPosition > this.y)) continue;
                                    entity.attack(new EntityDamageByBlockEvent(entityBlockChangeEvent.getTo(), entity, EntityDamageEvent.DamageCause.CONTACT, (float)Math.min(40.0, Math.max(0.0, (this.highestPosition - this.y) * 2.0))));
                                }
                            }
                        }
                    }
                }
                bl = true;
            }
            this.updateMovement();
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl || !this.onGround || Math.abs(this.motionX) > 1.0E-5 || Math.abs(this.motionY) > 1.0E-5 || Math.abs(this.motionZ) > 1.0E-5;
    }

    public int getBlock() {
        return this.blockId;
    }

    public int getDamage() {
        return this.damage;
    }

    @Override
    public int getNetworkId() {
        return 66;
    }

    @Override
    public void saveNBT() {
        this.namedTag.putInt("TileID", this.blockId);
        this.namedTag.putByte("Data", this.damage);
    }

    @Override
    public boolean canBeMovedByCurrents() {
        return false;
    }

    @Override
    public void resetFallDistance() {
        if (!this.closed) {
            this.highestPosition = this.y;
        }
    }
}

