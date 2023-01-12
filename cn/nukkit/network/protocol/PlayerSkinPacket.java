/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.network.protocol.DataPacket;
import java.util.UUID;

public class PlayerSkinPacket
extends DataPacket {
    public static final byte NETWORK_ID = 93;
    public UUID uuid;
    public Skin skin;
    public String newSkinName;
    public String oldSkinName;
    public boolean premium;

    @Override
    public byte pid() {
        return 93;
    }

    @Override
    public void decode() {
        this.uuid = this.getUUID();
        if (this.protocol < 388) {
            this.skin = new Skin();
            this.skin.setSkinId(this.getString());
            this.newSkinName = this.getString();
            this.oldSkinName = this.getString();
            this.skin.setSkinData(this.getByteArray());
            this.skin.setCapeData(this.getByteArray());
            this.skin.setGeometryName(this.getString());
            this.skin.setGeometryData(this.getString());
            if (this.protocol > 274) {
                this.premium = this.getBoolean();
            }
        } else {
            this.skin = this.getSkin(this.protocol);
            this.newSkinName = this.getString();
            this.oldSkinName = this.getString();
            if (!this.feof()) {
                this.getBoolean();
            }
            this.skin.setTrusted(false);
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putUUID(this.uuid);
        if (this.protocol < 388) {
            this.putString(this.skin.isLegacySlim ? "geometry.humanoid.customSlim" : "geometry.humanoid.custom");
            this.putString(this.newSkinName);
            this.putString(this.oldSkinName);
            this.putByteArray(this.skin.getSkinData().data);
            this.putByteArray(this.skin.getCapeData().data);
            this.putString(this.skin.isLegacySlim ? "geometry.humanoid.customSlim" : "geometry.humanoid.custom");
            this.putString(this.skin.getGeometryData());
            if (this.protocol > 274) {
                this.putBoolean(this.premium);
            }
        } else {
            this.putSkin(this.protocol, this.skin);
            this.putString(this.newSkinName);
            this.putString(this.oldSkinName);
            if (this.protocol >= 390) {
                this.putBoolean(this.skin.isTrusted());
            }
        }
    }

    public String toString() {
        return "PlayerSkinPacket(uuid=" + this.uuid + ", skin=" + this.skin + ", newSkinName=" + this.newSkinName + ", oldSkinName=" + this.oldSkinName + ", premium=" + this.premium + ")";
    }
}

