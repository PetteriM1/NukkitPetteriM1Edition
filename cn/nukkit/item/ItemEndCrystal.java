/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBedrock;
import cn.nukkit.block.BlockObsidian;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import java.util.concurrent.ThreadLocalRandom;

public class ItemEndCrystal
extends Item {
    public ItemEndCrystal() {
        this((Integer)0, 1);
    }

    public ItemEndCrystal(Integer n) {
        this(n, 1);
    }

    public ItemEndCrystal(Integer n, int n2) {
        super(426, n, n2, "End Crystal");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        Entity entity;
        if (!(block2 instanceof BlockBedrock) && !(block2 instanceof BlockObsidian)) {
            return false;
        }
        BaseFullChunk baseFullChunk = level.getChunk((int)block.getX() >> 4, (int)block.getZ() >> 4);
        if (baseFullChunk == null) {
            return false;
        }
        Block block3 = block2.up();
        if (block3.getId() != 0 || block3.up().getId() != 0) {
            return false;
        }
        Entity[] entityArray = level.getCollidingEntities(new AxisAlignedBB(block2.x, block2.y, block2.z, block2.x + 1.0, block2.y + 2.0, block2.z + 1.0));
        if (entityArray.length != 0) {
            return false;
        }
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", block2.x + 0.5)).add(new DoubleTag("", block3.y)).add(new DoubleTag("", block2.z + 0.5))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", ThreadLocalRandom.current().nextFloat() * 360.0f)).add(new FloatTag("", 0.0f)));
        if (this.hasCustomName()) {
            compoundTag.putString("CustomName", this.getCustomName());
        }
        if ((entity = Entity.createEntity("EndCrystal", (FullChunk)baseFullChunk, compoundTag, new Object[0])) != null) {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            entity.spawnToAll();
            return true;
        }
        return false;
    }
}

