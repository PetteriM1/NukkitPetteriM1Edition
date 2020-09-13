package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityParrot extends EntityFlyingAnimal {

    public static final int NETWORK_ID = 30;

    public int variant;

    private static final int[] VARIANTS = {0, 1, 2, 3, 4};

    public EntityParrot(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.9f;
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(6);

        if (this.namedTag.contains("Variant")) {
            this.variant = this.namedTag.getInt("Variant");
        } else {
            this.variant = getRandomVariant();
        }

        this.setDataProperty(new IntEntityData(DATA_VARIANT, this.variant));
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Variant", this.variant);
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.FEATHER, 0, Utils.rand(1, 2))};
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 3);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            return player.spawned && player.isAlive() && !player.closed
                    && (player.getInventory().getItemInHand().getId() == Item.SEEDS
                    || player.getInventory().getItemInHand().getId() == Item.BEETROOT_SEEDS
                    || player.getInventory().getItemInHand().getId() == Item.PUMPKIN_SEEDS
                    || player.getInventory().getItemInHand().getId() == Item.MELON_SEEDS)
                    && distance <= 40;
        }
        return false;
    }

    private static int getRandomVariant() {
        return VARIANTS[Utils.rand(0, VARIANTS.length - 1)];
    }
}
