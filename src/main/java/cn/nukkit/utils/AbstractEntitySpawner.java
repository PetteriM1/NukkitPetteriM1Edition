package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.entity.mob.EntityPhantom;
import cn.nukkit.entity.passive.EntityStrider;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.FastMathLite;
import cn.nukkit.math.NukkitMath;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Base class of the default mob spawners
 */
public abstract class AbstractEntitySpawner implements EntitySpawner {

    protected SpawnerTask spawnTask;

    private final boolean isMonsterSpawner;

    public AbstractEntitySpawner(SpawnerTask spawnTask) {
        this.spawnTask = spawnTask;

        this.isMonsterSpawner = Utils.monstersList.contains(this.getEntityNetworkId());
    }

    @Override
    public void spawn() {
        for (Player player : Server.getInstance().getOnlinePlayersList()) {
            if (isSpawningAllowed(player)) {
                spawnTo(player);
            }
        }
    }

    /**
     * Attempt to spawn a mob to a player
     *
     * @param player player
     */
    private void spawnTo(Player player) {
        Level level = player.getLevel();

        if (SpawnerTask.entitySpawnAllowed(level, this.getEntityNetworkId(), player)) {
            Position pos = new Position(player.getFloorX(), player.getFloorY(), player.getFloorZ(), level);

            if (this.getEntityNetworkId() == EntityPhantom.NETWORK_ID) {
                // Other checks are done in the spawner class
                pos.x = pos.x + Utils.rand(-2, 2);
                pos.y = pos.y + Utils.rand(20, 34);
                pos.z = pos.z + Utils.rand(-2, 2);
                spawn(player, pos, level);
            } else {
                ThreadLocalRandom random = ThreadLocalRandom.current();

                double r = 24.0 + 20.0 * random.nextDouble(); // Between min 24 and max 44 blocks from player
                double theta = 6.283185307179586 * random.nextDouble(); // 2pi

                pos.x += NukkitMath.ceilDouble(r * FastMathLite.cos(theta));
                pos.z += NukkitMath.ceilDouble(r * FastMathLite.sin(theta));

                FullChunk chunk = level.getChunkIfLoaded((int) pos.x >> 4, (int) pos.z >> 4);
                if (chunk == null || !chunk.isGenerated() || !chunk.isPopulated()) {
                    return;
                }

                if (level.isInSpawnRadius(pos)) { // Do not spawn mobs in the world spawn area
                    return;
                }

                if (this.isMonsterSpawner) {
                    int biome = chunk.getBiomeId(((int) pos.x) & 0x0f, ((int) pos.z) & 0x0f);
                    if (biome == 14 || biome == 15) {
                        return; // Mobs don't spawn on mushroom island
                    }
                }

                pos.y = SpawnerTask.getSafeYCoord(level, pos, chunk);

                if (this.isWaterMob()) {
                    pos.y--;
                }

                if (pos.y <= level.getMinBlockY() || pos.y > level.getMaxBlockY()) {
                    return;
                }

                if (isTooNearOfPlayer(pos)) {
                    return;
                }

                Block block = level.getBlock(pos, false);
                if (this.getEntityNetworkId() == EntityStrider.NETWORK_ID) {
                    if (!(block instanceof BlockLava)) {
                        return;
                    }
                } else {
                    if (block.getId() == Block.BROWN_MUSHROOM_BLOCK || block.getId() == Block.RED_MUSHROOM_BLOCK) { // Mushrooms aren't transparent but shouldn't have mobs spawned on them
                        return;
                    }

                    if (block.isTransparent() && block.getId() != Block.SNOW_LAYER) { // Snow layer is an exception
                        if (!Block.isWater(block.getId()) || !this.isWaterMob()) {
                            return;
                        }
                    }
                }

                spawn(player, pos, level);
            }
        }
    }

    /**
     * Check if mob spawning is allowed in the world the player is in
     *
     * @param player player
     * @return mob spawning allowed
     */
    private boolean isSpawningAllowed(Player player) {
        if (player.isSpectator()) {
            return false;
        }
        if (!player.getLevel().isMobSpawningAllowed()) {
            return false;
        }
        return !this.isMonsterSpawner || Server.getInstance().getDifficulty() != 0;
    }

    /**
     * Check if mob spawn position is too close to player
     *
     * @param pos position
     * @return whether the position is too close to player
     */
    private static boolean isTooNearOfPlayer(Position pos) {
        for (Player p : pos.getLevel().getPlayersList()) {
            if (p.distanceSquared(pos) < 576) { // 24 blocks
                return true;
            }
        }
        return false;
    }
}
