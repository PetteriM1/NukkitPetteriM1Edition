/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.passive.EntityHorseBase;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;

public class EntityHorse
extends EntityHorseBase {
    public static final int NETWORK_ID = 23;
    public int variant;
    private static final int[] w = new int[]{0, 1, 2, 3, 4, 5, 6, 256, 257, 258, 259, 260, 261, 262, 512, 513, 514, 515, 516, 517, 518, 768, 769, 770, 771, 772, 773, 774, 1024, 1025, 1026, 1027, 1028, 1029, 1030};

    public EntityHorse(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 23;
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
        this.setMaxHealth(Utils.rand(15, 30));
        super.initEntity();
        this.variant = this.namedTag.contains("Variant") ? this.namedTag.getInt("Variant") : EntityHorse.b();
        this.setDataProperty(new IntEntityData(2, this.variant));
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Variant", this.variant);
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        boolean bl = super.targetOption(entityCreature, d2);
        if (bl && entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            return player.spawned && player.isAlive() && !player.closed && this.isFeedItem(player.getInventory().getItemInHandFast()) && d2 <= 40.0;
        }
        return false;
    }

    @Override
    public Item[] getDrops() {
        ArrayList<Item> arrayList = new ArrayList<Item>();
        if (!this.isBaby()) {
            for (int k = 0; k < Utils.rand(0, 2); ++k) {
                arrayList.add(Item.get(334, 0, 1));
            }
        }
        if (this.isSaddled()) {
            arrayList.add(Item.get(329, 0, 1));
        }
        return arrayList.toArray(new Item[0]);
    }

    private static int b() {
        return w[Utils.rand(0, w.length - 1)];
    }

    @Override
    public double getHorseJumpSpeed() {
        return 0.07;
    }
}

