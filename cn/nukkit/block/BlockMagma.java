/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.GameRule;
import cn.nukkit.utils.BlockColor;

public class BlockMagma
extends BlockSolid {
    @Override
    public int getId() {
        return 213;
    }

    @Override
    public String getName() {
        return "Magma Block";
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 30.0;
    }

    @Override
    public int getLightLevel() {
        return 3;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe()) {
            return new Item[]{this.toItem()};
        }
        return new Item[0];
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (!entity.hasEffect(12)) {
            if (entity instanceof Player) {
                Player player = (Player)entity;
                PlayerInventory playerInventory = player.getInventory();
                if (playerInventory == null || playerInventory.getBootsFast().hasEnchantment(25) || !entity.level.gameRules.getBoolean(GameRule.FIRE_DAMAGE)) {
                    return;
                }
                if (!(player.isCreative() || player.isSpectator() || player.isSneaking())) {
                    entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.MAGMA, 1.0f));
                }
            } else {
                entity.attack(new EntityDamageByBlockEvent(this, entity, EntityDamageEvent.DamageCause.MAGMA, 1.0f));
            }
        }
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.NETHERRACK_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }
}

