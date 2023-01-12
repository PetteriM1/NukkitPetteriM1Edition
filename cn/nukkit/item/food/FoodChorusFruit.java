/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.food;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.food.FoodNormal;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Utils;

public class FoodChorusFruit
extends FoodNormal {
    public FoodChorusFruit() {
        super(4, 2.4f);
        this.addRelative(432);
    }

    @Override
    protected boolean onEatenBy(Player player) {
        super.onEatenBy(player);
        int n = player.getFloorX() - 8;
        int n2 = player.getFloorY() - 8;
        int n3 = player.getFloorZ() - 8;
        int n4 = n + 16;
        int n5 = n2 + 16;
        int n6 = n3 + 16;
        Level level = player.getLevel();
        if (level == null) {
            return false;
        }
        for (int k = 0; k < 16; ++k) {
            int n7;
            int n8 = Utils.rand(n, n4);
            int n9 = Utils.rand(n3, n6);
            if (n7 < 0) continue;
            BaseFullChunk baseFullChunk = level.getChunk(n8 >> 4, n9 >> 4);
            for (n7 = Utils.rand(n2, n5); n7 >= 0 && !level.getBlock(baseFullChunk, n8, n7 + 1, n9, true).isSolid(); --n7) {
            }
            Block block = level.getBlock(baseFullChunk, n8, ++n7 + 1, n9, true);
            Block block2 = level.getBlock(baseFullChunk, n8, n7 + 2, n9, true);
            if (block.isSolid() || block instanceof BlockLiquid || block2.isSolid() || block2 instanceof BlockLiquid) continue;
            level.addLevelSoundEvent(player, 118);
            if (!player.teleport(new Vector3((double)n8 + 0.5, n7 + 1, (double)n9 + 0.5), PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) break;
            level.addLevelSoundEvent(player, 118);
            break;
        }
        return true;
    }
}

