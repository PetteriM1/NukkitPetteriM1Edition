/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFire;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.block.BlockSolidMeta;
import cn.nukkit.entity.item.EntityFallingBlock;
import cn.nukkit.event.block.BlockFallEvent;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

public abstract class BlockFallableMeta
extends BlockSolidMeta {
    protected BlockFallableMeta(int n) {
        super(n);
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (!this.getLevel().randomTickingEnabled()) {
                return n;
            }
            Block block = this.down();
            if (block.getId() == 0 || block instanceof BlockLiquid || block instanceof BlockFire) {
                BlockFallEvent blockFallEvent = new BlockFallEvent(this);
                this.level.getServer().getPluginManager().callEvent(blockFallEvent);
                if (blockFallEvent.isCancelled()) {
                    return n;
                }
                this.level.setBlock(this, Block.get(0), true, true);
                CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", this.x + 0.5)).add(new DoubleTag("", this.y)).add(new DoubleTag("", this.z + 0.5))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))).putInt("TileID", this.getId()).putByte("Data", this.getDamage());
                EntityFallingBlock entityFallingBlock = new EntityFallingBlock(this.getLevel().getChunk((int)this.x >> 4, (int)this.z >> 4), compoundTag);
                entityFallingBlock.spawnToAll();
            }
        }
        return n;
    }
}

