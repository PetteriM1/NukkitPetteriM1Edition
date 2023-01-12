/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.EntitySpawner;
import cn.nukkit.utils.SpawnerTask;
import cn.nukkit.utils.Utils;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public abstract class AbstractEntitySpawner
implements EntitySpawner {
    protected SpawnerTask spawnTask;
    private static final IntArrayList a = new IntArrayList(new int[]{110, 112, 109, 108, 111, 74, 31, 17, 129});

    public AbstractEntitySpawner(SpawnerTask spawnerTask) {
        this.spawnTask = spawnerTask;
    }

    @Override
    public void spawn() {
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if (!this.a(player)) continue;
            this.b(player);
        }
    }

    private void b(Player player) {
        Level level = player.getLevel();
        if (SpawnerTask.a(level, this.getEntityNetworkId(), player)) {
            Position position = new Position(player.getFloorX(), player.getFloorY(), player.getFloorZ(), level);
            if (this.getEntityNetworkId() == 58) {
                if (!level.isInSpawnRadius(position)) {
                    position.x += (double)Utils.rand(-2, 2);
                    position.y += (double)Utils.rand(20, 34);
                    position.z += (double)Utils.rand(-2, 2);
                    this.spawn(player, position, level);
                }
            } else {
                position.x += (double)SpawnerTask.a(Utils.rand(48, 52), Utils.rand(24, 28), Utils.rand(4, 8));
                position.z += (double)SpawnerTask.a(Utils.rand(48, 52), Utils.rand(24, 28), Utils.rand(4, 8));
                if (!level.isChunkLoaded((int)position.x >> 4, (int)position.z >> 4) || !level.isChunkGenerated((int)position.x >> 4, (int)position.z >> 4)) {
                    return;
                }
                if (level.isInSpawnRadius(position)) {
                    return;
                }
                position.y = SpawnerTask.a(level, position);
                if (position.y < 1.0 || position.y > 255.0 || level.getDimension() == 1 && position.y > 125.0) {
                    return;
                }
                if (AbstractEntitySpawner.a(position)) {
                    return;
                }
                Block block = level.getBlock(position, false);
                if (this.getEntityNetworkId() == 125) {
                    if (!(block instanceof BlockLava)) {
                        return;
                    }
                } else {
                    if (block.getId() == 99 || block.getId() == 100) {
                        return;
                    }
                    if (!(!block.isTransparent() || block.getId() == 78 || Block.hasWater(block.getId()) && a.contains(this.getEntityNetworkId()))) {
                        return;
                    }
                }
                this.spawn(player, position, level);
            }
        }
    }

    private boolean a(Player player) {
        if (!player.getLevel().isMobSpawningAllowed() || Utils.rand(1, 4) == 1) {
            return false;
        }
        if (Server.getInstance().getDifficulty() == 0) {
            return !Utils.monstersList.contains(this.getEntityNetworkId());
        }
        return true;
    }

    private static boolean a(Position position) {
        for (Player player : position.getLevel().getPlayers().values()) {
            if (!(player.distanceSquared(position) < 196.0)) continue;
            return true;
        }
        return false;
    }
}

