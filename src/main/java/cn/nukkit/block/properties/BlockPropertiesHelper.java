package cn.nukkit.block.properties;

import cn.nukkit.block.custom.container.BlockStorageContainer;

public interface BlockPropertiesHelper extends BlockStorageContainer {

    int getDamage();

    int getId();

    @Override
    default int getNukkitId() {
        return this.getId();
    }

    @Override
    default int getStorage() {
        return this.getDamage();
    }

    void setDamage(int meta);

    @Override
    default void setStorage(int damage) {
        this.setDamage(damage);
    }
}
