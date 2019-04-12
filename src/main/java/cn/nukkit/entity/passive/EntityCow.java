package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityCow extends EntityWalkingAnimal {

    public static final int NETWORK_ID = 11;

    public EntityCow(FullChunk chunk, CompoundTag nbt) {
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
            return 0.7f;
        }
        return 1.4f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(10);
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        super.onInteract(player, item);

        if (item.equals(Item.get(Item.BUCKET, 0), true) && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            player.getInventory().addItem(Item.get(Item.BUCKET, 1, 1));
            this.level.addLevelSoundEvent(this, LevelSoundEventPacket.SOUND_MILK);
        } else if (item.equals(Item.get(Item.WHEAT, 0)) && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(0, this.getMountedYOffset(), 0), Item.get(Item.WHEAT)));
            this.setInLove();
        }

        return true;
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;

            return player.isAlive() && !player.closed && player.getInventory().getItemInHand().getId() == Item.WHEAT && distance <= 40;
        }

        return false;
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();

        if (this.hasCustomName()) {
            drops.add(Item.get(Item.NAME_TAG, 0, 1));
        }

        if (this.lastDamageCause instanceof EntityDamageByEntityEvent && !this.isBaby()) {
            for (int i = 0; i < EntityUtils.rand(0, 3); i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }

            for (int i = 0; i < EntityUtils.rand(1, 4); i++) {
                drops.add(Item.get(this.isOnFire() ? Item.STEAK : Item.RAW_BEEF, 0, 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : EntityUtils.rand(1, 4);
    }
}
