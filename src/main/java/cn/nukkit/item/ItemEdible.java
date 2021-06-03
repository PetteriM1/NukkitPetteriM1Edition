package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.item.food.Food;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class ItemEdible extends Item {

    public ItemEdible(int id, Integer meta, int count, String name) {
        super(id, meta, count, name);
    }

    public ItemEdible(int id) {
        super(id);
    }

    public ItemEdible(int id, Integer meta) {
        super(id, meta);
    }

    public ItemEdible(int id, Integer meta, int count) {
        super(id, meta, count);
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        if (player.getFoodData().getLevel() < player.getFoodData().getMaxLevel() || player.isCreative()) {
            return true;
        }
        if (player.protocol > ProtocolInfo.v1_12_0) {
            player.getFoodData().sendFoodLevel();
        }
        return false;
    }

    @Override
    public boolean onUse(Player player, int ticksUsed) {
        if (ticksUsed < 10) return false;
        PlayerItemConsumeEvent consumeEvent = new PlayerItemConsumeEvent(player, this);

        player.getServer().getPluginManager().callEvent(consumeEvent);
        if (consumeEvent.isCancelled()) {
            return false; // Inventory#sendContents is called in Player
        }

        Food food = Food.getByRelative(this);
        if (food != null && food.eatenBy(player)) {
            player.getLevel().addSoundToViewers(player, Sound.RANDOM_BURP);
            if (!player.isCreative() && !player.isSpectator()) {
                --this.count;
                player.getInventory().setItemInHand(this);
            }
        }
        return true;
    }
}
