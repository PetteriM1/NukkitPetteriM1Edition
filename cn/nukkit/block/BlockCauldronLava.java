/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCauldron;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityCauldron;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityCombustByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerBucketFillEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public class BlockCauldronLava
extends BlockCauldron {
    public BlockCauldronLava() {
        this(8);
    }

    public BlockCauldronLava(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Lava Cauldron";
    }

    @Override
    public int getId() {
        return 465;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0).shrink(0.3, 0.3, 0.3);
    }

    @Override
    public void setFillLevel(int n) {
        super.setFillLevel(n);
        this.setDamage(this.getDamage() | 8);
    }

    @Override
    public void onEntityCollide(Entity entity) {
        EntityCombustByBlockEvent entityCombustByBlockEvent = new EntityCombustByBlockEvent(this, entity, 8);
        Server.getInstance().getPluginManager().callEvent(entityCombustByBlockEvent);
        if (!entityCombustByBlockEvent.isCancelled() && entity.isAlive() && entity.noDamageTicks == 0) {
            entity.setOnFire(entityCombustByBlockEvent.getDuration());
        }
        if (!entity.hasEffect(12)) {
            entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.LAVA, 4.0f));
        }
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 325 && item.getDamage() == 0) {
            if (!this.isFull()) {
                return false;
            }
            PlayerBucketFillEvent playerBucketFillEvent = new PlayerBucketFillEvent(player, this, null, item, Item.get(325, 10, 1));
            this.level.getServer().getPluginManager().callEvent(playerBucketFillEvent);
            if (!playerBucketFillEvent.isCancelled()) {
                this.replaceBucket(item, player, playerBucketFillEvent.getItem());
                if (!(this.level.getBlockEntity(this) instanceof BlockEntityCauldron)) {
                    BlockEntity.createBlockEntity("Cauldron", this.getChunk(), new CompoundTag("").putString("id", "Cauldron").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putShort("PotionId", 65535).putByte("SplashPotion", 0), new Object[0]);
                }
                this.level.setBlock(this, Block.get(118), true);
                this.getLevel().addSound((Vector3)this.add(0.5, 1.0, 0.5), Sound.BUCKET_FILL_LAVA);
            }
        }
        this.level.updateComparatorOutputLevel(this);
        return true;
    }

    @Override
    public boolean isFull() {
        return this.getDamage() == 14;
    }

    @Override
    public int onUpdate(int n) {
        return 0;
    }
}

