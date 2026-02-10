package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityFlying;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

public abstract class EntityFlyingAnimal extends EntityFlying implements EntityAnimal {

    public EntityFlyingAnimal(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean isFriendly() {
        return true;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (!this.isLeashed()) {
            if (item.getId() == Item.LEAD) {
                this.leash(player);
                return true; // onInteract: true = decrease count
            }
        } else {
            this.unleash();
            return false;
        }

        return super.onInteract(player, item, clickedPos);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (!this.isAlive()) {
            if (++this.deadTicks >= 23) {
                this.close();
                return false;
            }

            return true;
        }

        int tickDiff = currentTick - this.lastUpdate;
        this.lastUpdate = currentTick;
        this.entityBaseTick(tickDiff);

        Vector3 target = this.updateMove(tickDiff);

        /*if (target instanceof Player) {
            if (this.distanceSquared(target) <= 2) {
                //this.pitch = 22;
                this.x = this.lastX;
                this.y = this.lastY;
                this.z = this.lastZ;
            }
        } else if (target != null && this.distanceSquared(target) <= 1) {
            this.moveTime = 0;
        }*/

        return true;
    }
}
