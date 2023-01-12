/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBrewingStand;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityNameable;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.event.inventory.BrewEvent;
import cn.nukkit.event.inventory.InventoryEvent;
import cn.nukkit.event.inventory.StartBrewEvent;
import cn.nukkit.inventory.BrewingInventory;
import cn.nukkit.inventory.ContainerRecipe;
import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.MixRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.HashSet;
import java.util.List;

public class BlockEntityBrewingStand
extends BlockEntitySpawnable
implements InventoryHolder,
BlockEntityContainer,
BlockEntityNameable {
    protected BrewingInventory inventory;
    public static final int MAX_BREW_TIME = 400;
    public int brewTime;
    public int fuelTotal;
    public int fuelAmount;
    public static final List<Integer> ingredients = new IntArrayList(new int[]{372, 370, 348, 331, 289, 378, 377, 396, 375, 376, 382, 353, 414, 462, 469, 470, 437});

    public BlockEntityBrewingStand(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.inventory = new BrewingInventory(this);
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag("Items"));
        }
        ListTag<? extends Tag> listTag = this.namedTag.getList("Items");
        for (CompoundTag compoundTag : listTag.getAll()) {
            Item item = NBTIO.getItemHelper(compoundTag);
            if (item.getId() == 0 || item.getCount() <= 0) continue;
            this.inventory.slots.put(compoundTag.getByte("Slot"), item);
        }
        this.brewTime = !this.namedTag.contains("CookTime") || this.namedTag.getShort("CookTime") > 400 ? 400 : this.namedTag.getShort("CookTime");
        this.fuelAmount = this.namedTag.getShort("FuelAmount");
        this.fuelTotal = this.namedTag.getShort("FuelTotal");
        if (this.brewTime < 400) {
            this.scheduleUpdate();
        }
        super.initBlockEntity();
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Brewing Stand";
    }

    @Override
    public boolean hasName() {
        return this.namedTag.contains("CustomName");
    }

    @Override
    public void setName(String string) {
        if (string == null || string.isEmpty()) {
            this.namedTag.remove("CustomName");
            return;
        }
        this.namedTag.putString("CustomName", string);
    }

    @Override
    public void close() {
        if (!this.closed) {
            for (Player player : new HashSet<Player>(this.inventory.getViewers())) {
                player.removeWindow(this.inventory);
            }
            super.close();
        }
    }

    @Override
    public void onBreak() {
        for (Item item : this.inventory.getContents().values()) {
            this.level.dropItem(this, item);
        }
        this.inventory.clearAll();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putList(new ListTag("Items"));
        for (int k = 0; k < this.getSize(); ++k) {
            this.setItem(k, this.inventory.getItem(k));
        }
        this.namedTag.putShort("CookTime", this.brewTime);
        this.namedTag.putShort("FuelAmount", this.fuelAmount);
        this.namedTag.putShort("FuelTotal", this.fuelTotal);
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 117;
    }

    @Override
    public int getSize() {
        return 5;
    }

    protected int getSlotIndex(int n) {
        ListTag<CompoundTag> listTag = this.namedTag.getList("Items", CompoundTag.class);
        for (int k = 0; k < listTag.size(); ++k) {
            if (listTag.get(k).getByte("Slot") != n) continue;
            return k;
        }
        return -1;
    }

    @Override
    public Item getItem(int n) {
        int n2 = this.getSlotIndex(n);
        if (n2 < 0) {
            return new ItemBlock(Block.get(0), 0, 0);
        }
        CompoundTag compoundTag = (CompoundTag)this.namedTag.getList("Items").get(n2);
        return NBTIO.getItemHelper(compoundTag);
    }

    @Override
    public void setItem(int n, Item item) {
        int n2 = this.getSlotIndex(n);
        CompoundTag compoundTag = NBTIO.putItemHelper(item, n);
        if (item.getId() == 0 || item.getCount() <= 0) {
            if (n2 >= 0) {
                this.namedTag.getList("Items").getAll().remove(n2);
            }
        } else if (n2 < 0) {
            this.namedTag.getList("Items", CompoundTag.class).add(compoundTag);
        } else {
            this.namedTag.getList("Items", CompoundTag.class).add(n2, compoundTag);
        }
    }

    @Override
    public BrewingInventory getInventory() {
        return this.inventory;
    }

    protected boolean checkIngredient(Item item) {
        return ingredients.contains(item.getId());
    }

    @Override
    public boolean onUpdate() {
        InventoryEvent inventoryEvent;
        if (this.closed) {
            return false;
        }
        this.b();
        if (this.fuelAmount <= 0 || this.a(true)[0] == null) {
            this.a();
            return false;
        }
        if (this.brewTime == 400) {
            inventoryEvent = new StartBrewEvent(this);
            this.server.getPluginManager().callEvent(inventoryEvent);
            if (inventoryEvent.isCancelled()) {
                return false;
            }
            this.sendBrewTime();
        }
        if (--this.brewTime > 0) {
            if (this.brewTime % 40 == 0) {
                this.sendBrewTime();
            }
            return true;
        }
        inventoryEvent = new BrewEvent(this);
        this.server.getPluginManager().callEvent(inventoryEvent);
        if (inventoryEvent.isCancelled()) {
            this.a();
            return true;
        }
        boolean bl = false;
        MixRecipe[] mixRecipeArray = this.a(false);
        for (int k = 0; k < 3; ++k) {
            Item item;
            MixRecipe mixRecipe = mixRecipeArray[k];
            if (mixRecipe == null || (item = this.inventory.getItem(k + 1)).isNull()) continue;
            Item item2 = mixRecipe.getResult();
            item2.setCount(item.getCount());
            if (mixRecipe instanceof ContainerRecipe) {
                item2.setDamage(item.getDamage());
            }
            this.inventory.setItem(k + 1, item2);
            bl = true;
        }
        if (bl) {
            Item item = this.inventory.getIngredient();
            --item.count;
            this.inventory.setIngredient(item);
            --this.fuelAmount;
            this.sendFuel();
            this.getLevel().addLevelSoundEvent(this, 128);
        }
        this.a();
        return true;
    }

    private void b() {
        Item item = this.getInventory().getFuel();
        if (this.fuelAmount > 0 || item.getId() != 377 || item.getCount() <= 0) {
            return;
        }
        --item.count;
        this.fuelAmount = 20;
        this.fuelTotal = 20;
        this.inventory.setFuel(item);
        this.sendFuel();
    }

    private void a() {
        this.brewTime = 0;
        this.sendBrewTime();
        this.brewTime = 400;
    }

    private MixRecipe[] a(boolean bl) {
        MixRecipe[] mixRecipeArray = new MixRecipe[bl ? 1 : 3];
        Item item = this.inventory.getIngredient();
        CraftingManager craftingManager = this.getLevel().getServer().getCraftingManager();
        for (int k = 0; k < 3; ++k) {
            Item item2 = this.inventory.getItem(k + 1);
            if (item2.isNull()) continue;
            MixRecipe mixRecipe = craftingManager.matchBrewingRecipe(item, item2);
            if (mixRecipe == null) {
                mixRecipe = craftingManager.matchContainerRecipe(item, item2);
            }
            if (mixRecipe == null) continue;
            if (bl) {
                mixRecipeArray[0] = mixRecipe;
                return mixRecipeArray;
            }
            mixRecipeArray[k] = mixRecipe;
        }
        return mixRecipeArray;
    }

    protected void sendFuel() {
        ContainerSetDataPacket containerSetDataPacket = new ContainerSetDataPacket();
        for (Player player : this.inventory.getViewers()) {
            int n = player.getWindowId(this.inventory);
            if (n <= 0) continue;
            containerSetDataPacket.windowId = n;
            containerSetDataPacket.property = 1;
            containerSetDataPacket.value = this.fuelAmount;
            player.dataPacket(containerSetDataPacket);
            containerSetDataPacket.property = 2;
            containerSetDataPacket.value = this.fuelTotal;
            player.dataPacket(containerSetDataPacket);
        }
    }

    protected void sendBrewTime() {
        ContainerSetDataPacket containerSetDataPacket = new ContainerSetDataPacket();
        containerSetDataPacket.property = 0;
        containerSetDataPacket.value = this.brewTime;
        for (Player player : this.inventory.getViewers()) {
            int n = player.getWindowId(this.inventory);
            if (n <= 0) continue;
            containerSetDataPacket.windowId = n;
            player.dataPacket(containerSetDataPacket);
        }
    }

    public void updateBlock() {
        Block block = this.getLevelBlock();
        if (!(block instanceof BlockBrewingStand)) {
            return;
        }
        int n = 0;
        for (int k = 1; k <= 3; ++k) {
            Item item = this.inventory.getItem(k);
            int n2 = item.getId();
            if (n2 != 373 && n2 != 438 && n2 != 441 || item.getCount() <= 0) continue;
            n |= 1 << k - 1;
        }
        block.setDamage(n);
        this.level.setBlock(block, block, false, false);
        if (this.brewTime != 400 && this.a(true)[0] == null) {
            this.a();
        }
    }

    public int getFuel() {
        return this.fuelAmount;
    }

    public void setFuel(int n) {
        this.fuelAmount = n;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag().putString("id", "BrewingStand").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putShort("FuelTotal", this.fuelTotal).putShort("FuelAmount", this.fuelAmount);
        if (this.brewTime < 400) {
            compoundTag.putShort("CookTime", this.brewTime);
        }
        if (this.hasName()) {
            compoundTag.put("CustomName", this.namedTag.get("CustomName"));
        }
        return compoundTag;
    }
}

