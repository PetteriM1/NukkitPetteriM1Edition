/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFallableMeta;
import cn.nukkit.block.BlockLava;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockScaffolding
extends BlockFallableMeta {
    public BlockScaffolding() {
        this(0);
    }

    public BlockScaffolding(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Scaffolding";
    }

    @Override
    public int getId() {
        return 420;
    }

    public int getStability() {
        return this.getDamage() & 7;
    }

    public void setStability(int n) {
        this.setDamage(n & 7 | this.getDamage() & 8);
    }

    public boolean getStabilityCheck() {
        return (this.getDamage() & 8) > 0;
    }

    public void setStabilityCheck(boolean bl) {
        if (bl) {
            this.setDamage(this.getDamage() | 8);
        } else {
            this.setDamage(this.getDamage() & 7);
        }
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(420));
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block instanceof BlockLava) {
            return false;
        }
        Block block3 = this.down();
        if (block2.getId() != 420 && block3.getId() != 420 && block3.getId() != 0 && !block3.isSolid()) {
            boolean bl = false;
            for (int k = 0; k < 4; ++k) {
                Block block4;
                BlockFace blockFace2 = BlockFace.fromHorizontalIndex(k);
                if (blockFace2 == blockFace || (block4 = this.getSide(blockFace2)).getId() != 420) continue;
                bl = true;
                break;
            }
            if (!bl) {
                return false;
            }
        }
        this.setDamage(8);
        this.getLevel().setBlock(this, this, true, true);
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            Block block = this.down();
            if (block.isSolid()) {
                if (this.getDamage() != 0) {
                    this.setDamage(0);
                    this.getLevel().setBlock(this, this, true, true);
                }
                return n;
            }
            int n2 = 7;
            for (BlockFace blockFace : BlockFace.values()) {
                BlockScaffolding blockScaffolding;
                int n3;
                Block block2;
                if (blockFace == BlockFace.UP || (block2 = this.getSide(blockFace)).getId() != 420 || (n3 = (blockScaffolding = (BlockScaffolding)block2).getStability()) >= n2) continue;
                n2 = blockFace == BlockFace.DOWN ? n3 : n3 + 1;
            }
            if (n2 >= 7) {
                if (this.getStabilityCheck()) {
                    super.onUpdate(n);
                } else {
                    this.getLevel().scheduleUpdate(this, 0);
                }
                return n;
            }
            this.setStabilityCheck(false);
            this.setStability(n2);
            this.getLevel().setBlock(this, this, true, true);
            return n;
        }
        if (n == 3) {
            this.getLevel().useBreakOn(this);
            return n;
        }
        return 0;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 0.0;
    }

    @Override
    public int getBurnChance() {
        return 60;
    }

    @Override
    public int getBurnAbility() {
        return 60;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean canBeClimbed() {
        return true;
    }

    @Override
    public boolean canBeFlowedInto() {
        return false;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        entity.resetFallDistance();
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public double getMinY() {
        return this.y + 0.875;
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.TRANSPARENT_BLOCK_COLOR;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

