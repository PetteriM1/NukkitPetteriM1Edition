/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.item.EntityPainting;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class ItemPainting
extends Item {
    private static final int[] y = new int[]{2, 3, 4, 5};
    private static final int[] x = new int[]{4, 5, 3, 2};
    private static final double z = 0.53125;

    public ItemPainting() {
        this((Integer)0, 1);
    }

    public ItemPainting(Integer n) {
        this(n, 1);
    }

    public ItemPainting(Integer n, int n2) {
        super(321, 0, n2, "Painting");
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        if (player.isAdventure() || block2.isTransparent() || blockFace.getHorizontalIndex() == -1 || block.isSolid()) {
            return false;
        }
        BaseFullChunk baseFullChunk = level.getChunk((int)block.getX() >> 4, (int)block.getZ() >> 4);
        if (baseFullChunk == null) {
            return false;
        }
        ArrayList<EntityPainting.Motive> arrayList = new ArrayList<EntityPainting.Motive>();
        for (EntityPainting.Motive motive : EntityPainting.motives) {
            boolean bl = true;
            for (int k = 0; k < motive.width && bl; ++k) {
                for (int i2 = 0; i2 < motive.height && bl; ++i2) {
                    if (!block2.getSide(BlockFace.fromIndex(x[blockFace.getIndex() - 2]), k).isTransparent() && !block2.up(i2).isTransparent() && !block.getSide(BlockFace.fromIndex(x[blockFace.getIndex() - 2]), k).isSolid() && !block.up(i2).isSolid()) continue;
                    bl = false;
                }
            }
            if (!bl) continue;
            arrayList.add(motive);
        }
        int n = y[blockFace.getIndex() - 2];
        EntityPainting.Motive motive = (EntityPainting.Motive)((Object)arrayList.get(Utils.random.nextInt(arrayList.size())));
        Vector3 vector3 = new Vector3(block2.x + 0.5, block2.y + 0.5, block2.z + 0.5);
        double d5 = ItemPainting.b(motive.width);
        switch (blockFace.getHorizontalIndex()) {
            case 0: {
                vector3.x += d5;
                vector3.z += 0.53125;
                break;
            }
            case 1: {
                vector3.x -= 0.53125;
                vector3.z += d5;
                break;
            }
            case 2: {
                vector3.x -= d5;
                vector3.z -= 0.53125;
                break;
            }
            case 3: {
                vector3.x += 0.53125;
                vector3.z -= d5;
            }
        }
        vector3.y += ItemPainting.b(motive.height);
        CompoundTag compoundTag = new CompoundTag().putByte("Direction", n).putString("Motive", motive.title).putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("0", vector3.x)).add(new DoubleTag("1", vector3.y)).add(new DoubleTag("2", vector3.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("0", 0.0)).add(new DoubleTag("1", 0.0)).add(new DoubleTag("2", 0.0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("0", n * 90)).add(new FloatTag("1", 0.0f)));
        EntityPainting entityPainting = new EntityPainting(baseFullChunk, compoundTag);
        if (!player.isCreative()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
        }
        entityPainting.spawnToAll();
        return true;
    }

    private static double b(int n) {
        return n > 1 ? 0.5 : 0.0;
    }
}

