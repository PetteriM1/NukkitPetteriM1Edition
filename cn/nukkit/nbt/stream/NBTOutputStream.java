/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.stream;

import cn.nukkit.utils.VarInt;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class NBTOutputStream
implements DataOutput,
AutoCloseable {
    private final DataOutputStream b;
    private final ByteOrder a;
    private final boolean c;

    public NBTOutputStream(OutputStream outputStream) {
        this(outputStream, ByteOrder.BIG_ENDIAN);
    }

    public NBTOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
        this(outputStream, byteOrder, false);
    }

    public NBTOutputStream(OutputStream outputStream, ByteOrder byteOrder, boolean bl) {
        this.b = outputStream instanceof DataOutputStream ? (DataOutputStream)outputStream : new DataOutputStream(outputStream);
        this.a = byteOrder;
        this.c = bl;
    }

    public ByteOrder getEndianness() {
        return this.a;
    }

    public boolean isNetwork() {
        return this.c;
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        this.b.write(byArray);
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        this.b.write(byArray, n, n2);
    }

    @Override
    public void write(int n) throws IOException {
        this.b.write(n);
    }

    @Override
    public void writeBoolean(boolean bl) throws IOException {
        this.b.writeBoolean(bl);
    }

    @Override
    public void writeByte(int n) throws IOException {
        this.b.writeByte(n);
    }

    @Override
    public void writeShort(int n) throws IOException {
        if (this.a == ByteOrder.LITTLE_ENDIAN) {
            n = Integer.reverseBytes(n) >> 16;
        }
        this.b.writeShort(n);
    }

    @Override
    public void writeChar(int n) throws IOException {
        if (this.a == ByteOrder.LITTLE_ENDIAN) {
            n = Character.reverseBytes((char)n);
        }
        this.b.writeChar(n);
    }

    @Override
    public void writeInt(int n) throws IOException {
        if (this.c) {
            VarInt.writeVarInt(this.b, n);
        } else {
            if (this.a == ByteOrder.LITTLE_ENDIAN) {
                n = Integer.reverseBytes(n);
            }
            this.b.writeInt(n);
        }
    }

    @Override
    public void writeLong(long l) throws IOException {
        if (this.c) {
            VarInt.writeVarLong(this.b, l);
        } else {
            if (this.a == ByteOrder.LITTLE_ENDIAN) {
                l = Long.reverseBytes(l);
            }
            this.b.writeLong(l);
        }
    }

    @Override
    public void writeFloat(float f2) throws IOException {
        int n = Float.floatToIntBits(f2);
        if (this.a == ByteOrder.LITTLE_ENDIAN) {
            n = Integer.reverseBytes(n);
        }
        this.b.writeInt(n);
    }

    @Override
    public void writeDouble(double d2) throws IOException {
        long l = Double.doubleToLongBits(d2);
        if (this.a == ByteOrder.LITTLE_ENDIAN) {
            l = Long.reverseBytes(l);
        }
        this.b.writeLong(l);
    }

    @Override
    public void writeBytes(String string) throws IOException {
        this.b.writeBytes(string);
    }

    @Override
    public void writeChars(String string) throws IOException {
        this.b.writeChars(string);
    }

    @Override
    public void writeUTF(String string) throws IOException {
        byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
        if (this.c) {
            VarInt.writeUnsignedVarInt(this.b, (long)byArray.length);
        } else {
            this.writeShort(byArray.length);
        }
        this.b.write(byArray);
    }

    @Override
    public void close() throws IOException {
        this.b.close();
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

