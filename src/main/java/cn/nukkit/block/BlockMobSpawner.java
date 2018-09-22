package cn.nukkit.block;

import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.Player;
import cn.nukkit.math.BlockFace;
import cn.nukkit.blockentity.BlockEntitySpawner;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * Created by Pub4Game on 27.12.2015.
 */
public class BlockMobSpawner extends BlockSolid {

    public BlockMobSpawner() {
    }

    @Override
    public String getName() {
        return "Monster Spawner";
    }

    @Override
    public int getId() {
        return MONSTER_SPAWNER;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public double getResistance() {
        return 25;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        return this.place(item, block, target, face, fx, fy, fz, null);
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, double fx, double fy, double fz, Player player) {
        if (!super.place(item, block, target, face, fx, fy, fz, player)) return false;
        CompoundTag nbt = new CompoundTag();
        new BlockEntitySpawner(block.getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) == null) return new Item[0];
        return new Item[]{Item.get(Item.MONSTER_SPAWNER)};
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
