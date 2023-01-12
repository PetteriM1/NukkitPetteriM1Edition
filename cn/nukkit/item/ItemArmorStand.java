/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityArmorStand;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

public class ItemArmorStand
extends Item {
    public ItemArmorStand() {
        this((Integer)0);
    }

    public ItemArmorStand(Integer n) {
        this(n, 1);
    }

    public ItemArmorStand(Integer n, int n2) {
        super(425, n, n2, "Armor Stand");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        Entity entity2;
        BaseFullChunk baseFullChunk = level.getChunk((int)block.getX() >> 4, (int)block.getZ() >> 4);
        if (baseFullChunk == null) {
            return false;
        }
        for (Entity entity2 : baseFullChunk.getEntities().values()) {
            if (!(entity2 instanceof EntityArmorStand) || entity2.getY() != block.getY() || entity2.getX() != block.getX() + 0.5 || entity2.getZ() != block.getZ() + 0.5) continue;
            return false;
        }
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", block.getX() + 0.5)).add(new DoubleTag("", block.getY())).add(new DoubleTag("", block.getZ() + 0.5))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0)).add(new DoubleTag("", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", ItemArmorStand.getDirection((float)player.getYaw()))).add(new FloatTag("", 0.0f)));
        if (this.hasCustomName()) {
            compoundTag.putString("CustomName", this.getCustomName());
        }
        entity2 = Entity.createEntity("ArmorStand", (FullChunk)baseFullChunk, compoundTag, new Object[0]);
        if (!player.isCreative()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
        }
        if (entity2 != null) {
            entity2.spawnToAll();
            player.getLevel().addLevelSoundEvent(entity2, 1063);
        }
        return true;
    }

    public static float getDirection(float f2) {
        return Math.round(f2 / 22.5f / 2.0f) * 45 - 180;
    }
}

