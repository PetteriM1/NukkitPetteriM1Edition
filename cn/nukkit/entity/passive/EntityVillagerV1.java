/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntityWalkingAnimal;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.TradeInventory;
import cn.nukkit.inventory.TradeInventoryRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import java.util.ArrayList;
import java.util.List;

public class EntityVillagerV1
extends EntityWalkingAnimal
implements InventoryHolder {
    public static final int PROFESSION_FARMER = 0;
    public static final int PROFESSION_LIBRARIAN = 1;
    public static final int PROFESSION_PRIEST = 2;
    public static final int PROFESSION_BLACKSMITH = 3;
    public static final int PROFESSION_BUTCHER = 4;
    public static final int PROFESSION_GENERIC = 5;
    public static final int NETWORK_ID = 15;
    private TradeInventory w;
    private final List<TradeInventoryRecipe> A = new ArrayList<TradeInventoryRecipe>();
    private int u = 0;
    private boolean v = true;

    public EntityVillagerV1(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 15;
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
        block4: {
            this.setMaxHealth(10);
            super.initEntity();
            this.w = new TradeInventory(this);
            try {
                CompoundTag compoundTag = this.namedTag.getCompound("Offers");
                if (compoundTag != null) {
                    ListTag<CompoundTag> listTag = compoundTag.getList("Recipes", CompoundTag.class);
                    for (CompoundTag compoundTag2 : listTag.getAll()) {
                        this.A.add(TradeInventoryRecipe.toNBT(compoundTag2));
                    }
                }
            }
            catch (Exception exception) {
                this.server.getLogger().error("Failed to load trade recipes for a villager with entity id " + this.id, exception);
            }
            if (this.namedTag.contains("Profession")) break block4;
            this.setProfession(5);
        }
    }

    public int getProfession() {
        return this.namedTag.getInt("Profession");
    }

    public void setProfession(int n) {
        this.namedTag.putInt("Profession", n);
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public void onStruckByLightning(Entity entity) {
        Entity entity2 = Entity.createEntity("Witch", (Position)this, new Object[0]);
        if (entity2 != null) {
            CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(45, this, entity2.namedTag, CreatureSpawnEvent.SpawnReason.LIGHTNING);
            this.getServer().getPluginManager().callEvent(creatureSpawnEvent);
            if (creatureSpawnEvent.isCancelled()) {
                entity2.close();
                return;
            }
            entity2.yaw = this.yaw;
            entity2.pitch = this.pitch;
            entity2.setImmobile(this.isImmobile());
            if (this.hasCustomName()) {
                entity2.setNameTag(this.getNameTag());
                entity2.setNameTagVisible(this.isNameTagVisible());
                entity2.setNameTagAlwaysVisible(this.isNameTagAlwaysVisible());
            }
            this.close();
            entity2.spawnToAll();
        } else {
            super.onStruckByLightning(entity);
        }
    }

    public void setTradeTier(int n) {
        this.u = n;
    }

    public int getTradeTier() {
        return this.u;
    }

    public void setWilling(boolean bl) {
        this.v = bl;
    }

    public boolean isWilling() {
        return this.v;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 vector3) {
        if (this.A.size() > 0) {
            player.addWindow(this.getInventory());
            return true;
        }
        return false;
    }

    public void addTradeRecipe(TradeInventoryRecipe tradeInventoryRecipe) {
        this.A.add(tradeInventoryRecipe);
    }

    public List<TradeInventoryRecipe> getRecipes() {
        return this.A;
    }

    public CompoundTag getOffers() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putList(this.b());
        compoundTag.putList(this.c());
        return compoundTag;
    }

    private ListTag<CompoundTag> b() {
        ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("Recipes");
        for (TradeInventoryRecipe tradeInventoryRecipe : this.A) {
            listTag.add(tradeInventoryRecipe.toNBT());
        }
        return listTag;
    }

    private ListTag<CompoundTag> c() {
        ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("TierExpRequirements");
        listTag.add(new CompoundTag().putInt("0", 0));
        listTag.add(new CompoundTag().putInt("1", 10));
        listTag.add(new CompoundTag().putInt("2", 70));
        listTag.add(new CompoundTag().putInt("3", 150));
        listTag.add(new CompoundTag().putInt("4", 250));
        return listTag;
    }

    @Override
    public Inventory getInventory() {
        return this.w;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

