/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFallable;
import cn.nukkit.event.block.BlockFromToEvent;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Utils;

public class BlockDragonEgg
extends BlockFallable {
    @Override
    public String getName() {
        return "Dragon Egg";
    }

    @Override
    public int getId() {
        return 122;
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public double getResistance() {
        return 45.0;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.BLACK_BLOCK_COLOR;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 5) {
            this.teleport();
        }
        return super.onUpdate(n);
    }

    public void teleport() {
        if (!this.level.randomTickingEnabled()) {
            return;
        }
        for (int k = 0; k < 1000; ++k) {
            Block block = this.getLevel().getBlock(this.add(Utils.random.nextInt(-16, 16), Utils.random.nextInt(-16, 16), Utils.random.nextInt(-16, 16)));
            if (block.getId() != 0) continue;
            BlockFromToEvent blockFromToEvent = new BlockFromToEvent(this, block);
            this.level.getServer().getPluginManager().callEvent(blockFromToEvent);
            if (blockFromToEvent.isCancelled()) {
                return;
            }
            block = blockFromToEvent.getTo();
            int n = this.getFloorX() - block.getFloorX();
            int n2 = this.getFloorY() - block.getFloorY();
            int n3 = this.getFloorZ() - block.getFloorZ();
            LevelEventPacket levelEventPacket = new LevelEventPacket();
            levelEventPacket.evid = 2010;
            levelEventPacket.data = Math.abs(n) << 16 | Math.abs(n2) << 8 | Math.abs(n3) | (n < 0 ? 1 : 0) << 24 | (n2 < 0 ? 1 : 0) << 25 | (n3 < 0 ? 1 : 0) << 26;
            levelEventPacket.x = this.getFloorX();
            levelEventPacket.y = this.getFloorY();
            levelEventPacket.z = this.getFloorZ();
            this.getLevel().addChunkPacket(this.getChunkX(), this.getChunkZ(), levelEventPacket);
            this.getLevel().setBlock(this, BlockDragonEgg.get(0), true);
            this.getLevel().setBlock(block, this, true);
            return;
        }
    }

    @Override
    public boolean breakWhenPushed() {
        return true;
    }
}

