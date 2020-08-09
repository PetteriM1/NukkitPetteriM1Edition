package cn.nukkit.entity.item;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.entity.EntityBlockChangeEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.sound.AnvilFallSound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ProtocolInfo;
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

        if (namedTag != null) {
            if (namedTag.contains("TileID")) {
                blockId = namedTag.getInt("TileID");
            } else if (namedTag.contains("Tile")) {
                blockId = namedTag.getInt("Tile");
                namedTag.putInt("TileID", blockId);
            }

            if (namedTag.contains("Data")) {
                damage = namedTag.getByte("Data");
            }

            breakOnLava = namedTag.getBoolean("BreakOnLava");
        }

        if (blockId == 0) {
            close();
            return;
        }

        setDataProperty(new IntEntityData(DATA_VARIANT, GlobalBlockPalette.getOrCreateRuntimeId(ProtocolInfo.CURRENT_PROTOCOL, this.blockId, this.damage)));

        this.fireProof = true;
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

            Vector3 pos = (new Vector3(x - 0.5, y, z - 0.5)).round();
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
                if (block.isTransparent() && !block.canBeReplaced()) {
                    if (this.level.getGameRules().getBoolean(GameRule.DO_ENTITY_DROPS)) {
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
