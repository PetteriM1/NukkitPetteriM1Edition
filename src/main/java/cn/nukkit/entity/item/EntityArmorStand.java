package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.ItemArmorStand;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

/**
 * Created by PetteriM1
 */
public class EntityArmorStand extends Entity {

	public static final int NETWORK_ID = 61;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityArmorStand(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

	@Override
    protected void initEntity() {
        super.initEntity();

		this.setHealth(4);
        this.setMaxHealth(4);
    }

	@Override
	public void spawnTo(Player player) {
		AddEntityPacket pk = new AddEntityPacket();
		pk.type = EntityArmorStand.NETWORK_ID;
		pk.entityUniqueId = this.getId();
        pk.entityRuntimeId = getId();
		pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
		pk.speedX = (float) this.motionX;
        pk.speedY = (float) this.motionY;
        pk.speedZ = (float) this.motionZ;
		pk.metadata = new EntityMetadata();
		player.dataPacket(pk);
	}

	@Override
    public boolean attack(EntityDamageEvent source) {
        boolean onCreative = false;

        if (source instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
            onCreative = damager instanceof Player && ((Player) damager).isCreative();
        }

        if (!onCreative && level.getGameRules().getBoolean("doEntityDrops")) {
            this.level.dropItem(this, new ItemArmorStand());
        }
		this.remove();

        return true;
    }

	public void remove() {
		this.level.addParticle(new SmokeParticle(this));
        this.close();
		this.kill();

		Player[] players = this.getViewers().values().toArray(new Player[0]);
		RemoveEntityPacket pk = new RemoveEntityPacket();
		pk.eid = this.getId();
		Server.broadcastPacket(players, pk);
	}

    @Override
	public boolean canCollideWith(Entity entity) {
		return false;
	}
}
