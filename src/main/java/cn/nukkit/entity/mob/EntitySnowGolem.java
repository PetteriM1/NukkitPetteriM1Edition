package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.projectile.EntitySnowball;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;

public class EntitySnowGolem extends EntityWalkingMob {

    public static final int NETWORK_ID = 21;

    private boolean sheared = false;
    private boolean createSnow;
    private int cachedBiome;

    public EntitySnowGolem(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.SNOWBALL, 0, Utils.rand(0, 15))};
    }

    @Override
    protected float getFreezingDamage() {
        return 0f;
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }

    @Override
    public int getKillExperience() {
        return 0;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getNameTag() : "Snow Golem";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.7f;
    }

    /**
     * @return whether the snow golem is sheared
     */
    public boolean isSheared() {
        return this.sheared;
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        if (super.attack(ev)) {
            if (ev instanceof EntityDamageByEntityEvent) {
                this.isAngryTo = ((EntityDamageByEntityEvent) ev).getDamager().getId();
            }
            return true;
        }

        return false;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && Utils.rand(1, 32) < 4 && this.distanceSquared(player) <= 55) {
            if (!this.seesTarget(player)) {
                return;
            }

            this.attackDelay = 0;

            EntitySnowball shot = (EntitySnowball) Entity.createEntity("Snowball", this.add(0, this.getEyeHeight(), 0), this);

            if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                shot.close();
                return;
            }

            shot.setMotion(player.add(0, 0.3, 0).subtract(this).normalize().multiply(1.2));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(shot);
            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                shot.close();
            } else {
                shot.spawnToAll();
                this.level.addSound(this, Sound.MOB_SNOWGOLEM_SHOOT);
            }
        }
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean canTarget(Entity entity) {
        return entity.canBeFollowed() && entity.getId() == this.isAngryTo;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (hasUpdate && (!server.suomiCraftPEMode() || isInTickingRange())) {
            if (this.age % 20 == 0 && (this.level.getDimension() == Level.DIMENSION_NETHER || (this.level.isRaining() && this.canSeeSky() && !EnumBiome.getBiome(this.level.getBiomeId((int) x, (int) z)).isFreezing()))) {
                this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.FIRE_TICK, 1));
            }

            if (this.createSnow && !this.closed && this.isAlive() && this.age % 10 == 0) {
                if (this.age % 300 == 0) {
                    this.cachedBiome = chunk.getBiomeId(getFloorX() & 0x0f, getFloorZ() & 0x0f);
                }

                if (cachedBiome != 2 && cachedBiome != 8 && cachedBiome != 21 && cachedBiome != 22 && cachedBiome != 23 && cachedBiome != 37 && cachedBiome != 38 && cachedBiome != 39
                        && cachedBiome != 130 && cachedBiome != 149 && cachedBiome != 151 && cachedBiome != 165 && cachedBiome != 166 && cachedBiome != 167) {
                    int bid = level.getBlockIdAt(this.chunk, getFloorX(), getFloorY(), getFloorZ());
                    if (Block.isWater(bid)) {
                        this.attack(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.SUFFOCATION, 1));
                    } else if (bid == Block.AIR && !Block.isBlockTransparentById(level.getBlockIdAt(this.chunk, getFloorX(), getFloorY() - 1, getFloorZ()))) {
                        level.setBlockAt(getFloorX(), getFloorY(), getFloorZ(), Block.SNOW_LAYER, 0);
                    }
                }
            }
        }

        return hasUpdate;
    }

    @Override
    public boolean ignoredAsSaveReason() {
        return !this.sheared && super.ignoredAsSaveReason();
    }

    @Override
    public void initEntity() {
        this.setFriendly(true);
        this.setMaxHealth(4);
        super.initEntity();
        this.noFallDamage = true;

        if (this.namedTag.getBoolean("Sheared")) {
            this.shear(true);
        }

        this.createSnow = this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING);
        if (this.createSnow) {
            this.cachedBiome = chunk.getBiomeId(getFloorX() & 0x0f, getFloorZ() & 0x0f);
        }
    }

    @Override
    protected int nearbyDistanceMultiplier() {
        return target instanceof EntityLiving || followTarget instanceof EntityLiving ? 6 : 1;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (item.getId() == Item.SHEARS && !this.sheared) {
            this.shear(true);
            this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_SHEAR);
            player.getInventory().getItemInHand().setDamage(item.getDamage() + 1);
            return true;
        }

        return super.onInteract(player, item, clickedPos);
    }

    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putBoolean("Sheared", this.isSheared());
    }

    public void shear(boolean shear) {
        this.sheared = shear;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SHEARED, shear);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return (!(creature instanceof Player) || creature.getId() == this.isAngryTo) && creature.isAlive() && distance <= 256;
    }
}
