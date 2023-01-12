/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.block.BlockWater;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.entity.EntityCombustByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockLava
extends BlockLiquid {
    public BlockLava() {
        this(0);
    }

    public BlockLava(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public String getName() {
        return "Lava";
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.highestPosition -= (entity.highestPosition - entity.y) * 0.5;
        EntityCombustByBlockEvent entityCombustByBlockEvent = new EntityCombustByBlockEvent(this, entity, 8);
        Server.getInstance().getPluginManager().callEvent(entityCombustByBlockEvent);
        if (!entityCombustByBlockEvent.isCancelled() && entity.isAlive() && entity.noDamageTicks == 0) {
            entity.setOnFire(entityCombustByBlockEvent.getDuration());
        }
        if (!entity.hasEffect(12)) {
            entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.LAVA, 4.0f));
        }
        super.onEntityCollide(entity);
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        boolean bl = this.getLevel().setBlock(this, this, true, false);
        this.getLevel().scheduleUpdate(this, this.tickRate());
        return bl;
    }

    @Override
    public int onUpdate(int n) {
        int n2;
        block6: {
            n2 = super.onUpdate(n);
            if (n != 2 || !this.level.gameRules.getBoolean(GameRule.DO_FIRE_TICK)) break block6;
            int n3 = Utils.random.nextInt(3);
            if (n3 > 0) {
                for (int k = 0; k < n3; ++k) {
                    Position position = this.add(Utils.random.nextInt(3) - 1, 1.0, Utils.random.nextInt(3) - 1);
                    Block block = this.getLevel().getBlock(position);
                    if (block.getId() == 0) {
                        if (!this.isSurroundingBlockFlammable(block)) continue;
                        BlockIgniteEvent blockIgniteEvent = new BlockIgniteEvent(block, this, null, BlockIgniteEvent.BlockIgniteCause.LAVA);
                        this.level.getServer().getPluginManager().callEvent(blockIgniteEvent);
                        if (!blockIgniteEvent.isCancelled()) {
                            Block block2 = Block.get(51);
                            this.getLevel().setBlock(position, block2, true);
                            this.getLevel().scheduleUpdate(block2, block2.tickRate());
                            return 2;
                        }
                        return 0;
                    }
                    if (!block.isSolid()) continue;
                    return 2;
                }
            } else {
                for (int k = 0; k < 3; ++k) {
                    Position position = this.add(Utils.random.nextInt(3) - 1, 0.0, Utils.random.nextInt(3) - 1);
                    Block block = this.getLevel().getBlock(position);
                    if (block.up().getId() != 0 || block.getBurnChance() <= 0) continue;
                    BlockIgniteEvent blockIgniteEvent = new BlockIgniteEvent(block, this, null, BlockIgniteEvent.BlockIgniteCause.LAVA);
                    this.level.getServer().getPluginManager().callEvent(blockIgniteEvent);
                    if (blockIgniteEvent.isCancelled()) continue;
                    Block block3 = Block.get(51);
                    this.getLevel().setBlock(position, block3, true);
                    this.getLevel().scheduleUpdate(block3, block3.tickRate());
                }
            }
        }
        return n2;
    }

    protected boolean isSurroundingBlockFlammable(Block block) {
        for (BlockFace blockFace : BlockFace.values()) {
            if (block.getSide(blockFace).getBurnChance() <= 0) continue;
            return true;
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.LAVA_BLOCK_COLOR;
    }

    @Override
    public BlockLiquid getBlock(int n) {
        return (BlockLiquid)Block.get(10, n);
    }

    @Override
    public int tickRate() {
        if (this.level.getDimension() == 1) {
            return 10;
        }
        return 30;
    }

    @Override
    public int getFlowDecayPerBlock() {
        if (this.level.getDimension() == 1) {
            return 1;
        }
        return 2;
    }

    @Override
    protected void checkForHarden() {
        Block block = null;
        for (int k = 1; k < 6; ++k) {
            Block block2 = this.getSide(BlockFace.fromIndex(k));
            if (!(block2 instanceof BlockWater)) continue;
            block = block2;
            break;
        }
        if (block != null) {
            if (this.getDamage() == 0) {
                this.liquidCollide(block, Block.get(49));
            } else if (this.getDamage() <= 4) {
                this.liquidCollide(block, Block.get(4));
            }
        }
    }

    @Override
    protected void flowIntoBlock(Block block, int n) {
        if (block instanceof BlockWater) {
            ((BlockLiquid)block).liquidCollide(this, Block.get(1));
        } else {
            super.flowIntoBlock(block, n);
        }
    }

    @Override
    public void addVelocityToEntity(Entity entity, Vector3 vector3) {
        if (!(entity instanceof EntityPrimedTNT)) {
            super.addVelocityToEntity(entity, vector3);
        }
    }

    @Override
    protected boolean[] getOptimalFlowDirections() {
        int n;
        int[] nArray = new int[]{1000, 1000, 1000, 1000};
        int n2 = 4 / this.getFlowDecayPerBlock();
        for (int k = 0; k < 4; ++k) {
            int n3 = (int)this.x;
            n = (int)this.y;
            int n4 = (int)this.z;
            if (k == 0) {
                --n3;
            } else if (k == 1) {
                ++n3;
            } else {
                n4 = k == 2 ? --n4 : ++n4;
            }
            Block block = this.level.getBlock(n3, n, n4);
            if (!this.canFlowInto(block)) {
                this.flowCostVisited.put(Level.blockHash(n3, n, n4), (byte)-1);
                continue;
            }
            if (this.level.getBlock(n3, n - 1, n4).canBeFlowedInto()) {
                this.flowCostVisited.put(Level.blockHash(n3, n, n4), (byte)1);
                n2 = 0;
                nArray[k] = 0;
                continue;
            }
            if (n2 <= 0) continue;
            this.flowCostVisited.put(Level.blockHash(n3, n, n4), (byte)0);
            nArray[k] = this.calculateFlowCost(n3, n, n4, 1, n2, k ^ 1, k ^ 1);
            n2 = Math.min(n2, nArray[k]);
        }
        this.flowCostVisited.clear();
        double d2 = Double.MAX_VALUE;
        for (n = 0; n < 4; ++n) {
            double d3 = nArray[n];
            if (!(d3 < d2)) continue;
            d2 = d3;
        }
        boolean[] blArray = new boolean[4];
        for (int k = 0; k < 4; ++k) {
            blArray[k] = (double)nArray[k] == d2;
        }
        return blArray;
    }
}

