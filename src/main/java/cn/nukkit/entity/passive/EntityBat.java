package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.passive.EntityFlyingAnimal;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

public class EntityBat extends EntityFlyingAnimal {

  public static final int NETWORK_ID = 19;

  public EntityBat(FullChunk chunk, CompoundTag nbt) {
      super(chunk, nbt);
  }

  @Override
  public int getNetworkId() {
      return NETWORK_ID;
  }

  @Override
  public float getWidth() {
      return 0.5f;
  }

  @Override
  public float getHeight() {
      return 0.9f;
  }

  @Override
  public void initEntity() {
      super.initEntity();

      this.setMaxHealth(6);
  }

  @Override
  public boolean targetOption(EntityCreature creature, double distance) {
      return false;
  }

  @Override
  public Item[] getDrops() {
      return new Item[0];
  }

  @Override
  public int getKillExperience() {
      return 0;
  }

  @Override
  public void spawnTo(Player player) {
      AddEntityPacket pk = new AddEntityPacket();
      pk.type = this.getNetworkId();
      pk.entityUniqueId = this.getId();
      pk.entityRuntimeId = this.getId();
      pk.x = (float) this.x;
      pk.y = (float) this.y;
      pk.z = (float) this.z;
      pk.speedX = (float) this.motionX;
      pk.speedY = (float) this.motionY;
      pk.speedZ = (float) this.motionZ;
      pk.metadata = this.dataProperties;
      player.dataPacket(pk);

      super.spawnTo(player);
  }

}
