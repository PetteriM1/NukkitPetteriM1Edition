package cn.nukkit.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;

public class BlockMagma extends BlockSolid {

    public BlockMagma() {
        this(0);
    }

    public BlockMagma(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return MAGMA;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Magma Block";
    }

    @Override
    public int getLightLevel() {
        return 3;
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
    public void onEntityCollide(Entity entity) {
        if (!entity.hasEffect(Effect.FIRE_RESISTANCE)) {
            entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.LAVA, 1));
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BROWN_BLOCK_COLOR;
    }
}
