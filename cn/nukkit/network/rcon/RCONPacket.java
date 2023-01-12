/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.rcon;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class RCONPacket {
    private final int c;
    private final int a;
    private final byte[] b;

    public RCONPacket(int n, int n2, byte[] byArray) {
        this.c = n;
        this.a = n2;
        this.b = byArray;
    }

    public RCONPacket(ByteBuffer byteBuffer) throws IOException {
        int n = byteBuffer.getInt();
        if (n > 250000) {
            throw new RuntimeException("Too big RCON packet: " + n);
        }
        this.c = byteBuffer.getInt();
        this.a = byteBuffer.getInt();
        this.b = new byte[n - 10];
        byteBuffer.get(this.b);
        byteBuffer.get(new byte[2]);
    }

    public ByteBuffer toBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.b.length + 14);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(this.b.length + 10);
        byteBuffer.putInt(this.c);
        byteBuffer.putInt(this.a);
        byteBuffer.put(this.b);
        byteBuffer.put((byte)0);
        byteBuffer.put((byte)0);
        byteBuffer.flip();
        return byteBuffer;
    }

    public int getId() {
        return this.c;
    }

    public int getType() {
        return this.a;
    }

    public byte[] getPayload() {
        return this.b;
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

