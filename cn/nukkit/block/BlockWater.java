/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.block.WaterFrostEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;
import java.util.concurrent.ThreadLocalRandom;

public class BlockWater
extends BlockLiquid {
    private byte d;

    public BlockWater() {
        this(0);
    }

    public BlockWater(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public String getName() {
        return "Water";
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        boolean bl = this.getLevel().setBlock(this, this, true, false);
        this.getLevel().scheduleUpdate(this, this.tickRate());
        return bl;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WATER_BLOCK_COLOR;
    }

    @Override
    public BlockLiquid getBlock(int n) {
        return (BlockLiquid)Block.get(8, n);
    }

    @Override
    public void onEntityCollide(Entity entity) {
        super.onEntityCollide(entity);
        if (entity.fireTicks > 0) {
            entity.extinguish();
        }
    }

    @Override
    public int tickRate() {
        return 5;
    }

    @Override
    public int onUpdate(int n) {
        if (this.d != 1 && n == 2 && this.getDamage() == 0) {
            FullChunk fullChunk = this.getChunk();
            if (this.d < 1) {
                this.d = (byte)(Utils.freezingBiomes.contains(fullChunk.getBiomeId((int)this.x & 0xF, (int)this.z & 0xF)) ? 2 : 1);
            }
            if (this.d == 2 && ThreadLocalRandom.current().nextInt(10) == 0 && fullChunk.getBlockLight((int)this.x & 0xF, (int)this.y, (int)this.z & 0xF) < 12 && (double)fullChunk.getHighestBlockAt((int)this.x & 0xF, (int)this.z & 0xF, false) <= this.y) {
                WaterFrostEvent waterFrostEvent = new WaterFrostEvent(this);
                this.level.getServer().getPluginManager().callEvent(waterFrostEvent);
                if (!waterFrostEvent.isCancelled()) {
                    this.level.setBlock(this, Block.get(79), true, true);
                }
            }
            return 2;
        }
        return super.onUpdate(n);
    }
}

