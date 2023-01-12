/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.event.block.BlockSpreadEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.level.generator.object.ObjectTallGrass;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockGrass
extends BlockDirt {
    public BlockGrass() {
        this(0);
    }

    public BlockGrass(int n) {
        super(0);
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3.0;
    }

    @Override
    public String getName() {
        return "Grass Block";
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        Block block;
        if (item.getId() == 351 && item.getDamage() == 15) {
            ObjectTallGrass.growGrass(this.getLevel(), this, Utils.nukkitRandom);
            this.level.addParticle(new BoneMealParticle(this));
            if (player != null) {
                if (!player.isCreative()) {
                    --item.count;
                }
                player.getLevel().addSound((Vector3)player, Sound.STEP_GRASS);
            }
            return true;
        }
        if (item.isHoe()) {
            Block block2 = this.up();
            if (block2 instanceof BlockAir || block2 instanceof BlockFlowable) {
                item.useOn(this);
                this.getLevel().setBlock(this, Block.get(60));
                if (player != null) {
                    player.getLevel().addSound((Vector3)player, Sound.STEP_GRASS);
                }
                return true;
            }
        } else if (item.isShovel() && ((block = this.up()) instanceof BlockAir || block instanceof BlockFlowable)) {
            item.useOn(this);
            this.getLevel().setBlock(this, Block.get(198));
            if (player != null) {
                player.getLevel().addSound((Vector3)player, Sound.STEP_GRASS);
            }
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 2) {
            int n2 = Utils.rand((int)this.x - 1, (int)this.x + 1);
            int n3 = Utils.rand((int)this.y - 2, (int)this.y + 2);
            int n4 = Utils.rand((int)this.z - 1, (int)this.z + 1);
            Block block = this.getLevel().getBlock(n2, n3, n4);
            if (block.getId() == 3 && block.getDamage() == 0) {
                if (block.up() instanceof BlockAir) {
                    BlockSpreadEvent blockSpreadEvent = new BlockSpreadEvent(block, this, Block.get(2));
                    Server.getInstance().getPluginManager().callEvent(blockSpreadEvent);
                    if (!blockSpreadEvent.isCancelled()) {
                        this.getLevel().setBlock(block, blockSpreadEvent.getNewState());
                    }
                }
            } else if (block.getId() == 2 && block.up() instanceof BlockSolid) {
                BlockSpreadEvent blockSpreadEvent = new BlockSpreadEvent(block, this, Block.get(3));
                Server.getInstance().getPluginManager().callEvent(blockSpreadEvent);
                if (!blockSpreadEvent.isCancelled()) {
                    this.getLevel().setBlock(block, blockSpreadEvent.getNewState());
                }
            }
        }
        return 0;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRASS_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public int getFullId() {
        return this.getId() << 4;
    }

    @Override
    public void setDamage(int n) {
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        return new Item[]{new ItemBlock(Block.get(3))};
    }
}

