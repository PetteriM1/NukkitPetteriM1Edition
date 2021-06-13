package cn.nukkit.item.customitem;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.item.food.Food;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * @author lt_name
 */
public abstract class ItemCustomEdible extends ItemCustom {

    public ItemCustomEdible(int id) {
        this(id, 0, 1, UNKNOWN_STR);
    }

    public ItemCustomEdible(int id, Integer meta) {
        this(id, meta, 1, UNKNOWN_STR);
    }

    public ItemCustomEdible(int id, Integer meta, int count) {
        this(id, meta, count, UNKNOWN_STR);
    }

    public ItemCustomEdible(int id, Integer meta, int count, String name) {
        super(id, meta, count, name);
    }

    @Override
    public CompoundTag getComponentsData() {
        CompoundTag data = super.getComponentsData();

        if(this.isDrink()) {
            data.getCompound("components")
                    .putCompound("minecraft:food",
                            new CompoundTag()
                                    .putInt("nutrition", 0)
                                    .putBoolean("can_always_eat",true)
                    );
        }else {
            data.getCompound("components")
                    .putCompound("minecraft:food",
                            new CompoundTag()
                                    .putInt("nutrition", 0)
                    );
        }

        data.getCompound("components").getCompound("item_properties")
                .putInt("use_duration", this.getEatTick())
                .putInt("use_animation", this.isDrink() ? 2 : 1);


        return data;
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
        if (ticksUsed < this.getEatTick()) return false;
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

    public int getEatTick() {
        return 40;
    }

    public boolean isFood() {
        return true;
    }

    public boolean isDrink() {
        return false;
    }

}
