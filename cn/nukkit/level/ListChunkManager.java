/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.generic.BaseFullChunk;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListChunkManager
implements ChunkManager {
    private final ChunkManager b;
    private final List<Block> a;

    public ListChunkManager(ChunkManager chunkManager) {
        this.b = chunkManager;
        this.a = new ArrayList<Block>();
    }

    @Override
    public int getBlockIdAt(int n, int n2, int n3) {
        Optional<Block> optional = this.a.stream().filter(block -> block.getFloorX() == n && block.getFloorY() == n2 && block.getFloorZ() == n3).findAny();
        return optional.map(Block::getId).orElseGet(() -> this.b.getBlockIdAt(n, n2, n3));
    }

    @Override
    public void setBlockFullIdAt(int n, int n2, int n3, int n4) {
        this.a.removeIf(block -> block.getFloorX() == n && block.getFloorY() == n2 && block.getFloorZ() == n3);
        this.a.add(Block.get(n4, null, n, n2, n3));
    }

    @Override
    public void setBlockIdAt(int n, int n2, int n3, int n4) {
        Optional<Block> optional = this.a.stream().filter(block -> block.getFloorX() == n && block.getFloorY() == n2 && block.getFloorZ() == n3).findAny();
        Block block2 = optional.orElse(Block.get(this.getBlockIdAt(n, n2, n3), this.getBlockDataAt(n, n2, n3), new Position(n, n2, n3)));
        this.a.remove(block2);
        this.a.add(Block.get(this.getBlockIdAt(n, n2, n3), this.getBlockDataAt(n, n2, n3), new Position(n, n2, n3)));
    }

    @Override
    public void setBlockAt(int n, int n2, int n3, int n4, int n5) {
        this.a.removeIf(block -> block.getFloorX() == n && block.getFloorY() == n2 && block.getFloorZ() == n3);
        this.a.add(Block.get(n4, n5, new Position(n, n2, n3)));
    }

    @Override
    public int getBlockDataAt(int n, int n2, int n3) {
        Optional<Block> optional = this.a.stream().filter(block -> block.getFloorX() == n && block.getFloorY() == n2 && block.getFloorZ() == n3).findAny();
        return optional.map(Block::getDamage).orElseGet(() -> this.b.getBlockDataAt(n, n2, n3));
    }

    @Override
    public void setBlockDataAt(int n, int n2, int n3, int n4) {
        Optional<Block> optional = this.a.stream().filter(block -> block.getFloorX() == n && block.getFloorY() == n2 && block.getFloorZ() == n3).findAny();
        Block block2 = optional.orElse(Block.get(this.getBlockIdAt(n, n2, n3), this.getBlockDataAt(n, n2, n3), new Position(n, n2, n3)));
        this.a.remove(block2);
        block2.setDamage(n4);
        this.a.add(block2);
    }

    @Override
    public BaseFullChunk getChunk(int n, int n2) {
        return this.b.getChunk(n, n2);
    }

    @Override
    public void setChunk(int n, int n2) {
        this.b.setChunk(n, n2);
    }

    @Override
    public void setChunk(int n, int n2, BaseFullChunk baseFullChunk) {
        this.b.setChunk(n, n2, baseFullChunk);
    }

    @Override
    public long getSeed() {
        return this.b.getSeed();
    }

    public List<Block> getBlocks() {
        return this.a;
    }
}

