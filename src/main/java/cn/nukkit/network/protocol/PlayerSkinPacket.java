package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import lombok.ToString;

import java.util.UUID;

@ToString
public class PlayerSkinPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_SKIN_PACKET;

    public UUID uuid;
    public Skin skin;
    public String newSkinName;
    public String oldSkinName;
    public boolean premium;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        uuid = getUUID();
        skin = getSkin(protocol);
        newSkinName = getString();
        oldSkinName = getString();
        if (protocol < ProtocolInfo.v1_26_40 && !feof()) {
            getBoolean(); // skin.setTrusted(getBoolean());
        }
        skin.setTrusted(false); // Don't trust player skins
    }

    @Override
    public void encode() {
        reset();
        putUUID(uuid);
        putSkin(protocol, skin);
        putString(newSkinName);
        putString(oldSkinName);
        if (protocol < ProtocolInfo.v1_26_40 && protocol >= ProtocolInfo.v1_14_60) {
            putBoolean(skin.isTrusted());
        }
    }
}