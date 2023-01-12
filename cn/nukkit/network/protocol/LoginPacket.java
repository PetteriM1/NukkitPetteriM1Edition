/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.b;
import cn.nukkit.utils.PersonaPiece;
import cn.nukkit.utils.PersonaPieceTint;
import cn.nukkit.utils.SerializedImage;
import cn.nukkit.utils.SkinAnimation;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LoginPacket
extends DataPacket {
    public static final byte NETWORK_ID = 1;
    public String username;
    private int f;
    public UUID clientUUID;
    public long clientId;
    public Skin skin;
    public boolean invalidData;
    private static final Gson e = new Gson();

    @Override
    public byte pid() {
        return 1;
    }

    @Override
    public void decode() {
        block1: {
            this.f = this.getInt();
            if (this.f == 0) {
                this.setOffset(this.getOffset() + 2);
                this.f = this.getInt();
            }
            if (!ProtocolInfo.SUPPORTED_PROTOCOLS.contains(this.f)) break block1;
            this.setBuffer(this.getByteArray(), 0);
            this.c();
            this.d();
        }
    }

    @Override
    public void encode() {
        this.a();
    }

    public int getProtocol() {
        return this.f;
    }

    private void c() {
        String string;
        Map map;
        int n = this.getLInt();
        if (n > 0x300000) {
            if (Server.getInstance().doNotLimitSkinGeometry) {
                Server.getInstance().getLogger().warning("Received large login chain data (skin?) but do-not-limit-skin-geometry is enabled: " + n);
                if (n > 0xA00000) {
                    throw new IllegalArgumentException("10 MB hard limit! The chain data is too big!");
                }
            } else {
                throw new IllegalArgumentException("The chain data is too big: " + n);
            }
        }
        if ((map = (Map)e.fromJson(string = new String(this.get(n), StandardCharsets.UTF_8), new MapTypeToken(null).getType())).isEmpty() || !map.containsKey("chain") || ((List)map.get("chain")).isEmpty()) {
            return;
        }
        long l = System.currentTimeMillis();
        for (String string2 : (List)map.get("chain")) {
            JsonObject jsonObject;
            JsonObject jsonObject2 = LoginPacket.a(string2);
            if (jsonObject2 == null || !jsonObject2.has("extraData")) continue;
            if (jsonObject2.has("nbf") && jsonObject2.get("nbf").getAsLong() * 1000L > l + 60L) {
                this.invalidData = true;
                Server.getInstance().getLogger().debug("Auth: nbf");
            }
            if (jsonObject2.has("exp") && jsonObject2.get("exp").getAsLong() * 1000L < l - 60L) {
                this.invalidData = true;
                Server.getInstance().getLogger().debug("Auth: exp");
            }
            if ((jsonObject = jsonObject2.get("extraData").getAsJsonObject()).has("displayName")) {
                this.username = jsonObject.get("displayName").getAsString();
            }
            if (!jsonObject.has("identity")) continue;
            this.clientUUID = UUID.fromString(jsonObject.get("identity").getAsString());
        }
    }

    private void d() {
        JsonObject jsonObject;
        int n = this.getLInt();
        if (n > 0x300000) {
            if (Server.getInstance().doNotLimitSkinGeometry) {
                Server.getInstance().getLogger().warning("Received large skin data but do-not-limit-skin-geometry is enabled: " + n);
                if (n > 0xA00000) {
                    throw new IllegalArgumentException("10 MB hard limit! The skin data is too big!");
                }
            } else {
                throw new IllegalArgumentException("The chain data is too big: " + n);
            }
        }
        if ((jsonObject = LoginPacket.a(new String(this.get(n), StandardCharsets.UTF_8))) == null) {
            throw new RuntimeException("Invalid null skin token");
        }
        if (jsonObject.has("ClientRandomId")) {
            this.clientId = jsonObject.get("ClientRandomId").getAsLong();
        }
        this.skin = new Skin();
        this.skin.setTrusted(false);
        if (jsonObject.has("SkinId")) {
            this.skin.setSkinId(jsonObject.get("SkinId").getAsString());
        } else {
            this.skin.setSkinId(UUID.randomUUID().toString());
        }
        if (this.f < 388) {
            if (jsonObject.has("SkinData")) {
                this.skin.setSkinData(Base64.getDecoder().decode(jsonObject.get("SkinData").getAsString()));
            }
            if (jsonObject.has("CapeData")) {
                this.skin.setCapeData(Base64.getDecoder().decode(jsonObject.get("CapeData").getAsString()));
            }
            if (jsonObject.has("SkinGeometryName")) {
                this.skin.setGeometryName(jsonObject.get("SkinGeometryName").getAsString());
            }
            if (jsonObject.has("SkinGeometry")) {
                this.skin.setGeometryData(new String(Base64.getDecoder().decode(jsonObject.get("SkinGeometry").getAsString()), StandardCharsets.UTF_8));
            }
        } else {
            if (jsonObject.has("PlayFabId")) {
                this.skin.setPlayFabId(jsonObject.get("PlayFabId").getAsString());
            }
            if (jsonObject.has("CapeId")) {
                this.skin.setCapeId(jsonObject.get("CapeId").getAsString());
            }
            this.skin.setSkinData(LoginPacket.a(jsonObject, "Skin"));
            this.skin.setCapeData(LoginPacket.a(jsonObject, "Cape"));
            if (jsonObject.has("PremiumSkin")) {
                this.skin.setPremium(jsonObject.get("PremiumSkin").getAsBoolean());
            }
            if (jsonObject.has("PersonaSkin")) {
                this.skin.setPersona(jsonObject.get("PersonaSkin").getAsBoolean());
            }
            if (jsonObject.has("CapeOnClassicSkin")) {
                this.skin.setCapeOnClassic(jsonObject.get("CapeOnClassicSkin").getAsBoolean());
            }
            if (jsonObject.has("SkinResourcePatch")) {
                this.skin.setSkinResourcePatch(new String(Base64.getDecoder().decode(jsonObject.get("SkinResourcePatch").getAsString()), StandardCharsets.UTF_8));
            }
            if (jsonObject.has("SkinGeometryData")) {
                this.skin.setGeometryData(new String(Base64.getDecoder().decode(jsonObject.get("SkinGeometryData").getAsString()), StandardCharsets.UTF_8));
            }
            if (jsonObject.has("SkinAnimationData")) {
                this.skin.setAnimationData(new String(Base64.getDecoder().decode(jsonObject.get("SkinAnimationData").getAsString()), StandardCharsets.UTF_8));
            }
            if (jsonObject.has("AnimatedImageData")) {
                for (JsonElement jsonElement : jsonObject.get("AnimatedImageData").getAsJsonArray()) {
                    this.skin.getAnimations().add(LoginPacket.a(this.f, jsonElement.getAsJsonObject()));
                }
            }
            if (jsonObject.has("SkinColor")) {
                this.skin.setSkinColor(jsonObject.get("SkinColor").getAsString());
            }
            if (jsonObject.has("ArmSize")) {
                this.skin.setArmSize(jsonObject.get("ArmSize").getAsString());
            }
            if (jsonObject.has("PersonaPieces")) {
                for (JsonElement jsonElement : jsonObject.get("PersonaPieces").getAsJsonArray()) {
                    this.skin.getPersonaPieces().add(LoginPacket.a(jsonElement.getAsJsonObject()));
                }
            }
            if (jsonObject.has("PieceTintColors")) {
                for (JsonElement jsonElement : jsonObject.get("PieceTintColors").getAsJsonArray()) {
                    this.skin.getTintColors().add(LoginPacket.getTint(jsonElement.getAsJsonObject()));
                }
            }
        }
    }

    private static JsonObject a(String string) {
        String[] stringArray = string.split("\\.");
        if (stringArray.length < 2) {
            return null;
        }
        return e.fromJson(new String(Base64.getDecoder().decode(stringArray[1]), StandardCharsets.UTF_8), JsonObject.class);
    }

    private static SkinAnimation a(int n, JsonObject jsonObject) {
        float f2 = jsonObject.get("Frames").getAsFloat();
        int n2 = jsonObject.get("Type").getAsInt();
        byte[] byArray = Base64.getDecoder().decode(jsonObject.get("Image").getAsString());
        int n3 = jsonObject.get("ImageWidth").getAsInt();
        int n4 = jsonObject.get("ImageHeight").getAsInt();
        int n5 = n >= 419 ? jsonObject.get("AnimationExpression").getAsInt() : 0;
        return new SkinAnimation(new SerializedImage(n3, n4, byArray), n2, f2, n5);
    }

    private static SerializedImage a(JsonObject jsonObject, String string) {
        if (jsonObject.has(string + "Data")) {
            byte[] byArray = Base64.getDecoder().decode(jsonObject.get(string + "Data").getAsString());
            if (jsonObject.has(string + "ImageHeight") && jsonObject.has(string + "ImageWidth")) {
                int n = jsonObject.get(string + "ImageWidth").getAsInt();
                int n2 = jsonObject.get(string + "ImageHeight").getAsInt();
                return new SerializedImage(n, n2, byArray);
            }
            return SerializedImage.fromLegacy(byArray);
        }
        return SerializedImage.EMPTY;
    }

    private static PersonaPiece a(JsonObject jsonObject) {
        String string = jsonObject.get("PieceId").getAsString();
        String string2 = jsonObject.get("PieceType").getAsString();
        String string3 = jsonObject.get("PackId").getAsString();
        boolean bl = jsonObject.get("IsDefault").getAsBoolean();
        String string4 = jsonObject.get("ProductId").getAsString();
        return new PersonaPiece(string, string2, string3, bl, string4);
    }

    public static PersonaPieceTint getTint(JsonObject jsonObject) {
        String string = jsonObject.get("PieceType").getAsString();
        ArrayList<String> arrayList = new ArrayList<String>();
        for (JsonElement jsonElement : jsonObject.get("Colors").getAsJsonArray()) {
            arrayList.add(jsonElement.getAsString());
        }
        return new PersonaPieceTint(string, arrayList);
    }

    public String toString() {
        return "LoginPacket(username=" + this.username + ", protocol_=" + this.f + ", clientUUID=" + this.clientUUID + ", clientId=" + this.clientId + ", skin=" + this.skin + ", invalidData=" + this.invalidData + ")";
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }

    private static class MapTypeToken
    extends TypeToken<Map<String, List<String>>> {
        private MapTypeToken() {
        }

        /* synthetic */ MapTypeToken(b b2) {
            this();
        }
    }
}

