/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAnvil;
import cn.nukkit.block.BlockBeacon;
import cn.nukkit.block.BlockBrewingStand;
import cn.nukkit.block.BlockCactus;
import cn.nukkit.block.BlockCake;
import cn.nukkit.block.BlockCampfire;
import cn.nukkit.block.BlockChest;
import cn.nukkit.block.BlockDaylightDetector;
import cn.nukkit.block.BlockEnchantingTable;
import cn.nukkit.block.BlockEnderChest;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockGlass;
import cn.nukkit.block.BlockHopper;
import cn.nukkit.block.BlockIce;
import cn.nukkit.block.BlockShulkerBox;
import cn.nukkit.block.BlockSlab;
import cn.nukkit.block.BlockSnowLayer;
import cn.nukkit.block.BlockStairs;
import cn.nukkit.block.BlockTNT;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.entity.EntityCombustByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.GameRule;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockFire
extends BlockFlowable {
    public BlockFire() {
        this(0);
    }

    public BlockFire(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 51;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public String getName() {
        return "Fire Block";
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (entity instanceof EntityPotion) {
            if (((EntityPotion)entity).potionId == 0) {
                this.level.setBlock(this, Block.get(0));
            }
            return;
        }
        if (!entity.hasEffect(12) && this.level.getGameRules().getBoolean(GameRule.FIRE_DAMAGE)) {
            entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.FIRE, 1.0f));
        }
        EntityCombustByBlockEvent entityCombustByBlockEvent = new EntityCombustByBlockEvent(this, entity, 8);
        if (entity instanceof EntityArrow) {
            entityCombustByBlockEvent.setCancelled();
        }
        Server.getInstance().getPluginManager().callEvent(entityCombustByBlockEvent);
        if (!entityCombustByBlockEvent.isCancelled() && entity.isAlive() && entity.noDamageTicks == 0) {
            entity.setOnFire(entityCombustByBlockEvent.getDuration());
        }
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1 || n == 2) {
            if (!this.isBlockTopFacingSurfaceSolid(this.down()) && !this.canNeighborBurn()) {
                this.getLevel().setBlock(this, Block.get(0), true);
            } else if (!Server.getInstance().suomiCraftPEMode() && this.level.gameRules.getBoolean(GameRule.DO_FIRE_TICK) && !this.level.isUpdateScheduled(this, this)) {
                this.level.scheduleUpdate(this, this.tickRate());
            }
            return 1;
        }
        if (n == 3 && this.level.gameRules.getBoolean(GameRule.DO_FIRE_TICK)) {
            int n2;
            boolean bl;
            Block block = this.down();
            boolean bl2 = bl = this.getId() == 492 || block.getId() == 87 || block.getId() == 213 || block.getId() == 7 && this.level.getDimension() == 2;
            if (!bl && this.getLevel().isRaining() && (this.getLevel().canBlockSeeSky(this) || this.getLevel().canBlockSeeSky(this.east()) || this.getLevel().canBlockSeeSky(this.west()) || this.getLevel().canBlockSeeSky(this.south()) || this.getLevel().canBlockSeeSky(this.north()))) {
                this.getLevel().setBlock(this, Block.get(0), true);
            }
            if (Server.getInstance().suomiCraftPEMode()) {
                if (bl) {
                    return 0;
                }
                this.getLevel().setBlock(this, Block.get(0), true);
                return 0;
            }
            if (!this.isBlockTopFacingSurfaceSolid(block) && !this.canNeighborBurn()) {
                this.getLevel().setBlock(this, Block.get(0), true);
                return 0;
            }
            int n3 = this.getDamage();
            if (n3 < 15) {
                n2 = n3 + Utils.random.nextInt(3);
                if (n2 > 15) {
                    n2 = 15;
                }
                this.setDamage(n2);
                this.getLevel().setBlock(this, this, true);
            }
            this.getLevel().scheduleUpdate(this, this.tickRate() + Utils.random.nextInt(10));
            if (!bl && !this.canNeighborBurn()) {
                if (!this.isBlockTopFacingSurfaceSolid(this.down()) || n3 > 3) {
                    this.getLevel().setBlock(this, Block.get(0), true);
                }
            } else if (!bl && this.down().getBurnAbility() <= 0 && n3 == 15 && Utils.random.nextInt(4) == 0) {
                this.getLevel().setBlock(this, Block.get(0), true);
            } else {
                n2 = 0;
                this.a(this.east(), 300 + n2, n3);
                this.a(this.west(), 300 + n2, n3);
                this.a(this.down(), 250 + n2, n3);
                this.a(this.up(), 250 + n2, n3);
                this.a(this.south(), 300 + n2, n3);
                this.a(this.north(), 300 + n2, n3);
                for (int k = (int)(this.x - 1.0); k <= (int)(this.x + 1.0); ++k) {
                    for (int i2 = (int)(this.z - 1.0); i2 <= (int)(this.z + 1.0); ++i2) {
                        for (int i3 = (int)(this.y - 1.0); i3 <= (int)(this.y + 4.0); ++i3) {
                            int n4;
                            Block block2;
                            int n5;
                            if (k == (int)this.x && i3 == (int)this.y && i2 == (int)this.z) continue;
                            int n6 = 100;
                            if ((double)i3 > this.y + 1.0) {
                                n6 = (int)((double)n6 + ((double)i3 - (this.y + 1.0)) * 100.0);
                            }
                            if ((n5 = BlockFire.a(block2 = this.getLevel().getBlock(k, i3, i2))) <= 0 || (n4 = (n5 + 40 + this.getLevel().getServer().getDifficulty() * 7) / (n3 + 30)) <= 0 || Utils.random.nextInt(n6) > n4) continue;
                            int n7 = n3 + (Utils.random.nextInt(5) >> 2);
                            if (n7 > 15) {
                                n7 = 15;
                            }
                            BlockIgniteEvent blockIgniteEvent = new BlockIgniteEvent(block2, this, null, BlockIgniteEvent.BlockIgniteCause.SPREAD);
                            this.level.getServer().getPluginManager().callEvent(blockIgniteEvent);
                            if (blockIgniteEvent.isCancelled()) continue;
                            this.getLevel().setBlock(block2, Block.get(51, n7), true);
                            this.getLevel().scheduleUpdate(block2, this.tickRate());
                        }
                    }
                }
            }
        }
        return 0;
    }

    private void a(Block block, int n, int n2) {
        if (Utils.random.nextInt(n) < block.getBurnAbility()) {
            if (Utils.random.nextInt(n2 + 10) < 5) {
                int n3 = n2 + (Utils.random.nextInt(5) >> 2);
                if (n3 > 15) {
                    n3 = 15;
                }
                BlockIgniteEvent blockIgniteEvent = new BlockIgniteEvent(block, this, null, BlockIgniteEvent.BlockIgniteCause.SPREAD);
                this.level.getServer().getPluginManager().callEvent(blockIgniteEvent);
                if (!blockIgniteEvent.isCancelled()) {
                    this.getLevel().setBlock(block, Block.get(51, n3), true);
                    this.getLevel().scheduleUpdate(block, this.tickRate());
                }
            } else {
                BlockBurnEvent blockBurnEvent = new BlockBurnEvent(block);
                this.getLevel().getServer().getPluginManager().callEvent(blockBurnEvent);
                if (!blockBurnEvent.isCancelled()) {
                    this.getLevel().setBlock(block, Block.get(0), true);
                }
            }
            if (block instanceof BlockTNT) {
                ((BlockTNT)block).prime();
            }
        }
    }

    private static int a(Block block) {
        if (block.getId() != 0) {
            return 0;
        }
        int n = 0;
        n = Math.max(n, block.east().getBurnChance());
        n = Math.max(n, block.west().getBurnChance());
        n = Math.max(n, block.down().getBurnChance());
        n = Math.max(n, block.up().getBurnChance());
        n = Math.max(n, block.south().getBurnChance());
        n = Math.max(n, block.north().getBurnChance());
        return n;
    }

    public boolean canNeighborBurn() {
        for (BlockFace blockFace : BlockFace.values()) {
            if (this.getSide(blockFace).getBurnChance() <= 0) continue;
            return true;
        }
        return false;
    }

    public boolean isBlockTopFacingSurfaceSolid(Block block) {
        if (block != null) {
            if (block instanceof BlockStairs && (block.getDamage() & 4) == 4) {
                return true;
            }
            if (block instanceof BlockSlab && (this.getDamage() & 8) > 0) {
                return true;
            }
            if (block instanceof BlockSnowLayer && (block.getDamage() & 7) == 7) {
                return true;
            }
            if (block instanceof BlockGlass) {
                return false;
            }
            if (block instanceof BlockHopper || block instanceof BlockBeacon) {
                return false;
            }
            if (block instanceof BlockShulkerBox || block instanceof BlockChest || block instanceof BlockEnderChest) {
                return false;
            }
            if (block instanceof BlockAnvil || block instanceof BlockEnchantingTable || block instanceof BlockBrewingStand) {
                return false;
            }
            if (block instanceof BlockCampfire) {
                return false;
            }
            if (block instanceof BlockCactus) {
                return false;
            }
            if (block instanceof BlockDaylightDetector) {
                return false;
            }
            if (block instanceof BlockIce) {
                return false;
            }
            if (block instanceof BlockCake) {
                return false;
            }
            return block.isSolid();
        }
        return false;
    }

    @Override
    public int tickRate() {
        return 30;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(0));
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

