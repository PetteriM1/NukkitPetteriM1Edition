package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;

public class ItemCrossbow extends ItemBow {

    private int loadTick = 0; //TODO Improve this

    public ItemCrossbow() {
        this(0, 1);
    }

    public ItemCrossbow(Integer meta) {
        this(meta, 1);
    }

    public ItemCrossbow(Integer meta, int count) {
        super(CROSSBOW, meta, count, "Crossbow");
    }

    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_CROSSBOW;
    }

    @Override
    public boolean onUse(Player player, int ticksUsed) {
        if (ticksUsed < 20) {
            return true;
        }

        Item itemArrow = Item.get(Item.ARROW, 0, 1);

        Inventory inventory = player.getOffhandInventory();

        if (!inventory.contains(itemArrow) && !(inventory = player.getInventory()).contains(itemArrow) && player.isSurvival()) {
            player.getOffhandInventory().sendContents(player);
            inventory.sendContents(player);
            return true;
        }

        if (!this.isLoaded()) {
            if (!player.isCreative()) {
                if (!this.isUnbreakable()) {
                    Enchantment durability = this.getEnchantment(Enchantment.ID_DURABILITY);
                    if (!(durability != null && durability.getLevel() > 0 && (100 / (durability.getLevel() + 1)) <= Utils.random.nextInt(100))) {
                        this.setDamage(this.getDamage() + 2);
                        if (this.getDamage() >= DURABILITY_CROSSBOW) {
                            this.count--;
                        }
                        player.getInventory().setItemInHand(this);
                    }
                }

                inventory.removeItem(itemArrow);
            }

            this.loadArrow(player, itemArrow);
            player.getLevel().addLevelSoundEvent(player, LevelSoundEventPacket.SOUND_CROSSBOW_LOADING_END);
        }

        return true;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        return !this.launchArrow(player);
    }

    @Override
    public boolean onRelease(Player player, int ticksUsed) {
        return true;
    }

    public void loadArrow(Player player, Item arrow) {
        if (arrow == null) return;
        CompoundTag tag = this.getNamedTag() == null ? new CompoundTag() : this.getNamedTag();
        tag.putBoolean("Charged", true)
                .putCompound("chargedItem", new CompoundTag("chargedItem")
                        .putByte("Count", arrow.getCount())
                        .putShort("Damage", arrow.getDamage())
                        .putString("Name", "minecraft:arrow"));
        this.setCompoundTag(tag);
        this.loadTick = Server.getInstance().getTick();
        player.getInventory().setItemInHand(this);
    }

    public void useArrow(Player player) {
        this.setCompoundTag(this.getNamedTag().putBoolean("Charged", false).remove("chargedItem"));
        player.getInventory().setItemInHand(this);
    }

    public boolean isLoaded() {
        Tag itemInfo = this.getNamedTagEntry("chargedItem");
        if (itemInfo != null) {
            CompoundTag tag = (CompoundTag) itemInfo;
            return tag.getByte("Count") > 0 && tag.getString("Name") != null;
        }

        return false;
    }

    public boolean launchArrow(Player player) {
        if (this.isLoaded() && Server.getInstance().getTick() - this.loadTick > 20) {
            CompoundTag nbt = new CompoundTag()
                    .putList(new ListTag<DoubleTag>("Pos")
                            .add(new DoubleTag("", player.x))
                            .add(new DoubleTag("", player.y + player.getEyeHeight()))
                            .add(new DoubleTag("", player.z)))
                    .putList(new ListTag<DoubleTag>("Motion")
                            .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                            .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                            .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                    .putList(new ListTag<FloatTag>("Rotation")
                            .add(new FloatTag("", (player.yaw > 180 ? 360 : 0) - (float) player.yaw))
                            .add(new FloatTag("", (float) -player.pitch)));

            EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(player, this, new EntityArrow(player.chunk, nbt, player, false), 3.5);

            Server.getInstance().getPluginManager().callEvent(entityShootBowEvent);
            if (entityShootBowEvent.isCancelled()) {
                entityShootBowEvent.getProjectile().close();
                player.getInventory().sendContents(player);
            } else {
                entityShootBowEvent.getProjectile().setMotion(entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.getForce()));
                if (entityShootBowEvent.getProjectile() != null) {
                    EntityProjectile proj = entityShootBowEvent.getProjectile();
                    ProjectileLaunchEvent projectev = new ProjectileLaunchEvent(proj);
                    Server.getInstance().getPluginManager().callEvent(projectev);
                    if (projectev.isCancelled()) {
                        proj.close();
                    } else {
                        proj.spawnToAll();
                        player.getLevel().addLevelSoundEvent(player, LevelSoundEventPacket.SOUND_CROSSBOW_SHOOT);
                        this.useArrow(player);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
