/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.mob;

import cn.nukkit.entity.Entity;

public interface EntityMob {
    public void attackEntity(Entity var1);

    public int getDamage();

    public int getDamage(Integer var1);

    public int getMinDamage();

    public int getMinDamage(Integer var1);

    public int getMaxDamage();

    public int getMaxDamage(Integer var1);

    public void setDamage(int var1);

    public void setDamage(int[] var1);

    public void setDamage(int var1, int var2);

    public void setMinDamage(int var1);

    public void setMinDamage(int[] var1);

    public void setMinDamage(int var1, int var2);

    public void setMaxDamage(int var1);

    public void setMaxDamage(int[] var1);

    public void setMaxDamage(int var1, int var2);
}

