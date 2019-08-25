package com.nukkitx.network;

import io.netty.buffer.ByteBuf;

public interface NetworkPacket {

    void encode(ByteBuf buffer);

    void decode(ByteBuf buffer);
}
