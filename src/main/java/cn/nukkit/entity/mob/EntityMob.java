package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;

public interface EntityMob {

    int getDamage();

    int getMaxDamage();

    int getMinDamage();

    void setDamage(int damage);

    void setDamage(int[] damage);

    void setMaxDamage(int damage);

    void setMaxDamage(int[] damage);

    void setMinDamage(int damage);

    void setMinDamage(int[] damage);

    void attackEntity(Entity player);

    int getDamage(Integer difficulty);

    int getMaxDamage(Integer difficulty);

    int getMinDamage(Integer difficulty);

    void setDamage(int damage, int difficulty);

    void setMaxDamage(int damage, int difficulty);

    void setMinDamage(int damage, int difficulty);
}
