package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityChicken extends EntityWalkingAnimal {

    public static final int NETWORK_ID = 10;

    private int eggLayTime = getRandomEggLayTime();
    private boolean isChickenJockey = false;

    public EntityChicken(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    private boolean lastMoveOnGround;

    public void setChickenJockey(boolean chickenJockey) {
        isChickenJockey = chickenJockey;
    }

    @Override
    public float getDrag() {
        return 0.2f;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (!this.isBaby()) {
            for (int i = 0; i < Utils.rand(0, 2); i++) {
                drops.add(Item.get(Item.FEATHER, 0, 1));
            }

            drops.add(Item.get(this.isOnFire() ? Item.COOKED_CHICKEN : Item.RAW_CHICKEN, 0, 1));
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public float getGravity() {
        return 0.08f; //Should be lower but that breaks jumping
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.35f;
        }
        return 0.7f;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    private static int getRandomEggLayTime() {
        return Utils.rand(6000, 12000);
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.2f;
        }
        return 0.4f;
    }

    public boolean isChickenJockey() {
        return isChickenJockey;
    }

    @Override
    public void addMovement(double x, double y, double z, double yaw, double pitch, double headYaw) {
        MoveEntityAbsolutePacket pk = new MoveEntityAbsolutePacket();
        pk.eid = this.getId();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.yaw = yaw;
        pk.headYaw = headYaw;
        pk.pitch = pitch;

        // Hack: Fix chicken always flapping wings when moving
        pk.onGround = this.onGround || this.lastMoveOnGround;
        this.lastMoveOnGround = this.onGround;

        for (Player p : this.getViewers().values()) {
            p.dataPacket(pk);
        }
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        if (ev.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return super.attack(ev);
        }

        return false;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (this.getServer().mobsFromBlocks && !this.isBaby() && !this.isChickenJockey()) {
            if (this.eggLayTime > 0) {
                this.eggLayTime -= tickDiff;
            } else {
                this.eggLayTime = getRandomEggLayTime();
                this.level.dropItem(this, Item.get(Item.EGG, 0, 1));
                this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_PLOP);
            }
        }

        return hasUpdate;
    }

    public void initEntity() {
        this.setMaxHealth(4);
        super.initEntity();
        this.noFallDamage = true;

        if (!this.server.suomiCraftPEMode() && this.namedTag.contains("EggLayTime")) {
            this.eggLayTime = this.namedTag.getInt("EggLayTime");
        } else {
            this.eggLayTime = getRandomEggLayTime();
        }
        if (this.namedTag.contains("IsChickenJockey")) {
            this.isChickenJockey = this.namedTag.getBoolean("IsChickenJockey");
        } else {
            this.isChickenJockey = false;
        }
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if ((item.getId() == Item.SEEDS || item.getId() == Item.BEETROOT_SEEDS || item.getId() == Item.MELON_SEEDS || item.getId() == Item.PUMPKIN_SEEDS) && !this.isBaby() && !this.isInLoveCooldown()) {
            this.level.addParticle(new ItemBreakParticle(this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)), item));
            this.setInLove();
            return true;
        }
        return super.onInteract(player, item, clickedPos);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt("EggLayTime", this.eggLayTime);
        this.namedTag.putBoolean("IsChickenJockey", this.isChickenJockey);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            if (player.closed) {
                return false;
            }
            int id = player.getInventory().getItemInHandFast().getId();
            return player.isAlive()
                    && (id == Item.SEEDS
                    || id == Item.BEETROOT_SEEDS
                    || id == Item.MELON_SEEDS
                    || id == Item.PUMPKIN_SEEDS)
                    && distance <= 49;
        }
        return super.targetOption(creature, distance);
    }

    @Override
    public Vector3 updateMove(int tickDiff) {
        if (!this.onGround && this.motionY < -this.getGravity() && !this.isKnockback() && !this.isInsideOfWater()) {
            this.stayTime = tickDiff; // forces super to call move(0, motionY, 0) with no horizontal
        }
        Vector3 result = super.updateMove(tickDiff);
        if (!this.onGround && this.motionY < 0 && !this.isInsideOfWater()) {
            this.motionY *= 0.6;
        }
        return result;
    }
}
