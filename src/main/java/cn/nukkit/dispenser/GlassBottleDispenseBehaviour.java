package cn.nukkit.dispenser;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBeehive;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class GlassBottleDispenseBehaviour extends DefaultDispenseBehavior {

    @Override
    public Item dispense(BlockDispenser block, BlockFace face, Item item) {
        Block target = block.getSide(face);

        if (target instanceof BlockBeehive) {
            if (target.onActivate(item, null)) {
                return Item.get(Item.HONEY_BOTTLE);
            }
            return item;
        }

        return super.dispense(block, face, item);
    }
}
