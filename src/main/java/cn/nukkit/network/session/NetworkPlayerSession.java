package cn.nukkit.network.session;

import cn.nukkit.Player;
import cn.nukkit.network.CompressionProvider;
import cn.nukkit.network.protocol.DataPacket;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public interface NetworkPlayerSession {

    void disconnect(String reason);

    CompressionProvider getCompression();

    void setCompression(CompressionProvider compression);

    default long getPing() {
        return -1;
    }

    Player getPlayer();

    void sendImmediatePacket(DataPacket packet, Runnable callback);

    void sendPacket(DataPacket packet);

    default void setEncryption(SecretKey encryptionKey, Cipher encryptionCipher, Cipher decryptionCipher) {

    }
}
