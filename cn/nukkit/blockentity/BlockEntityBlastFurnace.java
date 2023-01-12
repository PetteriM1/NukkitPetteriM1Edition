/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.event.inventory.FurnaceSmeltEvent;
import cn.nukkit.inventory.FurnaceRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEntityBlastFurnace
extends BlockEntityFurnace {
    public BlockEntityBlastFurnace(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Blast Furnace";
    }

    @Override
    public boolean isBlockEntityValid() {
        int n = this.level.getBlockIdAt(this.chunk, (int)this.x, (int)this.y, (int)this.z);
        return n == 451 || n == 469;
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
        int n = item2.getId();
        if (!(n == 15 || n == 14 || n == 56 || n == 21 || n == 73 || n == 16 || n == 129 || n == 153 || item2 instanceof ItemTool && (item2.getTier() == 4 || item2.getTier() == 2) || item2 instanceof ItemArmor && item2.getTier() >= 2 && item2.getTier() <= 4)) {
            return true;
        }
        Item item3 = this.inventory.getResult();
        FurnaceRecipe furnaceRecipe = this.server.getCraftingManager().matchFurnaceRecipe(item2);
        boolean bl3 = bl = furnaceRecipe != null && item2.getCount() > 0 && (furnaceRecipe.getResult().equals(item3) && item3.getCount() < item3.getMaxStackSize() || item3.getId() == 0);
        if (this.burnTime <= 0 && bl && (item = this.inventory.getItemFast(1)).getFuelTime() != null && item.getCount() > 0) {
            this.checkFuel(item.clone());
        }
        if (this.burnTime > 0) {
            --this.burnTime;
            this.burnDuration = (int)Math.ceil((float)this.burnTime / (float)this.maxTime * 100.0f);
            if (this.crackledTime-- <= 0) {
                this.crackledTime = ThreadLocalRandom.current().nextInt(30, 110);
                this.getLevel().addLevelSoundEvent(this.add(0.5, 0.5, 0.5), 283);
            }
            if (furnaceRecipe != null && bl) {
                ++this.cookTime;
                if (this.cookTime >= 100) {
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
                    this.cookTime -= 100;
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
            int n2 = player.getWindowId(this.inventory);
            if (n2 <= 0) continue;
            ContainerSetDataPacket containerSetDataPacket = new ContainerSetDataPacket();
            containerSetDataPacket.windowId = n2;
            containerSetDataPacket.property = 0;
            containerSetDataPacket.value = this.cookTime;
            player.dataPacket(containerSetDataPacket);
            containerSetDataPacket = new ContainerSetDataPacket();
            containerSetDataPacket.windowId = n2;
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
        CompoundTag compoundTag = new CompoundTag().putString("id", "BlastFurnace").putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z).putShort("BurnDuration", this.burnDuration).putShort("BurnTime", this.burnTime).putShort("CookTime", this.cookTime);
        if (this.hasName()) {
            compoundTag.put("CustomName", this.namedTag.get("CustomName"));
        }
        return compoundTag;
    }
}

