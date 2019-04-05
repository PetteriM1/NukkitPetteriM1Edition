package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

import java.util.Random;

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
    public boolean onReleaseUsing(Player player) {
        Item itemArrow = Item.get(Item.ARROW, 0, 1);

        if (!player.isCreative() && !player.getInventory().contains(itemArrow)) {
            player.getInventory().sendContents(player);
            return false;
        }

        if (!this.isUnbreakable()) {
            Enchantment durability = this.getEnchantment(Enchantment.ID_DURABILITY);
            if (!(durability != null && durability.getLevel() > 0 && (100 / (durability.getLevel() + 1)) <= new Random().nextInt(100))) {
                this.setDamage(this.getDamage() + 2);
                if (this.getDamage() >= getMaxDurability()) {
                    this.count--;
                }
            }
        }

        if (!this.isLoaded()) {
            player.getInventory().removeItem(itemArrow);
            this.loadArrow(player, itemArrow);
        }

        player.getLevel().addLevelSoundEvent(player, LevelSoundEventPacket.SOUND_CROSSBOW_LOADING_END);

        return true;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        this.launchArrow(player);
        return true;
    }

    /*@Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        this.launchArrow(player);
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }*/

    public void loadArrow(Player player, Item arrow) {
        //this.getNamedTag().putCompound("chargedItem", new CompoundTag("chargedItem").putByte("Count", arrow.getCount()).putShort("Damage", arrow.getDamage()).putString("Name", "minecraft:arrow"));
        this.loaded = true;
    }

    public boolean isLoaded() {
        /*if (this.getNamedTagEntry("chargedItem") != null) {
            if (this.getNamedTag().getByte("Count") > 0 && this.getNamedTag().getString("Name") != null) {
                return true;
            }
        }*/

        return this.loaded;
    }

    private void launchArrow(Player player) {
        if (this.isLoaded()) {
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
                            .add(new FloatTag("", (float) -player.pitch)))
                    .putDouble("damage", 6);

            int diff = (Server.getInstance().getTick() - player.getStartActionTick());
            double p = (double) diff / 20;

            double f = Math.min((p * p + p * 2) / 3, 1) * 4;
            EntityShootBowEvent entityShootBowEvent = new EntityShootBowEvent(player, this, new EntityArrow(player.chunk, nbt, player, f == 2), f);

            if (f < 0.1 || diff < 5) {
                entityShootBowEvent.setCancelled();
            }

            Server.getInstance().getPluginManager().callEvent(entityShootBowEvent);
            if (entityShootBowEvent.isCancelled()) {
                entityShootBowEvent.getProjectile().kill();
                player.getInventory().sendContents(player);
            } else {
                entityShootBowEvent.getProjectile().setMotion(entityShootBowEvent.getProjectile().getMotion().multiply(entityShootBowEvent.getForce()));
                if (entityShootBowEvent.getProjectile() instanceof EntityProjectile) {
                    EntityProjectile proj = entityShootBowEvent.getProjectile();
                    ProjectileLaunchEvent projectev = new ProjectileLaunchEvent(proj);
                    Server.getInstance().getPluginManager().callEvent(projectev);
                    if (projectev.isCancelled()) {
                        proj.kill();
                    } else {
                        proj.spawnToAll();
                        player.getLevel().addLevelSoundEvent(player, LevelSoundEventPacket.SOUND_CROSSBOW_SHOOT);
                        this.clearNamedTag();
                    }
                } else {
                    entityShootBowEvent.getProjectile().spawnToAll();
                }
            }
        }
    }
}
