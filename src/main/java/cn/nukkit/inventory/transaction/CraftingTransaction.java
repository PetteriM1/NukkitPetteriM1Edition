package cn.nukkit.inventory.transaction;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.inventory.CraftItemEvent;
import cn.nukkit.inventory.*;
import cn.nukkit.inventory.transaction.action.DamageAnvilAction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.inventory.transaction.action.TakeLevelAction;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.types.ContainerIds;
import cn.nukkit.scheduler.Task;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author CreeperFace
 */
public class CraftingTransaction extends InventoryTransaction {

    protected int gridSize;

    protected List<Item> inputs;

    protected List<Item> secondaryOutputs;

    protected Item primaryOutput;

    protected Recipe recipe;

    protected int craftingType;

    public CraftingTransaction(Player source, List<InventoryAction> actions) {
        super(source, actions, false);

        this.craftingType = source.craftingType;
        this.gridSize = (source.getCraftingGrid() instanceof BigCraftingGrid) ? 3 : 2;

        this.inputs = new ArrayList<>();

        this.secondaryOutputs = new ArrayList<>();

        init(source, actions);
    }

    public void setInput(Item item) {
        if (inputs.size() < gridSize * gridSize) {
            for (Item existingInput : this.inputs) {
                if (existingInput.equals(item, item.hasMeta(), item.hasCompoundTag())) {
                    existingInput.setCount(existingInput.getCount() + item.getCount());
                    return;
                }
            }
            inputs.add(item.clone());
        } else {
            if (!Server.getInstance().suomiCraftPEMode()) throw new RuntimeException("Input list is full can't add " + item);
        }
    }

    public List<Item> getInputList() {
        return inputs;
    }

    public void setExtraOutput(Item item) {
        if (secondaryOutputs.size() < gridSize * gridSize) {
            secondaryOutputs.add(item.clone());
        } else {
            if (!Server.getInstance().suomiCraftPEMode()) throw new RuntimeException("Output list is full can't add " + item);
        }
    }

    public Item getPrimaryOutput() {
        return primaryOutput;
    }

