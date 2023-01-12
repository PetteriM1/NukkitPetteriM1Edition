/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;
import cn.nukkit.item.food.Food;
import cn.nukkit.potion.Effect;

public class PlayerFood {
    private int e;
    private float b;
    private short c = 0;
    private double a = 0.0;
    private final Player d;

    public PlayerFood(Player player, int n, float f2) {
        this.d = player;
        this.e = n;
        this.b = f2;
    }

    public Player getPlayer() {
        return this.d;
    }

    public int getLevel() {
        return this.e;
    }

    public int getMaxLevel() {
        return 20;
    }

    public void setLevel(int n) {
        this.setLevel(n, -1.0f);
    }

    public void setLevel(int n, float f2) {
        if (n > 20) {
            n = 20;
        }
        if (n < 0) {
            n = 0;
        }
        if (n <= 6 && this.e > 6 && this.d.isSprinting()) {
            this.d.setSprinting(false);
        }
        PlayerFoodLevelChangeEvent playerFoodLevelChangeEvent = new PlayerFoodLevelChangeEvent(this.d, n, f2);
        this.d.getServer().getPluginManager().callEvent(playerFoodLevelChangeEvent);
        if (playerFoodLevelChangeEvent.isCancelled()) {
            this.sendFoodLevel(this.e);
            return;
        }
        int n2 = playerFoodLevelChangeEvent.getFoodLevel();
        float f3 = playerFoodLevelChangeEvent.getFoodSaturationLevel();
        this.e = n;
        if (f3 != -1.0f) {
            if (f3 > (float)n) {
                f3 = n;
            }
            this.b = f3;
        }
        this.e = n2;
        this.sendFoodLevel();
    }

    public float getFoodSaturationLevel() {
        return this.b;
    }

    public void setFoodSaturationLevel(float f2) {
        if (f2 > (float)this.e) {
            f2 = this.e;
        }
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        PlayerFoodLevelChangeEvent playerFoodLevelChangeEvent = new PlayerFoodLevelChangeEvent(this.d, this.e, f2);
        this.d.getServer().getPluginManager().callEvent(playerFoodLevelChangeEvent);
        if (playerFoodLevelChangeEvent.isCancelled()) {
            return;
        }
        this.b = f2 = playerFoodLevelChangeEvent.getFoodSaturationLevel();
    }

    public void useHunger() {
        this.useHunger(1);
    }

    public void useHunger(int n) {
        float f2 = this.b;
        int n2 = this.e;
        if (f2 > 0.0f) {
            float f3 = f2 - (float)n;
            if (f3 < 0.0f) {
                f3 = 0.0f;
            }
            this.setFoodSaturationLevel(f3);
        } else {
            this.setLevel(n2 - n);
        }
    }

    public void addFoodLevel(Food food) {
        this.addFoodLevel(food.getRestoreFood(), food.getRestoreSaturation());
    }

    public void addFoodLevel(int n, float f2) {
        this.setLevel(this.e + n, this.b + f2);
    }

    public void sendFoodLevel() {
        this.sendFoodLevel(this.e);
    }

    public void reset() {
        this.e = 20;
        this.b = 20.0f;
        this.a = 0.0;
        this.c = 0;
        this.sendFoodLevel();
    }

    public void sendFoodLevel(int n) {
        if (this.d.spawned) {
            this.d.setAttribute(Attribute.getAttribute(7).setValue(n).setDefaultValue(this.getMaxLevel()));
        }
    }

    public void update(int n) {
        if (!this.d.isFoodEnabled()) {
            return;
        }
        if (this.d.isAlive()) {
            Object object;
            int n2 = Server.getInstance().getDifficulty();
            if (this.e > 17 || n2 == 0) {
                this.c = (short)(this.c + n);
                if (this.c >= 80) {
                    if (this.d.getHealth() < (float)this.d.getRealMaxHealth()) {
                        object = new EntityRegainHealthEvent(this.d, 1.0f, 1);
                        this.d.heal((EntityRegainHealthEvent)object);
                        this.updateFoodExpLevel(6.0);
                    }
                    this.c = 0;
                }
            } else if (this.e == 0) {
                this.c = (short)(this.c + n);
                if (this.c >= 80) {
                    object = new EntityDamageEvent((Entity)this.d, EntityDamageEvent.DamageCause.HUNGER, 1.0f);
                    float f2 = this.d.getHealth();
                    if (n2 == 1) {
                        if (f2 > 10.0f) {
                            this.d.attack((EntityDamageEvent)object);
                        }
                    } else if (n2 == 2) {
                        if (f2 > 1.0f) {
                            this.d.attack((EntityDamageEvent)object);
                        }
                    } else {
                        this.d.attack((EntityDamageEvent)object);
                    }
                    this.c = 0;
                }
            }
            if ((object = this.d.getEffect(17)) != null) {
                this.updateFoodExpLevel(0.1 * (double)(((Effect)object).getAmplifier() + 1));
            }
        }
    }

    public void updateFoodExpLevel(double d2) {
        if (!this.d.isFoodEnabled()) {
            return;
        }
        if (Server.getInstance().getDifficulty() == 0) {
            return;
        }
        if (this.d.hasEffect(23)) {
            return;
        }
        this.a += d2;
        if (this.a > 4.0) {
            this.useHunger(1);
            this.a = 0.0;
        }
    }

    public void setFoodLevel(int n) {
        this.setLevel(n);
    }

    public void setFoodLevel(int n, float f2) {
        this.setLevel(n, f2);
    }

    public String toString() {
        return "PlayerFood(player= " + this.d + ", foodLevel=" + this.e + ", foodSaturationLevel=" + this.b + ", foodTickTimer=" + this.c + ", foodExpLevel=" + this.a + ")";
    }
}

