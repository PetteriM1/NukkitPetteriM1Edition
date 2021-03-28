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
        if (protocol < 388) {
            skin = new Skin();
            skin.setSkinId(getString());
            newSkinName = getString();
            oldSkinName = getString();
            skin.setSkinData(getByteArray());
            skin.setCapeData(getByteArray());
            skin.setGeometryName(getString());
            skin.setGeometryData(getString());
            if (protocol > 274) {
                premium = getBoolean();
            }
        } else {
            skin = getSkin(protocol);
            newSkinName = getString();
            oldSkinName = getString();
            if (!feof()) {
                skin.setTrusted(getBoolean());
            }
        }
    }

    @Override
    public void encode() {
        reset();
        putUUID(uuid);
        if (protocol < 388) {
            putString(skin.isLegacySlim ? "geometry.humanoid.customSlim" : "geometry.humanoid.custom");
            putString(newSkinName);
            putString(oldSkinName);
            putByteArray(skin.getSkinData().data);
            putByteArray(skin.getCapeData().data);
            putString(skin.isLegacySlim ? "geometry.humanoid.customSlim" : "geometry.humanoid.custom");
            putString(skin.getGeometryData());
            if (protocol > 274) {
                putBoolean(premium);
            }
        } else {
            putSkin(protocol, skin);
            putString(newSkinName);
            putString(oldSkinName);
            if (protocol >= ProtocolInfo.v1_14_60) {
                putBoolean(skin.isTrusted());
            }
        }
    }
}