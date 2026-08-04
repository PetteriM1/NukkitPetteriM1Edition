package cn.nukkit.item;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * Created on 2015/12/27 by xtypr.
 * Package cn.nukkit.item in project Nukkit .
 */
public class ItemPotionSplash extends ProjectileItem {

    public ItemPotionSplash(Integer meta) {
        this(meta, 1);
    }

    public ItemPotionSplash(Integer meta, int count) {
        super(SPLASH_POTION, meta, count, "Splash Potion");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public String getProjectileEntityType() {
        return "ThrownPotion";
    }

    @Override
    public float getThrowForce() {
        return 0.50f;
    }

    @Override
    protected void correctNBT(CompoundTag nbt) {
        nbt.putInt("PotionId", this.meta);
    }

    @Override
    public boolean isSupportedOn(int protocol) {
        int damage = this.getDamage();
        if (damage <= 42) {
            return true;
        }
        if (damage == 43) {
            return protocol >= ProtocolInfo.v1_16_0;
        }
        return protocol >= ProtocolInfo.v1_21_0;
    }
}
