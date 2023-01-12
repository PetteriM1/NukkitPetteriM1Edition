/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCrops;
import cn.nukkit.block.BlockIceFrosted;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.BlockWater;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

public class BlockFarmland
extends BlockTransparentMeta {
    public BlockFarmland() {
        this(0);
    }

    public BlockFarmland(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Farmland";
    }

    @Override
    public int getId() {
        return 60;
    }

    @Override
    public double getResistance() {
        return 3.0;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 2) {
            Block block = this.up();
            if (block instanceof BlockCrops) {
                return 0;
            }
            if (block.isSolid()) {
                this.level.setBlock(this, Block.get(3), false, true);
                return 2;
            }
            boolean bl = false;
            Vector3 vector3 = new Vector3();
            if (this.level.isRaining()) {
                bl = true;
            } else {
                int n2 = (int)this.x - 4;
                while ((double)n2 <= this.x + 4.0) {
                    int n3 = (int)this.z - 4;
                    while ((double)n3 <= this.z + 4.0) {
                        int n4 = (int)this.y;
                        while ((double)n4 <= this.y + 1.0) {
                            if ((double)n3 != this.z || (double)n2 != this.x || (double)n4 != this.y) {
                                vector3.setComponents(n2, n4, n3);
                                int n5 = this.level.getBlockIdAt(vector3.getFloorX(), vector3.getFloorY(), vector3.getFloorZ());
                                if (Block.hasWater(n5) || n5 == 207) {
                                    bl = true;
                                    break;
                                }
                            }
                            ++n4;
                        }
                        ++n3;
                    }
                    ++n2;
                }
            }
            Block block2 = this.level.getBlock(vector3.setComponents(this.x, this.y - 1.0, this.z));
            if (bl || block2 instanceof BlockWater || block2 instanceof BlockIceFrosted) {
                if (this.getDamage() < 7) {
                    this.setDamage(7);
                    this.level.setBlock(this, this, false, false);
                }
                return 2;
            }
            if (this.getDamage() > 0) {
                this.setDamage(this.getDamage() - 1);
                this.level.setBlock(this, this, false, false);
            } else {
                this.level.setBlock(this, Block.get(3), false, true);
            }
            return 2;
        }
        if (n == 1 && this.up().isSolid()) {
            this.level.setBlock(this, Block.get(3), false, false);
            return 1;
        }
        return 0;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(3));
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.DIRT_BLOCK_COLOR;
    }
}

