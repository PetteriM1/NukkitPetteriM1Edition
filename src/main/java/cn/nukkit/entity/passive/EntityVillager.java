package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.mob.EntityWitch;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.TradeInventory;
import cn.nukkit.inventory.TradeInventoryRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
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
    private List<TradeInventoryRecipe> recipes;
    private int tradeTier = 0;
    private boolean willing = true;

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
        this.recipes = new ArrayList<>();
        
        this.dataProperties.putLong(DATA_TRADING_PLAYER_EID, 0L);
        
        CompoundTag offers = this.namedTag.getCompound("Offers");
        if (offers != null) {
            ListTag<CompoundTag> nbtRecipes = offers.getList("Recipes", CompoundTag.class);
            for (CompoundTag nbt : nbtRecipes.getAll()) {
                recipes.add(TradeInventoryRecipe.toNBT(nbt));
            }
        } else {
            CompoundTag nbt = new CompoundTag("Offers");
            nbt.putList(new ListTag<CompoundTag>("Recipes"));
            nbt.putList(this.getDefaultTierExpRequirements());
            this.namedTag.putCompound("Offers", nbt);
        }

        if (!this.namedTag.contains("Profession")) {
            if (this instanceof EntityVillagerV2) {
                this.setProfession(EntityVillagerV2.PROFESSION_UNEMPLOYED);
            }else {
                this.setProfession(PROFESSION_GENERIC);
            }
        }else {
            this.setProfession(this.getProfession());
        }
    
        if(!this.namedTag.contains("Willing")) {
            this.setWilling(true);
        }else {
            this.setWilling(this.isWilling());
        }
        
        if (!this.namedTag.contains("TradeTier")) {
            this.setTradeTier(0);
        }else {
            this.setTradeTier(this.getTradeTier());
        }
        
        if (!this.namedTag.contains("MaxTradeTier")) {
            this.setMaxTradeTier(4);
        }else {
            this.setMaxTradeTier(this.getMaxTradeTier());
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
        if (tier > 4) {
            tier = 4;
        }
        this.namedTag.putInt("TradeTier", tier);
        this.dataProperties.putInt(DATA_TRADE_TIER, tier);
        this.sendData(this.getViewers().values().toArray(new Player[0]),
                new EntityMetadata().putInt(DATA_TRADE_TIER, tier));
    }

    public int getTradeTier() {
        return this.namedTag.getInt("TradeTier");
    }
    
    public void setMaxTradeTier(int maxTier) {
        if (maxTier > 4) {
            maxTier = 4;
        }
        this.namedTag.putInt("MaxTradeTier", maxTier);
        this.dataProperties.putInt(DATA_MAX_TRADE_TIER, maxTier);
        this.sendData(this.getViewers().values().toArray(new Player[0]),
                new EntityMetadata().putInt(DATA_MAX_TRADE_TIER, maxTier));
    }
    
    public int getMaxTradeTier() {
        return this.namedTag.getInt("MaxTradeTier");
    }
    
    public void setExperience(int experience) {
        this.dataProperties.putInt(DATA_TRADE_EXPERIENCE, experience);
        this.sendData(this.getViewers().values().toArray(new Player[0]),
                new EntityMetadata().putInt(DATA_TRADE_EXPERIENCE, experience));
    }
    
    public int getExperience() {
        return this.dataProperties.getInt(DATA_TRADE_EXPERIENCE);
    }

    public void setWilling(boolean value) {
        this.namedTag.putBoolean("Willing", value);
    }

    public boolean isWilling() {
        return this.namedTag.getBoolean("Willing");
    }
    
    public void cancelTradingWithPlayer() {
        this.setTradingWith(0L);
    }
    
    public void setTradingWith(long eid) {
        this.dataProperties.putLong(DATA_TRADING_PLAYER_EID, eid);
        this.sendData(this.getViewers().values().toArray(new Player[0]),
                new EntityMetadata().putLong(DATA_TRADING_PLAYER_EID, eid));
    }
    
    public boolean isTrading() {
        return this.dataProperties.getLong(DATA_TRADING_PLAYER_EID) != 0L;
    }
    
    
    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (super.onInteract(player, item, clickedPos)) {
            return true;
        }
        return this.onInteract(player, item);
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (recipes.size() > 0 && !this.isTrading()) {
            player.addWindow(this.getInventory());
            return true;
        }
        return false;
    }

    public void addTradeRecipe(TradeInventoryRecipe recipe) {
        this.recipes.add(recipe);
    }

    public List<TradeInventoryRecipe> getRecipes() {
        return this.recipes;
    }

    public CompoundTag getOffers() {
        CompoundTag nbt = new CompoundTag();
        nbt.putList(this.recipesToNbt());
        nbt.putList(this.getDefaultTierExpRequirements());
        return nbt;
    }

    private ListTag<CompoundTag> recipesToNbt() {
        ListTag<CompoundTag> tag = new ListTag<>("Recipes");
        for (TradeInventoryRecipe recipe : this.recipes) {
            tag.add(recipe.toNBT());
        }
        return tag;
    }

    private ListTag<CompoundTag> getDefaultTierExpRequirements() {
        ListTag<CompoundTag> tag = new ListTag<>("TierExpRequirements");
        tag.add(new CompoundTag().putInt("0", 0));
        tag.add(new CompoundTag().putInt("1", 10));
        tag.add(new CompoundTag().putInt("2", 70));
        tag.add(new CompoundTag().putInt("3", 150));
        tag.add(new CompoundTag().putInt("4", 250));
        return tag;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
