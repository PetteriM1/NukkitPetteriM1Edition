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

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.2f;
        }
        return 0.4f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.35f;
        }
        return 0.7f;
    }

    @Override
    public float getDrag() {
        return 0.2f;
    }

    @Override
    public float getGravity() {
        return 0.08f; //Should be lower but that breaks jumping
    }

    @Override
    public void initEntity() {
        super.initEntity();

        if (this.namedTag.contains("EggLayTime")) {
            this.eggLayTime = this.namedTag.getInt("EggLayTime");
        } else {
            this.eggLayTime = getRandomEggLayTime();
        }
        if (this.namedTag.contains("IsChickenJockey")) {
            this.isChickenJockey = this.namedTag.getBoolean("IsChickenJockey");
        } else {
            this.isChickenJockey = false;
        }

        this.setMaxHealth(4);
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = super.entityBaseTick(tickDiff);

        if (this.getServer().mobsFromBlocks && !this.isBaby()) {
            if (this.eggLayTime > 0) {
                eggLayTime -= tickDiff;
            } else {
                this.level.dropItem(this, Item.get(Item.EGG, 0, 1));
                this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_PLOP);
                this.eggLayTime = getRandomEggLayTime();
            }
        }

        return hasUpdate;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            int id = player.getInventory().getItemInHandFast().getId();
            return player.isAlive() && !player.closed
                    && (id == Item.SEEDS
                            || id == Item.BEETROOT_SEEDS
                            || id == Item.MELON_SEEDS
                            || id == Item.PUMPKIN_SEEDS)
                    && distance <= 40;
        }
        return false;
    }

    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        if (item.getId() == Item.SEEDS && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(
                    this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)),
                    Item.get(Item.SEEDS)));
            this.setInLove();
        } else if (item.getId() == Item.BEETROOT_SEEDS && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(
                    this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)),
                    Item.get(Item.BEETROOT_SEEDS)));
            this.setInLove();
        } else if (item.getId() == Item.MELON_SEEDS && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(
                    this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)),
                    Item.get(Item.MELON_SEEDS)));
            this.setInLove();
        } else if (item.getId() == Item.PUMPKIN_SEEDS && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(
                    this.add(Utils.rand(-0.5, 0.5), this.getMountedYOffset(), Utils.rand(-0.5, 0.5)),
                    Item.get(Item.PUMPKIN_SEEDS)));
            this.setInLove();
        }
        return super.onInteract(player, item, clickedPos);
    }

    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt("EggLayTime", this.eggLayTime);
        this.namedTag.putBoolean("IsChickenJockey", this.isChickenJockey);
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

        return drops.toArray(Item.EMPTY_ARRAY);
    }

    @Override
    public boolean attack(EntityDamageEvent ev) {
        if (ev.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return super.attack(ev);
        }

        return false;
    }

    private static  int getRandomEggLayTime() {
        return Utils.rand(6000, 12000);
    }

    public boolean isChickenJockey() {
        return isChickenJockey;
    }

    public void setChickenJockey(boolean chickenJockey) {
        isChickenJockey = chickenJockey;
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : Utils.rand(1, 3);
    }
}
