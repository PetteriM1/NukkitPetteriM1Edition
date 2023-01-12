/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Utils;

public class ItemBow
extends ItemTool {
    public ItemBow() {
        this((Integer)0, 1);
    }

    public ItemBow(Integer n) {
        this(n, 1);
    }

    public ItemBow(Integer n, int n2) {
        this(261, n, n2, "Bow");
    }

    public ItemBow(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }

    @Override
    public int getMaxDurability() {
        return 385;
    }

    @Override
    public int getEnchantAbility() {
        return 1;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return this.a(player) || player.isCreative();
    }

    private boolean a(Player player) {
        if (player.getOffhandInventory().getItemFast(0).getId() == 262) {
            return true;
        }
        for (Item item : player.getInventory().getContents().values()) {
            if (item.getId() != 262) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean onRelease(Player player, int n) {
        Cloneable cloneable;
        EntityProjectile entityProjectile;
        boolean bl;
        Enchantment enchantment;
        Item item = null;
        boolean bl2 = false;
        if (player.getOffhandInventory().getItemFast(0).getId() == 262) {
            item = player.getOffhandInventory().getItemFast(0).clone();
            item.setCount(1);
            bl2 = true;
        } else {
            for (Item item2 : player.getInventory().getContents().values()) {
                if (item2.getId() != 262) continue;
                item = item2.clone();
                item.setCount(1);
                break;
            }
        }
        if (item == null) {
            if (player.isCreative()) {
                item = Item.get(262, 0, 1);
            } else {
                return false;
            }
        }
        double d2 = 2.0;
        Enchantment enchantment2 = this.getEnchantment(19);
        if (enchantment2 != null && enchantment2.getLevel() > 0) {
            d2 += (double)enchantment2.getLevel() * 0.5 + 0.5;
        }
        boolean bl3 = (enchantment = this.getEnchantment(21)) != null && enchantment.getLevel() > 0;
        float f2 = 0.3f;
        Enchantment enchantment3 = this.getEnchantment(20);
        if (enchantment3 != null) {
            f2 += (float)enchantment3.getLevel() * 0.1f;
        }
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", player.x)).add(new DoubleTag("", player.y + (double)player.getEyeHeight())).add(new DoubleTag("", player.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", -Math.sin(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", -Math.sin(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", Math.cos(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI)))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", (float)(player.yaw > 180.0 ? 360 : 0) - (float)player.yaw)).add(new FloatTag("", (float)(-player.pitch)))).putShort("Fire", bl3 ? 2700 : 0).putDouble("damage", d2).putFloat("knockback", f2);
        double d3 = (double)n / 20.0;
        double d4 = Math.min((d3 * d3 + d3 * 2.0) / 3.0, 1.0) * 2.8;
        EntityArrow entityArrow = new EntityArrow(player.chunk, compoundTag, player, d4 > 2.3, false);
        if (item.getDamage() > 0) {
            entityArrow.setData(item.getDamage());
        }
        EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(player, this, entityArrow, d4);
        if (d4 < 0.1 || n < 3) {
            entityShootBowEvent.setCancelled();
        }
        Server.getInstance().getPluginManager().callEvent(entityShootBowEvent);
        if (entityShootBowEvent.isCancelled()) {
            entityShootBowEvent.getProjectile().close();
            return false;
        }
        entityShootBowEvent.getProjectile().setMotion(entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.getForce()));
        Enchantment enchantment4 = this.getEnchantment(22);
        boolean bl4 = bl = enchantment4 != null && enchantment4.getLevel() > 0;
        if (bl && item.getDamage() == 0 && (entityProjectile = entityShootBowEvent.getProjectile()) instanceof EntityArrow) {
            ((EntityArrow)entityProjectile).setPickupMode(2);
        }
        if (!player.isCreative()) {
            if (!bl || item.getDamage() != 0) {
                if (bl2) {
                    player.getOffhandInventory().removeItem(item);
                } else {
                    player.getInventory().removeItem(item);
                }
            }
            if (!(this.isUnbreakable() || (cloneable = this.getEnchantment(17)) != null && ((Enchantment)cloneable).getLevel() > 0 && 100 / (((Enchantment)cloneable).getLevel() + 1) <= Utils.random.nextInt(100))) {
                this.setDamage(this.getDamage() + 2);
                if (this.getDamage() >= this.getMaxDurability()) {
                    --this.count;
                }
                player.getInventory().setItemInHand(this);
            }
        }
        if (entityShootBowEvent.getProjectile() != null) {
            cloneable = entityShootBowEvent.getProjectile();
            ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent((EntityProjectile)cloneable);
            Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
            if (projectileLaunchEvent.isCancelled()) {
                ((Entity)cloneable).close();
            } else {
                ((Entity)cloneable).spawnToAll();
                player.getLevel().addLevelSoundEvent(player, 21);
            }
        }
        return true;
    }
}