    public void setPrimaryOutput(Item item) {
        if (primaryOutput == null) {
            primaryOutput = item.clone();
        } else if (!primaryOutput.equals(item)) {
            if (!Server.getInstance().suomiCraftPEMode()) throw new RuntimeException("Primary result item has already been set and does not match the current item (expected " + primaryOutput + ", got " + item + ')');
        }
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public boolean canExecute() {
        if (craftingType == Player.CRAFTING_ANVIL) {
            Inventory inventory = source.getWindowById(Player.ANVIL_WINDOW_ID);
            if (inventory instanceof AnvilInventory) {
                AnvilInventory anvil = (AnvilInventory) inventory;
                addInventory(anvil);
                if (equalsIgnoringEnchantmentOrder(this.primaryOutput, anvil.getResult())) {
                    TakeLevelAction takeLevel = new TakeLevelAction(anvil.getLevelCost());
                    addAction(takeLevel);
                    if (takeLevel.isValid(source)) {
                        recipe = new RepairRecipe(InventoryType.ANVIL, this.primaryOutput, Collections.singletonList(inputs.get(0)));
                        PlayerUIInventory uiInventory = source.getUIInventory();
                        actions.add(new DamageAnvilAction(anvil, !source.isCreative() && ThreadLocalRandom.current().nextFloat() < 0.12F, this));
                        actions.stream()
                                .filter(a -> a instanceof SlotChangeAction)
                                .map(a-> (SlotChangeAction) a)
                                .filter(a -> a.getInventory() == uiInventory)
                                .filter(a -> a.getSlot() == 50)
                                .findFirst()
                                .ifPresent(a -> {
                                    // Move the set result action to the end, otherwise the result would be cleared too early
                                    actions.remove(a);
                                    actions.add(a);
                                });
                    }
                }
            }
            if (recipe == null) {
                source.sendExperienceLevel();
            }
            source.getUIInventory().setItem(AnvilInventory.RESULT, Item.get(0), false);
        } else {
            recipe = source.getServer().getCraftingManager().matchRecipe(inputs, this.primaryOutput, this.secondaryOutputs);
        }

        return this.recipe != null && super.canExecute();
    }

    protected boolean callExecuteEvent() {
        CraftItemEvent ev;

        this.source.getServer().getPluginManager().callEvent(ev = new CraftItemEvent(this));
        return !ev.isCancelled();
    }

    protected void sendInventories() {
        super.sendInventories();

        /*Optional<Inventory> topWindow = source.getTopWindow();
        if (topWindow.isPresent()) {
            source.removeWindow(topWindow.get());
            return;
        }*/

        /*
         * TODO: HACK!
         * we can't resend the contents of the crafting window, so we force the client to close it instead.
         * So people don't whine about messy desync issues when someone cancels CraftItemEvent, or when a crafting
         * transaction goes wrong.
         */
        ContainerClosePacket pk = new ContainerClosePacket();
        pk.windowId = ContainerIds.NONE;
        pk.wasServerInitiated = true;
        source.getServer().getScheduler().scheduleDelayedTask(new Task() {
            @Override
            public void onRun(int currentTick) {
                source.dataPacket(pk);
            }
        }, 10);

        this.source.resetCraftingGridType();
    }

    public boolean execute() {
        if (super.execute()) {
            switch (this.primaryOutput.getId()) {
                case Item.CRAFTING_TABLE:
                    source.awardAchievement("buildWorkBench");
                    break;
                case Item.WOODEN_PICKAXE:
                    source.awardAchievement("buildPickaxe");
                    break;
                case Item.FURNACE:
                    source.awardAchievement("buildFurnace");
                    break;
                case Item.WOODEN_HOE:
                    source.awardAchievement("buildHoe");
                    break;
                case Item.BREAD:
                    source.awardAchievement("makeBread");
                    break;
                case Item.CAKE:
                    source.awardAchievement("bakeCake");
                    break;
                case Item.STONE_PICKAXE:
                case Item.GOLDEN_PICKAXE:
                case Item.IRON_PICKAXE:
                case Item.DIAMOND_PICKAXE:
                    source.awardAchievement("buildBetterPickaxe");
                    break;
                case Item.WOODEN_SWORD:
                    source.awardAchievement("buildSword");
                    break;
                case Item.DIAMOND:
                    source.awardAchievement("diamond");
                    break;
            }

            return true;
        }

        return false;
    }

    public boolean checkForCraftingPart(List<InventoryAction> actions) {
        for (InventoryAction action : actions) {
            if (action instanceof SlotChangeAction) {
                SlotChangeAction slotChangeAction = (SlotChangeAction) action;
                if (slotChangeAction.getInventory().getType() == InventoryType.UI && slotChangeAction.getSlot() == 50 &&
                        !slotChangeAction.getSourceItem().equals(slotChangeAction.getTargetItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean equalsIgnoringEnchantmentOrder(Item thisItem, Item item) {
        if (!thisItem.equals(item, true, false)) {
            return false;
        }

        if (Arrays.equals(thisItem.getCompoundTag(), item.getCompoundTag())) {
            return true;
        }

        if (!thisItem.hasCompoundTag() || !item.hasCompoundTag()) {
            return false;
        }

        CompoundTag thisTags = thisItem.getNamedTag();
        CompoundTag otherTags = item.getNamedTag();
        if (thisTags.equals(otherTags)) {
            return true;
        }

        if (!thisTags.contains("ench") || !otherTags.contains("ench")
                || !(thisTags.get("ench") instanceof ListTag)
                || !(otherTags.get("ench") instanceof ListTag)
                || thisTags.getList("ench").size() != otherTags.getList("ench").size()) {
            return false;
        }

        ListTag<CompoundTag> thisEnchantmentTags = thisTags.getList("ench", CompoundTag.class);
        ListTag<CompoundTag> otherEnchantmentTags = otherTags.getList("ench", CompoundTag.class);

        int size = thisEnchantmentTags.size();
        Int2IntMap enchantments = new Int2IntArrayMap(size);
        enchantments.defaultReturnValue(Integer.MIN_VALUE);

        for (int i = 0; i < size; i++) {
            CompoundTag tag = thisEnchantmentTags.get(i);
            enchantments.put(tag.getShort("id"), tag.getShort("lvl"));
        }

        for (int i = 0; i < size; i++) {
            CompoundTag tag = otherEnchantmentTags.get(i);
            if (enchantments.get(tag.getShort("id")) != tag.getShort("lvl")) {
                return false;
            }
        }

        return true;
    }
}
