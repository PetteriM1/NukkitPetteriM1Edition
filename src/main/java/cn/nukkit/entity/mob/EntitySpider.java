package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntitySpider extends EntityWalkingMob {

    public static final int NETWORK_ID = 35;
    
    private boolean angry = false;

    public EntitySpider(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1.4f;
    }

    @Override
    public float getHeight() {
        return 0.9f;
    }

    @Override
    public double getSpeed() {
        return 1.13;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(16);
        this.setDamage(new int[] { 0, 2, 2, 3 });
    }

    @Override
    protected boolean checkJump(double dx, double dz) {
        if (this.motionY == this.getGravity() * 2) {
            return this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) this.y, NukkitMath.floorDouble(this.z))) instanceof BlockLiquid;
        } else {
            if (this.level.getBlock(new Vector3(NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z))) instanceof BlockLiquid) {
                this.motionY = this.getGravity() * 2;
                return true;
            }
        }

        Block block = this.getLevel().getBlock(new Vector3(NukkitMath.floorDouble(this.x + dx), (int) this.y, NukkitMath.floorDouble(this.z + dz)));
        Block directionBlock = block.getSide(this.getDirection());
        if (!directionBlock.canPassThrough()) {
            this.motionY = this.getGravity() * 3;
            return true;
        }
        return false;
    }

    @Override
    public void attackEntity(Entity player) {
        int time = player.getLevel().getTime() % Level.TIME_FULL;
        if (!this.isFriendly() || !(player instanceof Player)) {
            if ((time > 13184 && time < 22800) || angry) {
                if (this.attackDelay > 10 && this.distanceSquared(player) < 1.3) {
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
    }
    
    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (!ev.isCancelled()) {
            this.angry = true;
        }

        return true;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (this.hasCustomName()) {
            drops.add(Item.get(Item.NAME_TAG, 0, 1));
        }

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby()) {
            for (int i = 0; i < EntityUtils.rand(0, 3); i++) {
                drops.add(Item.get(Item.STRING, 0, 1));
            }

            for (int i = 0; i < (EntityUtils.rand(0, 3) == 0 ? 1 : 0); i++) {
                drops.add(Item.get(Item.SPIDER_EYE, 0, 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }
}
