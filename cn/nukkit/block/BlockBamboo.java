/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBambooSapling;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.BlockColor;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class BlockBamboo
extends BlockTransparentMeta {
    public static final int LEAF_SIZE_NONE = 0;
    public static final int LEAF_SIZE_SMALL = 1;
    public static final int LEAF_SIZE_LARGE = 2;

    public BlockBamboo() {
        this(0);
    }

    public BlockBamboo(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 418;
    }

    @Override
    public String getName() {
        return "Bamboo";
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (this.a()) {
                this.level.scheduleUpdate(this, 0);
            }
            return n;
        }
        if (n == 3) {
            this.level.useBreakOn(this, null, null, true);
        } else if (n == 2) {
            Block block = this.up();
            if (this.getAge() == 0 && block.getId() == 0 && this.level.isAnimalSpawningAllowedByTime() && ThreadLocalRandom.current().nextInt(3) == 0) {
                this.grow(block);
            }
            return n;
        }
        return 0;
    }

    public boolean grow(Block block) {
        BlockBamboo blockBamboo = (BlockBamboo)Block.get(418);
        if (this.isThick()) {
            blockBamboo.setThick(true);
            blockBamboo.setLeafSize(2);
        } else {
            blockBamboo.setLeafSize(1);
        }
        BlockGrowEvent blockGrowEvent = new BlockGrowEvent(block, blockBamboo);
        this.level.getServer().getPluginManager().callEvent(blockGrowEvent);
        if (!blockGrowEvent.isCancelled()) {
            Block block2 = blockGrowEvent.getNewState();
            block2.x = this.x;
            block2.y = block.y;
            block2.z = this.z;
            block2.level = this.level;
            block2.place(this.toItem(), block, this, BlockFace.DOWN, 0.5, 0.5, 0.5, null);
            return true;
        }
        return false;
    }

    public int countHeight() {
        Optional<Block> optional;
        int n = 0;
        Block block2 = this;
        while ((optional = block2.down().first(block -> block.getId() == 418)).isPresent()) {
            block2 = optional.get();
            if (++n < 16) continue;
            break;
        }
        return n;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        int n;
        Cloneable cloneable;
        Block block3 = this.down();
        int n2 = block3.getId();
        if (n2 != 418 && n2 != 419) {
            BlockBambooSapling blockBambooSapling = (BlockBambooSapling)Block.get(419);
            blockBambooSapling.x = this.x;
            blockBambooSapling.y = this.y;
            blockBambooSapling.z = this.z;
            blockBambooSapling.level = this.level;
            return blockBambooSapling.place(item, block, block2, blockFace, d2, d3, d4, player);
        }
        boolean bl = true;
        if (n2 == 419) {
            if (player != null) {
                cloneable = new AnimatePacket();
                ((AnimatePacket)cloneable).action = AnimatePacket.Action.SWING_ARM;
                ((AnimatePacket)cloneable).eid = player.getId();
                this.getLevel().addChunkPacket(player.getChunkX(), player.getChunkZ(), (DataPacket)cloneable);
            }
            this.setLeafSize(1);
        }
        if (block3 instanceof BlockBamboo) {
            cloneable = (BlockBamboo)block3;
            bl = ((BlockBamboo)cloneable).getAge() == 0;
            boolean bl2 = ((BlockBamboo)cloneable).isThick();
            if (!bl2) {
                boolean bl3 = true;
                for (int k = 2; k <= 3; ++k) {
                    if (this.getSide(BlockFace.DOWN, k).getId() == 418) continue;
                    bl3 = false;
                }
                if (bl3) {
                    this.setThick(true);
                    this.setLeafSize(2);
                    ((BlockBamboo)cloneable).setLeafSize(1);
                    ((BlockBamboo)cloneable).setThick(true);
                    ((BlockBamboo)cloneable).setAge(1);
                    this.level.setBlock((Vector3)cloneable, (Block)cloneable, false, true);
                    while ((block3 = block3.down()) instanceof BlockBamboo) {
                        cloneable = (BlockBamboo)block3;
                        ((BlockBamboo)cloneable).setThick(true);
                        ((BlockBamboo)cloneable).setLeafSize(0);
                        ((BlockBamboo)cloneable).setAge(1);
                        this.level.setBlock((Vector3)cloneable, (Block)cloneable, false, true);
                    }
                } else {
                    this.setLeafSize(1);
                    ((BlockBamboo)cloneable).setAge(1);
                    this.level.setBlock((Vector3)cloneable, (Block)cloneable, false, true);
                }
            } else {
                this.setThick(true);
                this.setLeafSize(2);
                this.setAge(0);
                ((BlockBamboo)cloneable).setLeafSize(2);
                ((BlockBamboo)cloneable).setAge(1);
                this.level.setBlock((Vector3)cloneable, (Block)cloneable, false, true);
                block3 = ((Block)cloneable).down();
                if (block3 instanceof BlockBamboo) {
                    cloneable = (BlockBamboo)block3;
                    ((BlockBamboo)cloneable).setLeafSize(1);
                    ((BlockBamboo)cloneable).setAge(1);
                    this.level.setBlock((Vector3)cloneable, (Block)cloneable, false, true);
                    block3 = ((Block)cloneable).down();
                    if (block3 instanceof BlockBamboo) {
                        cloneable = (BlockBamboo)block3;
                        ((BlockBamboo)cloneable).setLeafSize(0);
                        ((BlockBamboo)cloneable).setAge(1);
                        this.level.setBlock((Vector3)cloneable, (Block)cloneable, false, true);
                    }
                }
            }
        } else if (this.a()) {
            return false;
        }
        int n3 = n = bl ? this.countHeight() : 0;
        if (!bl || n >= 15 || n >= 11 && ThreadLocalRandom.current().nextFloat() < 0.25f) {
            this.setAge(1);
        }
        this.level.setBlock(this, this, false, true);
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        BlockBamboo blockBamboo;
        int n;
        Optional<Block> optional = this.down().first(block -> block instanceof BlockBamboo);
        if (optional.isPresent() && (n = (blockBamboo = (BlockBamboo)optional.get()).countHeight()) < 15 && (n < 11 || !(ThreadLocalRandom.current().nextFloat() < 0.25f))) {
            blockBamboo.setAge(0);
            this.level.setBlock(blockBamboo, blockBamboo, false, true);
        }
        return super.onBreak(item);
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    private boolean a() {
        int n = this.down().getId();
        return n != 418 && n != 3 && n != 2 && n != 12 && n != 13 && n != 243 && n != 419;
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public double getResistance() {
        return 5.0;
    }

    public boolean isThick() {
        return (this.getDamage() & 1) == 1;
    }

    public void setThick(boolean bl) {
        this.setDamage(this.getDamage() & 0xE | (bl ? 1 : 0));
    }

    @Override
    public int getToolType() {
        return 4;
    }

    public int getLeafSize() {
        return this.getDamage() >> 1 & 3;
    }

    public void setLeafSize(int n) {
        n = MathHelper.clamp(n, 0, 2) & 3;
        this.setDamage(this.getDamage() & 9 | n << 1);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        boolean bl;
        boolean bl2 = bl = item.getId() == 351 && item.getDamage() == 15;
        if (bl || item.getBlockUnsafe() != null && item.getBlockUnsafe().getId() == 418) {
            int n;
            int n2;
            int n3 = (int)this.y;
            int n4 = 1;
            for (n2 = 1; n2 <= 16 && (n = this.level.getBlockIdAt(this.getFloorX(), this.getFloorY() - n2, this.getFloorZ())) == 418; ++n2) {
                ++n4;
            }
            for (n2 = 1; n2 <= 16 && (n = this.level.getBlockIdAt(this.getFloorX(), this.getFloorY() + n2, this.getFloorZ())) == 418; ++n2) {
                ++n3;
                ++n4;
            }
            if (bl && n4 >= 15) {
                return false;
            }
            n2 = 0;
            Block block = this.up(n3 - (int)this.y + 1);
            if (block.getId() == 0) {
                n2 = this.grow(block) ? 1 : 0;
            }
            if (n2 != 0) {
                if (player != null && player.isSurvival()) {
                    --item.count;
                }
                if (bl) {
                    this.level.addParticle(new BoneMealParticle(this));
                } else {
                    this.level.addSound((Vector3)block, Sound.BLOCK_BAMBOO_PLACE);
                }
            }
            return true;
        }
        return false;
    }

    public int getAge() {
        return (this.getDamage() & 8) >> 3;
    }

    public void setAge(int n) {
        n = MathHelper.clamp(n, 0, 1) << 3;
        this.setDamage(this.getDamage() & 7 | n);
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

