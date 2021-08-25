package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.entity.mob.EntitySkeleton;
import cn.nukkit.entity.mob.EntitySpider;
import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class PopulatorDungeon extends Populator {

    private static final int[] MOBS = {EntitySkeleton.NETWORK_ID, EntityZombie.NETWORK_ID, EntityZombie.NETWORK_ID, EntitySpider.NETWORK_ID};

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        int X = chunkX << 4;
        int Z = chunkZ << 4;

        chance:
        for (int chance = 0; chance < 8; ++chance) {
            int x = X + random.nextBoundedInt(16) + 8;
            int y = random.nextBoundedInt(256);
            int z = Z + random.nextBoundedInt(16) + 8;

            int xv = random.nextBoundedInt(2) + 2;
            int x1 = -xv - 1;
            int x2 = xv + 1;

            int zv = random.nextBoundedInt(2) + 2;
            int z1 = -zv - 1;
            int z2 = zv + 1;

            int t = 0;

            for (int dx = x1; dx <= x2; ++dx) {
                for (int dy = -1; dy <= 4; ++dy) {
                    for (int dz = z1; dz <= z2; ++dz) {
                        int tx = x + dx;
                        int ty = y + dy;
                        int tz = z + dz;

                        int id = level.getBlockIdAt(tx, ty, tz);
                        boolean isSolid = Block.fullList[(id << 4) + level.getBlockDataAt(tx, ty, tz)].isSolid();

                        if (dy == -1 && !isSolid) {
                            continue chance;
                        }
                        if (dy == 4 && !isSolid) {
                            continue chance;
                        }
                        if ((dx == x1 || dx == x2 || dz == z1 || dz == z2) && dy == 0 && level.getBlockIdAt(tx, ty + 1, tz) == AIR) {
                            ++t;
                        }
                    }
                }
            }

            if (t >= 1 && t <= 5) {
                for (int dx = x1; dx <= x2; ++dx) {
                    for (int dy = 3; dy >= -1; --dy) {
                        for (int dz = z1; dz <= z2; ++dz) {
                            int tx = x + dx;
                            int ty = y + dy;
                            int tz = z + dz;

                            int id = level.getBlockIdAt(tx, ty, tz);

                            if (dx != x1 && dy != -1 && dz != z1 && dx != x2 && dy != 4 && dz != z2) {
                                if (id != CHEST) {
                                    level.setBlockAt(tx, ty, tz, AIR);
                                }
                            } else if (ty >= 0 && !Block.fullList[(level.getBlockIdAt(tx, ty - 1, tz) << 4) + level.getBlockDataAt(tx, ty - 1, tz)].isSolid()) {
                                level.setBlockAt(tx, ty, tz, AIR);
                            } else if (Block.fullList[(id << 4) + level.getBlockDataAt(tx, ty, tz)].isSolid() && id != CHEST) {
                                if (dy == -1 && random.nextBoundedInt(4) != 0) {
                                    level.setBlockAt(tx, ty, tz, MOSSY_STONE);
                                } else {
                                    level.setBlockAt(tx, ty, tz, COBBLESTONE);
                                }
                            }
                        }
                    }
                }

                for (int xx = 0; xx < 2; ++xx) {
                    for (int zz = 0; zz < 3; ++zz) {
                        int tx = x + random.nextBoundedInt((xv << 1) + 1) - xv;
                        int tz = z + random.nextBoundedInt((zv << 1) + 1) - zv;

                        if (level.getBlockIdAt(tx, y, tz) == AIR) {
                            int n = 0;

                            if (Block.fullList[(level.getBlockIdAt(tx - 1, y, tz) << 4) + level.getBlockDataAt(tx - 1, y, tz)].isSolid()) {
                                ++n;
                            }
                            if (Block.fullList[(level.getBlockIdAt(tx + 1, y, tz) << 4) + level.getBlockDataAt(tx + 1, y, tz)].isSolid()) {
                                ++n;
                            }
                            if (Block.fullList[(level.getBlockIdAt(tx, y, tz - 1) << 4) + level.getBlockDataAt(tx, y, tz - 1)].isSolid()) {
                                ++n;
                            }
                            if (Block.fullList[(level.getBlockIdAt(tx, y, tz + 1) << 4) + level.getBlockDataAt(tx, y, tz + 1)].isSolid()) {
                                ++n;
                            }

                            if (n == 1) {
                                level.setBlockAt(tx, y, tz, CHEST, 2);
                                Vector3 vec = new Vector3(tx, y, tz);
                                FullChunk ck = level.getChunk(vec.getChunkX(), vec.getChunkZ());
                                BlockEntity be = BlockEntity.createBlockEntity(BlockEntity.CHEST, ck, BlockEntity.getDefaultCompound(vec, BlockEntity.CHEST));
                                if (be != null) {
                                    Inventory inv = ((BlockEntityChest) be).getInventory();
                                    inv.setContents(getChestLoot(inv.getSize()));
                                    ck.addBlockEntity(be);
                                }
                                break;
                            }
                        }
                    }
                }

                level.setBlockAt(x, y, z, MONSTER_SPAWNER);
                BlockEntity be = BlockEntity.createBlockEntity(BlockEntity.MOB_SPAWNER, chunk, BlockEntity.getDefaultCompound(new Vector3(x, y, z), BlockEntity.MOB_SPAWNER).putInt("EntityId", MOBS[random.nextBoundedInt(MOBS.length)]));
                if (be != null) {
                    chunk.addBlockEntity(be);
                }
            }
        }
    }

    private static Map<Integer, Item> getChestLoot(int size) {
        Map<Integer, Item> items = new HashMap<>();
        boolean a = false, b = false, c = false, d = false, e = false, f = false, g = false, h = false, i = false, j = false, k = false, l = false, m = false, n = false, o = false, p = false, q = false, r = false, s = false, t = false, u = false, v = false, w = false;
        for (int x = 0; x < size; x++) {
            if (Utils.rand(1, 4) == 1) {
                switch (Utils.rand(1, 23)) {
                    case 1:
                        if (!a) items.put(x, Item.get(ItemID.BONE, 0, Utils.rand(1, 8)));
                        a = true;
                        break;
                    case 2:
                        if (!b) items.put(x, Item.get(ItemID.GUNPOWDER, 0, Utils.rand(1, 8)));
                        b = true;
                        break;
                    case 3:
                        if (!c) items.put(x, Item.get(ItemID.ROTTEN_FLESH, 0, Utils.rand(1, 8)));
                        c = true;
                        break;
                    case 4:
                        if (!d) items.put(x, Item.get(ItemID.STRING, 0, Utils.rand(1, 8)));
                        d = true;
                        break;
                    case 5:
                        if (!e) items.put(x, Item.get(ItemID.WHEAT, 0, Utils.rand(1, 4)));
                        e = true;
                        break;
                    case 6:
                        if (!f) items.put(x, Item.get(ItemID.BREAD, 0, 1));
                        f = true;
                        break;
                    case 7:
                        if (!g) items.put(x, Item.get(ItemID.NAME_TAG, 0, 1));
                        g = true;
                        break;
                    case 8:
                        if (!h) items.put(x, Item.get(ItemID.SADDLE, 0, 1));
                        h = true;
                        break;
                    case 9:
                        if (!i) items.put(x, Item.get(ItemID.COAL, 0, Utils.rand(1, 4)));
                        i = true;
                        break;
                    case 10:
                        if (!j) items.put(x, Item.get(ItemID.REDSTONE_DUST, 0, Utils.rand(1, 4)));
                        j = true;
                        break;
                    case 11:
                        if (!k && !l) items.put(x, Item.get(ItemID.RECORD_13, 0, 1));
                        k = true;
                        break;
                    case 12:
                        if (!l && !k) items.put(x, Item.get(ItemID.RECORD_CAT, 0, 1));
                        l = true;
                        break;
                    case 13:
                        if (!m) items.put(x, Item.get(ItemID.IRON_HORSE_ARMOR, 0, 1));
                        m = true;
                        break;
                    case 14:
                        if (!n) items.put(x, Item.get(ItemID.GOLDEN_APPLE, 0, 1));
                        n = true;
                        break;
                    case 15:
                        if (!o) items.put(x, Item.get(ItemID.BEETROOT_SEED, 0, Utils.rand(2, 4)));
                        o = true;
                        break;
                    case 16:
                        if (!p) items.put(x, Item.get(ItemID.MELON_SEEDS, 0, Utils.rand(2, 4)));
                        p = true;
                        break;
                    case 17:
                        if (!q) items.put(x, Item.get(ItemID.PUMPKIN_SEEDS, 0, Utils.rand(2, 4)));
                        q = true;
                        break;
                    case 18:
                        if (!r) items.put(x, Item.get(ItemID.IRON_INGOT, 0, Utils.rand(1, 4)));
                        r = true;
                        break;
                    case 19:
                        if (!s) items.put(x, Item.get(ItemID.BUCKET, 0, 1));
                        s = true;
                        break;
                    case 20:
                        if (!t && !v) items.put(x, Item.get(ItemID.GOLD_HORSE_ARMOR, 0, 1));
                        t = true;
                        break;
                    case 21:
                        if (!u) items.put(x, Item.get(ItemID.GOLD_INGOT, 0, Utils.rand(1, 4)));
                        u = true;
                        break;
                    case 22:
                        if (!v && !t && Utils.rand()) items.put(x, Item.get(ItemID.DIAMOND_HORSE_ARMOR, 0, 1));
                        v = true;
                        break;
                    case 23:
                        if (!w && Utils.rand()) items.put(x, Item.get(ItemID.GOLDEN_APPLE_ENCHANTED, 0, 1));
                        w = true;
                        break;
                }
            }
        }
        return items;
    }
}
