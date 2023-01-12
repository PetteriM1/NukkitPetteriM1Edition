/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockIceFrosted;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.block.BlockWater;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityCampfire;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.inventory.CampfireInventory;
import cn.nukkit.inventory.CampfireRecipe;
import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemCampfire;
import cn.nukkit.item.ItemCoal;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockCampfire
extends BlockTransparentMeta
implements Faceable {
    public BlockCampfire() {
        this(0);
    }

    public BlockCampfire(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Campfire";
    }

    @Override
    public int getId() {
        return 464;
    }

    @Override
    public int getLightLevel() {
        return this.isExtinguished() ? 0 : 15;
    }

    @Override
    public double getResistance() {
        return 10.0;
    }

    @Override
    public double getHardness() {
        return 2.0;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{new ItemCoal((Integer)0, 1 + ThreadLocalRandom.current().nextInt(1))};
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        boolean bl;
        if (this.down().getId() == 464) {
            return false;
        }
        this.setDamage(player != null ? player.getDirection().getOpposite().getHorizontalIndex() : 0);
        boolean bl2 = bl = block instanceof BlockWater && block.getDamage() == 0 || block.getDamage() >= 8 || block instanceof BlockIceFrosted;
        if (bl) {
            this.setExtinguished(true);
            this.level.addSound((Vector3)this, Sound.RANDOM_FIZZ);
        }
        this.level.setBlock(block, this, true, false);
        this.b(item);
        this.level.updateAround(this);
        return true;
    }

    private BlockEntityCampfire b(Item item) {
        CompoundTag compoundTag = new CompoundTag().putString("id", "Campfire").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        if (item.hasCustomBlockData()) {
            Map<String, Tag> map = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> entry : map.entrySet()) {
                compoundTag.put(entry.getKey(), entry.getValue());
            }
        }
        return (BlockEntityCampfire)BlockEntity.createBlockEntity("Campfire", this.getChunk(), compoundTag, new Object[0]);
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (!this.isExtinguished() && !entity.isSneaking()) {
            entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.FIRE, 1.0f));
        }
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (item.getId() == 0 || item.getCount() <= 0) {
            return false;
        }
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityCampfire)) {
            return false;
        }
        boolean bl = false;
        if (item.isShovel() && !this.isExtinguished()) {
            this.setExtinguished(true);
            this.level.setBlock(this, this, true, true);
            this.level.addSound((Vector3)this, Sound.RANDOM_FIZZ);
            bl = true;
        } else if (item.getId() == 259 || item.hasEnchantment(13)) {
            item.useOn(this);
            this.setExtinguished(false);
            this.level.setBlock(this, this, true, true);
            blockEntity.scheduleUpdate();
            this.level.addLevelSoundEvent(this, 50);
            bl = true;
        }
        BlockEntityCampfire blockEntityCampfire = (BlockEntityCampfire)blockEntity;
        Item item2 = item.clone();
        item2.setCount(1);
        CampfireInventory campfireInventory = blockEntityCampfire.getInventory();
        if (campfireInventory.canAddItem(item2)) {
            CampfireRecipe campfireRecipe = this.level.getServer().getCraftingManager().matchCampfireRecipe(item2);
            if (campfireRecipe != null) {
                campfireInventory.addItem(item2);
                item.setCount(item.getCount() - 1);
                return true;
            }
        } else {
            return false;
        }
        return bl;
    }

    @Override
    public double getMaxY() {
        return this.y + 0.5;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.SPRUCE_BLOCK_COLOR;
    }

    public boolean isExtinguished() {
        return (this.getDamage() & 4) == 4;
    }

    public void setExtinguished(boolean bl) {
        this.setDamage(this.getDamage() & 3 | (bl ? 4 : 0));
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 3);
    }

    public void setBlockFace(BlockFace blockFace) {
        if (blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) {
            return;
        }
        this.setDamage(this.getDamage() & 4 | blockFace.getHorizontalIndex());
    }

    @Override
    public Item toItem() {
        return new ItemCampfire();
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (blockEntity instanceof BlockEntityCampfire) {
            return ContainerInventory.calculateRedstone(((BlockEntityCampfire)blockEntity).getInventory());
        }
        return super.getComparatorInputOverride();
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

