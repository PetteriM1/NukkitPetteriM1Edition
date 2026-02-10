package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityDonkey extends EntityHorseBase implements InventoryHolder, EntityInteractable {

    public static final int NETWORK_ID = 24;

    protected DonkeyInventory inventory;

    private boolean chested;

    public EntityDonkey(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            for (int i = 0; i < Utils.rand(0, 2); i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }
        }

        if (this.isChested()) {
            drops.add(Item.get(Item.CHEST, 0, 1));

            if (this.inventory == null) {
                this.initInventory();
            }
            if (this.inventory != null) {
                this.inventory.getViewers().clear();
                for (Item item : this.inventory.getContents().values()) {
                    this.level.dropItem(this, item);
                }
                this.inventory.clearAll();
            }
        }

        if (this.isSaddled()) {
            drops.add(Item.get(Item.SADDLE, 0, 1));
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.8f;
        }
        return 1.6f;
    }

    @Override
    public String getInteractButtonText() {
        return "";
    }

    @Override
    public DonkeyInventory getInventory() {
        if (this.inventory == null) {
            this.initInventory();
        }
        return this.inventory;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.6982f;
        }
        return 1.3965f;
    }

    public boolean isChested() {
        return this.chested;
    }

    public void setChested(boolean chested) {
        this.chested = chested;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_CHESTED, chested, false);

        if (chested) {
            this.dataProperties
                    .putByte(DATA_CONTAINER_TYPE, InventoryType.DONKEY.getNetworkType())
                    .putInt(DATA_CONTAINER_BASE_SIZE, InventoryType.DONKEY.getDefaultSize())
                    .putInt(DATA_CONTAINER_EXTRA_SLOTS_PER_STRENGTH, 0);
        }

        this.sendData(this.getViewers().values().toArray(new Player[0]));
    }

    @Override
    public boolean canDespawn() {
        return !this.isChested() && super.canDespawn();
    }

    @Override
    public boolean canDoInteraction() {
        return this.isChested();
    }

    @Override
    public String getInteractButtonText(Player player) {
        if (player.sneakToBlockInteract()) {
            return "action.interact.opencontainer";
        }
        return "";
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(15);
        super.initEntity();

        if (this.namedTag.contains("ChestedHorse")) {
            this.setChested(this.namedTag.getBoolean("ChestedHorse"));
        }
    }

    private void initInventory() {
        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        }
        ListTag<CompoundTag> list = (ListTag<CompoundTag>) this.namedTag.getList("Items");

        this.inventory = new DonkeyInventory(this);

        for (CompoundTag compound : list.getAll()) {
            Item item = NBTIO.getItemHelper(compound);
            if (item.getId() != 0 && item.getCount() > 0) {
                this.inventory.slots.put(compound.getByte("Slot"), item);
            }
        }
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (!this.isBaby() && !this.isChested() && item.getId() == Item.CHEST) {
            this.setChested(true);
            return true;
        }

        if (this.isChested() && player.sneakToBlockInteract() && this.isAlive()) {
            player.addWindow(this.getInventory());
            return false;
        }

        return super.onInteract(player, item, clickedPos);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("ChestedHorse", this.isChested());

        if (this.inventory != null) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
            for (int slot = 0; slot < InventoryType.DONKEY.getDefaultSize(); ++slot) {
                Item item = this.inventory.getItem(slot);
                if (item != null && item.getId() != Item.AIR) {
                    this.namedTag.getList("Items", CompoundTag.class)
                            .add(NBTIO.putItemHelper(item, slot));
                }
            }
        }
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        boolean canTarget = super.targetOption(creature, distance);

        if (canTarget && (creature instanceof Player)) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed &&
                    this.isFeedItem(player.getInventory().getItemInHandFast()) && distance <= 49;
        }
        return false;
    }
}
