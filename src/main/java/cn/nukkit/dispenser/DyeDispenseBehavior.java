package cn.nukkit.dispenser;

import cn.nukkit.block.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.math.BlockFace;

public class DyeDispenseBehavior extends DefaultDispenseBehavior {

    @Override
    public Item dispense(BlockDispenser block, BlockFace face, Item item) {
        Block target = block.getSide(face);

        if (item.getDamage() == ItemDye.BONE_MEAL) {
            if (target instanceof BlockCrops || target instanceof BlockSapling || target instanceof BlockTallGrass
                    || target instanceof BlockDoublePlant || target instanceof BlockMushroom) {
                target.onActivate(item);

            }

            return null;
        }

        return super.dispense(block, face, item);
    }
}
