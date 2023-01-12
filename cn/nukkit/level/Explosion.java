/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTNT;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.item.EntityXPOrb;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.HugeExplodeSeedParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Hash;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import java.util.ArrayList;
import java.util.List;

public class Explosion {
    private static final int h = 16;
    private final Level d;
    private final Position b;
    private final double f;
    private List<Block> c = new ArrayList<Block>();
    private static final double a = 0.3;
    private final Object g;
    private boolean e = true;

    public Explosion(Position position, double d2, Entity entity) {
        this.d = position.getLevel();
        this.b = position;
        this.f = Math.max(d2, 0.0);
        this.g = entity;
    }

    public boolean explode() {
        if (this.explodeA()) {
            return this.explodeB();
        }
        return false;
    }

    public boolean explodeA() {
        if (this.g instanceof EntityExplosive && ((Entity)this.g).isInsideOfWater()) {
            this.e = false;
            return true;
        }
        if (this.f < 0.1) {
            return false;
        }
        if (!this.d.getServer().explosionBreakBlocks) {
            return true;
        }
        Vector3 vector3 = new Vector3(0.0, 0.0, 0.0);
        Vector3 vector32 = new Vector3(0.0, 0.0, 0.0);
        int n = 15;
        for (int k = 0; k < 16; ++k) {
            for (int i2 = 0; i2 < 16; ++i2) {
                block2: for (int i3 = 0; i3 < 16; ++i3) {
                    if (k != 0 && k != n && i2 != 0 && i2 != n && i3 != 0 && i3 != n) continue;
                    vector3.setComponents((double)k / (double)n * 2.0 - 1.0, (double)i2 / (double)n * 2.0 - 1.0, (double)i3 / (double)n * 2.0 - 1.0);
                    double d2 = vector3.length();
                    vector3.setComponents(vector3.x / d2 * 0.3, vector3.y / d2 * 0.3, vector3.z / d2 * 0.3);
                    double d3 = this.b.x;
                    double d4 = this.b.y;
                    double d5 = this.b.z;
                    for (double d6 = this.f * (double)Utils.random.nextInt(700, 1301) / 1000.0; d6 > 0.0; d6 -= 0.22499999999999998) {
                        int n2 = (int)d3;
                        int n3 = (int)d4;
                        int n4 = (int)d5;
                        vector32.x = d3 >= (double)n2 ? (double)n2 : (double)(n2 - 1);
                        vector32.y = d4 >= (double)n3 ? (double)n3 : (double)(n3 - 1);
                        double d7 = vector32.z = d5 >= (double)n4 ? (double)n4 : (double)(n4 - 1);
                        if (vector32.y < 0.0 || vector32.y > 255.0) continue block2;
                        Block block = this.d.getBlock(vector32);
                        if (block.getId() != 0 && (d6 -= (block.getResistance() / 5.0 + 0.3) * 0.3) > 0.0 && !this.c.contains(block)) {
                            this.c.add(block);
                        }
                        d3 += vector3.x;
                        d4 += vector3.y;
                        d5 += vector3.z;
                    }
                }
            }
        }
        return true;
    }

