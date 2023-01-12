/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntityBlastFurnace;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntityNameable;
import cn.nukkit.blockentity.BlockEntitySmoker;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.event.inventory.FurnaceBurnEvent;
import cn.nukkit.event.inventory.FurnaceSmeltEvent;
import cn.nukkit.inventory.BlastFurnaceInventory;
import cn.nukkit.inventory.FurnaceInventory;
import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.SmokerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEntityFurnace
extends BlockEntitySpawnable
implements InventoryHolder,
BlockEntityContainer,
BlockEntityNameable {
    protected FurnaceInventory inventory;
    protected int burnTime;
    protected int burnDuration;
    protected int cookTime;
    protected int maxTime;
    protected int crackledTime;
    protected double experience;
    public static final Map<Integer, Double> FURNACE_XP = new HashMap<Integer, Double>();

    public BlockEntityFurnace(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.inventory = this instanceof BlockEntityBlastFurnace ? new BlastFurnaceInventory((BlockEntityBlastFurnace)this) : (this instanceof BlockEntitySmoker ? new SmokerInventory((BlockEntitySmoker)this) : new FurnaceInventory(this));
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag("Items"));
        }
        ListTag<? extends Tag> listTag = this.namedTag.getList("Items");
        for (CompoundTag compoundTag : listTag.getAll()) {
            Item item = NBTIO.getItemHelper(compoundTag);
            if (item.getId() == 0 || item.getCount() <= 0) continue;
            this.inventory.slots.put(compoundTag.getByte("Slot"), item);
        }
        this.burnTime = !this.namedTag.contains("BurnTime") || this.namedTag.getShort("BurnTime") < 0 ? 0 : this.namedTag.getShort("BurnTime");
        this.cookTime = !this.namedTag.contains("CookTime") || this.namedTag.getShort("CookTime") < 0 || this.namedTag.getShort("BurnTime") == 0 && this.namedTag.getShort("CookTime") > 0 ? 0 : this.namedTag.getShort("CookTime");
        this.burnDuration = !this.namedTag.contains("BurnDuration") || this.namedTag.getShort("BurnDuration") < 0 ? 0 : this.namedTag.getShort("BurnDuration");
        if (!this.namedTag.contains("MaxTime")) {
            this.maxTime = this.burnTime;
            this.burnDuration = 0;
        } else {
            this.maxTime = this.namedTag.getShort("MaxTime");
        }
        if (this.namedTag.contains("BurnTicks")) {
            this.burnDuration = this.namedTag.getShort("BurnTicks");
            this.namedTag.remove("BurnTicks");
        }
        this.experience = this.namedTag.contains("Experience") && this.namedTag.getDouble("Experience") > 0.0 ? this.namedTag.getDouble("Experience") : 0.0;
        if (this.burnTime > 0) {
            this.scheduleUpdate();
        }
        super.initBlockEntity();
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Furnace";
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
        this.namedTag.putShort("CookTime", this.cookTime);
        this.namedTag.putShort("BurnTime", this.burnTime);
        this.namedTag.putShort("BurnDuration", this.burnDuration);
        this.namedTag.putShort("MaxTime", this.maxTime);
        this.namedTag.putDouble("Experience", this.experience);
    }

    @Override
    public boolean isBlockEntityValid() {
        int n = this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z);
        return n == 61 || n == 62;
    }

    @Override
    public int getSize() {
        return 3;
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
    public FurnaceInventory getInventory() {
        return this.inventory;
    }

    protected void checkFuel(Item item) {
        FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(this, item, item.getFuelTime() == null ? (short)0 : item.getFuelTime());
        this.server.getPluginManager().callEvent(furnaceBurnEvent);
        if (furnaceBurnEvent.isCancelled()) {
            return;
        }
        this.maxTime = furnaceBurnEvent.getBurnTime();
        this.burnTime = furnaceBurnEvent.getBurnTime();
        this.burnDuration = 0;
        if (this.getBlock().getId() == 61) {
            this.getLevel().setBlock(this, Block.get(62, this.getBlock().getDamage()), true);
        }
        if (this.burnTime > 0 && furnaceBurnEvent.isBurning()) {
            item.setCount(item.getCount() - 1);
            if (item.getCount() == 0) {
                if (item.getId() == 325 && item.getDamage() == 10) {
                    item.setDamage(0);
                    item.setCount(1);
                } else {
                    item = new ItemBlock(Block.get(0), 0, 0);
                }
            }
            this.inventory.setFuel(item);
        }
    }

    @Override
    public boolean onUpdate() {
        Item item;
        boolean bl;
        if (this.closed) {
            return false;
        }
        if (this.timing != null) {
            this.timing.startTiming();
        }
        boolean bl2 = false;
        Item item2 = this.inventory.getSmelting();
        Item item3 = this.inventory.getResult();
        FurnaceRecipe furnaceRecipe = this.server.getCraftingManager().matchFurnaceRecipe(item2);
        boolean bl3 = bl = furnaceRecipe != null && item2.getCount() > 0 && (furnaceRecipe.getResult().equals(item3) && item3.getCount() < item3.getMaxStackSize() || item3.getId() == 0);
        if (this.burnTime <= 0 && bl && (item = this.inventory.getItemFast(1)).getFuelTime() != null && item.getCount() > 0) {
            this.checkFuel(item.clone());
        }
        if (this.burnTime > 0) {
            --this.burnTime;
            this.burnDuration = (int)Math.ceil((float)this.burnTime / (float)this.maxTime * 200.0f);
            if (this.crackledTime-- <= 0) {
                this.crackledTime = ThreadLocalRandom.current().nextInt(30, 110);
                this.getLevel().addLevelSoundEvent(this.add(0.5, 0.5, 0.5), 283);
            }
            if (furnaceRecipe != null && bl) {
                ++this.cookTime;
                if (this.cookTime >= 200) {
                    item3 = Item.get(furnaceRecipe.getResult().getId(), furnaceRecipe.getResult().getDamage(), item3.getCount() + 1);
                    FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(this, item2, item3);
                    this.server.getPluginManager().callEvent(furnaceSmeltEvent);
                    if (!furnaceSmeltEvent.isCancelled()) {
                        this.inventory.setResult(furnaceSmeltEvent.getResult());
                        this.experience += FURNACE_XP.getOrDefault(furnaceSmeltEvent.getResult().getId(), 0.0).doubleValue();
                        item2.setCount(item2.getCount() - 1);
                        if (item2.getCount() == 0) {
                            item2 = new ItemBlock(Block.get(0), 0, 0);
                        }
                        this.inventory.setSmelting(item2);
                    }
                    this.cookTime -= 200;
                }
            } else if (this.burnTime <= 0) {
                this.burnTime = 0;
                this.cookTime = 0;
                this.burnDuration = 0;
            } else {
                this.cookTime = 0;
            }
            bl2 = true;
        } else {
            if (this.getBlock().getId() == 62) {
                this.getLevel().setBlock(this, Block.get(61, this.getBlock().getDamage()), true);
            }
            this.burnTime = 0;
            this.cookTime = 0;
            this.burnDuration = 0;
            this.crackledTime = 0;
        }
        for (Player player : this.inventory.getViewers()) {
            int n = player.getWindowId(this.inventory);
            if (n <= 0) continue;
            ContainerSetDataPacket containerSetDataPacket = new ContainerSetDataPacket();
            containerSetDataPacket.windowId = n;
            containerSetDataPacket.property = 0;
            containerSetDataPacket.value = this.cookTime;
            player.dataPacket(containerSetDataPacket);
            containerSetDataPacket = new ContainerSetDataPacket();
            containerSetDataPacket.windowId = n;
            containerSetDataPacket.property = 1;
            containerSetDataPacket.value = this.burnDuration;
            player.dataPacket(containerSetDataPacket);
        }
        if (this.timing != null) {
            this.timing.stopTiming();
        }
        return bl2;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag().putString("id", "Furnace").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putShort("BurnDuration", this.burnDuration).putShort("BurnTime", this.burnTime).putShort("CookTime", this.cookTime);
        if (this.hasName()) {
            compoundTag.put("CustomName", this.namedTag.get("CustomName"));
        }
        return compoundTag;
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public void setBurnTime(int n) {
        this.burnTime = n;
    }

    public int getBurnDuration() {
        return this.burnDuration;
    }

    public void setBurnDuration(int n) {
        this.burnDuration = n;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public void setCookTime(int n) {
        this.cookTime = n;
    }

    public int getMaxTime() {
        return this.maxTime;
    }

    public void setMaxTime(int n) {
        this.maxTime = n;
    }

    public double getExperience() {
        return this.experience;
    }

    public void setExperience(double d2) {
        this.experience = d2;
    }

    public void releaseExperience() {
        int n = NukkitMath.floorDouble(this.experience);
        if (n >= 1) {
            this.setExperience(this.experience - (double)n);
            this.level.dropExpOrb(this, n);
        }
    }

    static {
        FURNACE_XP.put(264, 1.0);
        FURNACE_XP.put(388, 1.0);
        FURNACE_XP.put(266, 1.0);
        FURNACE_XP.put(336, 0.3);
        FURNACE_XP.put(351, 0.2);
        FURNACE_XP.put(406, 0.2);
        FURNACE_XP.put(19, 0.15);
        FURNACE_XP.put(263, 0.1);
        FURNACE_XP.put(433, 0.1);
        FURNACE_XP.put(1, 0.1);
    }
}

