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
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
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

public class StrongExplosion
extends Explosion {
    private final Level l;
    private final Position j;
    private final double k;
    private final Object m;
    private boolean n = true;
    private List<Block> i = new ArrayList<Block>();

    public StrongExplosion(Position position, double d2, Entity entity) {
        super(position, d2, entity);
        this.l = position.getLevel();
        this.j = position;
        this.k = Math.max(d2, 0.0);
        this.m = entity;
    }

    @Override
    public boolean explodeA() {
        if (this.m instanceof EntityExplosive && ((Entity)this.m).isInsideOfWater()) {
            this.n = false;
            return true;
        }
        if (this.k < 0.1) {
            return false;
        }
        if (!this.l.getServer().explosionBreakBlocks) {
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
                    double d3 = this.j.x;
                    double d4 = this.j.y;
                    double d5 = this.j.z;
                    for (double d6 = this.k * (double)Utils.random.nextInt(700, 1301) / 1000.0; d6 > 0.0; d6 -= 0.22499999999999998) {
                        int n2 = (int)d3;
                        int n3 = (int)d4;
                        int n4 = (int)d5;
                        vector32.x = d3 >= (double)n2 ? (double)n2 : (double)(n2 - 1);
                        vector32.y = d4 >= (double)n3 ? (double)n3 : (double)(n3 - 1);
                        double d7 = vector32.z = d5 >= (double)n4 ? (double)n4 : (double)(n4 - 1);
                        if (vector32.y < 0.0 || vector32.y > 255.0) continue block2;
                        Block block = this.l.getBlock(vector32);
                        if (block.getId() != 0 && block.getId() != 7) {
                            if (block.getId() != 49) {
                                d6 -= (block.getResistance() / 5.0 + 0.3) * 0.3;
                            }
                            if (d6 > 0.0 && !this.i.contains(block)) {
                                this.i.add(block);
                            }
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

    @Override
    public boolean explodeB() {
        Entity[] entityArray;
        LongArraySet longArraySet = new LongArraySet();
        double d2 = 1.0 / this.k * 100.0;
        if (this.m instanceof Entity) {
            EntityExplodeEvent entityExplodeEvent = new EntityExplodeEvent((Entity)this.m, this.j, this.i, d2);
            this.l.getServer().getPluginManager().callEvent(entityExplodeEvent);
            if (entityExplodeEvent.isCancelled()) {
                return false;
            }
            d2 = entityExplodeEvent.getYield();
            this.i = entityExplodeEvent.getBlockList();
        }
        double d3 = this.k * 2.0;
        double d4 = NukkitMath.floorDouble(this.j.x - d3 - 1.0);
        double d5 = NukkitMath.ceilDouble(this.j.x + d3 + 1.0);
        double d6 = NukkitMath.floorDouble(this.j.y - d3 - 1.0);
        double d7 = NukkitMath.ceilDouble(this.j.y + d3 + 1.0);
        double d8 = NukkitMath.floorDouble(this.j.z - d3 - 1.0);
        double d9 = NukkitMath.ceilDouble(this.j.z + d3 + 1.0);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d4, d6, d8, d5, d7, d9);
        for (Entity entity : entityArray = this.l.getNearbyEntities(axisAlignedBB, this.m instanceof Entity ? (Entity)this.m : null)) {
            int n;
            double d10 = entity.distance(this.j) / d3;
            if (!(d10 <= 1.0)) continue;
            Vector3 vector3 = entity.subtract(this.j).normalize();
            int n2 = 1;
            double d11 = (1.0 - d10) * (double)n2;
            int n3 = n = this.n ? (int)((d11 * d11 + d11) / 2.0 * 8.0 * d3 + 1.0) : 0;
            if (this.m instanceof Entity) {
                entity.attack(new EntityDamageByEntityEvent((Entity)this.m, entity, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, n));
            } else if (this.m instanceof Block) {
                entity.attack(new EntityDamageByBlockEvent((Block)this.m, entity, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, n));
            } else {
                entity.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, n));
            }
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
            if (entity instanceof Player) {
                int n4 = 0;
                for (Item item : ((Player)entity).getInventory().getArmorContents()) {
                    if (item.getTier() != 6) continue;
                    ++n4;
                }
                if (n4 > 0) {
                    d11 *= 1.0 - 0.225 * (double)n4;
                }
            }
            entity.setMotion(vector3.multiply(d11));
        }
        Item item = Item.get(0);
        for (Block block : this.i) {
            if (block.getId() == 46) {
                ((BlockTNT)block).prime(Utils.rand(10, 30), this.m instanceof Entity ? (Entity)this.m : null);
            } else {
                if (block.getId() == 26 && (block.getDamage() & 8) == 8) {
                    this.l.setBlockAt((int)block.x, (int)block.y, (int)block.z, 0);
                    continue;
                }
                BlockEntity blockEntity = this.l.getBlockEntity(block);
                if (blockEntity instanceof InventoryHolder && !blockEntity.closed) {
                    if (this.l.getGameRules().getBoolean(GameRule.DO_TILE_DROPS)) {
                        Inventory inventory = ((InventoryHolder)((Object)blockEntity)).getInventory();
                        if (inventory != null) {
                            inventory.getViewers().clear();
                        }
                        if (blockEntity instanceof BlockEntityShulkerBox) {
                            this.l.dropItem(block.add(0.5, 0.5, 0.5), block.toItem());
                            ((InventoryHolder)((Object)blockEntity)).getInventory().clearAll();
                        } else {
                            for (Item item2 : ((InventoryHolder)((Object)blockEntity)).getInventory().getContents().values()) {
                                this.l.dropItem(block.add(0.5, 0.5, 0.5), item2);
                            }
                            ((InventoryHolder)((Object)blockEntity)).getInventory().clearAll();
                        }
                    }
                    blockEntity.close();
                } else if (Math.random() * 100.0 < d2 && block.getId() != 49) {
                    if (this.l.getBlockIdAt((int)block.x, (int)block.y, (int)block.z) == 0) continue;
                    for (Item item3 : block.getDrops(item)) {
                        this.l.dropItem(block.add(0.5, 0.5, 0.5), item3);
                    }
                }
            }
            this.l.setBlockAt((int)block.x, (int)block.y, (int)block.z, 0);
            Vector3 vector3 = new Vector3(block.x, block.y, block.z);
            block5: for (BlockFace blockFace : BlockFace.values()) {
                Vector3 vector32 = vector3.getSide(blockFace);
                long l = Hash.hashBlock((int)vector32.x, (int)vector32.y, (int)vector32.z);
                if (longArraySet.contains(l)) continue;
                for (Block block2 : this.i) {
                    if (block2.x != vector32.x || block2.y != vector32.y || block2.z != vector32.z) continue;
                    continue block5;
                }
                BlockUpdateEvent blockUpdateEvent = new BlockUpdateEvent(this.l.getBlock(vector32));
                this.l.getServer().getPluginManager().callEvent(blockUpdateEvent);
                if (!blockUpdateEvent.isCancelled()) {
                    blockUpdateEvent.getBlock().onUpdate(1);
                }
                longArraySet.add(l);
                this.l.antiXrayOnBlockChange(block);
            }
        }
        this.l.addParticle(new HugeExplodeSeedParticle(this.j));
        this.l.addLevelSoundEvent(this.j, 48);
        return true;
    }
}

