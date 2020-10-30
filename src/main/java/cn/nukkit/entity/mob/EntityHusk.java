package cn.nukkit.entity.mob;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;

public class EntityHusk extends EntityWalkingMob implements EntitySmite {

    public static final int NETWORK_ID = 47;

    public EntityHusk(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.95f;
    }

    @Override
    public double getSpeed() {
        return 1.1;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.setMaxHealth(20);
        this.setDamage(new int[]{0, 3, 4, 6});
    }

    public void setHealth(int health) {
        super.setHealth(health);

        if (this.isAlive()) {
            if (15 < this.getHealth()) {
                this.setDamage(new int[]{0, 2, 3, 4});
            } else if (10 < this.getHealth()) {
                this.setDamage(new int[]{0, 3, 4, 6});
            } else if (5 < this.getHealth()) {
                this.setDamage(new int[]{0, 3, 5, 7});
            } else {
                this.setDamage(new int[]{0, 4, 6, 9});
            }
        }
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && player.distanceSquared(this) <= 1) {
            this.attackDelay = 0;
            player.attack(new EntityDamageByEntityEvent(this, player, DamageCause.ENTITY_ATTACK, getDamage()));
            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = this.getId();
            pk.event = EntityEventPacket.ARM_SWING;
            Server.broadcastPacket(this.getViewers().values(), pk);
            player.addEffect(Effect.getEffect(Effect.HUNGER).setDuration(140));
        }
    }

    @Override
    public Item[] getDrops() {
        return this.isBaby() ? Item.EMPTY_ARRAY : new Item[]{Item.get(Item.ROTTEN_FLESH, 0, Utils.rand(0, 2))};
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        return super.entityBaseTick(tickDiff);
    }
}
