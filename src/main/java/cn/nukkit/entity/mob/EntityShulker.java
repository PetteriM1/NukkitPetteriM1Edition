package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityShulkerBullet;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.sound.EndermanTeleportSound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityShulker extends EntityWalkingMob {

    public static final int NETWORK_ID = 54;

    public EntityShulker(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1f;
    }

    @Override
    public float getHeight() {
        return 1f;
    }
    
    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.fireProof = true;
        this.setMaxHealth(15);
        this.setDamage(new int[] { 0, 0, 0, 0 });
    }

    @Override
    public void attackEntity(Entity player) {
    if (this.attackDelay > 23 && EntityUtils.rand(1, 32) < 4 && this.distanceSquared(player) <= 55) {
            this.attackDelay = 0;

            double f = 0.5;
        double yaw = this.yaw + EntityUtils.rand(-150.0, 150.0) / 10;
        double pitch = this.pitch + EntityUtils.rand(-75.0, 75.0) / 10;
            Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getHeight() - 0.18,
                    this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
            Entity k = EntityUtils.create("ShulkerBullet", pos, this);
            if (!(k instanceof EntityShulkerBullet)) {
                return;
            }

            EntityShulkerBullet bullet = (EntityShulkerBullet) k;
            bullet.setMotion(new Vector3(-Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f, -Math.sin(Math.toRadians(pitch)) * f * f,
                    Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * f * f));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(bullet);
            this.server.getPluginManager().callEvent(launch);

            if (launch.isCancelled()) {
                bullet.kill();
            } else {
                bullet.spawnToAll();
                this.level.addSound(this, Sound.MOB_SHULKER_SHOOT);
            }
        }
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        super.attack(ev);

        if (!ev.isCancelled()) {
            if (EntityUtils.rand(1, 10) == 1) {
                this.level.addSound(new EndermanTeleportSound(this));
                this.move(EntityUtils.rand(-10, 10), 0, EntityUtils.rand(-10, 10));
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

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby() && EntityUtils.rand(1, 2) == 1) {
            drops.add(Item.get(Item.SHULKER_SHELL, 0, 1));
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : 5;
    }

    @Override
    public void knockBack(Entity attacker, double damage, double x, double z, double base) {
    }
}
