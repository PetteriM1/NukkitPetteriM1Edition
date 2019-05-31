package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class EnchantInventory extends ContainerInventory {

    public EnchantInventory(Position position) {
        super(null, InventoryType.ENCHANT_TABLE);
        this.holder = new FakeBlockMenu(this, position);
    }

    @Override
    public FakeBlockMenu getHolder() {
        return (FakeBlockMenu) this.holder;
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        if (this.getViewers().isEmpty()) {
            for (int i = 0; i < 2; ++i) {
                who.getInventory().addItem(this.getItem(i));
                this.clear(i);
            }
        }
    }
}
