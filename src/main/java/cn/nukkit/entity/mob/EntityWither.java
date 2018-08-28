package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.mob.EntityFlyingMob;
import cn.nukkit.entity.EntityUtils;
import cn.nukkit.entity.projectile.EntityBlueWitherSkull;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class EntityWither extends EntityFlyingMob {

    public static final int NETWORK_ID = 52;

    public EntityWither(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public String getName() {
        return "Wither";
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
        return 1.2;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.fireProof = true;
        this.setMaxHealth(300);
        this.setDamage(new int[]{0, 2, 4, 6});
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed && player.isSurvival() && distance <= 100;
        }
        return creature.isAlive() && !creature.closed && distance <= 100;
    }

    @Override
    public int getKillExperience() {
        return 50;
    }

    @Override
    public void attackEntity(Entity player) {
    if (this.attackDelay > 5 && EntityUtils.rand(1, 5) < 6 && this.distance(player) <= 100) {
            this.attackDelay = 0;

            double f = 2;
            double yaw = this.yaw + EntityUtils.rand(-220, 220) / 10;
            double pitch = this.pitch + EntityUtils.rand(-120, 120) / 10;
            Location pos = new Location(this.x - Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, this.y + this.getEyeHeight(),
                    this.z + Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * 0.5, yaw, pitch, this.level);
            Entity k = EntityUtils.create("BlueWitherSkull", pos, this);
            if (!(k instanceof EntityBlueWitherSkull)) {
                return;
            }

            EntityBlueWitherSkull blueskull = (EntityBlueWitherSkull) k;
            blueskull.setExplode(true);
            blueskull.setMotion(new Vector3(-Math.sin(Math.toDegrees(yaw)) * Math.cos(Math.toDegrees(pitch)) * f * f, -Math.sin(Math.toDegrees(pitch)) * f * f,
                    Math.cos(Math.toDegrees(yaw)) * Math.cos(Math.toDegrees(pitch)) * f * f));

            ProjectileLaunchEvent launch = new ProjectileLaunchEvent(blueskull);
            this.server.getPluginManager().callEvent(launch);
            if (launch.isCancelled()) {
                blueskull.kill();
            } else {
                blueskull.spawnToAll();
                this.level.addSound(this, "mob.wither.shoot");
            }
        }
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int netherstar = EntityUtils.rand(0, 101) <= 3 ? 1 : 0;
            for (int i = 0; i < netherstar; i++) {
                drops.add(Item.get(Item.NETHER_STAR, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }
}
