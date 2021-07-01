package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityControllable;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.entity.mob.EntityZombiePigman;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static cn.nukkit.network.protocol.SetEntityLinkPacket.TYPE_RIDE;

public class EntityPig extends EntityWalkingAnimal implements EntityRideable, EntityControllable {

    public static final int NETWORK_ID = 12;

    private boolean saddled;

    public EntityPig(FullChunk chunk, CompoundTag nbt) {
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
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(10);

        if (this.namedTag.contains("Saddle")) {
            this.setSaddled(this.namedTag.getBoolean("Saddle"));
        }
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            int id = player.getInventory().getItemInHandFast().getId();
            return player.spawned && player.isAlive() && !player.closed
                    && (id == Item.CARROT
                    || id == Item.POTATO
                    || id == Item.CARROT_ON_A_STICK
                    || id == Item.BEETROOT)
                    && distance <= 40;
        }
        return false;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (item.getId() == Item.CARROT && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(0,this.getMountedYOffset(),0),Item.get(Item.CARROT)));
            this.setInLove();
            return true;
        } else if (item.getId() == Item.POTATO && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(0,this.getMountedYOffset(),0),Item.get(Item.POTATO)));
            this.setInLove();
            return true;
        } else if (item.getId() == Item.BEETROOT && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(0,this.getMountedYOffset(),0),Item.get(Item.BEETROOT)));
            this.setInLove();
            return true;
        } else if (item.getId() == Item.SADDLE && !this.isSaddled() && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_SADDLE);
            this.setSaddled(true);
        } else if (this.isSaddled() && this.passengers.isEmpty() && !this.isBaby() && !player.isSneaking()) {
            if (player.riding == null) {
                this.mountEntity(player);
            }
        }
        return super.onInteract(player, item, clickedPos);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            for (int i = 0; i < Utils.rand(1, 3); i++) {
                drops.add(Item.get(this.isOnFire() ? Item.COOKED_PORKCHOP : Item.RAW_PORKCHOP, 0, 1));
            }

            if (this.isSaddled()) {
                drops.add(Item.get(Item.SADDLE));
            }
        }

        return drops.toArray(Item.EMPTY_ARRAY);
    }

    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    public boolean mountEntity(Entity entity, byte mode) {
        Objects.requireNonNull(entity, "The target of the mounting entity can't be null");

        if (entity.riding != null) {
            dismountEntity(entity);
            this.motionX = 0;
            this.motionZ = 0;
            this.stayTime = 20;
        } else {
            if (isPassenger(entity)) {
                return false;
            }

            broadcastLinkPacket(entity, TYPE_RIDE);

            entity.riding = this;
            entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, true);
            entity.setDataProperty(new Vector3fEntityData(DATA_RIDER_SEAT_POSITION, new Vector3f(0, 1.85001f, 0)));
            entity.setDataProperty(new FloatEntityData(DATA_RIDER_MAX_ROTATION, 181));
            passengers.add(entity);
        }

        return true;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        Iterator<Entity> linkedIterator = this.passengers.iterator();

        while (linkedIterator.hasNext()) {
            Entity linked = linkedIterator.next();

            if (!linked.isAlive()) {
                if (linked.riding == this) {
                    linked.riding = null;
                }

                linkedIterator.remove();
            }
        }

        return super.onUpdate(currentTick);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("Saddle", this.isSaddled());
    }

    public boolean isSaddled() {
        return this.saddled;
    }

    public void setSaddled(boolean saddled) {
        this.saddled = saddled;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SADDLED, saddled);
    }

    @Override
    public void onPlayerInput(Player player, double strafe, double forward) {
        if (player.getInventory().getItemInHandFast().getId() == Item.CARROT_ON_A_STICK) {
            this.stayTime = 0;
            this.moveTime = 10;
            this.yaw = player.yaw;

            strafe *= 0.4;

            double f = strafe * strafe + forward * forward;
            double friction = 0.3;

            if (f >= 1.0E-4) {
                f = Math.sqrt(f);

                if (f < 1) {
                    f = 1;
                }

                f = friction / f;
                strafe *= f;
                forward *= f;
                double f1 = Math.sin(this.yaw * 0.017453292);
                double f2 = Math.cos(this.yaw * 0.017453292);
                this.motionX = (strafe * f2 - forward * f1);
                this.motionZ = (forward * f2 + strafe * f1);
            } else {
                this.motionX = 0;
                this.motionZ = 0;
            }
        }
    }

    @Override
    protected void checkTarget() {
        if (this.passengers.isEmpty() || !(this.getPassengers().get(0) instanceof Player) || ((Player) this.getPassengers().get(0)).getInventory().getItemInHandFast().getId() != Item.CARROT_ON_A_STICK) {
            super.checkTarget();
        }
    }

    @Override
    public boolean canDespawn() {
        if (this.isSaddled()) {
            return false;
        }

        return super.canDespawn();
    }

    @Override
    public void onStruckByLightning(Entity entity) {
        Entity ent = Entity.createEntity("ZombiePigman", this);
        if (ent != null) {
            CreatureSpawnEvent cse = new CreatureSpawnEvent(EntityZombiePigman.NETWORK_ID, this, ent.namedTag, CreatureSpawnEvent.SpawnReason.LIGHTNING);
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

    @Override
    public void updatePassengers() {
        if (this.passengers.isEmpty()) {
            return;
        }

        for (Entity passenger : new ArrayList<>(this.passengers)) {
            if (!passenger.isAlive() || this.isInsideOfWater()) {
                dismountEntity(passenger);
                continue;
            }

            updatePassengerPosition(passenger);
        }
    }
}
