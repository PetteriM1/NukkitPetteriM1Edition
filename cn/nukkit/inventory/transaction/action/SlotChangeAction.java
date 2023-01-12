/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction.action;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.BrewingInventory;
import cn.nukkit.inventory.FurnaceInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerUIComponent;
import cn.nukkit.inventory.PlayerUIInventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;
import java.util.HashSet;

public class SlotChangeAction
extends InventoryAction {
    protected Inventory inventory;
    private final int a;

    public SlotChangeAction(Inventory inventory, int n, Item item, Item item2) {
        super(item, item2);
        this.inventory = inventory;
        this.a = n;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getSlot() {
        return this.a;
    }

    @Override
    public boolean isValid(Player player) {
        if (this.inventory == null) {
            return false;
        }
        if (!(player.isCreative() || this.inventory instanceof PlayerUIComponent || this.inventory instanceof PlayerUIInventory || this.inventory.getViewers().contains(player))) {
            player.getServer().getLogger().debug(player.getName() + ": got SlotChangeAction but player is not a viewer of " + this.inventory);
            return false;
        }
        if (this.inventory.getHolder() instanceof BlockEntityContainer && (((BlockEntity)((Object)this.inventory.getHolder())).distanceSquared(player) > 4096.0 || !((BlockEntity)((Object)this.inventory.getHolder())).getLevel().equals(player.getLevel()))) {
            player.getServer().getLogger().debug(player.getName() + ": got SlotChangeAction but player is too far away from the holder of " + this.inventory);
            return false;
        }
        if (this.inventory.getHolder() != player && this.inventory.getHolder() instanceof Entity && (((Entity)((Object)this.inventory.getHolder())).distanceSquared(player) > 4096.0 || !((Entity)((Object)this.inventory.getHolder())).getLevel().equals(player.getLevel()))) {
            player.getServer().getLogger().debug(player.getName() + ": got SlotChangeAction but player is too far away from the holder of " + this.inventory);
            return false;
        }
        Item item = this.inventory.getItem(this.a);
        return item.equalsExact(this.sourceItem);
    }

    @Override
    public boolean execute(Player player) {
        return this.inventory.setItem(this.a, this.targetItem, false);
    }

    @Override
    public void onExecuteSuccess(Player player) {
        int n;
        HashSet<Player> hashSet = new HashSet<Player>(this.inventory.getViewers());
        hashSet.remove(player);
        this.inventory.sendSlot(this.a, hashSet);
        if (this.inventory instanceof FurnaceInventory && this.a == 2) {
            BlockEntityFurnace blockEntityFurnace = ((FurnaceInventory)this.inventory).getHolder();
            if (blockEntityFurnace != null && !blockEntityFurnace.closed) {
                blockEntityFurnace.releaseExperience();
            }
            switch (this.getSourceItemUnsafe().getId()) {
                case 265: {
                    player.awardAchievement("acquireIron");
                    break;
                }
                case 350: {
                    player.awardAchievement("cookFish");
                }
            }
        } else if (this.inventory instanceof BrewingInventory && this.a >= 1 && this.a <= 3 && ((n = this.getSourceItemUnsafe().getId()) == 373 || n == 438 || n == 441) && this.getSourceItemUnsafe().getDamage() != 0) {
            player.awardAchievement("potion");
        }
    }

    @Override
    public void onExecuteFail(Player player) {
        this.inventory.sendSlot(this.a, player);
    }

    @Override
    public void onAddToTransaction(InventoryTransaction inventoryTransaction) {
        inventoryTransaction.addInventory(this.inventory);
    }
}

