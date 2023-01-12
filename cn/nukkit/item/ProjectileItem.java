/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityExpBottle;
import cn.nukkit.entity.projectile.EntityEnderEye;
import cn.nukkit.entity.projectile.EntityEnderPearl;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemEnderEye;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

public abstract class ProjectileItem
extends Item {
    public ProjectileItem(int n, Integer n2, int n3, String string) {
        super(n, n2, n3, string);
    }

    public abstract String getProjectileEntityType();

    public abstract float getThrowForce();

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        if (this instanceof ItemEnderEye && player.getLevel().getDimension() != 0) {
            return false;
        }
        CompoundTag compoundTag = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", player.x)).add(new DoubleTag("", player.y + (double)player.getEyeHeight())).add(new DoubleTag("", player.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", vector3.x)).add(new DoubleTag("", vector3.y)).add(new DoubleTag("", vector3.z))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", (float)player.yaw)).add(new FloatTag("", (float)player.pitch)));
        this.correctNBT(compoundTag);
        Entity entity = Entity.createEntity(this.getProjectileEntityType(), (FullChunk)player.getLevel().getChunk(player.getChunkX(), player.getChunkZ()), compoundTag, player);
        if (entity != null) {
            Object object;
            if ((entity instanceof EntityEnderPearl || entity instanceof EntityEnderEye) && player.getServer().getTick() - player.getLastEnderPearlThrowingTick() < 20) {
                entity.close();
                return false;
            }
            if (entity instanceof EntityEnderEye) {
                if (player.getServer().getTick() - player.getLastEnderPearlThrowingTick() < 20) {
                    entity.close();
                    return false;
                }
                object = this.a(player);
                if (object == null) {
                    entity.close();
                    return false;
                }
                Vector3f vector3f = ((Position)object).subtract(player.getPosition()).asVector3f().normalize().multiply(1.0f);
                vector3f.y = 0.55f;
                entity.setMotion(vector3f.asVector3().divide(this.getThrowForce()));
            } else {
                entity.setMotion(entity.getMotion().multiply(this.getThrowForce()));
            }
            if (entity instanceof EntityProjectile) {
                object = new ProjectileLaunchEvent((EntityProjectile)entity);
                player.getServer().getPluginManager().callEvent((Event)object);
                if (((Event)object).isCancelled()) {
                    entity.close();
                } else if (player.getGamemode() == 1 && entity instanceof EntityExpBottle && !player.getServer().xpBottlesOnCreative) {
                    ((Event)object).setCancelled(true);
                    entity.close();
                    player.sendMessage("\u00a7cXP bottles are disabled on creative");
                } else {
                    if (!player.isCreative()) {
                        --this.count;
                    }
                    if (entity instanceof EntityEnderPearl || entity instanceof EntityEnderEye) {
                        player.onThrowEnderPearl();
                    }
                    entity.spawnToAll();
                    player.getLevel().addLevelSoundEvent(player, 21);
                }
            }
        }
        return true;
    }

    protected void correctNBT(CompoundTag compoundTag) {
    }

    private Position a(Player player) {
        return player.level.getDimension() == 0 ? player : null;
    }
}

