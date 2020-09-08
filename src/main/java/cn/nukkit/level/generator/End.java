package cn.nukkit.level.generator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.noise.Noise;
import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PetteriM1
 */
public class End extends Generator {

    private ChunkManager level;
    private NukkitRandom nukkitRandom;
    //private final List<Populator> populators = new ArrayList<>();
    //private List<Populator> generationPopulators = new ArrayList<>();

    private Simplex noiseBase;

    public End() {
        this(new HashMap<>());
    }

    public End(Map<String, Object> options) {
    }

    @Override
    public int getId() {
        return Generator.TYPE_THE_END;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_THE_END;
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }

    @Override
    public String getName() {
        return "the_end";
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.noiseBase = new Simplex(this.nukkitRandom, 4, 0.25f, 0.015625f);
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        this.nukkitRandom.setSeed(0xa6fe78dc ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());

        BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);

        double[][][] noise = getFastNoise3D(this.noiseBase, 16, 128, 16, 4, 8, 4, chunkX << 4, 0, chunkZ << 4);

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                chunk.setBiomeId(x, z, EnumBiome.END.biome.getId());

                for (int y = 12; y < 64; ++y) {
                    double noiseValue = 0 - noise[x][z][y];
                    noiseValue -= 0.5;

                    double distance = new Vector3(0, 64, 0).distance(new Vector3((chunkX << 4) + x, y / 1.3, (chunkZ << 4) + z));

                    if ((noiseValue < 0 && distance < 100) || (noiseValue < -0.2 && distance > 400)) {
                        chunk.setBlockId(x, y, z, Block.END_STONE);
                    }
                }
            }
        }

        /*for (Populator populator : this.generationPopulators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }*/
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        /*BaseFullChunk chunk = level.getChunk(chunkX, chunkZ);
        this.nukkitRandom.setSeed(0xa6fe78dc ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
        for (Populator populator : this.populators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, chunk);
        }

        Biome biome = EnumBiome.getBiome(EnumBiome.END.id);
        biome.populateChunk(this.level, chunkX, chunkZ, this.nukkitRandom);*/
    }

    public Vector3 getSpawn() {
        return new Vector3(0, 64, 0);
    }

    private double[][][] getFastNoise3D(Noise noise, int xSize, int ySize, int zSize, int xSamplingRate, int ySamplingRate, int zSamplingRate, int x, int y, int z) {
        double[][][] noiseArray = new double[xSize + 1][zSize + 1][ySize + 1];
        for (int xx = 0; xx <= xSize; xx += xSamplingRate) {
            for (int zz = 0; zz <= zSize; zz += zSamplingRate) {
                for (int yy = 0; yy <= ySize; yy += ySamplingRate) {
                    noiseArray[xx][zz][yy] = noise.noise3D(x + xx, y + yy, z + zz, true);
                }
            }
        }

        for (int xx = 0; xx < xSize; ++xx) {
            for (int zz = 0; zz < zSize; ++zz) {
                for (int yy = 0; yy < ySize; ++yy) {
                    if (xx % xSamplingRate != 0 || zz % zSamplingRate != 0 || yy % ySamplingRate != 0) {
                        int nx = xx / xSamplingRate * xSamplingRate;
                        int ny = yy / ySamplingRate * ySamplingRate;
                        int nz = zz / zSamplingRate * zSamplingRate;

                        int nnx = nx + xSamplingRate;
                        int nny = ny + ySamplingRate;
                        int nnz = nz + zSamplingRate;

                        double dx1 = ((double) (nnx - xx) / (double) (nnx - nx));
                        double dx2 = ((double) (xx - nx) / (double) (nnx - nx));
                        double dy1 = ((double) (nny - yy) / (double) (nny - ny));
                        double dy2 = ((double) (yy - ny) / (double) (nny - ny));

                        noiseArray[xx][zz][yy] = ((double) (nnz - zz) / (double) (nnz - nz)) * (
                                dy1 * (
                                        dx1 * noiseArray[nx][nz][ny] + dx2 * noiseArray[nnx][nz][ny]
                                ) + dy2 * (
                                        dx1 * noiseArray[nx][nz][nny] + dx2 * noiseArray[nnx][nz][nny]
                                )
                        ) + ((double) (zz - nz) / (double) (nnz - nz)) * (
                                dy1 * (
                                        dx1 * noiseArray[nx][nnz][ny] + dx2 * noiseArray[nnx][nnz][ny]
                                ) + dy2 * (
                                        dx1 * noiseArray[nx][nnz][nny] + dx2 * noiseArray[nnx][nnz][nny]
                                )
                        );
                    }
                }
            }
        }

        return noiseArray;
    }
}
