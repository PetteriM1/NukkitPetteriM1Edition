/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LittleEndianByteBufOutputStream
extends ByteBufOutputStream {
    private final ByteBuf a;

    public LittleEndianByteBufOutputStream(ByteBuf byteBuf) {
        super(byteBuf);
        this.a = byteBuf;
    }

    @Override
    public void writeChar(int n) throws IOException {
        this.a.writeChar(Character.reverseBytes((char)n));
    }

    @Override
    public void writeDouble(double d2) throws IOException {
        this.a.writeDoubleLE(d2);
    }

    @Override
    public void writeFloat(float f2) throws IOException {
        this.a.writeFloatLE(f2);
    }

    @Override
    public void writeShort(int n) throws IOException {
        this.a.writeShortLE(n);
    }

    @Override
    public void writeLong(long l) throws IOException {
        this.a.writeLongLE(l);
    }

    @Override
    public void writeInt(int n) throws IOException {
        this.a.writeIntLE(n);
    }

    @Override
    public void writeUTF(String string) throws IOException {
        byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
        this.writeShort(byArray.length);
        this.write(byArray);
    }
}

