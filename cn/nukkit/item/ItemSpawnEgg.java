/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockMobSpawner;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawner;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.entity.passive.EntityChicken;
import cn.nukkit.entity.passive.EntityCow;
import cn.nukkit.entity.passive.EntityPig;
import cn.nukkit.entity.passive.EntitySheep;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Utils;
import java.util.concurrent.ThreadLocalRandom;

public class ItemSpawnEgg
extends Item {
    public ItemSpawnEgg() {
        this((Integer)0, 1);
    }

    public ItemSpawnEgg(Integer n) {
        this(n, 1);
    }

    public ItemSpawnEgg(Integer n, int n2) {
        super(383, n, n2, "Spawn Entity Egg");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (player.isAdventure()) {
            return false;
        }
        if (!Server.getInstance().spawnEggsEnabled) {
            player.sendMessage("\u00a7cSpawn eggs are disabled on this server");
            return false;
        }
        if (block2 instanceof BlockMobSpawner) {
            BlockEntity blockEntity = level.getBlockEntity(block2);
            if (blockEntity instanceof BlockEntitySpawner) {
                if (((BlockEntitySpawner)blockEntity).getSpawnEntityType() != this.getDamage()) {
                    ((BlockEntitySpawner)blockEntity).setSpawnEntityType(this.getDamage());
                    if (!player.isCreative()) {
                        player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
                    }
                }
            } else {
                if (blockEntity != null) {
                    blockEntity.close();
                }
                CompoundTag compoundTag = new CompoundTag().putString("id", "MobSpawner").putInt("EntityId", this.getDamage()).putInt("x", (int)block2.x).putInt("y", (int)block2.y).putInt("z", (int)block2.z);
                BlockEntity.createBlockEntity("MobSpawner", level.getChunk(block2.getChunkX(), block2.getChunkZ()), compoundTag, new Object[0]);
                if (!player.isCreative()) {
                    player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
                }
            }
            return true;
        }
        BaseFullChunk baseFullChunk = level.getChunk((int)block.getX() >> 4, (int)block.getZ() >> 4);
        if (baseFullChunk == null) {
            return false;
        }
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", block.getX() + 0.5)).add(new DoubleTag("", block2.getBoundingBox() == null ? block.getY() : block2.getBoundingBox().maxY + (double)1.0E-4f)).add(new DoubleTag("", block.getZ() + 0.5))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360.0f)).add(new FloatTag("", 0.0f)));
        if (this.hasCustomName()) {
            compoundTag.putString("CustomName", this.getCustomName());
        }
        CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(this.meta, block, compoundTag, CreatureSpawnEvent.SpawnReason.SPAWN_EGG);
        level.getServer().getPluginManager().callEvent(creatureSpawnEvent);
        if (creatureSpawnEvent.isCancelled()) {
            return false;
        }
        Entity entity = Entity.createEntity(this.meta, (FullChunk)baseFullChunk, compoundTag, new Object[0]);
        if (entity != null) {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            entity.spawnToAll();
            if (Utils.rand(1, 20) == 1 && (entity instanceof EntityCow || entity instanceof EntityChicken || entity instanceof EntityPig || entity instanceof EntitySheep || entity instanceof EntityZombie)) {
                ((BaseEntity)entity).setBaby(true);
            }
            return true;
        }
        return false;
    }
}

