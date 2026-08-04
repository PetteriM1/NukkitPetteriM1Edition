package cn.nukkit.inventory;

import cn.nukkit.entity.passive.EntityDonkey;

public class DonkeyInventory extends ContainerInventory {

    public DonkeyInventory(EntityDonkey holder) {
        super(holder, InventoryType.DONKEY);
    }

    @Override
    public EntityDonkey getHolder() {
        return (EntityDonkey) super.getHolder();
    }
}
