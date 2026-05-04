package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityExplosive;
import cn.nukkit.entity.weather.EntityLightningStrike;
import cn.nukkit.event.entity.CreeperPowerEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityCreeper extends EntityWalkingMob implements EntityExplosive {

    public static final int NETWORK_ID = 33;

    private short bombTime;
    private int explodeTimer; // When ignited by player
    private long seenTarget = -1L;

    public EntityCreeper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public void setPowered(boolean charged) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_POWERED, charged);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        for (int i = 0; i < Utils.rand(0, 2); i++) {
            drops.add(Item.get(Item.GUNPOWDER, 0, 1));
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public float getHeight() {
        return 1.7f;
    }

    @Override
    public int getKillExperience() {
        return 5;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public double getSpeed() {
        return 0.9;
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    public boolean isPowered() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_POWERED);
    }

    @Override
    public void attackEntity(Entity player) {
    }

    public void explode() {
        if (this.closed) return;

        EntityExplosionPrimeEvent ev = new EntityExplosionPrimeEvent(this, this.isPowered() ? 6 : 3);
        this.server.getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this);

            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }

            explosion.explodeB();
        }

        this.close();
    }

    @Override
    public boolean ignoredAsSaveReason() {
        return !this.isPowered() && super.ignoredAsSaveReason();
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();

        if (this.namedTag.contains("powered")) {
            this.setPowered(this.namedTag.getBoolean("powered"));
        }
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (item.getId() == Item.FLINT_AND_STEEL && this.explodeTimer <= 0) {
            level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_IGNITE);
            this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, true);
            this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_TNT);
            this.stayTime = 31;
            this.explodeTimer = 31; // Explodes at 1
            return true;
        }

        return super.onInteract(player, item, clickedPos);
    }

    @Override
    public void onStruckByLightning(Entity lightning) {
        if (this.attack(new EntityDamageByEntityEvent(lightning, this, EntityDamageEvent.DamageCause.LIGHTNING, 5))) {
            if (this.fireTicks < 160) {
                this.setOnFire(8);
            }

            if (lightning instanceof EntityLightningStrike) {
                CreeperPowerEvent event = new CreeperPowerEvent(this, (EntityLightningStrike) lightning, CreeperPowerEvent.PowerCause.LIGHTNING);
                server.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.setPowered(true);
                }
            }
        }
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (this.server.getDifficulty() < 1) {
            this.close();
            return false;
        }

        if (!this.isAlive()) {
            if (++this.deadTicks >= 23) {
                this.close();
                return false;
            }
            return true;
        }

        if (this.explodeTimer > 0) {
            if (this.explodeTimer == 1) {
                this.explode();
                return false;
            }
            this.explodeTimer--;
        }

        int tickDiff = currentTick - this.lastUpdate;
        this.lastUpdate = currentTick;
        this.entityBaseTick(tickDiff);

        Vector3 target = this.updateMove(tickDiff);
        if (target != null) {
            double distance = target.distanceSquared(this);

            if (target instanceof EntityCreature && this.seenTarget == -1L && distance <= 16) {
                if (this.seesTarget(target)) {
                    this.seenTarget = ((EntityCreature) target).getId();
                }
            }

            if (distance <= 16 && target instanceof EntityCreature && this.seenTarget == ((EntityCreature) target).getId()) { // 4 blocks
                if (this.explodeTimer <= 0) {
                    if (bombTime == 0) {
                        this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_TNT);
                        this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, true);
                    }
                    this.bombTime += tickDiff;
                    if (this.bombTime >= 30) {
                        this.explode();
                        return false;
                    }
                }
                if (distance <= 1) {
                    this.stayTime = 10;
                }
            } else {
                if (this.explodeTimer <= 0) {
                    this.setDataFlag(DATA_FLAGS, DATA_FLAG_IGNITED, false);
                    this.bombTime = 0;
                }
            }
        }
        return true;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("powered", this.isPowered());
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            if (!player.closed && player.spawned && player.isAlive() && (player.isSurvival() || player.isAdventure())) {
                PlayerInventory inv = player.getInventory();
                Item helmet;
                if (inv != null && (helmet = inv.getHelmetFast()).getId() == Item.SKULL && helmet.getDamage() == ItemSkull.CREEPER_HEAD) {
                    return distance <= 64;
                }
                return distance <= 256;
            }
            return false;
        }
        return creature.isAlive() && !creature.closed && distance <= 256;
    }
}
