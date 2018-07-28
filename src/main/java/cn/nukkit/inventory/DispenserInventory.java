package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityDispenser;

/**
 * Created by PetteriM1
 */
public class DispenserInventory extends ContainerInventory {

    public DispenserInventory(BlockEntityDispenser dispenser) {
        super(dispenser, InventoryType.DISPENSER);
    }

    @Override
    public BlockEntityDispenser getHolder() {
        return (BlockEntityDispenser) super.getHolder();
    }
}
