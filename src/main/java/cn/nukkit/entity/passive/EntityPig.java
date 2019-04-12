package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityPig extends EntityWalkingAnimal {

    public static final int NETWORK_ID = 12;

    public EntityPig(FullChunk chunk, CompoundTag nbt) {
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
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(10);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed
                    && (player.getInventory().getItemInHand().getId() == Item.CARROT
                    || player.getInventory().getItemInHand().getId() == Item.POTATO
                    || player.getInventory().getItemInHand().getId() == Item.CARROT_ON_A_STICK
                    || player.getInventory().getItemInHand().getId() == Item.BEETROOT)
                    && distance <= 40;
        }
        return false;
    }

    @Override
    public boolean onInteract(Player player, Item item) {
        super.onInteract(player, item);
        if (item.equals(Item.get(Item.CARROT,0)) && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(0,this.getMountedYOffset(),0),Item.get(Item.CARROT)));
            this.setInLove();
            return true;
        } else if (item.equals(Item.get(Item.POTATO,0)) && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(0,this.getMountedYOffset(),0),Item.get(Item.POTATO)));
            this.setInLove();
            return true;
        } else if (item.equals(Item.get(Item.BEETROOT,0)) && !this.isBaby()) {
            player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            this.level.addParticle(new ItemBreakParticle(this.add(0,this.getMountedYOffset(),0),Item.get(Item.BEETROOT)));
            this.setInLove();
            return true;
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
            for (int i = 0; i < EntityUtils.rand(1, 4); i++) {
                drops.add(Item.get(this.isOnFire() ? Item.COOKED_PORKCHOP : Item.RAW_PORKCHOP, 0, 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    public int getKillExperience() {
        return this.isBaby() ? 0 : EntityUtils.rand(1, 4);
    }
}
