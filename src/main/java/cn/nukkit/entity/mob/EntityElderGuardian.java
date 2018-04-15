package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.WalkingMonster;
import cn.nukkit.entity.Utils;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;

import java.util.ArrayList;
import java.util.List;

public class EntityElderGuardian extends WalkingMonster {

  public static final int NETWORK_ID = 50;

  public EntityElderGuardian(FullChunk chunk, CompoundTag nbt) {
      super(chunk, nbt);
  }

  @Override
  public int getNetworkId() {
      return NETWORK_ID;
  }

  @Override
  public float getWidth() {
      return 1.9975f;
  }

  @Override
  public float getHeight() {
      return 1.9975f;
  }

  @Override
  public void initEntity() {
      super.initEntity();

      this.setMaxHealth(80);
  }

  @Override
  public boolean targetOption(EntityCreature creature, double distance) {
      return false;
  }

  @Override
  public void attackEntity(Entity player) {
  }

  @Override
  public Item[] getDrops() {
      List<Item> drops = new ArrayList<>();
      if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
          int prismarineShard = Utils.rand(0, 3);
          for (int i=0; i < prismarineShard; i++) {
              drops.add(Item.get(Item.PRISMARINE_SHARD, 0, 1));
          }
      }
      return drops.toArray(new Item[drops.size()]);
  }

  @Override
  public int getKillExperience() {
      return 10;
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
