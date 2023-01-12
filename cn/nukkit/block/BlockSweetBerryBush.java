/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSweetBerries;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import java.util.concurrent.ThreadLocalRandom;

public class BlockSweetBerryBush
extends BlockFlowable {
    public BlockSweetBerryBush() {
        this(0);
    }

    public BlockSweetBerryBush(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Sweet Berry Bush";
    }

    @Override
    public int getId() {
        return 462;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return this.getDamage() == 0 ? 0.0 : 0.25;
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
    public Item toItem() {
        return new ItemSweetBerries();
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (this.getDamage() > 0) {
            entity.resetFallDistance();
            if (!entity.isSneaking() && ThreadLocalRandom.current().nextInt(20) == 0 && entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.CONTACT, 1.0f))) {
                this.getLevel().addSound((Vector3)entity, Sound.BLOCK_SWEET_BERRY_BUSH_HURT);
            }
        }
    }

    @Override
    public boolean hasEntityCollision() {
        return this.getDamage() > 0;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        if (this.getDamage() > 0) {
            return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 1.0, this.z + 1.0);
        }
        return null;
    }

    @Override
    public Item[] getDrops(Item item) {
        int n = MathHelper.clamp(this.getDamage(), 0, 3);
        int n2 = 1;
        if (n > 1) {
            n2 = 1 + ThreadLocalRandom.current().nextInt(2);
            if (n == 3) {
                ++n2;
            }
        }
        return new Item[]{new ItemSweetBerries((Integer)0, n2)};
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        int n = MathHelper.clamp(this.getDamage(), 0, 3);
        if (n < 3 && item.getId() == 351 && item.getDamage() == DyeColor.WHITE.getDyeData()) {
            BlockSweetBerryBush blockSweetBerryBush = (BlockSweetBerryBush)this.clone();
            blockSweetBerryBush.setDamage(blockSweetBerryBush.getDamage() + 1);
            if (blockSweetBerryBush.getDamage() > 3) {
                blockSweetBerryBush.setDamage(3);
            }
            BlockGrowEvent blockGrowEvent = new BlockGrowEvent(this, blockSweetBerryBush);
            this.getLevel().getServer().getPluginManager().callEvent(blockGrowEvent);
            if (blockGrowEvent.isCancelled()) {
                return false;
            }
            this.getLevel().setBlock(this, blockGrowEvent.getNewState(), false, true);
            this.level.addParticle(new BoneMealParticle(this));
            if (player != null && (player.gamemode & 1) == 0) {
                --item.count;
            }
            return true;
        }
        if (n < 2) {
            return true;
        }
        int n2 = 1 + ThreadLocalRandom.current().nextInt(2);
        if (n == 3) {
            ++n2;
        }
        this.getLevel().setBlock(this, new BlockSweetBerryBush(1), true, true);
        Item[] itemArray = new Item[]{new ItemSweetBerries((Integer)0, n2)};
        Position position = this.add(0.5, 0.5, 0.5);
        for (Item item2 : itemArray) {
            this.getLevel().dropItem(position, item2);
        }
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            if (!BlockSweetBerryBush.isSupportValid(this.down())) {
                this.getLevel().useBreakOn(this);
                return 1;
            }
        } else if (n == 2) {
            BlockGrowEvent blockGrowEvent;
            if (this.getDamage() < 3 && ThreadLocalRandom.current().nextInt(5) == 0 && !(blockGrowEvent = new BlockGrowEvent(this, Block.get(this.getId(), this.getDamage() + 1))).isCancelled()) {
                this.getLevel().setBlock(this, blockGrowEvent.getNewState(), true, true);
            }
            return n;
        }
        return 0;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        if (block2.getId() == 462 || block.getId() != 0) {
            return false;
        }
        if (BlockSweetBerryBush.isSupportValid(this.down())) {
            this.getLevel().setBlock(block, this, true);
            return true;
        }
        return false;
    }

    public static boolean isSupportValid(Block block) {
        switch (block.getId()) {
            case 2: 
            case 3: 
            case 60: 
            case 110: 
            case 243: {
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.FOLIAGE_BLOCK_COLOR;
    }
}

