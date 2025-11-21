package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockMobSpawner;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawner;
import cn.nukkit.entity.BaseEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityChestBoat;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.passive.*;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class ItemSpawnEgg extends Item {

    public ItemSpawnEgg() {
        this(0, 1);
    }

    public ItemSpawnEgg(Integer meta) {
        this(meta, 1);
    }

    public ItemSpawnEgg(Integer meta, int count) {
        super(SPAWN_EGG, meta, count, "Spawn Entity Egg");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean isSupportedOn(int protocol) { // Copied from AddEntityPacket getEntityIdentifier
        int type = getDamage();

        if (protocol < ProtocolInfo.v1_21_100) {
            if (type == EntityCopperGolem.NETWORK_ID) {
                return false;
            }

            if (protocol < ProtocolInfo.v1_21_90) {
                if (type == EntityHappyGhast.NETWORK_ID) {
                    return false;
                }

                if (protocol < ProtocolInfo.v1_21_50) {
                    if (type == EntityCreaking.NETWORK_ID) {
                        return false;
                    } else if (type == 145) { // ominous_item_spawner
                        return false;
                    } else if (type == 143 || type == 141) { // wind_charge_projectile || breeze_wind_charge_projectile
                        return false;
                    }

                    if (protocol < ProtocolInfo.v1_21_0) {
                        if (type == EntityBogged.NETWORK_ID) {
                            return false;
                        } else if (type == EntityBreeze.NETWORK_ID) {
                            return false;
                        }

                        if (protocol < ProtocolInfo.v1_20_80) {
                            if (type == EntityArmadillo.NETWORK_ID) {
                                return false;
                            }

                            if (protocol < ProtocolInfo.v1_20_0_23) {
                                if (type == EntitySniffer.NETWORK_ID) {
                                    return false;
                                } else if (type == EntityCamel.NETWORK_ID) {
                                    return false;
                                }

                                if (protocol < ProtocolInfo.v1_19_0_29) {
                                    if (type == EntityChestBoat.NETWORK_ID) {
                                        return false;
                                    } else if (type == EntityAllay.NETWORK_ID) {
                                        return false;
                                    } else if (type == EntityWarden.NETWORK_ID) {
                                        return false;
                                    } else if (type == EntityTadpole.NETWORK_ID) {
                                        return false;
                                    } else if (type == EntityFrog.NETWORK_ID) {
                                        return false;
                                    }

                                    if (protocol < ProtocolInfo.v1_17_0) {
                                        if (type == EntityGoat.NETWORK_ID) {
                                            return false;
                                        } else if (type == EntityAxolotl.NETWORK_ID) {
                                            return false;
                                        } else if (type == EntityGlowSquid.NETWORK_ID) {
                                            return false;
                                        }

                                        if (protocol < ProtocolInfo.v1_16_0) {
                                            if (type == EntityPiglin.NETWORK_ID || type == EntityPiglinBrute.NETWORK_ID) {
                                                return false;
                                            } else if (type == EntityHoglin.NETWORK_ID || type == EntityStrider.NETWORK_ID || type == EntityZoglin.NETWORK_ID) {
                                                return false;
                                            }

                                            if (protocol < ProtocolInfo.v1_14_0) {
                                                if (type == EntityBee.NETWORK_ID) {
                                                    return false;
                                                }

                                                if (type == EntityFox.NETWORK_ID && protocol < ProtocolInfo.v1_13_0) {
                                                    return false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        if (player.isAdventure()) {
            return false;
        }

        if (!player.getServer().spawnEggsEnabled) {
            player.sendMessage(TextFormat.RED + "Spawn eggs are disabled on this server");
            return false;
        }

        if (target instanceof BlockMobSpawner) {
            BlockEntity blockEntity = level.getBlockEntity(target);
            if (blockEntity instanceof BlockEntitySpawner) {
                if (((BlockEntitySpawner) blockEntity).getSpawnEntityType() != this.getDamage()) {
                    ((BlockEntitySpawner) blockEntity).setSpawnEntityType(this.getDamage());

                    if (!player.isCreative()) {
                        this.count--;
                    }
                }
            } else {
                if (blockEntity != null) {
                    blockEntity.close();
                }

                CompoundTag nbt = new CompoundTag()
                        .putString("id", BlockEntity.MOB_SPAWNER)
                        .putInt("EntityId", this.getDamage())
                        .putInt("x", (int) target.x)
                        .putInt("y", (int) target.y)
                        .putInt("z", (int) target.z);
                BlockEntitySpawner spawner = (BlockEntitySpawner) BlockEntity.createBlockEntity(BlockEntity.MOB_SPAWNER, level.getChunk(target.getChunkX(), target.getChunkZ()), nbt);
                spawner.spawnToAll();

                if (!player.isCreative()) {
                    this.count--;
                }
            }

            return true;
        }

        FullChunk chunk = level.getChunk((int) block.getX() >> 4, (int) block.getZ() >> 4);

        if (chunk == null) {
            return false;
        }

        CompoundTag nbt = new CompoundTag()
                .putBoolean("Persistent", true)
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", block.getX() + 0.5))
                        .add(new DoubleTag("", target.getBoundingBox() == null ? block.getY() : target.getBoundingBox().getMaxY() + 0.0001f))
                        .add(new DoubleTag("", block.getZ() + 0.5)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360))
                        .add(new FloatTag("", 0)));

        if (this.hasCustomName()) {
            nbt.putString("CustomName", this.getCustomName());
        }

        CreatureSpawnEvent ev = new CreatureSpawnEvent(this.meta, block, nbt, CreatureSpawnEvent.SpawnReason.SPAWN_EGG);
        level.getServer().getPluginManager().callEvent(ev);

        if (ev.isCancelled()) {
            return false;
        }

        Entity entity = Entity.createEntity(this.meta, chunk, nbt);

        if (entity != null) {
            if (!player.isCreative()) {
                this.count--;
            }

            entity.spawnToAll();

            if (Utils.rand(1, 20) == 1 &&
                    (entity instanceof EntityCow ||
                            entity instanceof EntityChicken ||
                            entity instanceof EntityPig ||
                            entity instanceof EntitySheep ||
                            entity instanceof EntityZombie)) {

                ((BaseEntity) entity).setBaby(true);
            }

            return true;
        }
        return false;
    }
}
