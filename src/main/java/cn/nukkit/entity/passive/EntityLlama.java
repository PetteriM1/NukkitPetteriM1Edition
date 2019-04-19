package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityLlamaSpit;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EntityLlama extends EntityWalkingAnimal {

    public static final int NETWORK_ID = 29;

    protected float lastSplit = 0;

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
                if (delay.get()) return true;
                delay.set(true);
                server.getScheduler().scheduleDelayedTask(() -> delay.compareAndSet(true, false), 40);

                this.getServer().getScheduler().scheduleDelayedTask(null, () -> {
                    if (this.isAlive()) {
                        if (this.distance(damager) < 10) {
                            this.moveTime = 0;
                            this.stayTime = 100;

                            double f = 2;
                            double yaw = this.yaw;
                            double pitch = this.pitch;
                            Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                                    this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
                            Entity k = EntityUtils.create("LlamaSplit", pos, this);
                            if (!(k instanceof EntityLlamaSpit)) return;
                            
                            EntityLlamaSpit split = (EntityLlamaSpit) k;
                            split.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                                    Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

                            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(split);
                            this.server.getPluginManager().callEvent(launch);
                            if (launch.isCancelled()) {
                                split.kill();
                            } else {
                                split.spawnToAll();
                                this.level.addSound(this, Sound.MOB_LLAMA_SPIT);
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
        List<Item> drops = new ArrayList<>();

        if (this.hasCustomName()) {
            drops.add(Item.get(Item.NAME_TAG, 0, 1));
        }

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby()) {
            for (int i = 0; i < EntityUtils.rand(0, 2); i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : EntityUtils.rand(1, 3);
    }
}
