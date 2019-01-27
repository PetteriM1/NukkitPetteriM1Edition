package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmorStand;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.RemoveEntityPacket;

public class EntityArmorStand extends Entity {

	public static final int NETWORK_ID = 61;

	public final String TAG_MAINHAND = "Mainhand";
	public final String TAG_OFFHAND = "Offhand";
	public final String TAG_POSE_INDEX = "PoseIndex";
	public final String TAG_ARMOR = "Armor";

	@Override
	public int getNetworkId() {
		return NETWORK_ID;
	}

	@Override
    protected float getGravity() {
        return 0.04f;
    }

	@Override
    public float getHeight() {
        return 1.975f;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

	public EntityArmorStand(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);

		if (nbt.contains(TAG_POSE_INDEX)) {
            this.setPose(nbt.getInt(TAG_POSE_INDEX));
        }
	}

	@Override
	protected void initEntity() {
		super.initEntity();

		this.setHealth(6);
		this.setMaxHealth(6);
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
		this.level.addParticle(new DestroyBlockParticle(this, Block.get(Block.WOODEN_PLANKS)));
		this.kill();

		Player[] players = this.getLevel().getPlayers().values().toArray(new Player[0]);
		RemoveEntityPacket pk = new RemoveEntityPacket();
		pk.eid = this.getId();
		Server.broadcastPacket(players, pk);
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return false;
	}

	@Override
	public boolean onUpdate(int currentTick) {
		if (this.closed) {
			return false;
		}

		this.timing.startTiming();

		boolean hasUpdate = super.onUpdate(currentTick);

		if (!this.isOnGround()) {
			this.motionX = 0;
			this.motionY -= getGravity();
			this.motionZ = 0;
		}

		this.move(this.motionX, this.motionY, this.motionZ);

		this.timing.stopTiming();

		return hasUpdate;
	}

	@Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt(TAG_POSE_INDEX, this.getPose());
    }

	@Override
	public boolean onInteract(Player player, Item item) {
		if (player.isSneaking()) {
			if (this.getPose() >= 12) {
				this.setPose(0);
			} else {
				this.setPose(this.getPose() + 1);
			}
			return true;
		}
		return false;
	}

	public int getPose() {
		return this.dataProperties.getInt(Entity.DATA_ARMOR_STAND_POSE_INDEX);
	}

	public void setPose(int pose) {
		this.dataProperties.putInt(Entity.DATA_ARMOR_STAND_POSE_INDEX, pose);
	}
}
