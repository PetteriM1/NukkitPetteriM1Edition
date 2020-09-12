package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.projectile.EntityLlamaSpit;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;
import org.apache.commons.math3.util.FastMath;

import java.util.concurrent.atomic.AtomicBoolean;

public class EntityLlama extends EntityHorseBase {

    public static final int NETWORK_ID = 29;

    private AtomicBoolean delay = new AtomicBoolean();

    public EntityLlama(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.935f;
        }
        return 1.87f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(15);
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (ev instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) ev).getDamager();
            if (damager instanceof Player) {
                if (this.delay.get()) return true;
                this.delay.set(true);
                this.server.getScheduler().scheduleDelayedTask(() -> this.delay.compareAndSet(true, false), 40);

                this.getServer().getScheduler().scheduleDelayedTask(null, () -> {
                    if (this.isAlive()) {
                        if (this.distanceSquared(damager) < 100) {
                            this.moveTime = 0;
                            this.stayTime = 100;

                            double f = 2;
                            double yaw = this.yaw;
                            double pitch = this.pitch;
                            Location pos = new Location(this.x - Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                                    this.z + Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
                            Entity k = Entity.createEntity("LlamaSpit", pos, this);
                            if (!(k instanceof EntityLlamaSpit)) return;
                            
                            EntityLlamaSpit spit = (EntityLlamaSpit) k;
                            spit.setMotion(new Vector3(-Math.sin(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f, -Math.sin(FastMath.toRadians(pitch)) * f * f,
                                    Math.cos(FastMath.toRadians(yaw)) * Math.cos(FastMath.toRadians(pitch)) * f * f));

                            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(spit);
                            this.server.getPluginManager().callEvent(launch);
                            if (launch.isCancelled()) {
                                spit.close();
                            } else {
                                spit.spawnToAll();
                                this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_SHOOT, -1, "minecraft:llama", false, false);
                            }
                        }
                    }
                }, 30);
            }
        }

        return true;
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.LEATHER, 0, Utils.rand(0, 2))};
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        boolean canTarget = super.targetOption(creature, distance);

        if (canTarget && (creature instanceof Player)) {
            Player player = (Player) creature;
            return player.isAlive() && !player.closed && player.getInventory().getItemInHand().getId() == Item.WHEAT && distance <= 40;
        }

        return false;
    }
}
