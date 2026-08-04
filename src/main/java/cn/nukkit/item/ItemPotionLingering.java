package cn.nukkit.item;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ProtocolInfo;

public class ItemPotionLingering extends ProjectileItem {

    public ItemPotionLingering() {
        this(0, 1);
    }

    public ItemPotionLingering(Integer meta) {
        this(meta, 1);
    }

    public ItemPotionLingering(Integer meta, int count) {
        super(LINGERING_POTION, meta, count, "Lingering Potion");
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
        return "ThrownLingeringPotion";
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
        if (damage < 42) {
            return true;
        }
        if (damage == 42) {
            return protocol >= ProtocolInfo.v1_16_0;
        }
        return protocol >= ProtocolInfo.v1_21_0; // lingering potion has different version changelog than others?
    }
}
