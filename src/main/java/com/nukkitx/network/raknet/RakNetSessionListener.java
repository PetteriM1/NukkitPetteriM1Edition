package com.nukkitx.network.raknet;

import com.nukkitx.network.util.DisconnectReason;
import io.netty.buffer.ByteBuf;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface RakNetSessionListener {

    void onDirect(ByteBuf buf);

    void onDisconnect(DisconnectReason reason);

    void onEncapsulated(EncapsulatedPacket packet);

    void onSessionChangeState(RakNetState state);
}
