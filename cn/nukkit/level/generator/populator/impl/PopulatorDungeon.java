/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Utils;
import java.util.HashMap;
import java.util.Map;

public class PopulatorDungeon
extends Populator {
    private static final int[] a = new int[]{34, 32, 32, 35};

    @Override
    public void populate(ChunkManager chunkManager, int n, int n2, NukkitRandom nukkitRandom, FullChunk fullChunk) {
        int n3 = n << 4;
        int n4 = n2 << 4;
        block0: for (int k = 0; k < 8; ++k) {
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            int n11;
            int n12 = n3 + nukkitRandom.nextBoundedInt(16) + 8;
            int n13 = nukkitRandom.nextBoundedInt(256);
            int n14 = n4 + nukkitRandom.nextBoundedInt(16) + 8;
            int n15 = nukkitRandom.nextBoundedInt(2) + 2;
            int n16 = -n15 - 1;
            int n17 = n15 + 1;
            int n18 = nukkitRandom.nextBoundedInt(2) + 2;
            int n19 = -n18 - 1;
            int n20 = n18 + 1;
            int n21 = 0;
            for (n11 = n16; n11 <= n17; ++n11) {
                for (n10 = -1; n10 <= 4; ++n10) {
                    for (n9 = n19; n9 <= n20; ++n9) {
                        n8 = n12 + n11;
                        n7 = n13 + n10;
                        n6 = n14 + n9;
                        n5 = chunkManager.getBlockIdAt(n8, n7, n6);
                        boolean bl = Block.fullList[(n5 << 4) + chunkManager.getBlockDataAt(n8, n7, n6)].isSolid();
                        if (n10 == -1 && !bl || n10 == 4 && !bl) continue block0;
                        if (n11 != n16 && n11 != n17 && n9 != n19 && n9 != n20 || n10 != 0 || chunkManager.getBlockIdAt(n8, n7 + 1, n6) != 0) continue;
                        ++n21;
                    }
                }
            }
            if (n21 < true || n21 > 5) continue;
            for (n11 = n16; n11 <= n17; ++n11) {
                for (n10 = 3; n10 >= -1; --n10) {
                    for (n9 = n19; n9 <= n20; ++n9) {
                        n8 = n12 + n11;
                        n7 = n13 + n10;
                        n6 = n14 + n9;
                        n5 = chunkManager.getBlockIdAt(n8, n7, n6);
                        if (n11 != n16 && n10 != -1 && n9 != n19 && n11 != n17 && n10 != 4 && n9 != n20) {
                            if (n5 == 54) continue;
                            chunkManager.setBlockAt(n8, n7, n6, 0);
                            continue;
                        }
                        if (n7 >= 0 && !Block.fullList[(chunkManager.getBlockIdAt(n8, n7 - 1, n6) << 4) + chunkManager.getBlockDataAt(n8, n7 - 1, n6)].isSolid()) {
                            chunkManager.setBlockAt(n8, n7, n6, 0);
                            continue;
                        }
                        if (!Block.fullList[(n5 << 4) + chunkManager.getBlockDataAt(n8, n7, n6)].isSolid() || n5 == 54) continue;
                        if (n10 == -1 && nukkitRandom.nextBoundedInt(4) != 0) {
                            chunkManager.setBlockAt(n8, n7, n6, 48);
                            continue;
                        }
                        chunkManager.setBlockAt(n8, n7, n6, 4);
                    }
                }
            }
            block7: for (n11 = 0; n11 < 2; ++n11) {
                for (n10 = 0; n10 < 3; ++n10) {
                    n9 = n12 + nukkitRandom.nextBoundedInt((n15 << 1) + 1) - n15;
                    if (chunkManager.getBlockIdAt(n9, n13, n8 = n14 + nukkitRandom.nextBoundedInt((n18 << 1) + 1) - n18) != 0) continue;
                    n7 = 0;
                    if (Block.fullList[(chunkManager.getBlockIdAt(n9 - 1, n13, n8) << 4) + chunkManager.getBlockDataAt(n9 - 1, n13, n8)].isSolid()) {
                        ++n7;
                    }
                    if (Block.fullList[(chunkManager.getBlockIdAt(n9 + 1, n13, n8) << 4) + chunkManager.getBlockDataAt(n9 + 1, n13, n8)].isSolid()) {
                        ++n7;
                    }
                    if (Block.fullList[(chunkManager.getBlockIdAt(n9, n13, n8 - 1) << 4) + chunkManager.getBlockDataAt(n9, n13, n8 - 1)].isSolid()) {
                        ++n7;
                    }
                    if (Block.fullList[(chunkManager.getBlockIdAt(n9, n13, n8 + 1) << 4) + chunkManager.getBlockDataAt(n9, n13, n8 + 1)].isSolid()) {
                        ++n7;
                    }
                    if (n7 != 1) continue;
                    chunkManager.setBlockAt(n9, n13, n8, 54, 2);
                    Vector3 vector3 = new Vector3(n9, n13, n8);
                    BaseFullChunk baseFullChunk = chunkManager.getChunk(vector3.getChunkX(), vector3.getChunkZ());
                    BlockEntity blockEntity = BlockEntity.createBlockEntity("Chest", baseFullChunk, BlockEntity.getDefaultCompound(vector3, "Chest"), new Object[0]);
                    if (blockEntity == null) continue block7;
                    BaseInventory baseInventory = ((BlockEntityChest)blockEntity).getInventory();
                    baseInventory.setContents(PopulatorDungeon.a(baseInventory.getSize()));
                    baseFullChunk.addBlockEntity(blockEntity);
                    continue block7;
                }
            }
            chunkManager.setBlockAt(n12, n13, n14, 52);
            BlockEntity blockEntity = BlockEntity.createBlockEntity("MobSpawner", fullChunk, BlockEntity.getDefaultCompound(new Vector3(n12, n13, n14), "MobSpawner").putInt("EntityId", a[nukkitRandom.nextBoundedInt(a.length)]), new Object[0]);
            if (blockEntity == null) continue;
            fullChunk.addBlockEntity(blockEntity);
        }
    }

    private static Map<Integer, Item> a(int n) {
        HashMap<Integer, Item> hashMap = new HashMap<Integer, Item>();
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;
        boolean bl7 = false;
        boolean bl8 = false;
        boolean bl9 = false;
        boolean bl10 = false;
        boolean bl11 = false;
        boolean bl12 = false;
        boolean bl13 = false;
        boolean bl14 = false;
        boolean bl15 = false;
        boolean bl16 = false;
        boolean bl17 = false;
        boolean bl18 = false;
        boolean bl19 = false;
        boolean bl20 = false;
        boolean bl21 = false;
        boolean bl22 = false;
        boolean bl23 = false;
        block25: for (int k = 0; k < n; ++k) {
            if (Utils.rand(1, 4) != 1) continue;
            switch (Utils.rand(1, 23)) {
                case 1: {
                    if (!bl) {
                        hashMap.put(k, Item.get(352, 0, Utils.rand(1, 8)));
                    }
                    bl = true;
                    continue block25;
                }
                case 2: {
                    if (!bl2) {
                        hashMap.put(k, Item.get(289, 0, Utils.rand(1, 8)));
                    }
                    bl2 = true;
                    continue block25;
                }
                case 3: {
                    if (!bl3) {
                        hashMap.put(k, Item.get(367, 0, Utils.rand(1, 8)));
                    }
                    bl3 = true;
                    continue block25;
                }
                case 4: {
                    if (!bl4) {
                        hashMap.put(k, Item.get(287, 0, Utils.rand(1, 8)));
                    }
                    bl4 = true;
                    continue block25;
                }
                case 5: {
                    if (!bl5) {
                        hashMap.put(k, Item.get(296, 0, Utils.rand(1, 4)));
                    }
                    bl5 = true;
                    continue block25;
                }
                case 6: {
                    if (!bl6) {
                        hashMap.put(k, Item.get(297, 0, 1));
                    }
                    bl6 = true;
                    continue block25;
                }
                case 7: {
                    if (!bl7) {
                        hashMap.put(k, Item.get(421, 0, 1));
                    }
                    bl7 = true;
                    continue block25;
                }
                case 8: {
                    if (!bl8) {
                        hashMap.put(k, Item.get(329, 0, 1));
                    }
                    bl8 = true;
                    continue block25;
                }
                case 9: {
                    if (!bl9) {
                        hashMap.put(k, Item.get(263, 0, Utils.rand(1, 4)));
                    }
                    bl9 = true;
                    continue block25;
                }
                case 10: {
                    if (!bl10) {
                        hashMap.put(k, Item.get(331, 0, Utils.rand(1, 4)));
                    }
                    bl10 = true;
                    continue block25;
                }
                case 11: {
                    if (!bl11 && !bl12) {
                        hashMap.put(k, Item.get(500, 0, 1));
                    }
                    bl11 = true;
                    continue block25;
                }
                case 12: {
                    if (!bl12 && !bl11) {
                        hashMap.put(k, Item.get(501, 0, 1));
                    }
                    bl12 = true;
                    continue block25;
                }
                case 13: {
                    if (!bl13) {
                        hashMap.put(k, Item.get(417, 0, 1));
                    }
                    bl13 = true;
                    continue block25;
                }
                case 14: {
                    if (!bl14) {
                        hashMap.put(k, Item.get(322, 0, 1));
                    }
                    bl14 = true;
                    continue block25;
                }
                case 15: {
                    if (!bl15) {
                        hashMap.put(k, Item.get(458, 0, Utils.rand(2, 4)));
                    }
                    bl15 = true;
                    continue block25;
                }
                case 16: {
                    if (!bl16) {
                        hashMap.put(k, Item.get(362, 0, Utils.rand(2, 4)));
                    }
                    bl16 = true;
                    continue block25;
                }
                case 17: {
                    if (!bl17) {
                        hashMap.put(k, Item.get(361, 0, Utils.rand(2, 4)));
                    }
                    bl17 = true;
                    continue block25;
                }
                case 18: {
                    if (!bl18) {
                        hashMap.put(k, Item.get(265, 0, Utils.rand(1, 4)));
                    }
                    bl18 = true;
                    continue block25;
                }
                case 19: {
                    if (!bl19) {
                        hashMap.put(k, Item.get(325, 0, 1));
                    }
                    bl19 = true;
                    continue block25;
                }
                case 20: {
                    if (!bl20 && !bl22) {
                        hashMap.put(k, Item.get(418, 0, 1));
                    }
                    bl20 = true;
                    continue block25;
                }
                case 21: {
                    if (!bl21) {
                        hashMap.put(k, Item.get(266, 0, Utils.rand(1, 4)));
                    }
                    bl21 = true;
                    continue block25;
                }
                case 22: {
                    if (!bl22 && !bl20 && Utils.rand()) {
                        hashMap.put(k, Item.get(419, 0, 1));
                    }
                    bl22 = true;
                    continue block25;
                }
                case 23: {
                    if (!bl23 && Utils.rand()) {
                        hashMap.put(k, Item.get(466, 0, 1));
                    }
                    bl23 = true;
                }
            }
        }
        return hashMap;
    }
}

