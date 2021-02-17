package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityMooshroom extends EntityWalkingAnimal {

    public static final int NETWORK_ID = 16;

    public EntityMooshroom(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.7f;
        }
        return 1.4f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(10);

        if (this.namedTag.contains("Variant")) {
            this.setBrown(this.namedTag.getInt("Variant") == 1);
        }
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && player.getInventory().getItemInHand().getId() == Item.WHEAT && distance <= 40;
        }
        return false;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            for (int i = 0; i < Utils.rand(0, 2); i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }

            for (int i = 0; i < Utils.rand(1, 3); i++) {
                drops.add(Item.get(this.isOnFire() ? Item.STEAK : Item.RAW_BEEF, 0, 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }
    
    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (item.getId() == Item.BOWL) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            player.getInventory().addItem(Item.get(Item.MUSHROOM_STEW, 0, 1));
            return true;
        } else if (item.getId() == Item.BUCKET) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            Item newBucket = Item.get(Item.BUCKET, 1, 1);
            if (player.getInventory().getItemFast(player.getInventory().getHeldItemIndex()).count > 0) {
                if (player.getInventory().canAddItem(newBucket)) {
                    player.getInventory().addItem(newBucket);
                } else {
                    player.dropItem(newBucket);
                }
            } else {
                player.getInventory().setItemInHand(newBucket);
            }
            this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_MILK);
            return true;
        } else if (item.getId() == Item.WHEAT && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(0, this.getMountedYOffset(), 0), Item.get(Item.WHEAT)));
            this.setInLove();
        }
        return super.onInteract(player, item, clickedPos);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Variant", this.isBrown() ? 1 : 0);
    }

    @Override
    public void onStruckByLightning(Entity entity) {
        this.setBrown(!this.isBrown());
        super.onStruckByLightning(entity);
    }

    public boolean isBrown() {
        return this.getDataPropertyInt(DATA_VARIANT) == 1;
    }

    public void setBrown(boolean brown) {
        this.setDataProperty(new IntEntityData(DATA_VARIANT, brown ? 1 : 0));
    }
}
