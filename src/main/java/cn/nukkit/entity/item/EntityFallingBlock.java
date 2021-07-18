package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityBlockChangeEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.sound.AnvilFallSound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import sun.security.krb5.internal.crypto.Des;

/**
 * @author MagicDroidX
 */
public class EntityFallingBlock extends Entity {

    public static final int NETWORK_ID = 66;

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
        return false;
    }

    protected int blockId;
    protected int damage;
    protected boolean breakOnLava;

    public EntityFallingBlock(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (this.namedTag != null) {
            if (this.namedTag.contains("TileID")) {
                this.blockId = this.namedTag.getInt("TileID");
            } else if (this.namedTag.contains("Tile")) {
                this.blockId = this.namedTag.getInt("Tile");
                this.namedTag.putInt("TileID", blockId);
            }

            if (this.namedTag.contains("Data")) {
                damage = this.namedTag.getByte("Data");
            }

            this.breakOnLava = this.namedTag.getBoolean("BreakOnLava");
        }

        if (this.blockId == 0) {
            this.close();
            return;
        }

        this.fireProof = true;
    }

    @Override // Multiversion: display correct block
    public void spawnTo(Player player) {
        if (!this.hasSpawned.containsKey(player.getLoaderId()) && player.usedChunks.containsKey(Level.chunkHash(this.chunk.getX(), this.chunk.getZ()))) {
            AddEntityPacket addEntity = new AddEntityPacket();
            addEntity.type = this.getNetworkId();
            addEntity.entityUniqueId = this.id;
            addEntity.entityRuntimeId = this.id;
            addEntity.yaw = (float) this.yaw;
            addEntity.headYaw = (float) this.yaw;
            addEntity.pitch = (float) this.pitch;
            addEntity.x = (float) this.x;
            addEntity.y = (float) this.y;
            addEntity.z = (float) this.z;
            addEntity.speedX = (float) this.motionX;
            addEntity.speedY = (float) this.motionY;
            addEntity.speedZ = (float) this.motionZ;
            addEntity.metadata = this.dataProperties.clone().put(new IntEntityData(DATA_VARIANT, GlobalBlockPalette.getOrCreateRuntimeId(player.protocol, this.blockId, this.damage)));
            player.dataPacket(addEntity);
            this.hasSpawned.put(player.getLoaderId(), player);
        }
    }

    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return source.getCause() == DamageCause.VOID && super.attack(source);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.timing != null) this.timing.startTiming();

        int tickDiff = currentTick - lastUpdate;
        if (tickDiff <= 0 && !justCreated) {
            return true;
        }

        lastUpdate = currentTick;

        boolean hasUpdate = this.entityBaseTick(tickDiff);

        if (this.isAlive()) {
            motionY -= this.getGravity();

            this.move(motionX, motionY, motionZ);

            float friction = 1 - this.getDrag();

            motionX *= friction;
            motionY *= 1 - this.getDrag();
            motionZ *= friction;

            Vector3 pos = new Vector3(x - 0.5, y, z - 0.5).round();
            if (breakOnLava && level.getBlock(pos.subtract(0, 1, 0)) instanceof BlockLava) {
                this.close();
                if (this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
                    this.level.dropItem(this, Block.get(this.getBlock(), this.getDamage()).toItem());
                }
                level.addParticle(new DestroyBlockParticle(pos, Block.get(this.getBlock(), this.getDamage())));
                return true;
            }

            if (this.onGround) {
                this.close();
                Block block = this.level.getBlock(pos);
                Block floorBlock = this.level.getBlock(pos);
                if (this.getBlock() == Block.SNOW_LAYER && floorBlock.getId() == Block.SNOW_LAYER && (floorBlock.getDamage() & 0x7) != 0x7) {
                    int mergedHeight = (floorBlock.getDamage() & 0x7) + 1 + (this.getDamage() & 0x7) + 1;
                    if (mergedHeight > 8) {
                        EntityBlockChangeEvent event = new EntityBlockChangeEvent(this, floorBlock, Block.get(Block.SNOW_LAYER, 0x7));
                        this.server.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            this.level.setBlock(pos, event.getTo(), true);

                            Vector3 abovePos = pos.up();
                            Block aboveBlock = this.level.getBlock(abovePos);
                            if (aboveBlock.getId() == Block.AIR) {
                                EntityBlockChangeEvent event2 = new EntityBlockChangeEvent(this, aboveBlock, Block.get(Block.SNOW_LAYER, mergedHeight - 9)); // -8-1
                                this.server.getPluginManager().callEvent(event2);
                                if (!event2.isCancelled()) {
                                    this.level.setBlock(abovePos, event2.getTo(), true);
                                }
                            }
                        }
                    } else {
                        EntityBlockChangeEvent event = new EntityBlockChangeEvent(this, floorBlock, Block.get(Block.SNOW_LAYER, mergedHeight - 1));
                        this.server.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            this.level.setBlock(pos, event.getTo(), true);
                        }
                    }
                } else if ((block.isTransparent() && !block.canBeReplaced() || this.getBlock() == Block.SNOW_LAYER && block instanceof BlockLiquid)) {
                    if (this.getBlock() != Block.SNOW_LAYER ? this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS) : this.level.getGameRules().getBoolean(GameRule.DO_TILE_DROPS)) {
                        this.level.dropItem(this, Block.get(this.getBlock(), this.getDamage()).toItem());
                    }
                } else {
                    EntityBlockChangeEvent event = new EntityBlockChangeEvent(this, block, Block.get(blockId, damage));
                    this.server.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        this.level.setBlock(pos, event.getTo(), true, true);
                        this.level.scheduleUpdate(this.level.getBlock(pos), 1);

                        if (event.getTo().getId() == Item.ANVIL) {
                            this.level.addSound(new AnvilFallSound(pos));
                        }
                    }
                }
                hasUpdate = true;
            }

            this.updateMovement();
        }

        if (this.timing != null) this.timing.stopTiming();

        return hasUpdate || !this.onGround || Math.abs(motionX) > 0.00001 || Math.abs(motionY) > 0.00001 || Math.abs(motionZ) > 0.00001;
    }

    public int getBlock() {
        return blockId;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void saveNBT() {
        namedTag.putInt("TileID", blockId);
        namedTag.putByte("Data", damage);
    }

    @Override
    public boolean canBeMovedByCurrents() {
        return false;
    }
}
