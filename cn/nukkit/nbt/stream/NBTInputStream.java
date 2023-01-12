/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.nbt.stream;

import cn.nukkit.utils.VarInt;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class NBTInputStream
implements DataInput,
AutoCloseable {
    private final DataInputStream a;
    private final ByteOrder b;
    private final boolean c;

    public NBTInputStream(InputStream inputStream) {
        this(inputStream, ByteOrder.BIG_ENDIAN);
    }

    public NBTInputStream(InputStream inputStream, ByteOrder byteOrder) {
        this(inputStream, byteOrder, false);
    }

    public NBTInputStream(InputStream inputStream, ByteOrder byteOrder, boolean bl) {
        this.a = inputStream instanceof DataInputStream ? (DataInputStream)inputStream : new DataInputStream(inputStream);
        this.b = byteOrder;
        this.c = bl;
    }

    public ByteOrder getEndianness() {
        return this.b;
    }

    public boolean isNetwork() {
        return this.c;
    }

    @Override
    public void readFully(byte[] byArray) throws IOException {
        this.a.readFully(byArray);
    }

    @Override
    public void readFully(byte[] byArray, int n, int n2) throws IOException {
        this.a.readFully(byArray, n, n2);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return this.a.skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return this.a.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return this.a.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return this.a.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        short s2 = this.a.readShort();
        if (this.b == ByteOrder.LITTLE_ENDIAN) {
            s2 = Short.reverseBytes(s2);
        }
        return s2;
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return this.readShort() & 0xFFFF;
    }

    @Override
    public char readChar() throws IOException {
        char c2 = this.a.readChar();
        if (this.b == ByteOrder.LITTLE_ENDIAN) {
            c2 = Character.reverseBytes(c2);
        }
        return c2;
    }

    @Override
    public int readInt() throws IOException {
        if (this.c) {
            return VarInt.readVarInt(this.a);
        }
        int n = this.a.readInt();
        if (this.b == ByteOrder.LITTLE_ENDIAN) {
            n = Integer.reverseBytes(n);
        }
        return n;
    }

    @Override
    public long readLong() throws IOException {
        if (this.c) {
            return VarInt.readVarLong(this.a);
        }
        long l = this.a.readLong();
        if (this.b == ByteOrder.LITTLE_ENDIAN) {
            l = Long.reverseBytes(l);
        }
        return l;
    }

    @Override
    public float readFloat() throws IOException {
        int n = this.a.readInt();
        if (this.b == ByteOrder.LITTLE_ENDIAN) {
            n = Integer.reverseBytes(n);
        }
        return Float.intBitsToFloat(n);
    }

    @Override
    public double readDouble() throws IOException {
        long l = this.a.readLong();
        if (this.b == ByteOrder.LITTLE_ENDIAN) {
            l = Long.reverseBytes(l);
        }
        return Double.longBitsToDouble(l);
    }

    @Override
    public String readLine() throws IOException {
        return this.a.readLine();
    }

    @Override
    public String readUTF() throws IOException {
        int n = this.c ? (int)VarInt.readUnsignedVarInt(this.a) : this.readUnsignedShort();
        byte[] byArray = new byte[n];
        this.a.read(byArray);
        return new String(byArray, StandardCharsets.UTF_8);
    }

    public int available() throws IOException {
        return this.a.available();
    }

    @Override
    public void close() throws IOException {
        this.a.close();
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

