package cn.nukkit.network.protocol;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.utils.PersonaPiece;
import cn.nukkit.utils.PersonaPieceTint;
import cn.nukkit.utils.SerializedImage;
import cn.nukkit.utils.SkinAnimation;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.*;

@ToString
public class LoginPacket extends DataPacket {

    public String username;
    private int protocol_;
    public UUID clientUUID;
    public long clientId;
    public Skin skin;

    private static final Gson GSON = new Gson();

    @Override
    public byte pid() {
        return ProtocolInfo.LOGIN_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.protocol_ = this.getInt();
        if (protocol_ == 0) {
            setOffset(getOffset() + 2);
            this.protocol_ = getInt();
        }
        this.setBuffer(this.getByteArray(), 0);
        decodeChainData();
        decodeSkinData();
    }

    @Override
    public void encode(int protocolId) {
    }

    public int getProtocol() {
        return protocol_;
    }

    private void decodeChainData() {
        int size = this.getLInt();
        if (size > 3000000) {
            throw new IllegalArgumentException("The chain data is too big: " + size);
        }

        String data = new String(this.get(size), StandardCharsets.UTF_8);

        Map<String, List<String>> map = GSON.fromJson(data, new MapTypeToken().getType());
        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) return;
        for (String c : map.get("chain")) {
            JsonObject chainMap = decodeToken(c);
            if (chainMap == null) continue;
            if (chainMap.has("extraData")) {
                JsonObject extra = chainMap.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) this.username = extra.get("displayName").getAsString();
                if (extra.has("identity")) this.clientUUID = UUID.fromString(extra.get("identity").getAsString());
            }
        }
    }

    private void decodeSkinData() {
        int size = this.getLInt();
        if (size > 3000000) {
            throw new IllegalArgumentException("The skin data is too big: " + size);
        }

        String data = new String(this.get(size));
        JsonObject skinToken = decodeToken(data);

        if (skinToken == null) return;

        if (skinToken.has("ClientRandomId")) {
            this.clientId = skinToken.get("ClientRandomId").getAsLong();
        }

        skin = new Skin();

        if (skinToken.has("SkinId")) {
            skin.setSkinId(skinToken.get("SkinId").getAsString());
        }

        if (protocol_ < 388) {
            if (skinToken.has("SkinData")) {
                skin.setSkinData(Base64.getDecoder().decode(skinToken.get("SkinData").getAsString()));
            }

            if (skinToken.has("CapeData")) {
                skin.setCapeData(Base64.getDecoder().decode(skinToken.get("CapeData").getAsString()));
            }

            if (skinToken.has("SkinGeometryName")) {
                skin.setGeometryName(skinToken.get("SkinGeometryName").getAsString());
            }

            if (skinToken.has("SkinGeometry")) {
                skin.setGeometryData(new String(Base64.getDecoder().decode(skinToken.get("SkinGeometry").getAsString()), StandardCharsets.UTF_8));
            }
        } else {
            if (skinToken.has("CapeId")) {
                skin.setCapeId(skinToken.get("CapeId").getAsString());
            }

            skin.setSkinData(getImage(skinToken, "Skin"));
            skin.setCapeData(getImage(skinToken, "Cape"));

            if (skinToken.has("PremiumSkin")) {
                skin.setPremium(skinToken.get("PremiumSkin").getAsBoolean());
            }

            if (skinToken.has("PersonaSkin")) {
                skin.setPersona(skinToken.get("PersonaSkin").getAsBoolean());
            }

            if (skinToken.has("CapeOnClassicSkin")) {
                skin.setCapeOnClassic(skinToken.get("CapeOnClassicSkin").getAsBoolean());
            }

            if (skinToken.has("SkinResourcePatch")) {
                skin.setSkinResourcePatch(new String(Base64.getDecoder().decode(skinToken.get("SkinResourcePatch").getAsString()), StandardCharsets.UTF_8));
            }

            if (skinToken.has("SkinGeometryData")) {
                skin.setGeometryData(new String(Base64.getDecoder().decode(skinToken.get("SkinGeometryData").getAsString()), StandardCharsets.UTF_8));
            }

            if (skinToken.has("AnimationData")) {
                skin.setAnimationData(new String(Base64.getDecoder().decode(skinToken.get("AnimationData").getAsString()), StandardCharsets.UTF_8));
            }

            if (skinToken.has("AnimatedImageData")) {
                for (JsonElement element : skinToken.get("AnimatedImageData").getAsJsonArray()) {
                    skin.getAnimations().add(getAnimation(element.getAsJsonObject()));
                }
            }

            if (skinToken.has("SkinColor")) {
                skin.setSkinColor(skinToken.get("SkinColor").getAsString());
            }

            if (skinToken.has("ArmSize")) {
                skin.setArmSize(skinToken.get("ArmSize").getAsString());
            }

            if (skinToken.has("PersonaPieces")) {
                for (JsonElement object : skinToken.get("PersonaPieces").getAsJsonArray()) {
                    skin.getPersonaPieces().add(getPersonaPiece(object.getAsJsonObject()));
                }
            }

            if (skinToken.has("PieceTintColors")) {
                for (JsonElement object : skinToken.get("PieceTintColors").getAsJsonArray()) {
                    skin.getTintColors().add(getTint(object.getAsJsonObject()));
                }
            }
        }
    }

    private static JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) return null;
        return GSON.fromJson(new String(Base64.getDecoder().decode(base[1]), StandardCharsets.UTF_8), JsonObject.class);
    }

    private static SkinAnimation getAnimation(JsonObject element) {
        float frames = element.get("Frames").getAsFloat();
        int type = element.get("Type").getAsInt();
        byte[] data = Base64.getDecoder().decode(element.get("Image").getAsString());
        int width = element.get("ImageWidth").getAsInt();
        int height = element.get("ImageHeight").getAsInt();
        //int expression = element.get("AnimationExpression").getAsInt(); //TODO: Make this work with multiversion
        return new SkinAnimation(new SerializedImage(width, height, data), type, frames, 0);
    }

    private static SerializedImage getImage(JsonObject token, String name) {
        if (token.has(name + "Data")) {
            byte[] skinImage = Base64.getDecoder().decode(token.get(name + "Data").getAsString());
            if (token.has(name + "ImageHeight") && token.has(name + "ImageWidth")) {
                int width = token.get(name + "ImageWidth").getAsInt();
                int height = token.get(name + "ImageHeight").getAsInt();
                return new SerializedImage(width, height, skinImage);
            } else {
                return SerializedImage.fromLegacy(skinImage);
            }
        }
        return SerializedImage.EMPTY;
    }

    private static PersonaPiece getPersonaPiece(JsonObject object) {
        String pieceId = object.get("PieceId").getAsString();
        String pieceType = object.get("PieceType").getAsString();
        String packId = object.get("PackId").getAsString();
        boolean isDefault = object.get("IsDefault").getAsBoolean();
        String productId = object.get("ProductId").getAsString();
        return new PersonaPiece(pieceId, pieceType, packId, isDefault, productId);
    }

    public static PersonaPieceTint getTint(JsonObject object) {
        String pieceType = object.get("PieceType").getAsString();
        List<String> colors = new ArrayList<>();
        for (JsonElement element : object.get("Colors").getAsJsonArray()) {
            colors.add(element.getAsString()); // remove #
        }
        return new PersonaPieceTint(pieceType, colors);
    }

    private static class MapTypeToken extends TypeToken<Map<String, List<String>>> {
    }
}