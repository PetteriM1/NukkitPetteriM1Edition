package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityDropper;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Faceable;

import java.util.Map;

public class BlockDropper extends BlockSolidMeta implements Faceable {

    public BlockDropper() {
        this(0);
    }

    public BlockDropper(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DROPPER;
    }

    @Override
    public String getName() {
        return "Dropper";
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 17.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new Item[]{
                    toItem()
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public boolean canBeActivated() {
        return true;
    }
    
    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        int[] faces = {2, 5, 3, 4};
        this.setDamage(faces[player != null ? player.getDirection().getHorizontalIndex() : 0]);

        this.getLevel().setBlock(block, this, true, true);
        CompoundTag nbt = new CompoundTag("")
                .putString("id", BlockEntity.DROPPER)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z);

        if (item.hasCustomName()) {
            nbt.putString("CustomName", item.getCustomName());
        }

        if (item.hasCustomBlockData()) {
            Map<String, Tag> customData = item.getCustomBlockData().getTags();
            for (Map.Entry<String, Tag> tag : customData.entrySet()) {
                nbt.put(tag.getKey(), tag.getValue());
            }
        }

        new BlockEntityDropper(this.getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player != null) {

            BlockEntity t = this.getLevel().getBlockEntity(this);
            BlockEntityDropper inv;
            if (t instanceof BlockEntityDropper) {
                inv = (BlockEntityDropper) t;
            } else {
                CompoundTag nbt = new CompoundTag("")
                        .putString("id", BlockEntity.DROPPER)
                        .putInt("x", (int) this.x)
                        .putInt("y", (int) this.y)
                        .putInt("z", (int) this.z);
                inv = new BlockEntityDropper(this.getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);
            }

            player.addWindow(inv.getInventory());
        }

        return true;
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 0x7);
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, 0);
    }
}
