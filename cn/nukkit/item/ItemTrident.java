/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.projectile.EntityThrownTrident;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

public class ItemTrident
extends ItemTool {
    public ItemTrident() {
        this((Integer)0, 1);
    }

    public ItemTrident(Integer n) {
        this(n, 1);
    }

    public ItemTrident(Integer n, int n2) {
        super(455, n, n2, "Trident");
    }

    @Override
    public int getMaxDurability() {
        return 251;
    }

    @Override
    public boolean isSword() {
        return true;
    }

    @Override
    public int getAttackDamage() {
        return 9;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return true;
    }

    @Override
    public boolean onRelease(Player player, int n) {
        if (this.hasEnchantment(30)) {
            return true;
        }
        this.useOn(player);
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", player.x)).add(new DoubleTag("", player.y + (double)player.getEyeHeight())).add(new DoubleTag("", player.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", -Math.sin(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", -Math.sin(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", Math.cos(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI)))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", (float)(player.yaw > 180.0 ? 360 : 0) - (float)player.yaw)).add(new FloatTag("", (float)(-player.pitch))));
        EntityThrownTrident entityThrownTrident = new EntityThrownTrident(player.chunk, compoundTag, player);
        entityThrownTrident.setItem(this);
        if (player.isCreative()) {
            entityThrownTrident.setPickupMode(2);
        }
        if (this.hasEnchantment(31)) {
            entityThrownTrident.setFavoredSlot(player.getInventory().getHeldItemIndex());
        }
        double d2 = (double)n / 20.0;
        double d3 = Math.min((d2 * d2 + d2 * 2.0) / 3.0, 1.0) * 2.5;
        EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(player, this, entityThrownTrident, d3);
        if (d3 < 0.1 || n < 5) {
            entityShootBowEvent.setCancelled();
        }
        Server.getInstance().getPluginManager().callEvent(entityShootBowEvent);
        if (entityShootBowEvent.isCancelled()) {
            entityShootBowEvent.getProjectile().close();
        } else {
            entityShootBowEvent.getProjectile().setMotion(entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.getForce()));
            ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityShootBowEvent.getProjectile());
            Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
            if (projectileLaunchEvent.isCancelled()) {
                entityShootBowEvent.getProjectile().close();
            } else {
                entityShootBowEvent.getProjectile().spawnToAll();
                player.getLevel().addLevelSoundEvent(player, 183);
                if (!player.isCreative()) {
                    --this.count;
                    player.getInventory().setItemInHand(this);
                }
            }
        }
        return true;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 261;
    }
}

