package cn.nukkit.level.util;

import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.function.IntConsumer;

public class PalettedBlockStorage {

    private static final int SIZE = 4096;

    private final IntList palette;
    private BitArray bitArray;

    public PalettedBlockStorage() {
        this(0);
    }

    public PalettedBlockStorage(int protocol) {
        this(BitArrayVersion.V2, protocol);
    }

    public PalettedBlockStorage(BitArrayVersion version) {
        this(version, 0);
    }

    public PalettedBlockStorage(BitArrayVersion version, int protocol) {
        this.bitArray = version.createPalette();
        this.palette = new IntArrayList(16);

        // Air is at the start of every palette.
        if (protocol >= ProtocolInfo.v1_16_100) {
            this.palette.add(GlobalBlockPalette.getOrCreateRuntimeId(protocol, 0));
        } else {
            this.palette.add(0);
        }
    }

    private PalettedBlockStorage(BitArray bitArray, IntList palette) {
        this.palette = palette;
        this.bitArray = bitArray;
    }

    private int getPaletteHeader(BitArrayVersion version) {
        return (version.getId() << 1) | 1;
    }

    private int getPaletteHeader(BitArrayVersion version, boolean runtime) {
        return (version.getId() << 1) | (runtime ? 1 : 0);
    }

    public void setBlock(int index, int runtimeId) {
        try {
            int id = this.idFor(runtimeId);
            this.bitArray.set(index, id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unable to set block runtime ID: " + runtimeId + ", palette: " + palette, e);
        }
    }

    public void writeTo(int protocol, BinaryStream stream) {
        stream.putByte((byte) getPaletteHeader(bitArray.getVersion()));

        for (int word : bitArray.getWords()) {
            stream.putLInt(word);
        }

        stream.putVarInt(palette.size());
        palette.forEach((IntConsumer) stream::putVarInt);
    }

    private void onResize(BitArrayVersion version) {
        BitArray newBitArray = version.createPalette();

        for (int i = 0; i < SIZE; i++) {
            newBitArray.set(i, this.bitArray.get(i));
        }
        this.bitArray = newBitArray;
    }

    private int idFor(int runtimeId) {
        int index = this.palette.indexOf(runtimeId);
        if (index != -1) {
            return index;
        }

        index = this.palette.size();
        BitArrayVersion version = this.bitArray.getVersion();
        if (index > version.getMaxEntryValue()) {
            BitArrayVersion next = version.next();
            if (next != null) {
                this.onResize(next);
            }
        }
        this.palette.add(runtimeId);
        return index;
    }

    public boolean isEmpty() {
        if (this.palette.size() == 1) {
            return true;
        }
        for (int word : this.bitArray.getWords()) {
            if (Integer.toUnsignedLong(word) != 0L) {
                return false;
            }
        }
        return true;
    }

    public PalettedBlockStorage copy() {
        return new PalettedBlockStorage(this.bitArray.copy(), new IntArrayList(this.palette));
    }
}
