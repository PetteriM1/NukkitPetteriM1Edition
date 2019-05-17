package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EntityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static cn.nukkit.network.protocol.SetEntityLinkPacket.TYPE_REMOVE;
import static cn.nukkit.network.protocol.SetEntityLinkPacket.TYPE_RIDE;

public class EntityHorse extends EntityWalkingAnimal implements EntityRideable {

    public static final int NETWORK_ID = 23;

    public int variant = this.getRandomVariant();

    public EntityHorse(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
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

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.8f;
        }
        return 1.6f;
    }

    @Override
    public float getMaxJumpHeight() {
        return 2;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(EntityUtils.rand(15, 30));

        if (this.namedTag.contains("Variant")) {
            this.variant = this.namedTag.getInt("Variant");
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt("Variant", this.variant);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed
                    && (player.getInventory().getItemInHand().getId() == Item.WHEAT
                            || player.getInventory().getItemInHand().getId() == Item.APPLE
                            || player.getInventory().getItemInHand().getId() == Item.HAY_BALE
                            || player.getInventory().getItemInHand().getId() == Item.GOLDEN_APPLE
                            || player.getInventory().getItemInHand().getId() == Item.SUGAR
                            || player.getInventory().getItemInHand().getId() == Item.BREAD
                            || player.getInventory().getItemInHand().getId() == Item.GOLDEN_CARROT)
                    && distance <= 40;
        }
        return false;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (this.hasCustomName()) {
            drops.add(Item.get(Item.NAME_TAG, 0, 1));
        }

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby()) {
            for (int i = 0; i < EntityUtils.rand(0, 2); i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : EntityUtils.rand(1, 3);
    }

    private int getRandomVariant() {
        int[] variantList = {0, 1, 2, 3, 4, 5, 6, 256, 257, 258, 259, 260, 261, 262, 512, 513, 514, 515, 516, 517, 518,
                768, 769, 770, 771, 772, 773, 774, 1024, 1025, 1026, 1027, 1028, 1029, 1030};
        return variantList[EntityUtils.rand(0, variantList.length - 1)];
    }

    @Override
    public boolean mountEntity(Entity entity) {
        Objects.requireNonNull(entity, "The target of the mounting entity can't be null");

        if (entity.riding != null) {
            dismountEntity(entity);
            entity.resetFallDistance();
        } else {
            if (isPassenger(entity)) {
                return false;
            }

            broadcastLinkPacket(entity, TYPE_RIDE);

            entity.riding = this;
            entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, true);
            entity.setDataProperty(new Vector3fEntityData(DATA_RIDER_SEAT_POSITION, new Vector3f(0, 2.3f, 0)));
            passengers.add(entity);
        }

        return true;
    }

    @Override
    public boolean dismountEntity(Entity entity) {
        broadcastLinkPacket(entity, TYPE_REMOVE);
        entity.riding = null;
        entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
        passengers.remove(entity);
        entity.setSeatPosition(new Vector3f());
        updatePassengerPosition(entity);
        return true;
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        if (this.passengers.isEmpty() && !this.isBaby() && !player.isSneaking()) {
            this.mountEntity(player);
        }

        return super.onInteract(player, item);
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        updatePassengers();
        return super.entityBaseTick(tickDiff);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        Iterator<Entity> linkedIterator = this.passengers.iterator();

        while (linkedIterator.hasNext()) {
            cn.nukkit.entity.Entity linked = linkedIterator.next();

            if (!linked.isAlive()) {
                if (linked.riding == this) {
                    linked.riding = null;
                }

                linkedIterator.remove();
            }
        }

        return super.onUpdate(currentTick);
    }
}
