/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFallableMeta;
import cn.nukkit.inventory.AnvilInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

public class BlockAnvil
extends BlockFallableMeta
implements Faceable {
    private static final int[] e = new int[]{1, 2, 3, 0};
    private static final String[] d = new String[]{"Anvil", "Anvil", "Anvil", "Anvil", "Slighty Damaged Anvil", "Slighty Damaged Anvil", "Slighty Damaged Anvil", "Slighty Damaged Anvil", "Very Damaged Anvil", "Very Damaged Anvil", "Very Damaged Anvil", "Very Damaged Anvil"};

    public BlockAnvil() {
        this(0);
    }

    public BlockAnvil(int n) {
        super(n);
    }

    @Override
    public int getFullId() {
        return 2320 + this.getDamage();
    }

    @Override
    public int getId() {
        return 145;
    }

    @Override
    public boolean canBeActivated() {
        return Server.getInstance().anvilsEnabled;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public double getHardness() {
        return 5.0;
    }

    @Override
    public double getResistance() {
        return 6000.0;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public String getName() {
        return d[this.getDamage() > 11 ? 0 : this.getDamage()];
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        int n = this.getDamage();
        this.setDamage(e[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        if (n >= 4 && n <= 7) {
            this.setDamage(this.getDamage() | 4);
        } else if (n >= 8 && n <= 11) {
            this.setDamage(this.getDamage() | 8);
        }
        this.getLevel().setBlock(block, this, true);
        this.getLevel().addLevelEvent(this, 1022);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {
            player.addWindow(new AnvilInventory(player.getUIInventory(), this), 2);
        }
        return true;
    }

    @Override
    public Item toItem() {
        int n = this.getDamage();
        if (n >= 4 && n <= 7) {
            return new ItemBlock((Block)this, this.getDamage() & 4);
        }
        if (n >= 8 && n <= 11) {
            return new ItemBlock((Block)this, this.getDamage() & 8);
        }
        return new ItemBlock(this);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 3);
    }

    @Override
    public double getMinX() {
        return this.x + (this.getBlockFace().getAxis() == BlockFace.Axis.X ? 0.0 : 0.125);
    }

    @Override
    public double getMinZ() {
        return this.z + (this.getBlockFace().getAxis() == BlockFace.Axis.Z ? 0.0 : 0.125);
    }

    @Override
    public double getMaxX() {
        return this.x + (this.getBlockFace().getAxis() == BlockFace.Axis.X ? 1.0 : 0.875);
    }

    @Override
    public double getMaxZ() {
        return this.z + (this.getBlockFace().getAxis() == BlockFace.Axis.Z ? 1.0 : 0.875);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}

