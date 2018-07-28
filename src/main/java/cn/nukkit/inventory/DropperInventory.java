package cn.nukkit.inventory;

import cn.nukkit.blockentity.BlockEntityDropper;

/**
 * Created by PetteriM1
 */
public class DropperInventory extends ContainerInventory {

    public DropperInventory(BlockEntityDropper dropper) {
        super(dropper, InventoryType.DROPPER);
    }

    @Override
    public BlockEntityDropper getHolder() {
        return (BlockEntityDropper) super.getHolder();
    }
}
