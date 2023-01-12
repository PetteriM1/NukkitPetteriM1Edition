/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;

public class ServerToClientHandshakePacket
extends DataPacket {
    public static final byte NETWORK_ID = 3;
    public String publicKey;
    public String serverToken;
    public String privateKey;

    @Override
    public byte pid() {
        return 3;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "ServerToClientHandshakePacket(publicKey=" + this.publicKey + ", serverToken=" + this.serverToken + ", privateKey=" + this.privateKey + ")";
    }
}

