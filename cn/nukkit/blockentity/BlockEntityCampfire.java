/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockCampfire;
import cn.nukkit.blockentity.BlockEntityContainer;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.inventory.CampfireInventory;
import cn.nukkit.inventory.CampfireRecipe;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEntityCampfire
extends BlockEntitySpawnable
implements InventoryHolder,
BlockEntityContainer {
    private CampfireInventory b;
    private int[] d;
    private CampfireRecipe[] e;
    private boolean[] c;

    public BlockEntityCampfire(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.b = new CampfireInventory(this, InventoryType.CAMPFIRE);
        this.d = new int[4];
        this.e = new CampfireRecipe[4];
        this.c = new boolean[4];
        for (int k = 1; k <= this.d.length; ++k) {
            this.d[k - 1] = this.namedTag.getInt("ItemTime" + k);
            this.c[k - 1] = this.namedTag.getBoolean("KeepItem1");
            if (!this.namedTag.contains("Item" + k) || !(this.namedTag.get("Item" + k) instanceof CompoundTag)) continue;
            this.b.setItem(k - 1, NBTIO.getItemHelper(this.namedTag.getCompound("Item" + k)));
        }
        super.initBlockEntity();
        this.scheduleUpdate();
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }
        boolean bl = false;
        Block block = this.getBlock();
        boolean bl2 = block instanceof BlockCampfire && !((BlockCampfire)block).isExtinguished();
        for (int k = 0; k < this.b.getSize(); ++k) {
            int n;
            Item item = this.b.getItem(k);
            if (item == null || item.getId() == 0 || item.getCount() <= 0) {
                this.d[k] = 0;
                this.e[k] = null;
                continue;
            }
            if (this.c[k]) continue;
            CampfireRecipe campfireRecipe = this.e[k];
            if (campfireRecipe == null) {
                campfireRecipe = this.server.getCraftingManager().matchCampfireRecipe(item);
                if (campfireRecipe == null) {
                    this.b.setItem(k, Item.get(0));
                    ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
                    this.level.dropItem(this.add(threadLocalRandom.nextFloat(), 0.5, threadLocalRandom.nextFloat()), item);
                    this.d[k] = 0;
                    this.e[k] = null;
                    continue;
                }
                this.d[k] = 600;
                this.e[k] = campfireRecipe;
            }
            if ((n = this.d[k]) <= 0) {
                Item item2 = Item.get(campfireRecipe.getResult().getId(), campfireRecipe.getResult().getDamage(), item.getCount());
                this.b.setItem(k, Item.get(0));
                ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
                this.level.dropItem(this.add(threadLocalRandom.nextFloat(), 0.5, threadLocalRandom.nextFloat()), item2);
                this.d[k] = 0;
                this.e[k] = null;
                continue;
            }
            if (bl2) {
                int n2 = k;
                this.d[n2] = this.d[n2] - 1;
                bl = true;
                continue;
            }
            this.d[k] = 600;
        }
        return bl;
    }

    public boolean getKeepItem(int n) {
        if (n < 0 || n >= this.c.length) {
            return false;
        }
        return this.c[n];
    }

    public void setKeepItem(int n, boolean bl) {
        if (n < 0 || n >= this.c.length) {
            return;
        }
        this.c[n] = bl;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        for (int k = 1; k <= this.d.length; ++k) {
            Item item = this.b.getItem(k - 1);
            if (item == null || item.getId() == 0 || item.getCount() <= 0) {
                this.namedTag.remove("Item" + k);
                this.namedTag.putInt("ItemTime" + k, 0);
                this.namedTag.remove("KeepItem" + k);
                continue;
            }
            this.namedTag.putCompound("Item" + k, NBTIO.putItemHelper(item));
            this.namedTag.putInt("ItemTime" + k, this.d[k - 1]);
            this.namedTag.putBoolean("KeepItem" + k, this.c[k - 1]);
        }
    }

    public void setRecipe(int n, CampfireRecipe campfireRecipe) {
        this.e[n] = campfireRecipe;
    }

    @Override
    public void close() {
        if (!this.closed) {
            for (Player player : new HashSet<Player>(this.getInventory().getViewers())) {
                player.removeWindow(this.getInventory());
            }
            super.close();
        }
    }

    @Override
    public void onBreak() {
        for (Item item : this.b.getContents().values()) {
            this.level.dropItem(this, item);
        }
        this.b.clearAll();
    }

    @Override
    public String getName() {
        return "Campfire";
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag().putString("id", "Campfire").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
        for (int k = 1; k <= this.d.length; ++k) {
            Item item = this.b.getItem(k - 1);
            if (item == null || item.getId() == 0 || item.getCount() <= 0) {
                compoundTag.remove("Item" + k);
                continue;
            }
            compoundTag.putCompound("Item" + k, NBTIO.putItemHelper(item));
        }
        return compoundTag;
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z) == 464;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public Item getItem(int n) {
        if (n < 0 || n >= this.getSize()) {
            return new ItemBlock((Block)new BlockAir(), 0, 0);
        }
        CompoundTag compoundTag = this.namedTag.getCompound("Item" + (n + 1));
        return NBTIO.getItemHelper(compoundTag);
    }

    @Override
    public void setItem(int n, Item item) {
        if (n < 0 || n >= this.getSize()) {
            return;
        }
        CompoundTag compoundTag = NBTIO.putItemHelper(item);
        this.namedTag.putCompound("Item" + (n + 1), compoundTag);
    }

    @Override
    public CampfireInventory getInventory() {
        return this.b;
    }
}

