package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntitySpider extends EntityWalkingMob implements EntityArthropod {

    public static final int NETWORK_ID = 35;

    private int angry = 0;

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
            int b = level.getBlockIdAt(chunk, NukkitMath.floorDouble(this.x), (int) this.y, NukkitMath.floorDouble(this.z));
            return b == BlockID.WATER || b == BlockID.STILL_WATER;
        } else {
            int b = level.getBlockIdAt(chunk, NukkitMath.floorDouble(this.x), (int) (this.y + 0.8), NukkitMath.floorDouble(this.z));
            if (b == BlockID.WATER || b == BlockID.STILL_WATER) {
                this.motionY = this.getGravity() * 2;
                return true;
            }
        }

        try {
            Block block = this.getLevel().getBlock(new Vector3(NukkitMath.floorDouble(this.x + dx), (int) this.y, NukkitMath.floorDouble(this.z + dz)));
            Block directionBlock = block.getSide(this.getDirection());
            if (!directionBlock.canPassThrough()) {
                this.motionY = this.getGravity() * 3;
                return true;
            }
        } catch (Exception ignore) {}

        return false;
    }

    @Override
    public void attackEntity(Entity player) {
        if (!this.isFriendly() || !(player instanceof Player)) {
            if (this.isAngry()) {
                if (this.attackDelay > 23 && this.distanceSquared(player) < 1.3) {
                    this.attackDelay = 0;
                    HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
                    damage.put(EntityDamageEvent.DamageModifier.BASE, (float) this.getDamage());

                    if (player instanceof Player) {
                        HashMap<Integer, Float> armorValues = new ArmorPoints();

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

        if (!ev.isCancelled() && ev instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) ev).getDamager() instanceof Player) {
                this.setAngry(2400);
            }
        }

        return true;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        for (int i = 0; i < Utils.rand(0, 2); i++) {
            drops.add(Item.get(Item.STRING, 0, 1));
        }

        for (int i = 0; i < (Utils.rand(0, 2) == 0 ? 1 : 0); i++) {
            drops.add(Item.get(Item.SPIDER_EYE, 0, 1));
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        if (this.angry > 0) {
            this.angry--;
        }

        return super.entityBaseTick(tickDiff);
    }

    public boolean isAngry() {
        int time = this.level.getTime() % Level.TIME_FULL;
        return this.angry > 0 || (time > 13184 && time < 22800);
    }

    public void setAngry(int val) {
        this.angry = val;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return this.isAngry() && super.targetOption(creature, distance);
    }
}
