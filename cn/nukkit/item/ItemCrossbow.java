/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityCrossbowFirework;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBow;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.Utils;

public class ItemCrossbow
extends ItemBow {
    public ItemCrossbow() {
        this((Integer)0, 1);
    }

    public ItemCrossbow(Integer n) {
        this(n, 1);
    }

    public ItemCrossbow(Integer n, int n2) {
        super(471, n, n2, "Crossbow");
    }

    @Override
    public int getMaxDurability() {
        return 385;
    }

    @Override
    public boolean onUse(Player player, int n) {
        Object object;
        int n2 = 25;
        Enchantment enchantment = this.getEnchantment(35);
        if (enchantment != null) {
            n2 -= enchantment.getLevel() * 5;
        }
        if (n < n2) {
            return true;
        }
        Item item = null;
        boolean bl = false;
        Item item2 = player.getOffhandInventory().getItemFast(0);
        if (item2.getId() == 262 || item2.getId() == 401) {
            item = item2.clone();
            item.setCount(1);
            bl = true;
        } else {
            object = player.getInventory().getContents().values().iterator();
            while (object.hasNext()) {
                Item item3 = object.next();
                if (item3.getId() != 262) continue;
                item = item3.clone();
                item.setCount(1);
                break;
            }
        }
        if (item == null) {
            if (player.isCreative()) {
                item = Item.get(262, 0, 1);
            } else {
                return true;
            }
        }
        if (!this.isLoaded()) {
            if (!player.isCreative()) {
                if (!(this.isUnbreakable() || (object = this.getEnchantment(17)) != null && ((Enchantment)object).getLevel() > 0 && 100 / (((Enchantment)object).getLevel() + 1) <= Utils.random.nextInt(100))) {
                    this.setDamage(this.getDamage() + 2);
                    if (this.getDamage() >= 385) {
                        --this.count;
                    }
                }
                if (bl) {
                    player.getOffhandInventory().removeItem(item);
                } else {
                    player.getInventory().removeItem(item);
                }
            }
            player.getInventory().setItemInHand(this.loadArrow(item));
            player.getLevel().addLevelSoundEvent(player, 247);
        }
        return true;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 vector3) {
        return false;
    }

    @Override
    public boolean onRelease(Player player, int n) {
        return true;
    }

    public Item loadArrow(Item item) {
        CompoundTag compoundTag;
        if (item == null) {
            throw new IllegalArgumentException("null cannot be loaded into a crossbow!");
        }
        if (item.getId() != 262 && item.getId() != 401) {
            throw new IllegalArgumentException(item + " cannot be loaded into a crossbow!");
        }
        if (item.getCount() != 1) {
            throw new IllegalArgumentException("Only one arrow per crossbow is supported!");
        }
        CompoundTag compoundTag2 = this.getNamedTag() == null ? new CompoundTag() : this.getNamedTag();
        boolean bl = item.getId() == 401;
        CompoundTag compoundTag3 = new CompoundTag("chargedItem").putByte("Count", item.getCount()).putShort("Damage", item.getDamage()).putString("Name", bl ? "minecraft:firework_rocket" : "minecraft:arrow");
        if (bl && (compoundTag = item.getNamedTag()) != null) {
            compoundTag3.putCompound("tag", compoundTag);
        }
        compoundTag2.putBoolean("Charged", true).putCompound("chargedItem", compoundTag3);
        this.setCompoundTag(compoundTag2);
        return this;
    }

    public boolean isLoaded() {
        Tag tag = this.getNamedTagEntry("chargedItem");
        if (tag != null) {
            CompoundTag compoundTag = (CompoundTag)tag;
            return compoundTag.getByte("Count") > 0 && !compoundTag.getString("Name").isEmpty();
        }
        return false;
    }

    public boolean launchArrow(Player player) {
        Object object;
        EntityProjectile entityProjectile;
        CompoundTag compoundTag = (CompoundTag)this.getNamedTagEntry("chargedItem");
        if (compoundTag == null) {
            throw new IllegalArgumentException("A crossbow without charged item cannot be launched!");
        }
        int n = compoundTag.getShort("Damage");
        CompoundTag compoundTag2 = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", player.x)).add(new DoubleTag("", player.y + (double)player.getEyeHeight())).add(new DoubleTag("", player.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", -Math.sin(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", -Math.sin(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", Math.cos(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI)))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", (float)(player.yaw > 180.0 ? 360 : 0) - (float)player.yaw)).add(new FloatTag("", (float)(-player.pitch))));
        boolean bl = "minecraft:firework_rocket".equals(compoundTag.getString("Name"));
        if (bl) {
            entityProjectile = new EntityCrossbowFirework(player.chunk, compoundTag2, player);
            object = Item.get(401, n, 1);
            if (compoundTag.contains("tag")) {
                ((Item)object).setCompoundTag(compoundTag.getCompound("tag"));
            }
            ((EntityCrossbowFirework)entityProjectile).setFirework((Item)object);
        } else {
            entityProjectile = new EntityArrow(player.chunk, compoundTag2, player, false, true);
            if (n > 0) {
                ((EntityArrow)entityProjectile).setData(n);
            }
            if (this.hasEnchantment(34)) {
                entityProjectile.piercing = 1;
            }
        }
        object = new EntityShootBowEvent(player, this, entityProjectile, bl ? 1.0 : 3.5);
        Server.getInstance().getPluginManager().callEvent((Event)object);
        if (((Event)object).isCancelled()) {
            ((EntityShootBowEvent)object).getProjectile().close();
            return false;
        }
        ((EntityShootBowEvent)object).getProjectile().setMotion(((EntityShootBowEvent)object).getProjectile().getMotion().multiply(((EntityShootBowEvent)object).getForce()));
        if (((EntityShootBowEvent)object).getProjectile() != null) {
            EntityProjectile entityProjectile2 = ((EntityShootBowEvent)object).getProjectile();
            ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(entityProjectile2);
            Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
            if (projectileLaunchEvent.isCancelled()) {
                entityProjectile2.close();
                return false;
            }
            entityProjectile2.spawnToAll();
            if (!bl && this.hasEnchantment(33)) {
                CompoundTag compoundTag3 = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", player.x)).add(new DoubleTag("", player.y + (double)player.getEyeHeight())).add(new DoubleTag("", player.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", -Math.sin(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", -Math.sin(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", Math.cos(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI)))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", (float)(player.yaw > 180.0 ? 360 : 0) - (float)player.yaw - 10.0f)).add(new FloatTag("", (float)(-player.pitch))));
                EntityArrow entityArrow = new EntityArrow(player.chunk, compoundTag3, player, false, true);
                entityArrow.setPickupMode(-1);
                if (n > 0) {
                    entityArrow.setData(n);
                }
                if (this.hasEnchantment(34)) {
                    entityArrow.piercing = 1;
                }
                entityArrow.setMotion(entityArrow.getMotion().multiply(((EntityShootBowEvent)object).getForce()).add(-0.3, 0.0, 0.3));
                entityArrow.spawnToAll();
                CompoundTag compoundTag4 = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", player.x)).add(new DoubleTag("", player.y + (double)player.getEyeHeight())).add(new DoubleTag("", player.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", -Math.sin(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", -Math.sin(player.pitch / 180.0 * Math.PI))).add(new DoubleTag("", Math.cos(player.yaw / 180.0 * Math.PI) * Math.cos(player.pitch / 180.0 * Math.PI)))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", (float)(player.yaw > 180.0 ? 360 : 0) - (float)player.yaw + 10.0f)).add(new FloatTag("", (float)(-player.pitch))));
                EntityArrow entityArrow2 = new EntityArrow(player.chunk, compoundTag4, player, false, true);
                entityArrow2.setPickupMode(-1);
                if (n > 0) {
                    entityArrow2.setData(n);
                }
                if (this.hasEnchantment(34)) {
                    entityArrow2.piercing = 1;
                }
                entityArrow2.setMotion(entityArrow2.getMotion().multiply(((EntityShootBowEvent)object).getForce()).add(0.3, 0.0, -0.3));
                entityArrow2.spawnToAll();
            }
            player.getLevel().addLevelSoundEvent(player, 248);
            this.setCompoundTag(this.getNamedTag().putBoolean("Charged", false).remove("chargedItem"));
            player.getInventory().setItemInHand(this);
        }
        return true;
    }

    @Override
    public int getEnchantAbility() {
        return 1;
    }

    @Override
    public boolean isSupportedOn(int n) {
        return n >= 313;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

