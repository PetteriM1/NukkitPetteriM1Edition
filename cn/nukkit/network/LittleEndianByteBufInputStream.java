/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.IOException;

public class LittleEndianByteBufInputStream
extends ByteBufInputStream {
    private final ByteBuf a;

    public LittleEndianByteBufInputStream(ByteBuf byteBuf) {
        super(byteBuf);
        this.a = byteBuf;
    }

    @Override
    public char readChar() throws IOException {
        return Character.reverseBytes(this.a.readChar());
    }

    @Override
    public double readDouble() throws IOException {
        return this.a.readDoubleLE();
    }

    @Override
    public float readFloat() throws IOException {
        return this.a.readFloatLE();
    }

    @Override
    public short readShort() throws IOException {
        return this.a.readShortLE();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return this.a.readUnsignedShortLE();
    }

    @Override
    public long readLong() throws IOException {
        return this.a.readLongLE();
    }

    @Override
    public int readInt() throws IOException {
        return this.a.readIntLE();
    }
}

