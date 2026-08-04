package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.*;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.projectile.EntityBlueWitherSkull;
import cn.nukkit.entity.projectile.EntityWitherSkull;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Utils;

import java.util.concurrent.ThreadLocalRandom;

public class EntityWither extends EntityFlyingMob implements EntityBoss, EntitySmite {

    public static final int NETWORK_ID = 52;

    /**
     * Whether the wither is exploded and dying
     */
    private boolean exploded;
    /**
     * Did a block breaking "explosion" last time the wither was attacked
     */
    private boolean wasExplosion;

    public EntityWither(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        return 3.5f;
    }

    @Override
    public double getSpeed() {
        return 1.3;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(witherMaxHealth());
        super.initEntity();

        this.fireProof = true;
        this.setDamage(new int[]{0, 2, 4, 6});
        if (this.age == 0) {
            this.setDataProperty(new IntEntityData(DATA_WITHER_INVULNERABLE_TICKS, 200));

            this.stayTime = 220;
        }
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            if (!player.isSurvival() && !player.isAdventure()) {
                return false;
            }
        }
        return creature.isAlive() && !creature.closed && distance <= 10000;
    }

    @Override
    public int getKillExperience() {
        return 50;
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.age > 220 && this.attackDelay > 40 && this.distanceSquared(player) <= 4096) {
            this.attackDelay = 0;

            Entity k;
            ProjectileLaunchEvent launch;
            EntityWitherSkull shot;
            if (Utils.rand(0, 200) > 180 || Utils.rand(0, 200) < 20) {
                k = Entity.createEntity("BlueWitherSkull", this.add(0, this.getEyeHeight(), 0), this);
                shot = (EntityBlueWitherSkull) k;
                ((EntityBlueWitherSkull) shot).setExplode(true);
                shot.setMotion(player.subtract(this).normalize().multiply(1.1));
            } else {
                k = Entity.createEntity("WitherSkull", this.add(0, this.getEyeHeight(), 0), this);
                shot = (EntityWitherSkull) k;
                shot.setMotion(player.subtract(this).normalize().multiply(1.2));
            }

            if (shot.level.hasCollisionBlocks(shot, shot.boundingBox)) {
                shot.close();
                return;
            }

            launch = new ProjectileLaunchEvent(shot);

            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                shot.close();
            } else {
                shot.spawnToAll();
                this.level.addSound(this, Sound.MOB_WITHER_SHOOT);
            }
        }
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.NETHER_STAR, 0, 1), Block.get(Item.WITHER_ROSE, 0).toItem()};
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
        addEntity.attributes = new Attribute[]{Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(witherMaxHealth()).setValue(witherMaxHealth())};
        return addEntity;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getDifficulty() == 0) {
            this.close();
            return true;
        }

        if (!this.closed && this.age == 200) {
            this.explode();
            this.setDataProperty(new IntEntityData(DATA_WITHER_INVULNERABLE_TICKS, 0));
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    protected int nearbyDistanceMultiplier() {
        return target instanceof EntityLiving || followTarget instanceof EntityLiving ? 20 : 1;
    }

    @Override
    public void kill() {
        if (!this.isAlive()) {
            return;
        }

        if (!this.exploded && this.lastDamageCause != null && EntityDamageEvent.DamageCause.SUICIDE != this.lastDamageCause.getCause()) {
            if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) this.lastDamageCause).getDamager();
                if (damager instanceof Player) {
                    ((Player) damager).awardAchievement("killWither");
                }
            }

            this.exploded = true;
            this.explode();
        }

        super.kill();
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        if (this.age <= 200 && ev.getCause() != EntityDamageEvent.DamageCause.SUICIDE) {
            return false;
        }

        boolean r = super.attack(ev);
        if (this.wasExplosion) {
            this.wasExplosion = false;
        } else if (r && server.explosionBreakBlocks && ev instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) ev).getDamager() instanceof Player && !this.closed && this.isAlive() && Utils.rand() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
            this.wasExplosion = true;

            EntityExplosionPrimeEvent explosionPrimeEvent = new EntityExplosionPrimeEvent(this, 1);
            this.server.getPluginManager().callEvent(explosionPrimeEvent);

            if (!explosionPrimeEvent.isCancelled()) {
                this.level.addSound(this, Sound.MOB_WITHER_BREAK_BLOCK);

                if (!explosionPrimeEvent.isBlockBreaking()) {
                    return true;
                }

                int fx = this.getFloorX();
                int fy = this.getFloorY();
                int fz = this.getFloorZ();
                Item tool = Item.get(Item.DIAMOND_PICKAXE);
                Vector3 pos = new Vector3(0, 0, 0);
                ThreadLocalRandom random = ThreadLocalRandom.current();

                for (int x = fx - 2; x <= fx + 2; x++) {
                    for (int y = fy; y <= fy + 4; y++) {
                        for (int z = fz - 2; z <= fz + 2; z++) {
                            Block block = this.level.getBlock(this.chunk, x, y, z, true);
                            if (block.isBreakable(tool)) {
                                this.level.setBlock(x, y, z, Block.LAYER_NORMAL, Block.get(Block.AIR), false, true);
                                BlockEntity blockEntity = this.level.getBlockEntityIfLoaded(this.chunk, pos.setComponents(x, y, z));
                                if (blockEntity != null) {
                                    blockEntity.onBreak();
                                    blockEntity.close();
                                    this.level.updateComparatorOutputLevel(block);
                                }
                                if (this.level.getGameRules().getBoolean(GameRule.DO_TILE_DROPS) && random.nextDouble() * 100 < 14) {
                                    for (Item drop : block.getDrops(tool)) {
                                        this.level.dropItem(block, drop);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return r;
    }

    private int witherMaxHealth() {
        switch (this.getServer().getDifficulty()) {
            case 2:
                return 450;
            case 3:
                return 600;
            default:
                return 300;
        }
    }

    private void explode() {
        EntityExplosionPrimeEvent ev = new EntityExplosionPrimeEvent(this, 7);
        this.server.getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            Explosion explosion = new Explosion(this, (float) ev.getForce(), this);

            if (ev.isBlockBreaking() && this.level.getGameRules().getBoolean(GameRule.MOB_GRIEFING)) {
                explosion.explodeA();
            }

            explosion.explodeB();
        }
    }

    @Override
    public boolean canTarget(Entity entity) {
        return entity.canBeFollowed();
    }

    @Override
    public boolean canDespawn() {
        return false;
    }
}
