package cn.nukkit.level.format.leveldb.structure;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockUnknown;
import cn.nukkit.level.format.leveldb.BlockStateMapping;
import org.cloudburstmc.nbt.NbtMap;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BlockStateSnapshot {
    private final NbtMap vanillaState;
    private final int runtimeId;
    private final int version;

    @Builder.Default
    private boolean custom = false;

    @Builder.Default
    private int legacyId = -1;
    @Builder.Default
    private int legacyData = -1;

    @Builder.Default
    private Block block = null;

    private Block getBlock() {
        if (this.block == null) {
            this.block = Block.get(this.getLegacyId(), this.getLegacyData());
        }
        return this.block;
    }

    public int getLegacyData() {
        if (this.legacyData != -1) {
            return this.legacyData;
        }

        int data = BlockStateMapping.get().getLegacyData(this.runtimeId);
        if (this.version == BlockStateMapping.get().getVersion()) {
            this.legacyData = data;
        }
        return data;
    }

    public int getLegacyData(int protocol) {
        if (protocol >= this.version || this.getBlock().getMinimumVersion() == 0 || this.getBlock() instanceof BlockUnknown) {
            return this.getLegacyData();
        }
        return this.getBlock().getAlternateMeta(protocol);
    }

    public int getLegacyId(int protocol) {
        if (protocol >= this.version || this.getBlock().getMinimumVersion() == 0 || this.getBlock() instanceof BlockUnknown) {
            return this.getLegacyId();
        }
        return this.getBlock().getAlternateBlock(protocol).getLegacyId();
    }

    public int getLegacyId() {
        if (this.legacyId != -1) {
            return this.legacyId;
        }

        int id = BlockStateMapping.get().getLegacyId(this.runtimeId);
        if (this.version == BlockStateMapping.get().getVersion()) {
            // Cache legacyId only if the mapping is same version,
            // plugins might be passing custom states
            // with unknown legacyId to vanilla mapping
            this.legacyId = id;
        }
        return id;
    }
}
