/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.BinaryStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class VarInt {
    private VarInt() {
    }

    public static long encodeZigZag32(int n) {
        return (long)(n << 1 ^ n >> 31) & 0xFFFFFFFFL;
    }

    public static int decodeZigZag32(long l) {
        return (int)(l >> 1) ^ -((int)(l & 1L));
    }

    public static long encodeZigZag64(long l) {
        return l << 1 ^ l >> 63;
    }

    public static long decodeZigZag64(long l) {
        return l >>> 1 ^ -(l & 1L);
    }

    private static long a(BinaryStream binaryStream, int n) {
        int n2;
        long l = 0L;
        int n3 = 0;
        while (((n2 = binaryStream.getByte()) & 0x80) == 128) {
            l |= (long)(n2 & 0x7F) << n3++ * 7;
            if (n3 < n) continue;
            throw new IllegalArgumentException("VarLong too big");
        }
        return l | (long)(n2 & 0x7F) << n3 * 7;
    }

    private static long a(InputStream inputStream, int n) throws IOException {
        int n2;
        long l = 0L;
        int n3 = 0;
        while (((n2 = inputStream.read()) & 0x80) == 128) {
            l |= (long)(n2 & 0x7F) << n3++ * 7;
            if (n3 < n) continue;
            throw new IllegalArgumentException("VarLong too big");
        }
        return l | (long)(n2 & 0x7F) << n3 * 7;
    }

    public static int readVarInt(BinaryStream binaryStream) {
        return VarInt.decodeZigZag32(VarInt.readUnsignedVarInt(binaryStream));
    }

    public static int readVarInt(InputStream inputStream) throws IOException {
        return VarInt.decodeZigZag32(VarInt.readUnsignedVarInt(inputStream));
    }

    public static long readUnsignedVarInt(BinaryStream binaryStream) {
        return VarInt.a(binaryStream, 5);
    }

    public static long readUnsignedVarInt(InputStream inputStream) throws IOException {
        return VarInt.a(inputStream, 5);
    }

    public static long readVarLong(BinaryStream binaryStream) {
        return VarInt.decodeZigZag64(VarInt.readUnsignedVarLong(binaryStream));
    }

    public static long readVarLong(InputStream inputStream) throws IOException {
        return VarInt.decodeZigZag64(VarInt.readUnsignedVarLong(inputStream));
    }

    public static long readUnsignedVarLong(BinaryStream binaryStream) {
        return VarInt.a(binaryStream, 10);
    }

    public static long readUnsignedVarLong(InputStream inputStream) throws IOException {
        return VarInt.a(inputStream, 10);
    }

    private static void a(BinaryStream binaryStream, long l) {
        do {
            byte by = (byte)(l & 0x7FL);
            if ((l >>>= 7) != 0L) {
                by = (byte)(by | 0x80);
            }
            binaryStream.putByte(by);
        } while (l != 0L);
    }

    private static void a(OutputStream outputStream, long l) throws IOException {
        do {
            byte by = (byte)(l & 0x7FL);
            if ((l >>>= 7) != 0L) {
                by = (byte)(by | 0x80);
            }
            outputStream.write(by);
        } while (l != 0L);
    }

    public static void writeVarInt(BinaryStream binaryStream, int n) {
        VarInt.writeUnsignedVarInt(binaryStream, VarInt.encodeZigZag32(n));
    }

    public static void writeVarInt(OutputStream outputStream, int n) throws IOException {
        VarInt.writeUnsignedVarInt(outputStream, VarInt.encodeZigZag32(n));
    }

    public static void writeUnsignedVarInt(BinaryStream binaryStream, long l) {
        VarInt.a(binaryStream, l);
    }

    public static void writeUnsignedVarInt(OutputStream outputStream, long l) throws IOException {
        VarInt.a(outputStream, l);
    }

    public static void writeVarLong(BinaryStream binaryStream, long l) {
        VarInt.writeUnsignedVarLong(binaryStream, VarInt.encodeZigZag64(l));
    }

    public static void writeVarLong(OutputStream outputStream, long l) throws IOException {
        VarInt.writeUnsignedVarLong(outputStream, VarInt.encodeZigZag64(l));
    }

    public static void writeUnsignedVarLong(BinaryStream binaryStream, long l) {
        VarInt.a(binaryStream, l);
    }

    public static void writeUnsignedVarLong(OutputStream outputStream, long l) throws IOException {
        VarInt.a(outputStream, l);
    }
}

