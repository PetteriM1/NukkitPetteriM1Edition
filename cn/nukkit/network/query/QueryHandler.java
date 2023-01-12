/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.query;

import cn.nukkit.Server;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class QueryHandler {
    public static final byte HANDSHAKE = 9;
    public static final byte STATISTICS = 0;
    private final Server c = Server.getInstance();
    private byte[] b;
    private byte[] a;
    private byte[] e;
    private byte[] f;
    private long d;

    public QueryHandler() {
        this.regenerateToken();
        this.b = this.a;
        this.regenerateInfo();
    }

    public void regenerateInfo() {
        QueryRegenerateEvent queryRegenerateEvent = this.c.getQueryInformation();
        this.e = queryRegenerateEvent.getLongQuery();
        this.f = queryRegenerateEvent.getShortQuery();
        this.d = System.currentTimeMillis() + (long)queryRegenerateEvent.getTimeout();
    }

    public void regenerateToken() {
        this.b = this.a;
        byte[] byArray = new byte[16];
        for (int k = 0; k < 16; ++k) {
            byArray[k] = (byte)Utils.random.nextInt(255);
        }
        this.a = byArray;
    }

    public static byte[] getTokenString(String string, InetAddress inetAddress) {
        return QueryHandler.getTokenString(string.getBytes(StandardCharsets.UTF_8), inetAddress);
    }

    public static byte[] getTokenString(byte[] byArray, InetAddress inetAddress) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(inetAddress.toString().getBytes(StandardCharsets.UTF_8));
            messageDigest.update(byArray);
            return Arrays.copyOf(messageDigest.digest(), 4);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return ByteBuffer.allocate(4).putInt(Utils.random.nextInt()).array();
        }
    }

    public void handle(InetSocketAddress inetSocketAddress, ByteBuf byteBuf) {
        short s2 = byteBuf.readUnsignedByte();
        int n = byteBuf.readInt();
        switch (s2) {
            case 9: {
                ByteBuf byteBuf2 = ByteBufAllocator.DEFAULT.ioBuffer(10);
                byteBuf2.writeByte(9);
                byteBuf2.writeInt(n);
                byteBuf2.writeBytes(QueryHandler.getTokenString(this.a, inetSocketAddress.getAddress()));
                byteBuf2.writeByte(0);
                this.c.getNetwork().sendPacket(inetSocketAddress, byteBuf2);
                break;
            }
            case 0: {
                byte[] byArray = new byte[4];
                byteBuf.readBytes(byArray);
                if (!Arrays.equals(byArray, QueryHandler.getTokenString(this.a, inetSocketAddress.getAddress())) && !Arrays.equals(byArray, QueryHandler.getTokenString(this.b, inetSocketAddress.getAddress()))) break;
                if (this.d < System.currentTimeMillis()) {
                    this.regenerateInfo();
                }
                ByteBuf byteBuf3 = ByteBufAllocator.DEFAULT.directBuffer(64);
                byteBuf3.writeByte(0);
                byteBuf3.writeInt(n);
                if (byteBuf.readableBytes() == 8) {
                    byteBuf3.writeBytes(this.e);
                } else {
                    byteBuf3.writeBytes(this.f);
                }
                this.c.getNetwork().sendPacket(inetSocketAddress, byteBuf3);
            }
        }
    }
}

