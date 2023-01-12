/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.block.AnvilDamageEvent;
import cn.nukkit.event.inventory.RepairItemEvent;
import cn.nukkit.inventory.AnvilInventory;
import cn.nukkit.inventory.FakeBlockMenu;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.a;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.RepairItemAction;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.Vector3;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RepairItemTransaction
extends InventoryTransaction {
    private Item b;
    private Item e;
    private Item c;
    private int d;

    public RepairItemTransaction(Player player, List<InventoryAction> list) {
        super(player, list);
    }

    @Override
    public boolean canExecute() {
        Inventory inventory = this.getSource().getWindowById(2);
        if (inventory == null) {
            return false;
        }
        AnvilInventory anvilInventory = (AnvilInventory)inventory;
        return this.b != null && this.c != null && this.b.equals(anvilInventory.getInputSlot(), true, true) && (!this.d() || this.e.equals(anvilInventory.getMaterialSlot(), true, true)) && this.b();
    }

    @Override
    public boolean execute() {
        Object object2;
        if (this.hasExecuted() || !this.canExecute()) {
            this.source.removeAllWindows(false);
            this.sendInventories();
            return false;
        }
        AnvilInventory anvilInventory = (AnvilInventory)this.getSource().getWindowById(2);
        if (anvilInventory.getCost() != this.d && !this.source.isCreative()) {
            this.source.getServer().getLogger().debug("Got unexpected cost " + anvilInventory.getCost() + " from " + this.source.getName() + "(expected " + this.d + ')');
        }
        RepairItemEvent repairItemEvent = new RepairItemEvent(anvilInventory, this.b, this.c, this.e, this.d, this.source);
        this.source.getServer().getPluginManager().callEvent(repairItemEvent);
        if (repairItemEvent.isCancelled()) {
            this.source.removeAllWindows(false);
            this.sendInventories();
            return true;
        }
        for (Object object2 : this.actions) {
            if (((InventoryAction)object2).execute(this.source)) {
                ((InventoryAction)object2).onExecuteSuccess(this.source);
                continue;
            }
            ((InventoryAction)object2).onExecuteFail(this.source);
        }
        FakeBlockMenu fakeBlockMenu = anvilInventory.getHolder();
        object2 = this.source.level.getBlock(fakeBlockMenu.getFloorX(), fakeBlockMenu.getFloorY(), fakeBlockMenu.getFloorZ());
        if (((Block)object2).getId() == 145) {
            int n = ((Block)object2).getDamage() >= 8 ? 2 : (((Block)object2).getDamage() >= 4 ? 1 : 0);
            int n2 = !this.source.isCreative() && ThreadLocalRandom.current().nextInt(100) < 12 ? n + 1 : n;
            AnvilDamageEvent anvilDamageEvent = new AnvilDamageEvent((Block)object2, n, n2, AnvilDamageEvent.DamageCause.USE, this.source);
            anvilDamageEvent.setCancelled(n == n2);
            this.source.getServer().getPluginManager().callEvent(anvilDamageEvent);
            if (!anvilDamageEvent.isCancelled()) {
                n2 = anvilDamageEvent.getNewDamage();
                if (n2 > 2) {
                    this.source.level.setBlock((Vector3)object2, Block.get(0), true);
                    this.source.level.addLevelEvent((Vector3)object2, 1020);
                } else {
                    if (n2 < 0) {
                        n2 = 0;
                    }
                    if (n2 != n) {
                        ((Block)object2).setDamage(n2 << 2 | ((Block)object2).getDamage() & 3);
                        this.source.level.setBlock((Vector3)object2, (Block)object2, true);
                    }
                    this.source.level.addLevelEvent((Vector3)object2, 1021);
                }
            } else {
                this.source.level.addLevelEvent((Vector3)object2, 1021);
            }
        }
        if (!this.source.isCreative()) {
            this.source.setExperience(this.source.getExperience(), this.source.getExperienceLevel() - repairItemEvent.getCost());
        }
        return true;
    }

    @Override
    public void addAction(InventoryAction inventoryAction) {
        super.addAction(inventoryAction);
        if (inventoryAction instanceof RepairItemAction) {
            switch (((RepairItemAction)inventoryAction).getType()) {
                case -10: {
                    this.b = inventoryAction.getTargetItem();
                    break;
                }
                case -12: {
                    this.c = inventoryAction.getSourceItem();
                    break;
                }
                case -11: {
                    this.e = inventoryAction.getTargetItem();
                }
            }
        }
    }

    private boolean b() {
        int n;
        int n2;
        int n3;
        int n4 = this.b.getRepairCost();
        if (this.c()) {
            if (!this.a()) {
                return false;
            }
            n4 = 0;
        } else if (this.d()) {
            n4 += this.e.getRepairCost();
            if (this.b.getMaxDurability() != -1 && this.e()) {
                n3 = this.b.getMaxDurability() / 4;
                n2 = Math.min(this.b.getDamage(), n3);
                if (n2 <= 0) {
                    return false;
                }
                int n5 = this.b.getDamage();
                for (n = 0; n2 > 0 && n < this.e.getCount(); ++n) {
                    n2 = Math.min(n5 -= n2, n3);
                }
                if (this.c.getDamage() != n5) {
                    return false;
                }
            } else {
                int n6 = n3 = this.e.getId() == 403 && this.e.hasEnchantments() ? 1 : 0;
                if (n3 == 0 && (this.b.getMaxDurability() == -1 || this.b.getId() != this.e.getId())) {
                    return false;
                }
                if (n3 == 0 && this.b.getMaxDurability() != -1) {
                    int n7 = this.b.getDamage() - this.b.getMaxDurability() + this.e.getDamage() - this.b.getMaxDurability() * 12 / 100 + 1;
                    if (n7 < 0) {
                        n7 = 0;
                    }
                    if (n7 < this.b.getDamage()) {
                        if (this.c.getDamage() != n7) {
                            return false;
                        }
                        n += 2;
                    }
                }
                Int2IntOpenHashMap int2IntOpenHashMap = new Int2IntOpenHashMap();
                int2IntOpenHashMap.defaultReturnValue(-1);
                for (Enchantment enchantment : this.b.getEnchantments()) {
                    int2IntOpenHashMap.put(enchantment.getId(), enchantment.getLevel());
                }
                boolean bl = false;
                int n8 = 0;
                for (Enchantment enchantment : this.e.getEnchantments()) {
                    int n9;
                    Enchantment enchantment2 = this.b.getEnchantment(enchantment.getId());
                    int n10 = enchantment2 != null ? enchantment2.getLevel() : 0;
                    int n11 = enchantment.getLevel();
                    int n12 = n10 == n11 ? n11 + 1 : Math.max(n11, n10);
                    boolean bl2 = enchantment.canEnchant(this.b) || this.b.getId() == 403;
                    for (Enchantment enchantment3 : this.b.getEnchantments()) {
                        if (enchantment3.getId() == enchantment.getId() || enchantment.isCompatibleWith(enchantment3)) continue;
                        bl2 = false;
                        ++n;
                    }
                    if (!bl2) {
                        n8 = 1;
                        continue;
                    }
                    bl = true;
                    if (n12 > enchantment.getMaxLevel()) {
                        n12 = enchantment.getMaxLevel();
                    }
                    int2IntOpenHashMap.put(enchantment.getId(), n12);
                    switch (a.a[enchantment.getRarity().ordinal()]) {
                        case 1: {
                            n9 = 1;
                            break;
                        }
                        case 2: {
                            n9 = 2;
                            break;
                        }
                        case 3: {
                            n9 = 4;
                            break;
                        }
                        default: {
                            n9 = 8;
                        }
                    }
                    if (n3 != 0) {
                        n9 = Math.max(1, n9 / 2);
                    }
                    n += n9 * Math.max(0, n12 - n10);
                    if (this.b.getCount() <= 1) continue;
                    n = 40;
                }
                Enchantment[] enchantmentArray = this.c.getEnchantments();
                if (n8 != 0 && !bl || int2IntOpenHashMap.size() != enchantmentArray.length) {
                    return false;
                }
                for (Enchantment enchantment2 : enchantmentArray) {
                    if (int2IntOpenHashMap.get(enchantment2.getId()) == enchantment2.getLevel()) continue;
                    return false;
                }
            }
        }
        n3 = 0;
        if (!this.b.getCustomName().equals(this.c.getCustomName())) {
            if (this.c.getCustomName().length() > 30) {
                return false;
            }
            n3 = 1;
            n += n3;
        }
        this.d = n4 + n;
        if (n3 == n && n3 > 0 && this.d >= 40) {
            this.d = 39;
        }
        if (n4 < 0 || n < 0 || n == 0 && !this.c() || this.d > 39 && !this.source.isCreative()) {
            return false;
        }
        n2 = this.b.getRepairCost();
        if (!this.c()) {
            if (this.d() && n2 < this.e.getRepairCost()) {
                n2 = this.e.getRepairCost();
            }
            if (n3 == 0 || n3 != n) {
                n2 = 2 * n2 + 1;
            }
        }
        if (this.c.getRepairCost() != n2) {
            this.source.getServer().getLogger().debug("Got unexpected base cost " + this.c.getRepairCost() + " from " + this.source.getName() + "(expected " + n2 + ')');
            return false;
        }
        return true;
    }

    private boolean d() {
        return this.e != null && !this.e.isNull();
    }

    private boolean c() {
        return !(!this.d() || this.b.getId() != 358 && this.b.getId() != 395 || this.e.getId() != 395 && this.e.getId() != 339 && this.e.getId() != 345);
    }

    private boolean a() {
        if (this.b.getId() == 395) {
            return this.b.getDamage() != 2 && this.e.getId() == 345 && this.c.getId() == 395 && this.c.getDamage() == 2 && this.c.getCount() == 1;
        }
        if (this.b.getId() == 358 && this.c.getDamage() == this.b.getDamage()) {
            if (this.e.getId() == 345) {
                return this.b.getDamage() != 2 && this.c.getId() == 358 && this.c.getCount() == 1;
            }
            if (this.e.getId() == 395) {
                return this.c.getId() == 358 && this.c.getCount() == 2;
            }
            if (this.e.getId() == 339 && this.e.getCount() >= 8) {
                return this.b.getDamage() < 3 && this.c.getId() == 358 && this.c.getCount() == 1;
            }
        }
        return false;
    }

    private boolean e() {
        switch (this.b.getId()) {
            case 298: 
            case 299: 
            case 300: 
            case 301: {
                return this.e.getId() == 334;
            }
            case 268: 
            case 269: 
            case 270: 
            case 271: 
            case 290: 
            case 513: {
                return this.e.getId() == 5;
            }
            case 272: 
            case 273: 
            case 274: 
            case 275: 
            case 291: {
                return this.e.getId() == 4;
            }
            case 256: 
            case 257: 
            case 258: 
            case 267: 
            case 292: 
            case 302: 
            case 303: 
            case 304: 
            case 305: 
            case 306: 
            case 307: 
            case 308: 
            case 309: {
                return this.e.getId() == 265;
            }
            case 283: 
            case 284: 
            case 285: 
            case 286: 
            case 294: 
            case 314: 
            case 315: 
            case 316: 
            case 317: {
                return this.e.getId() == 266;
            }
            case 276: 
            case 277: 
            case 278: 
            case 279: 
            case 293: 
            case 310: 
            case 311: 
            case 312: 
            case 313: {
                return this.e.getId() == 264;
            }
            case 743: 
            case 744: 
            case 745: 
            case 746: 
            case 747: 
            case 748: 
            case 749: 
            case 750: 
            case 751: {
                return this.e.getId() == 742;
            }
            case 469: {
                return this.e.getId() == 468;
            }
            case 444: {
                return this.e.getId() == 470;
            }
        }
        return false;
    }

    public Item getInputItem() {
        return this.b;
    }

    public Item getMaterialItem() {
        return this.e;
    }

    public Item getOutputItem() {
        return this.c;
    }

    public int getCost() {
        return this.d;
    }

    public static boolean checkForRepairItemPart(List<InventoryAction> list) {
        for (InventoryAction inventoryAction : list) {
            if (!(inventoryAction instanceof RepairItemAction)) continue;
            return true;
        }
        return false;
    }
}

