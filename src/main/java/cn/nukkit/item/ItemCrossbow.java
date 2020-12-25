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
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.utils.Utils;

public class ItemCrossbow extends ItemBow {

    private boolean loaded; // TODO: Remove when fixed

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
    public boolean onRelease(Player player, int ticksUsed) {
        if (player.getServer().getTick() - player.getStartActionTick() < 20 && player.getStartActionTick() != -1) {
            return false;
        }

        Item itemArrow = Item.get(Item.ARROW, 0, 1);

        Inventory inventory = player.getOffhandInventory();

        if (!inventory.contains(itemArrow) && !(inventory = player.getInventory()).contains(itemArrow) && player.isSurvival()) {
            player.getOffhandInventory().sendContents(player);
            inventory.sendContents(player);
            return false;
        }

        if (!this.loaded) {
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

        return false; //HACK
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        this.launchArrow(player);
        return true;
    }

    public void loadArrow(Player player, Item arrow) {
        //this.getNamedTag().putCompound("chargedItem", new CompoundTag("chargedItem").putByte("Count", arrow.getCount()).putShort("Damage", arrow.getDamage()).putString("Name", "minecraft:arrow"));
        this.loaded = true;
        player.getInventory().setItemInHand(this);
    }

    public boolean isLoaded() {
        /*if (this.getNamedTagEntry("chargedItem") != null) {
            if (this.getNamedTag().getByte("Count") > 0 && this.getNamedTag().getString("Name") != null) {
                return true;
            }
        }

        return false;*/
        return this.loaded;
    }

    public void launchArrow(Player player) {
        if (this.loaded) {
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
                        this.loaded = false;
                        //this.getNamedTag().remove("chargedItem");
                    }
                }
            }
        }
    }
}
