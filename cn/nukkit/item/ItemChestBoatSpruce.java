/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockWater;
import cn.nukkit.entity.item.EntityChestBoat;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

public class ItemChestBoatSpruce
extends Item {
    public ItemChestBoatSpruce() {
        this((Integer)0, 1);
    }

    public ItemChestBoatSpruce(Integer n) {
        this(n, 1);
    }

    public ItemChestBoatSpruce(Integer n, int n2) {
        super(641, n, n2, "Oak Boat with Chest");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (blockFace != BlockFace.UP) {
            return false;
        }
        EntityChestBoat entityChestBoat = new EntityChestBoat(level.getChunk(block.getChunkX(), block.getChunkZ()), new CompoundTag("").putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", block.getX() + 0.5)).add(new DoubleTag("", block.getY() - (block2 instanceof BlockWater ? 0.1 : 0.0))).add(new DoubleTag("", block.getZ() + 0.5))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", (float)((player.yaw + 90.0) % 360.0))).add(new FloatTag("", 0.0f))).putInt("Variant", 1));
        if (!player.isCreative()) {
            --this.count;
        }
        entityChestBoat.spawnToAll();
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 524;
    }
}