    public boolean explodeB() {
        Entity[] entityArray;
        LongArraySet longArraySet = new LongArraySet();
        double d2 = 1.0 / this.f * 100.0;
        if (this.g instanceof Entity) {
            EntityExplodeEvent entityExplodeEvent = new EntityExplodeEvent((Entity)this.g, this.b, this.c, d2);
            this.d.getServer().getPluginManager().callEvent(entityExplodeEvent);
            if (entityExplodeEvent.isCancelled()) {
                return false;
            }
            d2 = entityExplodeEvent.getYield();
            this.c = entityExplodeEvent.getBlockList();
        }
        double d3 = this.f * 2.0;
        double d4 = NukkitMath.floorDouble(this.b.x - d3 - 1.0);
        double d5 = NukkitMath.ceilDouble(this.b.x + d3 + 1.0);
        double d6 = NukkitMath.floorDouble(this.b.y - d3 - 1.0);
        double d7 = NukkitMath.ceilDouble(this.b.y + d3 + 1.0);
        double d8 = NukkitMath.floorDouble(this.b.z - d3 - 1.0);
        double d9 = NukkitMath.ceilDouble(this.b.z + d3 + 1.0);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d4, d6, d8, d5, d7, d9);
        for (Entity entity : entityArray = this.d.getNearbyEntities(axisAlignedBB, this.g instanceof Entity ? (Entity)this.g : null)) {
            int n;
            double d10 = entity.distance(this.b) / d3;
            if (!(d10 <= 1.0)) continue;
            Vector3 vector3 = entity.subtract(this.b).normalize();
            double d11 = this.getSeenPercent(this.b, entity);
            double d12 = (1.0 - d10) * d11;
            int n2 = n = this.e ? Math.max((int)((d12 * d12 + d12) / 2.0 * 8.0 * d3 + 1.0), 0) : 0;
            if (this.g instanceof Entity) {
                entity.attack(new EntityDamageByEntityEvent((Entity)this.g, entity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, n));
            } else if (this.g instanceof Block) {
                entity.attack(new EntityDamageByBlockEvent((Block)this.g, entity, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, n));
            } else {
                entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, n));
            }
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
            if (entity instanceof Player) {
                int n3 = 0;
                for (Item item : ((Player)entity).getInventory().getArmorContents()) {
                    if (item.getTier() != 6) continue;
                    ++n3;
                }
                if (n3 > 0) {
                    d12 *= 1.0 - 0.225 * (double)n3;
                }
            }
            entity.setMotion(vector3.multiply(d12));
        }
        Item item = Item.get(0);
        for (Block block : this.c) {
            if (block.getId() == 46) {
                ((BlockTNT)block).prime(Utils.rand(10, 30), this.g instanceof Entity ? (Entity)this.g : null);
            } else {
                if (block.getId() == 26 && (block.getDamage() & 8) == 8) {
                    this.d.setBlockAt((int)block.x, (int)block.y, (int)block.z, 0);
                    continue;
                }
                BlockEntity blockEntity = this.d.getBlockEntity(block);
                if (blockEntity instanceof InventoryHolder && !blockEntity.closed) {
                    if (this.d.getGameRules().getBoolean(GameRule.DO_TILE_DROPS)) {
                        Inventory inventory = ((InventoryHolder)((Object)blockEntity)).getInventory();
                        if (inventory != null) {
                            inventory.getViewers().clear();
                        }
                        if (blockEntity instanceof BlockEntityShulkerBox) {
                            this.d.dropItem(block.add(0.5, 0.5, 0.5), block.toItem());
                            ((InventoryHolder)((Object)blockEntity)).getInventory().clearAll();
                        } else {
                            for (Item item2 : ((InventoryHolder)((Object)blockEntity)).getInventory().getContents().values()) {
                                this.d.dropItem(block.add(0.5, 0.5, 0.5), item2);
                            }
                            ((InventoryHolder)((Object)blockEntity)).getInventory().clearAll();
                        }
                    }
                    blockEntity.close();
                } else if (Math.random() * 100.0 < d2) {
                    if (this.d.getBlockIdAt((int)block.x, (int)block.y, (int)block.z) == 0) continue;
                    for (Item item3 : block.getDrops(item)) {
                        this.d.dropItem(block.add(0.5, 0.5, 0.5), item3);
                    }
                }
            }
            this.d.setBlockAt((int)block.x, (int)block.y, (int)block.z, 0);
            Vector3 vector3 = new Vector3(block.x, block.y, block.z);
            block5: for (BlockFace blockFace : BlockFace.values()) {
                Vector3 vector32 = vector3.getSide(blockFace);
                long l = Hash.hashBlock((int)vector32.x, (int)vector32.y, (int)vector32.z);
                if (longArraySet.contains(l)) continue;
                for (Block block2 : this.c) {
                    if (block2.x != vector32.x || block2.y != vector32.y || block2.z != vector32.z) continue;
                    continue block5;
                }
                BlockUpdateEvent blockUpdateEvent = new BlockUpdateEvent(this.d.getBlock(vector32));
                this.d.getServer().getPluginManager().callEvent(blockUpdateEvent);
                if (!blockUpdateEvent.isCancelled()) {
                    blockUpdateEvent.getBlock().onUpdate(1);
                }
                longArraySet.add(l);
                this.d.antiXrayOnBlockChange(block);
            }
        }
        this.d.addParticle(new HugeExplodeSeedParticle(this.b));
        this.d.addLevelSoundEvent(this.b, 48);
        return true;
    }

    public double getSeenPercent(Vector3 vector3, Entity entity) {
        AxisAlignedBB axisAlignedBB = entity.getBoundingBox();
        if (axisAlignedBB.isVectorInside(vector3)) {
            return 1.0;
        }
        double d2 = 1.0 / ((axisAlignedBB.getMaxX() - axisAlignedBB.getMinX()) * 2.0 + 1.0);
        double d3 = 1.0 / ((axisAlignedBB.getMaxY() - axisAlignedBB.getMinY()) * 2.0 + 1.0);
        double d4 = 1.0 / ((axisAlignedBB.getMaxZ() - axisAlignedBB.getMinZ()) * 2.0 + 1.0);
        double d5 = (1.0 - Math.floor(1.0 / d2) * d2) / 2.0;
        double d6 = (1.0 - Math.floor(1.0 / d4) * d4) / 2.0;
        int n = 0;
        int n2 = 0;
        for (double d7 = 0.0; d7 <= 1.0; d7 += d2) {
            for (double d8 = 0.0; d8 <= 1.0; d8 += d3) {
                for (double d9 = 0.0; d9 <= 1.0; d9 += d4) {
                    Vector3 vector32 = new Vector3(axisAlignedBB.getMinX() + d7 * (axisAlignedBB.getMaxX() - axisAlignedBB.getMinX()) + d5, axisAlignedBB.getMinY() + d8 * (axisAlignedBB.getMaxY() - axisAlignedBB.getMinY()), axisAlignedBB.getMinZ() + d9 * (axisAlignedBB.getMaxZ() - axisAlignedBB.getMinZ()) + d6);
                    if (this.a(vector3, vector32) == null) {
                        ++n;
                    }
                    ++n2;
                }
            }
        }
        return n2 != 0 ? (double)n / (double)n2 : 0.0;
    }

    private MovingObjectPosition a(Vector3 vector3, Vector3 vector32) {
        Vector3 vector33 = vector3;
        Vector3 vector34 = vector32.subtract(vector3).normalize();
        double d2 = Explosion.a(vector34.getX());
        double d3 = Explosion.a(vector34.getY());
        double d4 = Explosion.a(vector34.getZ());
        double d5 = Explosion.a(vector3.getX(), vector34.getX());
        double d6 = Explosion.a(vector3.getY(), vector34.getY());
        double d7 = Explosion.a(vector3.getZ(), vector34.getZ());
        double d8 = vector34.getX() == 0.0 ? 0.0 : d2 / vector34.getX();
        double d9 = vector34.getY() == 0.0 ? 0.0 : d3 / vector34.getY();
        double d10 = vector34.getZ() == 0.0 ? 0.0 : d4 / vector34.getZ();
        double d11 = vector3.distance(vector32);
        while (true) {
            Block block;
            MovingObjectPosition movingObjectPosition;
            if ((movingObjectPosition = (block = this.d.getBlock(vector33)).calculateIntercept(vector3, vector32)) != null && block.isNormalBlock()) {
                return movingObjectPosition;
            }
            if (d5 < d6 && d5 < d7) {
                if (d5 > d11) break;
                vector33 = vector33.add(d2, 0.0, 0.0);
                d5 += d8;
                continue;
            }
            if (d6 < d7) {
                if (d6 > d11) break;
                vector33 = vector33.add(0.0, d3, 0.0);
                d6 += d9;
                continue;
            }
            if (d7 > d11) break;
            vector33 = vector33.add(0.0, 0.0, d4);
            d7 += d10;
        }
        return null;
    }

    private static double a(double d2) {
        if (d2 > 0.0) {
            return 1.0;
        }
        if (d2 < 0.0) {
            return -1.0;
        }
        return 0.0;
    }

    private static double a(double d2, double d3) {
        if (d3 == 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        if (d3 < 0.0) {
            d2 = -d2;
            d3 = -d3;
            if (Math.floor(d2) == d2) {
                return 0.0;
            }
        }
        return (1.0 - (d2 - Math.floor(d2))) / d3;
    }
}

