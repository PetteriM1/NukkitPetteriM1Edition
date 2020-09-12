package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static cn.nukkit.network.protocol.SetEntityLinkPacket.TYPE_REMOVE;
import static cn.nukkit.network.protocol.SetEntityLinkPacket.TYPE_RIDE;

/**
 * @author PetteriM1
 */
public class EntityHorseBase extends EntityWalkingAnimal implements EntityRideable {

    public EntityHorseBase(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return -1;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public boolean mountEntity(Entity entity) {
        Objects.requireNonNull(entity, "The target of the mounting entity can't be null");

        if (entity.riding != null) {
            this.dismountEntity(entity);
            entity.resetFallDistance();
        } else {
            if (this.isPassenger(entity)) {
                return false;
            }

            this.broadcastLinkPacket(entity, TYPE_RIDE);

            entity.riding = this;
            entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, true);
            entity.setDataProperty(new Vector3fEntityData(DATA_RIDER_SEAT_POSITION, new Vector3f(0, this instanceof EntityDonkey ? 2.1f : 2.3f, 0)));
            this.passengers.add(entity);
        }

        return true;
    }

    @Override
    public boolean dismountEntity(Entity entity) {
        this.broadcastLinkPacket(entity, TYPE_REMOVE);
        entity.riding = null;
        entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
        this.passengers.remove(entity);
        entity.setSeatPosition(new Vector3f());
        this.updatePassengerPosition(entity);
        return true;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (this.passengers.isEmpty() && !this.isBaby() && !player.isSneaking()) {
            if (player.riding == null) {
                this.mountEntity(player);
            }
        }

        return super.onInteract(player, item, clickedPos);
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

    public void onPlayerInput(Player player, double strafe, double forward) {
        this.stayTime = 0;
        this.moveTime = 10;
        this.yaw = player.yaw;

        strafe *= 0.4;

        double f = strafe * strafe + forward * forward;
        double friction = 0.6;

        if (f >= 1.0E-4) {
            f = Math.sqrt(f);

            if (f < 1) {
                f = 1;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            double f1 = Math.sin(this.yaw * 0.017453292);
            double f2 = Math.cos(this.yaw * 0.017453292);
            this.motionX = (strafe * f2 - forward * f1);
            this.motionZ = (forward * f2 + strafe * f1);
        } else {
            this.motionX = 0;
            this.motionZ = 0;
        }
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return this.passengers.isEmpty();
    }

    @Override
    public boolean canDespawn() {
        if (!this.getPassengers().isEmpty()) {
            return false;
        }

        return super.canDespawn();
    }

    @Override
    public void updatePassengers() {
        if (this.passengers.isEmpty()) {
            return;
        }

        for (Entity passenger : new ArrayList<>(this.passengers)) {
            if (!passenger.isAlive() || this.isInsideOfWater()) {
                this.dismountEntity(passenger);
                continue;
            }
            this.updatePassengerPosition(passenger);
        }
    }
}
