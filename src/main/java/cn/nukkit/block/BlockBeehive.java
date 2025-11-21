package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBeehive;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;

import java.util.Map;

public class BlockBeehive extends BlockSolidMeta implements Faceable {

    private static final short[] FACES = {2, 3, 0, 1};
    private static final int FACING_MASK = 0b11;
    private static final int HONEY_MASK = 0b11100;

    @SuppressWarnings("unused")
    public BlockBeehive() {
        this(0);
    }

    public BlockBeehive(int meta) {
        super(meta);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & FACING_MASK);
    }

    @Override
    public int getBurnAbility() {
        return 20;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public int getComparatorInputOverride() {
        return getHoneyLevel();
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    public int getHoneyLevel() {
        return (this.getDamage() & HONEY_MASK) >> 2;
    }

    public void setHoneyLevel(int honeyLevel) {
        honeyLevel = NukkitMath.clamp(honeyLevel, 0, 5);
        setDamage((this.getDamage() & ~HONEY_MASK) | ((honeyLevel << 2) & HONEY_MASK));
    }

    @Override
    public int getId() {
        return BEEHIVE;
    }

    @Override
    public String getName() {
        return "Beehive";
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    public boolean isEmpty() {
        return getHoneyLevel() == 0;
    }

    public boolean isFull() {
        return getHoneyLevel() == 5;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (isFull()) {
            if (item.getId() == ItemID.SHEARS) {
                BlockEntityBeehive beehive = (BlockEntityBeehive) level.getBlockEntityIfLoaded(this);
                if (beehive == null) {
                    return false;
                } else {
                    this.setHoneyLevel(0);
                    this.getLevel().setBlock(this, this, true, true);
                    if (player != null && level.getServer().getDifficulty() > 0 && !player.isCreative() && !(down() instanceof BlockCampfire)) {
                        beehive.angerBees(player);
                    }
                }
                this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BLOCK_BEEHIVE_SHEAR);
                item.useOn(this);
                for (int i = 0; i < 3; ++i) {
                    level.dropItem(this, Item.get(ItemID.HONEYCOMB));
                }
                return true;
            } else if (item.getId() == ItemID.GLASS_BOTTLE) {
                BlockEntityBeehive beehive = (BlockEntityBeehive) level.getBlockEntityIfLoaded(this);
                if (beehive == null) {
                    return false;
                } else {
                    this.setHoneyLevel(0);
                    this.getLevel().setBlock(this, this, true, true);
                    if (player != null && level.getServer().getDifficulty() > 0 && !player.isCreative() && !(down() instanceof BlockCampfire)) {
                        beehive.angerBees(player);
                    }
                }
                this.getLevel().addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_BLOCK_BEEHIVE_DRIP);
                if (player != null) {
                    if (item.count == 1) {
                        player.getInventory().setItemInHand(Item.get(Item.HONEY_BOTTLE));
                    } else if (item.count > 1) {
                        item.count--;
                        player.getInventory().setItemInHand(item);
                        Item potion = Item.get(Item.HONEY_BOTTLE);
                        if (player.getInventory().canAddItem(potion)) {
                            player.getInventory().addItem(potion);
                        } else {
                            player.getLevel().dropItem(player.add(0, 1.3, 0), potion, player.getDirectionVector().multiply(0.4));
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        this.setDamage(FACES[player != null ? player.getDirection().getHorizontalIndex() : 0]);
        this.getLevel().setBlock(this, this, true, true);

        CompoundTag cData = item.getNamedTag();
        if (cData != null) {
            cData = (CompoundTag) cData.get("BeehiveTag");
        }

        CompoundTag nbt = new CompoundTag()
                .putString("id", BlockEntity.BEEHIVE)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z);

        if (cData != null) {
            int honeyLevel = cData.getByte("HoneyLevel");
            if (honeyLevel != 0) {
                this.setHoneyLevel(honeyLevel);
            }

            Map<String, Tag> customData = cData.getTags();
            for (Map.Entry<String, Tag> tag : customData.entrySet()) {
                nbt.put(tag.getKey(), tag.getValue());
            }
        }

        // We somehow lost the data so need to spawn new bees if this tag doesn't exist
        nbt.putList(new ListTag<>("Occupants"));

        BlockEntity.createBlockEntity(BlockEntity.BEEHIVE, this.getChunk(), nbt);
        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(this.getId(), 0), 0);
    }
}
