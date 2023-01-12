/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.item.Item;
import cn.nukkit.level.generator.object.BasicGenerator;
import cn.nukkit.level.generator.object.tree.NewJungleTree;
import cn.nukkit.level.generator.object.tree.ObjectBigSpruceTree;
import cn.nukkit.level.generator.object.tree.ObjectDarkOakTree;
import cn.nukkit.level.generator.object.tree.ObjectJungleBigTree;
import cn.nukkit.level.generator.object.tree.ObjectSavannaTree;
import cn.nukkit.level.generator.object.tree.ObjectTree;
import cn.nukkit.level.generator.object.tree.TreeGenerator;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;
import java.util.concurrent.ThreadLocalRandom;

public class BlockSapling
extends BlockFlowable {
    public static final int OAK = 0;
    public static final int SPRUCE = 1;
    public static final int BIRCH = 2;
    public static final int JUNGLE = 3;
    public static final int ACACIA = 4;
    public static final int DARK_OAK = 5;
    public static final int BIRCH_TALL = 10;

    public BlockSapling() {
        this(0);
    }

    public BlockSapling(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Oak Sapling", "Spruce Sapling", "Birch Sapling", "Jungle Sapling", "Acacia Sapling", "Dark Oak Sapling", "", ""};
        return stringArray[this.getDamage() & 7];
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        Block block3 = this.down();
        int n = block3.getId();
        if (n == 2 || n == 3 || n == 60 || n == 243 || n == 110) {
            this.getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 351 && item.getDamage() == 15) {
            if (player != null && !player.isCreative()) {
                --item.count;
            }
            this.level.addParticle(new BoneMealParticle(this));
            if ((double)ThreadLocalRandom.current().nextFloat() >= 0.45) {
                return true;
            }
            TreeGenerator treeGenerator = null;
            boolean bl = false;
            int n = 0;
            int n2 = 0;
            switch (this.getDamage()) {
                case 3: {
                    block6: for (n = 0; n >= -1; --n) {
                        for (n2 = 0; n2 >= -1; --n2) {
                            if (!this.a(n, n2, 3)) continue;
                            treeGenerator = new ObjectJungleBigTree(10, 20, Block.get(17, 3), Block.get(18, 3));
                            bl = true;
                            break block6;
                        }
                    }
                    if (bl) break;
                    treeGenerator = new NewJungleTree(4, 7);
                    break;
                }
                case 4: {
                    treeGenerator = new ObjectSavannaTree();
                    break;
                }
                case 5: {
                    bl = false;
                    block8: for (n = 0; n >= -1; --n) {
                        for (n2 = 0; n2 >= -1; --n2) {
                            if (!this.a(n, n2, 5)) continue;
                            treeGenerator = new ObjectDarkOakTree();
                            bl = true;
                            break block8;
                        }
                    }
                    if (bl) break;
                    return false;
                }
                case 1: {
                    block10: for (n = 0; n >= -1; --n) {
                        for (n2 = 0; n2 >= -1; --n2) {
                            if (!this.a(n, n2, 1)) continue;
                            new ObjectBigSpruceTree(0.5f, 5, true).placeObject(this.level, (int)this.x + n, (int)this.y, (int)this.z + n2, new NukkitRandom());
                            bl = true;
                            break block10;
                        }
                    }
                    if (!bl) {
                        ObjectTree.growTree(this.getLevel(), (int)this.x, (int)this.y, (int)this.z, new NukkitRandom(), this.getDamage() & 7);
                    } else {
                        Block block = Block.get(0);
                        this.level.setBlock(this.add(this.x, 0.0, this.z), block, true, false);
                        this.level.setBlock(this.add(this.x + 1.0, 0.0, this.z), block, true, false);
                        this.level.setBlock(this.add(this.x, 0.0, this.z + 1.0), block, true, false);
                        this.level.setBlock(this.add(this.x + 1.0, 0.0, this.z + 1.0), block, true, false);
                    }
                    return true;
                }
                default: {
                    ObjectTree.growTree(this.getLevel(), (int)this.x, (int)this.y, (int)this.z, new NukkitRandom(), this.getDamage() & 7);
                    return true;
                }
            }
            Block block = Block.get(0);
            if (bl) {
                this.level.setBlock(this.add(n, 0.0, n2), block, true, false);
                this.level.setBlock(this.add(n + 1, 0.0, n2), block, true, false);
                this.level.setBlock(this.add(n, 0.0, n2 + 1), block, true, false);
                this.level.setBlock(this.add(n + 1, 0.0, n2 + 1), block, true, false);
            } else {
                this.level.setBlock(this, block, true, false);
            }
            if (!((BasicGenerator)treeGenerator).generate(this.level, new NukkitRandom(), this.add(n, 0.0, n2))) {
                if (bl) {
                    this.level.setBlock(this.add(n, 0.0, n2), this, true, false);
                    this.level.setBlock(this.add(n + 1, 0.0, n2), this, true, false);
                    this.level.setBlock(this.add(n, 0.0, n2 + 1), this, true, false);
                    this.level.setBlock(this.add(n + 1, 0.0, n2 + 1), this, true, false);
                } else {
                    this.level.setBlock(this, this, true, false);
                }
            }
            return true;
        }
        this.getLevel().loadChunk((int)this.x >> 4, (int)this.z >> 4);
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (!this.down().isTransparent()) return 1;
            this.getLevel().useBreakOn(this);
            return 1;
        }
        if (n != 2) return 1;
        if (Utils.rand(1, 7) != 1) return 2;
        if ((this.getDamage() & 8) == 8) {
            if ((this.getDamage() & 7) == 4) {
                this.level.setBlock(this, Block.get(0), true, false);
                new ObjectSavannaTree().generate(this.level, new NukkitRandom(), this);
                return 1;
            } else {
                ObjectTree.growTree(this.getLevel(), (int)this.x, (int)this.y, (int)this.z, new NukkitRandom(), this.getDamage() & 7);
            }
            return 1;
        } else {
            this.setDamage(this.getDamage() | 8);
            this.getLevel().setBlock(this, this, true);
            return 2;
        }
    }

    private boolean a(int n, int n2, int n3) {
        return this.isSameType(this.add(n, 0.0, n2), n3) && this.isSameType(this.add(n + 1, 0.0, n2), n3) && this.isSameType(this.add(n, 0.0, n2 + 1), n3) && this.isSameType(this.add(n + 1, 0.0, n2 + 1), n3);
    }

    public boolean isSameType(Vector3 vector3, int n) {
        Block block = this.level.getBlock(vector3);
        return block.getId() == this.getId() && (block.getDamage() & 7) == (n & 7);
    }

    @Override
    public Item toItem() {
        return Item.get(6, this.getDamage() & 7);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

