package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityBoss;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;

public class EntityEnderDragon extends EntityFlyingMob implements EntityBoss {

    public static final int NETWORK_ID = 53;

    public EntityEnderDragon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected boolean applyNameTag(Player player, Item nameTag) {
        return false;
    }

    @Override
    public void attackEntity(Entity player) {
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntity = new AddEntityPacket();
        addEntity.type = NETWORK_ID;
        addEntity.entityUniqueId = this.getId();
        addEntity.entityRuntimeId = this.getId();
        addEntity.yaw = (float) this.yaw;
        addEntity.headYaw = (float) this.yaw;
        addEntity.pitch = (float) this.pitch;
        addEntity.x = (float) this.x;
        addEntity.y = (float) this.y;
        addEntity.z = (float) this.z;
        addEntity.speedX = (float) this.motionX;
        addEntity.speedY = (float) this.motionY;
        addEntity.speedZ = (float) this.motionZ;
        addEntity.metadata = this.dataProperties.clone();
        addEntity.attributes = new Attribute[]{Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(200).setValue(200)};
        return addEntity;
    }

    @Override
    public float getHeight() {
        return 8f;
    }

    @Override
    public int getKillExperience() {
        for (int i = 0; i < 167; ) {
            this.level.dropExpOrb(this, 3);
            i++;
        }
        return 0;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Ender Dragon";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public double getSpeed() {
        return 3;
    }

    @Override
    public float getWidth() {
        return 16f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(200);
        super.initEntity();

        this.fireProof = true;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_FIRE_IMMUNE, true);
    }

    @Override
    public void knockBack(Entity attacker, double damage, double x, double z, double base) {
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return false;
    }
}
