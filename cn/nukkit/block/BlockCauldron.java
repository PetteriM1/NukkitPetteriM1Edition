/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCauldronLava;
import cn.nukkit.block.BlockTransparentMeta;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityCauldron;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerBucketFillEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemBucket;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockCauldron
extends BlockTransparentMeta {
    private byte d;

    public BlockCauldron() {
        super(0);
    }

    public BlockCauldron(int n) {
        super(n);
    }

    @Override
    public int getId() {
        return 118;
    }

    @Override
    public String getName() {
        return "Cauldron Block";
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
        return 3;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    public boolean isFull() {
        return (this.getDamage() & 6) == 6;
    }

    public boolean isEmpty() {
        return this.getDamage() == 0;
    }

    public int getFillLevel() {
        return (this.getDamage() & 6) >> 1;
    }

    public void setFillLevel(int n) {
        n = MathHelper.clamp(n, 0, 3);
        this.setDamage(n << 1);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        BlockEntity blockEntity = this.level.getBlockEntity(this);
        if (!(blockEntity instanceof BlockEntityCauldron)) {
            return false;
        }
        BlockEntityCauldron blockEntityCauldron = (BlockEntityCauldron)blockEntity;
        switch (item.getId()) {
            case 325: {
                if (item.getDamage() == 0) {
                    if (!this.isFull() || blockEntityCauldron.isCustomColor() || blockEntityCauldron.hasPotion()) break;
                    ItemBucket itemBucket = (ItemBucket)item.clone();
                    itemBucket.setCount(1);
                    itemBucket.setDamage(8);
                    PlayerBucketFillEvent playerBucketFillEvent = new PlayerBucketFillEvent(player, this, null, item, itemBucket);
                    this.level.getServer().getPluginManager().callEvent(playerBucketFillEvent);
                    if (playerBucketFillEvent.isCancelled()) break;
                    this.replaceBucket(item, player, playerBucketFillEvent.getItem());
                    this.setFillLevel(0);
                    this.level.setBlock(this, this, true);
                    blockEntityCauldron.clearCustomColor();
                    this.getLevel().addSound((Vector3)this, Sound.CAULDRON_TAKEWATER);
                    break;
                }
                if (item.getDamage() != 8 && item.getDamage() != 10 || this.isFull() && !blockEntityCauldron.isCustomColor() && !blockEntityCauldron.hasPotion() && item.getDamage() == 8) break;
                ItemBucket itemBucket = (ItemBucket)item.clone();
                itemBucket.setCount(1);
                itemBucket.setDamage(0);
                PlayerBucketEmptyEvent playerBucketEmptyEvent = new PlayerBucketEmptyEvent(player, this, null, item, itemBucket);
                this.level.getServer().getPluginManager().callEvent(playerBucketEmptyEvent);
                if (playerBucketEmptyEvent.isCancelled()) break;
                if (player.isSurvival() || player.isAdventure()) {
                    this.replaceBucket(item, player, playerBucketEmptyEvent.getItem());
                }
                if (blockEntityCauldron.hasPotion()) {
                    this.clearWithFizz(blockEntityCauldron);
                    break;
                }
                if (item.getDamage() == 8) {
                    this.setFillLevel(3);
                    blockEntityCauldron.clearCustomColor();
                    this.level.setBlock(this, this, true);
                    this.getLevel().addSound((Vector3)this, Sound.CAULDRON_FILLWATER);
                    break;
                }
                if (this.isEmpty()) {
                    BlockCauldronLava blockCauldronLava = new BlockCauldronLava(14);
                    blockCauldronLava.setFillLevel(3);
                    this.level.setBlock(this, blockCauldronLava, true, true);
                    blockEntityCauldron.clearCustomColor();
                    this.getLevel().addSound((Vector3)this.add(0.5, 1.0, 0.5), Sound.BUCKET_EMPTY_LAVA);
                    break;
                }
                this.clearWithFizz(blockEntityCauldron);
                break;
            }
            case 351: {
                if (this.isEmpty() || blockEntityCauldron.hasPotion()) break;
                if (player.isSurvival() || player.isAdventure()) {
                    item.setCount(item.getCount() - 1);
                    player.getInventory().setItemInHand(item);
                }
                BlockColor blockColor = new ItemDye((Integer)item.getDamage()).getDyeColor().getColor();
                if (!blockEntityCauldron.isCustomColor()) {
                    blockEntityCauldron.setCustomColor(blockColor);
                } else {
                    BlockColor blockColor2 = blockEntityCauldron.getCustomColor();
                    BlockColor blockColor3 = new BlockColor(blockColor2.getRed() + (blockColor.getRed() - blockColor2.getRed() >> 1), blockColor2.getGreen() + (blockColor.getGreen() - blockColor2.getGreen() >> 1), blockColor2.getBlue() + (blockColor.getBlue() - blockColor2.getBlue() >> 1));
                    blockEntityCauldron.setCustomColor(blockColor3);
                }
                this.level.addSound((Vector3)this, Sound.CAULDRON_ADDDYE);
                break;
            }
            case 298: 
            case 299: 
            case 300: 
            case 301: 
            case 416: {
                if (this.isEmpty() || blockEntityCauldron.hasPotion()) break;
                CompoundTag compoundTag = item.getNamedTag();
                if (compoundTag == null) {
                    compoundTag = new CompoundTag();
                }
                compoundTag.putInt("customColor", blockEntityCauldron.getCustomColor().getRGB());
                item.setCompoundTag(compoundTag);
                player.getInventory().setItemInHand(item);
                this.setFillLevel(this.getFillLevel() - 1);
                this.level.setBlock(this, this, true, true);
                this.level.addSound((Vector3)this, Sound.CAULDRON_DYEARMOR);
                break;
            }
            case 373: 
            case 438: 
            case 441: {
                if (!this.isEmpty() && (blockEntityCauldron.hasPotion() ? blockEntityCauldron.getPotionId() != item.getDamage() : item.getDamage() != 0)) {
                    this.clearWithFizz(blockEntityCauldron);
                    this.a(item, player);
                    break;
                }
                if (this.isFull()) break;
                if (item.getDamage() != 0 && this.isEmpty()) {
                    blockEntityCauldron.setPotionId(item.getDamage());
                }
                blockEntityCauldron.setPotionType(item.getId() == 373 ? 0 : (item.getId() == 438 ? 1 : 2));
                blockEntityCauldron.spawnToAll();
                this.setFillLevel(this.getFillLevel() + 1);
                this.level.setBlock(this, this, true);
                this.a(item, player);
                this.getLevel().addSound((Vector3)this, Sound.CAULDRON_FILLPOTION);
                break;
            }
            case 374: {
                boolean bl;
                Item item2;
                int n;
                if (this.isEmpty()) break;
                int n2 = n = blockEntityCauldron.hasPotion() ? blockEntityCauldron.getPotionId() : 0;
                if (n == 0) {
                    item2 = Item.get(373);
                } else {
                    switch (blockEntityCauldron.getPotionType()) {
                        case 1: {
                            item2 = Item.get(438, n);
                            break;
                        }
                        case 2: {
                            item2 = Item.get(441, n);
                            break;
                        }
                        default: {
                            item2 = Item.get(373, n);
                        }
                    }
                }
                this.setFillLevel(this.getFillLevel() - 1);
                if (this.isEmpty()) {
                    blockEntityCauldron.setPotionId(65535);
                    blockEntityCauldron.clearCustomColor();
                }
                this.level.setBlock(this, this, true);
                boolean bl2 = bl = player.isSurvival() || player.isAdventure();
                if (bl && item.getCount() == 1) {
                    player.getInventory().setItemInHand(item2);
                } else if (item.getCount() > 1) {
                    if (bl) {
                        item.setCount(item.getCount() - 1);
                        player.getInventory().setItemInHand(item);
                    }
                    if (player.getInventory().canAddItem(item2)) {
                        player.getInventory().addItem(item2);
                    } else {
                        player.getLevel().dropItem(player.add(0.0, 1.3, 0.0), item2, player.getDirectionVector().multiply(0.4));
                    }
                }
                this.getLevel().addSound((Vector3)this, Sound.CAULDRON_TAKEPOTION);
                break;
            }
            case 262: {
                if (item.getDamage() > 1 || !blockEntityCauldron.hasPotion()) break;
                if (!player.isCreative() && item.getCount() == 1) {
                    item.setDamage(BlockCauldron.a(blockEntityCauldron.getPotionId()));
                    player.getInventory().setItemInHand(item);
                } else if (item.getCount() > 1) {
                    Item item3 = item.clone();
                    item3.setCount(1);
                    item3.setDamage(BlockCauldron.a(blockEntityCauldron.getPotionId()));
                    if (!player.isCreative()) {
                        item.setCount(item.getCount() - 1);
                        player.getInventory().setItemInHand(item);
                    }
                    if (player.getInventory().canAddItem(item3)) {
                        player.getInventory().addItem(item3);
                    } else {
                        player.getLevel().dropItem(player.add(0.0, 1.3, 0.0), item3, player.getDirectionVector().multiply(0.4));
                    }
                }
                this.setFillLevel(this.getFillLevel() - 1);
                if (this.isEmpty()) {
                    blockEntityCauldron.setPotionId(65535);
                    blockEntityCauldron.clearCustomColor();
                }
                this.level.setBlock(this, this, true);
                this.level.addLevelEvent(this.add(0.5, 0.375 + (double)this.getDamage() * 0.125, 0.5), 3502);
            }
            case 218: {
                if (this.isEmpty() || blockEntityCauldron.isCustomColor() || blockEntityCauldron.hasPotion()) break;
                player.getInventory().setItemInHand(Item.get(205).setCompoundTag(item.getCompoundTag()));
                this.setFillLevel(this.getFillLevel() - 1);
                this.level.setBlock(this, this, true);
                this.getLevel().addSound((Vector3)this, Sound.CAULDRON_TAKEPOTION);
                break;
            }
            default: {
                return true;
            }
        }
        this.level.updateComparatorOutputLevel(this);
        return true;
    }

    private static int a(int n) {
        int n2 = n & 0xFFFF;
        if (n2 < 5 || n2 > 43) {
            return 1;
        }
        return n2 < 43 ? n2 + 1 : n2;
    }

    protected void replaceBucket(Item item, Player player, Item item2) {
        if (player.isSurvival() || player.isAdventure()) {
            if (item.getCount() == 1) {
                player.getInventory().setItemInHand(item2);
            } else {
                item.setCount(item.getCount() - 1);
                if (player.getInventory().canAddItem(item2)) {
                    player.getInventory().addItem(item2);
                } else {
                    player.getLevel().dropItem(player.add(0.0, 1.3, 0.0), item2, player.getDirectionVector().multiply(0.4));
                }
            }
        }
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        CompoundTag compoundTag = new CompoundTag("").putString("id", "Cauldron").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putShort("PotionId", 65535).putByte("SplashPotion", 0);
        if (item.hasCustomBlockData()) {
            Map<String, Tag> map = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> entry : map.entrySet()) {
                compoundTag.put(entry.getKey(), entry.getValue());
            }
        }
        BlockEntity.createBlockEntity("Cauldron", this.getChunk(), compoundTag, new Object[0]);
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.getTier() >= 1) {
            return new Item[]{Item.get(380)};
        }
        return new Item[0];
    }

    @Override
    public int onUpdate(int n) {
        if (n == 2 && this.level.isRaining() && !this.isFull()) {
            if (this.d < 1) {
                this.d = (byte)(Utils.freezingBiomes.contains(this.level.getBiomeId((int)this.x, (int)this.z)) ? 2 : 1);
            }
            if (this.d == 1 && ThreadLocalRandom.current().nextInt(20) == 0 && this.level.canBlockSeeSky(this)) {
                this.setFillLevel(this.getFillLevel() + 1);
                this.getLevel().setBlock(this, this, true, true);
                return 2;
            }
        }
        return super.onUpdate(n);
    }

    @Override
    public Item toItem() {
        return Item.get(380);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        return this.getFillLevel();
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    private void a(Item item, Player player) {
        if (player.isSurvival() || player.isAdventure()) {
            if (item.getCount() == 1) {
                player.getInventory().setItemInHand(new ItemBlock(Block.get(0)));
            } else if (item.getCount() > 1) {
                item.setCount(item.getCount() - 1);
                player.getInventory().setItemInHand(item);
                Item item2 = Item.get(374);
                if (player.getInventory().canAddItem(item2)) {
                    player.getInventory().addItem(item2);
                } else {
                    player.getLevel().dropItem(player.add(0.0, 1.3, 0.0), item2, player.getDirectionVector().multiply(0.4));
                }
            }
        }
    }

    public void clearWithFizz(BlockEntityCauldron blockEntityCauldron) {
        this.setFillLevel(0);
        blockEntityCauldron.setPotionId(65535);
        blockEntityCauldron.setSplashPotion(false);
        blockEntityCauldron.clearCustomColor();
        this.level.setBlock(this, new BlockCauldron(0), true);
        this.level.addLevelSoundEvent(this, 27);
        this.getLevel().addParticle(new SmokeParticle(this.add(Math.random(), 1.2, Math.random())), null, 8);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GRAY_BLOCK_COLOR;
    }
}

