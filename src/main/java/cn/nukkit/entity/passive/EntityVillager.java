package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityWitch;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.TradeInventory;
import cn.nukkit.inventory.TradeInventoryRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;

import java.util.ArrayList;
import java.util.List;

public class EntityVillager extends EntityWalkingAnimal implements InventoryHolder {

    public static final int PROFESSION_FARMER = 0;
    public static final int PROFESSION_LIBRARIAN = 1;
    public static final int PROFESSION_PRIEST = 2;
    public static final int PROFESSION_BLACKSMITH = 3;
    public static final int PROFESSION_BUTCHER = 4;
    public static final int PROFESSION_GENERIC = 5;

    public static final int NETWORK_ID = 15;

    private TradeInventory inventory;
    private final List<TradeInventoryRecipe> recipes = new ArrayList<>();
    private int tradeTier = 0;

    public EntityVillager(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.3f;
        }
        return 0.6f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.975f;
        }
        return 1.95f;
    }

    @Override
    public double getSpeed() {
        return 1.1;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(10);

        this.inventory = new TradeInventory(this);

        if (!this.namedTag.contains("Profession")) {
            this.setProfession(PROFESSION_GENERIC);
        }
    }

    public int getProfession() {
        return this.namedTag.getInt("Profession");
    }

    public void setProfession(int profession) {
        this.namedTag.putInt("Profession", profession);
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public void onStruckByLightning(Entity entity) {
        Entity ent = Entity.createEntity("Witch", this);
        if (ent != null) {
            CreatureSpawnEvent cse = new CreatureSpawnEvent(EntityWitch.NETWORK_ID, this, ent.namedTag, CreatureSpawnEvent.SpawnReason.LIGHTNING);
            this.getServer().getPluginManager().callEvent(cse);

            if (cse.isCancelled()) {
                ent.close();
                return;
            }

            ent.yaw = this.yaw;
            ent.pitch = this.pitch;
            ent.setImmobile(this.isImmobile());
            if (this.hasCustomName()) {
                ent.setNameTag(this.getNameTag());
                ent.setNameTagVisible(this.isNameTagVisible());
                ent.setNameTagAlwaysVisible(this.isNameTagAlwaysVisible());
            }

            this.close();
            ent.spawnToAll();
        } else {
            super.onStruckByLightning(entity);
        }
    }

    public void setTradeTier(int tier) {
        this.tradeTier = tier;
    }

    public int getTradeTier() {
        return this.tradeTier;
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (recipes.size() > 0) {
            player.addWindow(this.getInventory());
            return true;
        }
        return false;
    }

    public void addTradeRcipe(TradeInventoryRecipe recipe) {
        this.recipes.add(recipe);
    }

    public CompoundTag getOffers() {
        CompoundTag nbt = new CompoundTag();
        nbt.putList(recipesToNbt());
        nbt.putList(getTierExpRequirements());
        return nbt;
    }

    private ListTag<CompoundTag> recipesToNbt() {
        ListTag<CompoundTag> tag = new ListTag<>("Recipes");
        for(TradeInventoryRecipe recipe : this.recipes) {
            tag.add(recipe.toNBT());
        }
        return tag;
    }

    private ListTag<CompoundTag> getTierExpRequirements() {
        ListTag<CompoundTag> tag = new ListTag<>("TierExpRequirements");
        tag.add(new CompoundTag().putInt("0", 0));
        tag.add(new CompoundTag().putInt("1", 10));
        tag.add(new CompoundTag().putInt("2", 60));
        tag.add(new CompoundTag().putInt("3", 160));
        tag.add(new CompoundTag().putInt("4", 310));
        return tag;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
