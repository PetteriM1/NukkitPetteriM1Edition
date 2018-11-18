package cn.nukkit.entity.mob;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;

public class EntityEnderDragon extends EntityFlyingMob {

    public static final int NETWORK_ID = 53;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityEnderDragon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        return 13f;
    }

    @Override
    public float getHeight() {
        return 4f;
    }

    @Override
    public double getSpeed() {
        return 2;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(200);
        this.setHealth(200);
    }

    @Override
    public int getKillExperience() {
        for (int i = 0; i < 167;) {
            this.level.dropExpOrb(this, 3);
            i++;
        }
        return 0;
    }

    @Override
    public void attackEntity(Entity player) {
    }

    @Override
    public String getName() {
        return "Ender Dragon";
    }

    @Override
    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntity = new AddEntityPacket();
        addEntity.type = this.getNetworkId();
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
        addEntity.metadata = this.dataProperties;
        addEntity.attributes = new Attribute[]{Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(200).setValue(200)};
        return addEntity;
    }
}
