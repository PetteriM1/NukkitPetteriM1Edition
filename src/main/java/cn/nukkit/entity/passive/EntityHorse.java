package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.utils.EntityUtils;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class EntityHorse extends EntityWalkingAnimal {

    public static final int NETWORK_ID = 23;

    private int Type = 0;

    private int Variant = this.getRandomVariant();

    public EntityHorse(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.6982f;
        }
        return 1.3965f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.8f;
        }
        return 1.6f;
    }

    public float getMaxJumpHeight() {
        return 2;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(15);
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putByte("Type", this.Type);
        this.namedTag.putInt("Variant", this.Variant);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed
                    && (player.getInventory().getItemInHand().getId() == Item.WHEAT
                            || player.getInventory().getItemInHand().getId() == Item.APPLE
                            || player.getInventory().getItemInHand().getId() == Item.HAY_BALE
                            || player.getInventory().getItemInHand().getId() == Item.GOLDEN_APPLE
                            || player.getInventory().getItemInHand().getId() == Item.SUGAR
                            || player.getInventory().getItemInHand().getId() == Item.BREAD
                            || player.getInventory().getItemInHand().getId() == Item.GOLDEN_CARROT)
                    && distance <= 40;
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
        }

        return drops.toArray(new Item[0]);
    }

    @Override
    public int getKillExperience() {
        return this.isBaby() ? 0 : EntityUtils.rand(1, 4);
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getVariant() {
        return Variant;
    }

    public void setVariant(int variant) {
        Variant = variant;
    }

    private int getRandomVariant() {
        int VariantList[] = { 0, 1, 2, 3, 4, 5, 6, 256, 257, 258, 259, 260, 261, 262, 512, 513, 514, 515, 516, 517, 518,
                768, 769, 770, 771, 772, 773, 774, 1024, 1025, 1026, 1027, 1028, 1029, 1030 };
        return VariantList[EntityUtils.rand(0, VariantList.length)];
    }
}
