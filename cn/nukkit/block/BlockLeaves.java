/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.event.block.LeavesDecayEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Hash;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;

public class BlockLeaves
extends BlockTransparentMeta {
    public static final int OAK = 0;
    public static final int SPRUCE = 1;
    public static final int BIRCH = 2;
    public static final int JUNGLE = 3;

    public BlockLeaves() {
        this(0);
    }

    public BlockLeaves(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 18;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public int getToolType() {
        return 6;
    }

    @Override
    public String getName() {
        String[] stringArray = new String[]{"Oak Leaves", "Spruce Leaves", "Birch Leaves", "Jungle Leaves"};
        return stringArray[this.getDamage() & 3];
    }

    @Override
    public int getBurnChance() {
        return 30;
    }

    @Override
    public int getBurnAbility() {
        return 60;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.setPersistent(true);
        this.getLevel().setBlock(this, this, true);
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, this.getDamage() & 3, 1);
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isShears()) {
            return new Item[]{this.toItem()};
        }
        if (item.hasEnchantment(16)) {
            return new Item[]{this.toItem()};
        }
        if (this.canDropApple() && Utils.random.nextInt(200) == 0) {
            return new Item[]{Item.get(260)};
        }
        if (Utils.random.nextInt(20) == 0) {
            if (Utils.random.nextBoolean()) {
                return new Item[]{Item.get(280, 0, Utils.random.nextInt(1, 2))};
            }
            if ((this.getDamage() & 3) != 3 || Utils.random.nextInt(20) == 0) {
                return new Item[]{this.getSapling()};
            }
        }
        return new Item[0];
    }

    @Override
    public int onUpdate(int n) {
        if (n == 2 && !this.isPersistent() && !this.isCheckDecay()) {
            this.setCheckDecay(true);
            this.getLevel().setBlock((int)this.x, (int)this.y, (int)this.z, this, false, false, false);
        } else if (n == 2 && this.isCheckDecay() && !this.isPersistent()) {
            this.setDamage(this.getDamage() & 3);
            LeavesDecayEvent leavesDecayEvent = new LeavesDecayEvent(this);
            Server.getInstance().getPluginManager().callEvent(leavesDecayEvent);
            if (leavesDecayEvent.isCancelled() || this.a(this, new LongArraySet(), 0, 0).booleanValue()) {
                this.getLevel().setBlock((int)this.x, (int)this.y, (int)this.z, this, false, false, false);
            } else {
                this.getLevel().useBreakOn(this);
                return 1;
            }
        }
        return 0;
    }

    private Boolean a(Block block, LongSet longSet, Integer n, Integer n2) {
        return this.a(block, longSet, n, n2, null);
    }

    private Boolean a(Block block, LongSet longSet, Integer n, Integer n2, BlockFace blockFace) {
        n2 = n2 + 1;
        long l = Hash.hashBlock((int)block.x, (int)block.y, (int)block.z);
        if (longSet.contains(l)) {
            return false;
        }
        if (block.getId() == 17 || block.getId() == 162) {
            return true;
        }
        if ((block.getId() == 18 || block.getId() == 161) && n < 6) {
            longSet.add(l);
            int n3 = block.down().getId();
            if (n3 == 17 || n3 == 162) {
                return true;
            }
            if (blockFace == null) {
                for (int k = 2; k <= 5; ++k) {
                    if (!this.a(block.getSide(BlockFace.fromIndex(k)), longSet, n + 1, n2, BlockFace.fromIndex(k)).booleanValue()) continue;
                    return true;
                }
            } else {
                switch (blockFace) {
                    case NORTH: {
                        if (this.a(block.getSide(BlockFace.NORTH), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                        if (this.a(block.getSide(BlockFace.WEST), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                        if (!this.a(block.getSide(BlockFace.EAST), longSet, n + 1, n2, blockFace).booleanValue()) break;
                        return true;
                    }
                    case SOUTH: {
                        if (this.a(block.getSide(BlockFace.SOUTH), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                        if (this.a(block.getSide(BlockFace.WEST), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                        if (!this.a(block.getSide(BlockFace.EAST), longSet, n + 1, n2, blockFace).booleanValue()) break;
                        return true;
                    }
                    case WEST: {
                        if (this.a(block.getSide(BlockFace.NORTH), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                        if (this.a(block.getSide(BlockFace.SOUTH), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                        if (this.a(block.getSide(BlockFace.WEST), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                    }
                    case EAST: {
                        if (this.a(block.getSide(BlockFace.NORTH), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                        if (this.a(block.getSide(BlockFace.SOUTH), longSet, n + 1, n2, blockFace).booleanValue()) {
                            return true;
                        }
                        if (!this.a(block.getSide(BlockFace.EAST), longSet, n + 1, n2, blockFace).booleanValue()) break;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckDecay() {
        return (this.getDamage() & 8) != 0;
    }

    public void setCheckDecay(boolean bl) {
        if (bl) {
            this.setDamage(this.getDamage() | 8);
        } else {
            this.setDamage(this.getDamage() & 0xFFFFFFF7);
        }
    }

    public boolean isPersistent() {
        return (this.getDamage() & 4) != 0;
    }

    public void setPersistent(boolean bl) {
        if (bl) {
            this.setDamage(this.getDamage() | 4);
        } else {
            this.setDamage(this.getDamage() & 0xFFFFFFFB);
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    protected boolean canDropApple() {
        return (this.getDamage() & 3) == 0;
    }

    protected Item getSapling() {
        return Item.get(6, this.getDamage() & 3);
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

