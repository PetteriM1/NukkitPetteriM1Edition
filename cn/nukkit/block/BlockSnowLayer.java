/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFallableMeta;
import cn.nukkit.event.block.BlockFadeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

public class BlockSnowLayer
extends BlockFallableMeta {
    public BlockSnowLayer() {
        this(0);
    }

    public BlockSnowLayer(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Top Snow";
    }

    @Override
    public int getId() {
        return 78;
    }

    @Override
    public double getHardness() {
        return 0.1;
    }

    @Override
    public double getResistance() {
        return 0.5;
    }

    @Override
    public int getToolType() {
        return 2;
    }

    @Override
    public boolean canBeReplaced() {
        return (this.getDamage() & 7) != 7;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (this.a()) {
            this.getLevel().setBlock(block, this, true);
            return true;
        }
        return false;
    }

    private boolean a() {
        Block block = this.down();
        return block.getId() != 79 && block.getId() != 174 && block.getId() != 207 && (block.isSolid() || (this.getDamage() & 7) == 7);
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if ((this.getDamage() & 7) != 7 || this.up().getId() != 78) {
                super.onUpdate(n);
            }
            if (this.level.getBlockIdAt(this.getFloorX(), this.getFloorY(), this.getFloorZ()) == 78 && !this.a()) {
                this.level.useBreakOn(this, null, null, true);
                if (this.level.getGameRules().getBoolean(GameRule.DO_TILE_DROPS)) {
                    this.level.dropItem(this, this.toItem());
                }
            }
        } else if (n == 2 && this.getLevel().getBlockLightAt((int)this.x, (int)this.y, (int)this.z) >= 10) {
            BlockFadeEvent blockFadeEvent = new BlockFadeEvent(this, (this.getDamage() & 7) > 0 ? BlockSnowLayer.get(78, this.getDamage() - 1) : BlockSnowLayer.get(0));
            this.level.getServer().getPluginManager().callEvent(blockFadeEvent);
            if (!blockFadeEvent.isCancelled()) {
                this.level.setBlock(this, blockFadeEvent.getNewState(), true);
            }
            return 1;
        }
        return 0;
    }

    @Override
    public Item toItem() {
        return Item.get(332);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShovel() && item.getTier() >= 1) {
            Item item2 = this.toItem();
            int n = this.getDamage() & 7;
            item2.setCount(n < 3 ? 1 : (n < 5 ? 2 : (n == 7 ? 4 : 3)));
            return new Item[]{item2};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SNOW_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return (this.getDamage() & 7) != 7;
    }

    @Override
    public boolean canBeFlowedInto() {
        return true;
    }

    @Override
    public boolean isSolid() {
        return (this.getDamage() & 7) == 7;
    }

    @Override
    public double getMaxY() {
        int n = this.getDamage() & 7;
        return n < 3 ? this.y : (n == 7 ? this.y + 1.0 : this.y + 0.5);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.isShovel() && (player == null || (player.gamemode & 2) == 0)) {
            item.useOn(this);
            this.level.useBreakOn(this, item.clone().clearNamedTag(), null, true);
            return true;
        }
        if (item.getId() == 78 && (player == null || (player.gamemode & 2) == 0)) {
            if ((this.getDamage() & 7) != 7) {
                this.setDamage(this.getDamage() + 1);
                this.level.setBlock(this, this, true);
                if (player != null && (player.gamemode & 1) == 0) {
                    --item.count;
                }
                return true;
            }
            this.level.setBlock(this, this, true);
        }
        return false;
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

