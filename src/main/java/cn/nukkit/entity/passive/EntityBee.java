package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBeehive;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityArthropod;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityPotionEffectEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDye;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Utils;

import java.util.HashMap;

public class EntityBee extends EntityFlyingMob implements EntityArthropod { // A mob because it needs to have an attack behavior

    public static final int NETWORK_ID = 122;

    private int angry;
    private int dieInTicks = -1;
    private boolean hasNectar;
    private int stayAtFlower;
    private int plantsFertilized;
    private Vector3 hive;
    private Vector3 followBlock;

    public EntityBee(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        this.setNectar(false);

        if (source.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            if (ticksLived < 10) {
                source.setCancelled();
                return false;
            }
        }

        if (source instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) source;
            for (Entity entity : getLevel().getCollidingEntities(this.getBoundingBox().grow(8, 8, 8))) {
                if (entity instanceof EntityBee && ((EntityBee) entity).hasSting()) {
                    ((EntityBee) entity).setAngry(event.getDamager());
                }
            }
        }

        return super.attack(source);
    }

    @Override
    public void attackEntity(Entity player) {
        if (this.attackDelay > 23 && this.distanceSquared(player) < 1.3) {
            this.attackDelay = 0;

            this.target = this.followTarget = null;
            this.dieInTicks = 500;
            this.setAngry(0);

            HashMap<EntityDamageEvent.DamageModifier, Float> damage = new HashMap<>();
            damage.put(EntityDamageEvent.DamageModifier.BASE, (float) this.getDamage());
            if (player instanceof Player) {
                float points = 0;
                for (Item i : ((Player) player).getInventory().getArmorContents()) {
                    points += this.getArmorPoints(i.getId());
                }
                damage.put(EntityDamageEvent.DamageModifier.ARMOR,
                        (float) (damage.getOrDefault(EntityDamageEvent.DamageModifier.ARMOR, 0f) - Math.floor(
                                damage.getOrDefault(EntityDamageEvent.DamageModifier.BASE, 1f) * points * 0.04)));
            }
            if (player.attack(new EntityDamageByEntityEvent(this, player, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage))) {
                if (this.getServer().getDifficulty() == 2) {
                    player.addEffect(Effect.getEffect(Effect.POISON).setDuration(200), EntityPotionEffectEvent.Cause.ATTACK);
                } else if (this.getServer().getDifficulty() == 3) {
                    player.addEffect(Effect.getEffect(Effect.POISON).setDuration(360), EntityPotionEffectEvent.Cause.ATTACK);
                }
            }
        }
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.25f;
        }
        return 0.5f;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public double getSpeed() {
        return this.angry > 0 ? 1.5 : 1;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.275f;
        }
        return 0.55f;
    }

    public boolean hasNectar() {
        return this.hasNectar;
    }

    public boolean hasSting() {
        return dieInTicks == -1;
    }

    @Override
    public void initEntity() {
        this.setFriendly(true);
        this.setMaxHealth(10);
        super.initEntity();
        this.setDamage(new int[]{0, 2, 2, 3});

        this.setNectar(this.namedTag.getBoolean("hasNectar"));

        if (namedTag.contains("hiveX") && namedTag.contains("hiveY") && namedTag.contains("hiveZ")) {
            this.hive = new Vector3(namedTag.getDouble("hiveX"), namedTag.getDouble("hiveY"), namedTag.getDouble("hiveZ"));
        }
    }

    public boolean isAngry() {
        return this.angry > 0;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (closed) {
            return false;
        }

        if (!hasSting() && isAlive()) {
            dieInTicks--;
            if (dieInTicks <= 0) {
                kill();
            }
        }

        if (this.angry > 0) {
            if (this.angry == 1) {
                this.setAngry(0); // Reset flag
            } else {
                this.angry--;
            }
        }

        if (followTarget != null && followTarget.closed) {
            followTarget = null;
        }

        if (!isAlive() || !this.isInTickingRange(server.entityActivationRange)) {
            return super.onUpdate(currentTick);
        }

        if (age % 100 == 0 && hasSting() && !isAngry() && this.followTarget == null) {
            if (hasNectar() || getLevel().isRaining() || !getLevel().isAnimalSpawningAllowedByTime()) { // Search for hive
                {
                    int minX = (int) (x - 2);
                    int minY = NukkitMath.clamp((int) (y - 2), level.getMinBlockY(), level.getMaxBlockY());
                    int minZ = (int) (z - 2);
                    int maxX = (int) (x + 2);
                    int maxY = NukkitMath.clamp((int) (y + 2), level.getMinBlockY(), level.getMaxBlockY());
                    int maxZ = (int) (z + 2);

                    FullChunk checkChunk = this.chunk;

                    top:
                    for (int z = minZ; z <= maxZ; ++z) {
                        for (int x = minX; x <= maxX; ++x) {
                            for (int y = minY; y <= maxY; ++y) {
                                int cx = x >> 4;
                                int cz = z >> 4;
                                if (checkChunk == null || cx != checkChunk.getX() || cz != checkChunk.getZ()) {
                                    checkChunk = level.getChunkIfLoaded(cx, cz);
                                    if (checkChunk == null) continue;
                                }

                                int block = checkChunk.getBlockId(x & 15, y, z & 15, Block.LAYER_NORMAL);
                                if (block == BlockID.BEEHIVE || block == BlockID.BEE_NEST) {
                                    BlockEntity be;
                                    if ((be = level.getBlockEntityIfLoaded(new Vector3(x, y, z))) instanceof BlockEntityBeehive && ((BlockEntityBeehive) be).getOccupantsCount() < 3 && !((BlockEntityBeehive) be).isHoneyFull()) {
                                        ((BlockEntityBeehive) be).addOccupant(this);
                                        break top;
                                    }
                                } else if (hasNectar() && plantsFertilized < 10 && (block == BlockID.WHEAT_BLOCK || block == BlockID.POTATO_BLOCK || block == BlockID.CARROT_BLOCK || block == BlockID.BEETROOT_BLOCK || block == BlockID.MELON_STEM || block == BlockID.SWEET_BERRY_BUSH || block == BlockID.PUMPKIN_STEM)) {
                                    if (x == (int) this.x && z == (int) this.z) {
                                        level.getBlock(this.chunk, x, y, z, false).onActivate(Item.get(Item.DYE, ItemDye.BONE_MEAL), null);
                                        plantsFertilized++;
                                    }
                                }
                            }
                        }
                    }
                }

                if (!this.closed) { // No addOccupant
                    if (this.hive != null && this.distanceSquared(this.hive) > 25) {
                        target(this.hive);
                        return super.onUpdate(currentTick);
                    }

                    int minX = (int) x - 16;
                    int minY = NukkitMath.clamp((int) y - 16, level.getMinBlockY(), level.getMaxBlockY());
                    int minZ = (int) z - 16;
                    int maxX = (int) x + 16;
                    int maxY = NukkitMath.clamp((int) y + 16, level.getMinBlockY(), level.getMaxBlockY());
                    int maxZ = (int) z + 16;

                    double dist = Integer.MAX_VALUE;
                    Vector3 temp = new Vector3(0, 0, 0);
                    this.followBlock = null;
                    FullChunk checkChunk = this.chunk;

                    for (int z = minZ; z <= maxZ; ++z) {
                        for (int x = minX; x <= maxX; ++x) {
                            for (int y = minY; y <= maxY; ++y) {
                                int cx = x >> 4;
                                int cz = z >> 4;
                                if (checkChunk == null || cx != checkChunk.getX() || cz != checkChunk.getZ()) {
                                    checkChunk = level.getChunkIfLoaded(cx, cz);
                                    if (checkChunk == null) continue;
                                }

                                int block = checkChunk.getBlockId(x & 15, y, z & 15, Block.LAYER_NORMAL);
                                double newDist;
                                if ((block == BlockID.BEEHIVE || block == BlockID.BEE_NEST) && (newDist = temp.setComponents(x, y, z).distanceSquared(this)) < dist) {
                                    BlockEntity be;
                                    // Update optimal distance only if target is not full
                                    if ((be = level.getBlockEntityIfLoaded(temp)) instanceof BlockEntityBeehive && ((BlockEntityBeehive) be).getOccupantsCount() < 3) {
                                        dist = newDist;
                                        if (this.followBlock == null) { // No better target found yet
                                            target(new Vector3(x + 0.5, y + 0.5, z + 0.5));
                                        }
                                    } else {
                                        target(new Vector3(x + 0.5, y + 0.5, z + 0.5));
                                    }
                                }
                            }
                        }
                    }
                }
            } else { // Search for flower
                {
                    int minX = (int) (x - 2);
                    int minY = NukkitMath.clamp((int) (y - 2), level.getMinBlockY(), level.getMaxBlockY());
                    int minZ = (int) (z - 2);
                    int maxX = (int) (x + 2);
                    int maxY = NukkitMath.clamp((int) (y + 2), level.getMinBlockY(), level.getMaxBlockY());
                    int maxZ = (int) (z + 2);

                    FullChunk checkChunk = this.chunk;

                    top:
                    for (int z = minZ; z <= maxZ; ++z) {
                        for (int x = minX; x <= maxX; ++x) {
                            for (int y = minY; y <= maxY; ++y) {
                                int cx = x >> 4;
                                int cz = z >> 4;
                                if (checkChunk == null || cx != checkChunk.getX() || cz != checkChunk.getZ()) {
                                    checkChunk = level.getChunkIfLoaded(cx, cz);
                                    if (checkChunk == null) continue;
                                }

                                int block = checkChunk.getBlockId(x & 15, y, z & 15, Block.LAYER_NORMAL);
                                if (block == BlockID.RED_FLOWER || block == BlockID.DANDELION || block == BlockID.WITHER_ROSE || block == BlockID.FLOWERING_AZALEA || block == BlockID.AZALEA_LEAVES_FLOWERED || block == BlockID.CHORUS_FLOWER || block == BlockID.MANGROVE_PROPAGULE || block == BlockID.SPORE_BLOSSOM) {
                                    if (stayAtFlower == 1) {
                                        this.setNectar(true);
                                        this.getLevel().addSound(this, Sound.MOB_BEE_POLLINATE);
                                    }
                                    stayAtFlower = stayAtFlower > 0 ? stayAtFlower - 1 : 5;
                                    break top;
                                }
                            }
                        }
                    }
                }

                if (stayAtFlower == 0 && !hasNectar) {
                    int minX = (int) x - 16;
                    int minY = NukkitMath.clamp((int) y - 16, level.getMinBlockY(), level.getMaxBlockY());
                    int minZ = (int) z - 16;
                    int maxX = (int) x + 16;
                    int maxY = NukkitMath.clamp((int) y + 16, level.getMinBlockY(), level.getMaxBlockY());
                    int maxZ = (int) z + 16;

                    double dist = Integer.MAX_VALUE;
                    Vector3 temp = new Vector3(0, 0, 0);
                    this.followBlock = null;
                    FullChunk checkChunk = this.chunk;

                    for (int z = minZ; z <= maxZ; ++z) {
                        for (int x = minX; x <= maxX; ++x) {
                            for (int y = minY; y <= maxY; ++y) {
                                int cx = x >> 4;
                                int cz = z >> 4;
                                if (checkChunk == null || cx != checkChunk.getX() || cz != checkChunk.getZ()) {
                                    checkChunk = level.getChunkIfLoaded(cx, cz);
                                    if (checkChunk == null) continue;
                                }

                                int block = checkChunk.getBlockId(x & 15, y, z & 15, Block.LAYER_NORMAL);
                                double newDist;
                                if ((block == BlockID.RED_FLOWER || block == BlockID.DANDELION || block == BlockID.WITHER_ROSE || block == BlockID.FLOWERING_AZALEA || block == BlockID.AZALEA_LEAVES_FLOWERED || block == BlockID.CHORUS_FLOWER || block == BlockID.MANGROVE_PROPAGULE || block == BlockID.SPORE_BLOSSOM) &&
                                        (newDist = temp.setComponents(x, y, z).distanceSquared(this)) < dist) {
                                    dist = newDist;
                                    target(new Vector3(x + 0.5, y + 0.5, z + 0.5));
                                }
                            }
                        }
                    }
                }
            }
        }

        return super.onUpdate(currentTick);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        if (hasNectar) {
            this.namedTag.putBoolean("hasNectar", true);
        } else {
            this.namedTag.remove("hasNectar");
        }
    }

    public void setAngry(int ticks) {
        this.followBlock = null;
        this.angry = ticks;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_ANGRY, this.angry > 0);
        this.setFriendly(ticks <= 0);
    }

    public void setAngry(Entity entity) {
        setAngry(500);
        target(entity);
    }

    public void setNectar(boolean hasNectar) {
        this.hasNectar = hasNectar;
    }

    private void target(Vector3 pos) {
        this.target = this.followBlock = pos;
        this.stayTime = 0;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        return this.isAngry() && super.targetOption(creature, distance);
    }

    @Override
    public Vector3 updateMove(int tickDiff) {
        if (!this.isInTickingRange(server.entityActivationRange)) {
            return null;
        }

        if (!this.isImmobile()) {
            if (this.isKnockback()) {
                if (this.riding == null) {
                    this.move(this.motionX, this.motionY, this.motionZ);
                    this.updateMovement();
                }
                return this.followTarget != null ? this.followTarget : this.followBlock != null ? this.followBlock : this.target;
            }

            if (this.followTarget != null && !this.followTarget.closed && this.followTarget.isAlive() && this.followTarget.canBeFollowed()) {
                double x = this.followTarget.x - this.x;
                double y = this.followTarget.y - this.y;
                double z = this.followTarget.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                if (this.riding != null || diff == 0 || this.stayTime > 0 || this.distance(this.followTarget) <= (this.getWidth() / 2 + 0.3)) {
                    this.motionX = 0;
                    this.motionZ = 0;
                    if (this.riding == null) {
                        this.motionY = this.getVerticalSpeed(0.01, y);
                    }
                } else {
                    this.motionX = this.getSpeed() * 0.15 * (x / diff);
                    this.motionY = this.getVerticalSpeed(0.27, (y / diff));
                    this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                }
                if (this.noRotateTicks <= 0 && (this.stayTime <= 0 || Utils.rand()) && diff != 0) {
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(x / diff, z / diff)));
                }

                if (this.stayTime <= 0 && this.motionY == 0 && (Math.abs(motionX) > 0 || Math.abs(motionZ) > 0) &&
                        (Block.solid[this.level.getBlockIdAt(chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ())])) {
                    this.motionY = 0.05;
                }
                this.move(this.motionX, this.motionY, this.motionZ);
                this.updateMovement();
                return this.followTarget;
            }

            if (this.followBlock != null) {
                double x = this.followBlock.x - this.x;
                double y = this.followBlock.y - this.y;
                double z = this.followBlock.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                if (this.riding != null || diff == 0 || this.stayTime > 0 || this.distance(this.followBlock) <= (this.getWidth() / 2 + 0.3)) {
                    this.motionX = 0;
                    this.motionZ = 0;
                    if (this.riding != null) {
                        this.motionY = this.getVerticalSpeed(0.01, y);
                    }
                } else {
                    this.motionX = this.getSpeed() * 0.15 * (x / diff);
                    this.motionY = this.getVerticalSpeed(0.27, (y / diff));
                    this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                }
                if (this.noRotateTicks <= 0 && (this.stayTime <= 0 || Utils.rand()) && diff != 0)
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(x / diff, z / diff)));

                if (this.stayTime <= 0 && this.motionY == 0 && (Math.abs(motionX) > 0 || Math.abs(motionZ) > 0) &&
                        (Block.solid[this.level.getBlockIdAt(chunk, this.getFloorX(), this.getFloorY() - 1, this.getFloorZ())])) {
                    this.motionY = 0.05;
                }
                this.move(this.motionX, this.motionY, this.motionZ);
                this.updateMovement();
                return this.followBlock;
            }

            Vector3 before = this.target;
            if (this.isLookupForTarget()) {
                this.checkTarget();
            }
            if (this.target instanceof EntityCreature || !this.isLookupForTarget() || before != this.target) {
                double x = this.target.x - this.x;
                double y = this.target.y - this.y;
                double z = this.target.z - this.z;

                double diff = Math.abs(x) + Math.abs(z);
                if (this.riding != null || diff == 0 || this.stayTime > 0 || this.distance(this.target) <= (this.getWidth() / 2 + 0.3) * nearbyDistanceMultiplier()) {
                    this.motionX = 0;
                    this.motionZ = 0;
                    if (this.riding == null) {
                        this.motionY = this.getVerticalSpeed(0.01, y);
                    }
                } else {
                    this.motionX = this.getSpeed() * 0.15 * (x / diff);
                    this.motionY = this.getVerticalSpeed(0.27, (y / diff));
                    this.motionZ = this.getSpeed() * 0.15 * (z / diff);
                }
                if (this.noRotateTicks <= 0 && (this.stayTime <= 0 || Utils.rand()) && diff != 0) {
                    this.setBothYaw(FastMathLite.toDegrees(-FastMathLite.atan2(x / diff, z / diff)));
                }
            }

            if (this.target != null && this.stayTime <= 0 && this.motionY == 0 && (Math.abs(motionX) > 0 || Math.abs(motionZ) > 0) && distanceSquared(target) > 1) {
                this.motionY = 0.05;
            }

            double dx = this.motionX;
            double dy = this.motionY;
            double dz = this.motionZ;
            if (this.stayTime > 0) {
                this.stayTime -= tickDiff;
                this.move(0, dy, 0);
            } else {
                Vector2 be = new Vector2(this.x + dx, this.z + dz);
                this.move(dx, dy, dz);
                Vector2 af = new Vector2(this.x, this.z);

                if (be.x != af.x || be.y != af.y) {
                    this.moveTime -= 90;
                }
            }

            this.updateMovement();
            return this.target;
        }
        return null;
    }
}
