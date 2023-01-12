/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.BlockWater;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.block.BlockFromToEvent;
import cn.nukkit.event.block.LiquidFlowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;

public abstract class BlockLiquid
extends BlockTransparentMeta {
    protected static final byte CAN_FLOW_DOWN = 1;
    protected static final byte CAN_FLOW = 0;
    protected static final byte BLOCKED = -1;
    public int adjacentSources = 0;
    protected Vector3 flowVector = null;
    protected Long2ByteMap flowCostVisited = new Long2ByteOpenHashMap();

    protected BlockLiquid(int n) {
        super(n);
    }

    @Override
    public boolean canBeFlowedInto() {
        return true;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return null;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
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
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 0.9, this.z + 1.0);
    }

    public float getFluidHeightPercent() {
        float f2 = this.getDamage();
        if (f2 >= 8.0f) {
            f2 = 0.0f;
        }
        return (f2 + 1.0f) / 9.0f;
    }

    protected int getFlowDecay(Block block) {
        if (block.getId() != this.getId()) {
            return -1;
        }
        return block.getDamage();
    }

    protected int getEffectiveFlowDecay(Block block) {
        if (block.getId() != this.getId()) {
            return -1;
        }
        int n = block.getDamage();
        if (n >= 8) {
            n = 0;
        }
        return n;
    }

    public void clearCaches() {
        this.flowVector = null;
        this.flowCostVisited.clear();
    }

    public Vector3 getFlowVector() {
        FullChunk fullChunk;
        if (this.flowVector != null) {
            return this.flowVector;
        }
        Vector3 vector3 = new Vector3(0.0, 0.0, 0.0);
        int n = this.getEffectiveFlowDecay(this);
        for (int k = 0; k < 4; ++k) {
            int n2;
            int n3 = (int)this.x;
            int n4 = (int)this.y;
            int n5 = (int)this.z;
            switch (k) {
                case 0: {
                    --n3;
                    break;
                }
                case 1: {
                    ++n3;
                    break;
                }
                case 2: {
                    --n5;
                    break;
                }
                default: {
                    ++n5;
                }
            }
            BaseFullChunk baseFullChunk = this.level.getChunk(n3 >> 4, n5 >> 4);
            Block block = this.level.getBlock(baseFullChunk, n3, n4, n5, true);
            int n6 = this.getEffectiveFlowDecay(block);
            if (n6 < 0) {
                if (!block.canBeFlowedInto() || (n6 = this.getEffectiveFlowDecay(this.level.getBlock(baseFullChunk, n3, n4 - 1, n5, true))) < 0) continue;
                n2 = n6 - (n - 8);
                vector3.x += (block.x - this.x) * (double)n2;
                vector3.y += (block.y - this.y) * (double)n2;
                vector3.z += (block.z - this.z) * (double)n2;
                continue;
            }
            n2 = n6 - n;
            vector3.x += (block.x - this.x) * (double)n2;
            vector3.y += (block.y - this.y) * (double)n2;
            vector3.z += (block.z - this.z) * (double)n2;
        }
        if (!(this.getDamage() < 8 || this.canFlowInto(this.level.getBlock(fullChunk = this.getChunk(), (int)this.x, (int)this.y, (int)this.z - 1, true)) && this.canFlowInto(this.level.getBlock(fullChunk, (int)this.x, (int)this.y, (int)this.z + 1, true)) && this.canFlowInto(this.level.getBlock(fullChunk, (int)this.x - 1, (int)this.y, (int)this.z, true)) && this.canFlowInto(this.level.getBlock(fullChunk, (int)this.x + 1, (int)this.y, (int)this.z, true)) && this.canFlowInto(this.level.getBlock(fullChunk, (int)this.x, (int)this.y + 1, (int)this.z - 1, true)) && this.canFlowInto(this.level.getBlock(fullChunk, (int)this.x, (int)this.y + 1, (int)this.z + 1, true)) && this.canFlowInto(this.level.getBlock(fullChunk, (int)this.x - 1, (int)this.y + 1, (int)this.z, true)) && this.canFlowInto(this.level.getBlock(fullChunk, (int)this.x + 1, (int)this.y + 1, (int)this.z, true)))) {
            vector3 = vector3.normalize().add(0.0, -6.0, 0.0);
        }
        this.flowVector = vector3.normalize();
        return this.flowVector;
    }

    @Override
    public void addVelocityToEntity(Entity entity, Vector3 vector3) {
        if (entity.canBeMovedByCurrents()) {
            Vector3 vector32 = this.getFlowVector();
            vector3.x += vector32.x;
            vector3.y += vector32.y;
            vector3.z += vector32.z;
        }
    }

    public int getFlowDecayPerBlock() {
        return 1;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            this.checkForHarden();
            this.level.scheduleUpdate(this, this.tickRate());
            return 0;
        }
        if (n == 3) {
            int n2;
            int n3 = this.getFlowDecay(this);
            int n4 = this.getFlowDecayPerBlock();
            FullChunk fullChunk = this.getChunk();
            if (n3 > 0) {
                int n5;
                int n6 = -100;
                this.adjacentSources = 0;
                n6 = this.a(this.level.getBlock(fullChunk, (int)this.x, (int)this.y, (int)this.z - 1, true), n6);
                n6 = this.a(this.level.getBlock(fullChunk, (int)this.x, (int)this.y, (int)this.z + 1, true), n6);
                n6 = this.a(this.level.getBlock(fullChunk, (int)this.x - 1, (int)this.y, (int)this.z, true), n6);
                n6 = this.a(this.level.getBlock(fullChunk, (int)this.x + 1, (int)this.y, (int)this.z, true), n6);
                n2 = n6 + n4;
                if (n2 >= 8 || n6 < 0) {
                    n2 = -1;
                }
                if ((n5 = this.getFlowDecay(this.level.getBlock(fullChunk, (int)this.x, (int)this.y + 1, (int)this.z, true))) >= 0) {
                    n2 = n5 | 8;
                }
                if (this.adjacentSources >= 2 && this instanceof BlockWater) {
                    Block block = this.level.getBlock(fullChunk, (int)this.x, (int)this.y - 1, (int)this.z, true);
                    if (block.isSolid()) {
                        n2 = 0;
                    } else if (block instanceof BlockWater && block.getDamage() == 0) {
                        n2 = 0;
                    }
                }
                if (n2 != n3) {
                    n3 = n2;
                    boolean bl = n3 < 0;
                    Block block = bl ? Block.get(0) : this.getBlock(n3);
                    BlockFromToEvent blockFromToEvent = new BlockFromToEvent(this, block);
                    this.level.getServer().getPluginManager().callEvent(blockFromToEvent);
                    if (!blockFromToEvent.isCancelled()) {
                        this.level.setBlock(this, blockFromToEvent.getTo(), true, true);
                        if (!bl) {
                            this.level.scheduleUpdate(this, this.tickRate());
                        }
                    }
                }
            }
            if (n3 >= 0) {
                Block block = this.level.getBlock(fullChunk, (int)this.x, (int)this.y - 1, (int)this.z, true);
                this.flowIntoBlock(block, n3 | 8);
                if (!(n3 != 0 && block.canBeFlowedInto() || (n2 = n3 >= 8 ? 1 : n3 + n4) >= 8)) {
                    boolean[] blArray = this.getOptimalFlowDirections();
                    if (blArray[0]) {
                        this.flowIntoBlock(this.level.getBlock(fullChunk, (int)this.x - 1, (int)this.y, (int)this.z, true), n2);
                    }
                    if (blArray[1]) {
                        this.flowIntoBlock(this.level.getBlock(fullChunk, (int)this.x + 1, (int)this.y, (int)this.z, true), n2);
                    }
                    if (blArray[2]) {
                        this.flowIntoBlock(this.level.getBlock(fullChunk, (int)this.x, (int)this.y, (int)this.z - 1, true), n2);
                    }
                    if (blArray[3]) {
                        this.flowIntoBlock(this.level.getBlock(fullChunk, (int)this.x, (int)this.y, (int)this.z + 1, true), n2);
                    }
                }
                this.checkForHarden();
            }
        }
        return 0;
    }

    protected void flowIntoBlock(Block block, int n) {
        if (this.canFlowInto(block) && !(block instanceof BlockLiquid)) {
            LiquidFlowEvent liquidFlowEvent = new LiquidFlowEvent(block, this, n);
            this.level.getServer().getPluginManager().callEvent(liquidFlowEvent);
            if (!liquidFlowEvent.isCancelled()) {
                if (block.getId() != 0) {
                    this.level.useBreakOn(block, block.getId() == 30 ? Item.get(268) : null);
                }
                this.level.setBlock(block, this.getBlock(n), true, true);
                this.level.scheduleUpdate(block, this.tickRate());
            }
        }
    }

    protected int calculateFlowCost(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8 = 1000;
        for (int k = 0; k < 4; ++k) {
            int n9;
            int n10;
            if (k == n6 || k == n7) continue;
            int n11 = n;
            int n12 = n3;
            if (k == 0) {
                --n11;
            } else if (k == 1) {
                ++n11;
            } else {
                n12 = k == 2 ? --n12 : ++n12;
            }
            long l = Level.blockHash(n11, n2, n12);
            if (this.flowCostVisited.containsKey(l)) {
                n10 = this.flowCostVisited.get(l);
            } else {
                BaseFullChunk baseFullChunk = this.level.getChunk(n11 >> 4, n12 >> 4);
                Block block = this.level.getBlock(baseFullChunk, n11, n2, n12, true);
                if (!this.canFlowInto(block)) {
                    this.flowCostVisited.put(l, (byte)-1);
                    n10 = -1;
                } else if (this.level.getBlock(baseFullChunk, n11, n2 - 1, n12, true).canBeFlowedInto()) {
                    this.flowCostVisited.put(l, (byte)1);
                    n10 = 1;
                } else {
                    this.flowCostVisited.put(l, (byte)0);
                    n10 = 0;
                }
            }
            if (n10 == -1) continue;
            if (n10 == 1) {
                return n4;
            }
            if (n4 >= n5 || (n9 = this.calculateFlowCost(n11, n2, n12, n4 + 1, n5, n6, k ^ 1)) >= n8) continue;
            n8 = n9;
        }
        return n8;
    }

    @Override
    public double getHardness() {
        return 100.0;
    }

    @Override
    public double getResistance() {
        return 500.0;
    }

    protected boolean[] getOptimalFlowDirections() {
        int n;
        int[] nArray = new int[]{1000, 1000, 1000, 1000};
        int n2 = 4;
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
            BaseFullChunk baseFullChunk = this.level.getChunk(n3 >> 4, n4 >> 4);
            Block block = this.level.getBlock(baseFullChunk, n3, n, n4, true);
            if (!this.canFlowInto(block)) {
                this.flowCostVisited.put(Level.blockHash(n3, n, n4), (byte)-1);
                continue;
            }
            if (this.level.getBlock(baseFullChunk, n3, n - 1, n4, true).canBeFlowedInto()) {
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

    private int a(Block block, int n) {
        int n2 = this.getFlowDecay(block);
        if (n2 < 0) {
            return n;
        }
        if (n2 == 0) {
            ++this.adjacentSources;
        } else if (n2 >= 8) {
            n2 = 0;
        }
        return n >= 0 && n2 >= n ? n : n2;
    }

    protected void checkForHarden() {
    }

    protected void triggerLavaMixEffects(Vector3 vector3) {
        this.getLevel().addSound((Vector3)this, Sound.RANDOM_FIZZ);
        this.getLevel().addParticle(new SmokeParticle(vector3.add(Math.random(), 1.2, Math.random())), null, 8);
    }

    public abstract BlockLiquid getBlock(int var1);

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.resetFallDistance();
    }

    protected boolean liquidCollide(Block block, Block block2) {
        BlockFromToEvent blockFromToEvent = new BlockFromToEvent(this, block2);
        this.level.getServer().getPluginManager().callEvent(blockFromToEvent);
        if (blockFromToEvent.isCancelled()) {
            return false;
        }
        this.level.setBlock(this, blockFromToEvent.getTo(), true, true);
        this.getLevel().addLevelSoundEvent(this.add(0.5, 0.5, 0.5), 27);
        return true;
    }

    protected boolean canFlowInto(Block block) {
        return block.canBeFlowedInto() && (!(block instanceof BlockLiquid) || block.getDamage() != 0);
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

