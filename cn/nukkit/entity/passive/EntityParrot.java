/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.passive.EntityFlyingAnimal;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Utils;

public class EntityParrot
extends EntityFlyingAnimal {
    public static final int NETWORK_ID = 30;
    private int t;
    private static final int[] u = new int[]{0, 1, 2, 3, 4};

    public EntityParrot(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    public int getNetworkId() {
        return 30;
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
        this.setMaxHealth(6);
        super.initEntity();
        this.t = this.namedTag.contains("Variant") ? this.namedTag.getInt("Variant") : EntityParrot.b();
        this.setDataProperty(new IntEntityData(2, this.t));
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putInt("Variant", this.t);
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(288, 0, Utils.rand(1, 2))};
    }

    @Override
    public int getKillExperience() {
        return Utils.rand(1, 3);
    }

    @Override
    public boolean targetOption(EntityCreature entityCreature, double d2) {
        if (entityCreature instanceof Player) {
            Player player = (Player)entityCreature;
            int n = player.getInventory().getItemInHandFast().getId();
            return player.spawned && player.isAlive() && !player.closed && (n == 295 || n == 458 || n == 361 || n == 362) && d2 <= 40.0;
        }
        return super.targetOption(entityCreature, d2);
    }

    private static int b() {
        return u[Utils.rand(0, u.length - 1)];
    }
}

