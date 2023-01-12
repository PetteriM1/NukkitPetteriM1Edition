/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRail;
import cn.nukkit.entity.item.EntityMinecartHopper;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Rail;

public class ItemMinecartHopper
extends Item {
    public ItemMinecartHopper() {
        this((Integer)0, 1);
    }

    public ItemMinecartHopper(Integer n) {
        this(n, 1);
    }

    public ItemMinecartHopper(Integer n, int n2) {
        super(408, n, n2, "Minecart with Hopper");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (Rail.isRailBlock(block2)) {
            Rail.Orientation orientation = ((BlockRail)block2).getOrientation();
            double d5 = 0.0;
            if (orientation.isAscending()) {
                d5 = 0.5;
            }
            EntityMinecartHopper entityMinecartHopper = new EntityMinecartHopper(level.getChunk(block2.getChunkX(), block2.getChunkZ()), new CompoundTag("").putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", block2.getX() + 0.5)).add(new DoubleTag("", block2.getY() + 0.0625 + d5)).add(new DoubleTag("", block2.getZ() + 0.5))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0.0f)).add(new FloatTag("", 0.0f))));
            entityMinecartHopper.spawnToAll();
            --this.count;
            return true;
        }
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}

