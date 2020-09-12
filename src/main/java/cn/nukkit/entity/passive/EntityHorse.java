package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EntityHorse extends EntityHorseBase {

    public static final int NETWORK_ID = 23;

    public int variant = getRandomVariant();

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

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(Utils.rand(15, 30));

        if (this.namedTag.contains("Variant")) {
            this.variant = this.namedTag.getInt("Variant");
        }

        this.setDataProperty(new IntEntityData(DATA_VARIANT, this.variant));
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt("Variant", this.variant);
    }

    @Override
    public boolean targetOption(EntityCreature creature, double distance) {
        boolean canTarget = super.targetOption(creature, distance);

        if (canTarget && (creature instanceof Player)) {
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

        if (!this.isBaby()) {
            for (int i = 0; i < Utils.rand(0, 2); i++) {
                drops.add(Item.get(Item.LEATHER, 0, 1));
            }
        }

        return drops.toArray(new Item[0]);
    }

    private static int getRandomVariant() {
        int[] variantList = {0, 1, 2, 3, 4, 5, 6, 256, 257, 258, 259, 260, 261, 262, 512, 513, 514, 515, 516, 517, 518,
                768, 769, 770, 771, 772, 773, 774, 1024, 1025, 1026, 1027, 1028, 1029, 1030};
        return variantList[Utils.rand(0, variantList.length - 1)];
    }
}
