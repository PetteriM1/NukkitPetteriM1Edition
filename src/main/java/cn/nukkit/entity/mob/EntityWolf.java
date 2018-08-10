package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.mob.EntityTameableMob;
import cn.nukkit.entity.EntityUtils;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.network.protocol.AddEntityPacket;

import java.util.HashMap;

public class EntityWolf extends EntityTameableMob {

  public static final int     NETWORK_ID           = 14;

  private static final String NBT_KEY_ANGRY        = "Angry";

  private static final String NBT_KEY_COLLAR_COLOR = "CollarColor";

  private int                 angry                = 0;

  private DyeColor            collarColor          = DyeColor.RED;

  public EntityWolf(FullChunk chunk, CompoundTag nbt) {
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
      return 0.85f;
  }

  @Override
  public double getSpeed() {
      return 1.2;
  }

  @Override
  protected void initEntity() {
      super.initEntity();
      this.setDamage(new int[] { 0, 3, 4, 6 });

      if (this.namedTag.contains(NBT_KEY_ANGRY)) {
          this.angry = this.namedTag.getInt(NBT_KEY_ANGRY);
      }

      if (this.namedTag.contains(NBT_KEY_COLLAR_COLOR)) {
          this.collarColor = DyeColor.getByDyeData(this.namedTag.getInt(NBT_KEY_COLLAR_COLOR));
      }

      this.setMaxHealth(8);
  }

  @Override
  public void saveNBT() {
      super.saveNBT();
      this.namedTag.putInt(NBT_KEY_ANGRY, this.angry);
      this.namedTag.putInt(NBT_KEY_COLLAR_COLOR, this.collarColor.getDyeData());
  }

  @Override
  public boolean targetOption(EntityCreature creature, double distance) {
      return this.isAngry() && super.targetOption(creature, distance);
  }

  public boolean isAngry() {
      return this.angry > 0;
  }

  public void setAngry(boolean angry) {
      this.angry = angry ? 1 : 0;
  }

  @Override
  public boolean onInteract(Player player, Item item) {
      if(item.equals(Item.get(Item.BONE))){
          if(!this.hasOwner()) {
              player.getInventory().removeItem(Item.get(Item.BONE, 0, 1));
              if (EntityUtils.rand(0, 3) == 3) {
                  EntityEventPacket packet = new EntityEventPacket();
                  packet.eid = player.getId();
                  packet.event = EntityEventPacket.TAME_SUCCESS;
                  Server.broadcastPacket(new Player[]{player}, packet);
                  this.setOwner(player);
                  this.setCollarColor(DyeColor.RED);
                  return true;
              } else {
                  EntityEventPacket packet = new EntityEventPacket();
                  packet.eid = player.getId();
                  packet.event = EntityEventPacket.TAME_FAIL;
                  Server.broadcastPacket(new Player[]{player}, packet);
              }
          }
      }else if(item.equals(Item.get(Item.DYE),false)){
          if(this.hasOwner() && player.equals(this.getOwner())){
              this.setCollarColor(((ItemDye)item).getDyeColor());
              return true;
          }
      }
      return false;
  }

  @Override
  public boolean attack(EntityDamageEvent ev) {
      super.attack(ev);

      if (!ev.isCancelled()) {
          this.setAngry(true);
      }
      return true;
  }

  @Override
  public void attackEntity(Entity player) {
      if (this.getServer().getMobAiEnabled()) {
          if (this.attackDelay > 10 && this.distanceSquared(player) < 1.6) {
              this.attackDelay = 0;
              HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
              damage.put(EntityDamageEvent.DamageModifier.BASE, (float) this.getDamage());

              if (player instanceof Player) {
                  @SuppressWarnings("serial")
                  HashMap<Integer, Float> armorValues = new HashMap<Integer, Float>() {

                      {
                          put(Item.LEATHER_CAP, 1f);
                          put(Item.LEATHER_TUNIC, 3f);
                          put(Item.LEATHER_PANTS, 2f);
                          put(Item.LEATHER_BOOTS, 1f);
                          put(Item.CHAIN_HELMET, 1f);
                          put(Item.CHAIN_CHESTPLATE, 5f);
                          put(Item.CHAIN_LEGGINGS, 4f);
                          put(Item.CHAIN_BOOTS, 1f);
                          put(Item.GOLD_HELMET, 1f);
                          put(Item.GOLD_CHESTPLATE, 5f);
                          put(Item.GOLD_LEGGINGS, 3f);
                          put(Item.GOLD_BOOTS, 1f);
                          put(Item.IRON_HELMET, 2f);
                          put(Item.IRON_CHESTPLATE, 6f);
                          put(Item.IRON_LEGGINGS, 5f);
                          put(Item.IRON_BOOTS, 2f);
                          put(Item.DIAMOND_HELMET, 3f);
                          put(Item.DIAMOND_CHESTPLATE, 8f);
                          put(Item.DIAMOND_LEGGINGS, 6f);
                          put(Item.DIAMOND_BOOTS, 3f);
                      }
                  };

                  float points = 0;
                  for (Item i : ((Player) player).getInventory().getArmorContents()) {
                      points += armorValues.getOrDefault(i.getId(), 0f);
                  }

                  damage.put(EntityDamageEvent.DamageModifier.ARMOR,
                          (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
              }
              player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
          }
      }
  }

  @Override
  public Item[] getDrops() {
      return new Item[0];
  }

  @Override
  public int getKillExperience() {
      return 3;
  }

  public void setCollarColor(DyeColor color) {
      this.namedTag.putInt(NBT_KEY_COLLAR_COLOR, color.getDyeData());
      this.setDataProperty(new IntEntityData(DATA_COLOUR, color.getColor().getRGB()));
      this.collarColor = color;
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
