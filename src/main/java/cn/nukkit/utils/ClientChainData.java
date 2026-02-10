package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.network.encryption.EncryptionUtils;
import cn.nukkit.network.protocol.LoginPacket;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.ECDSAVerifier;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * ClientChainData is a container of chain data sent from clients.
 * <p>
 * Device information such as client UUID, xuid and serverAddress, can be
 * read from instances of this object.
 * <p>
 * To get chain data, you can use player.getLoginChainData() or read(loginPacket)
 * <p>
 * ===============
 *
 * @author boybook
 * Nukkit Project
 * ===============
 */
public final class ClientChainData implements LoginChainData {

    private static final Gson GSON = new Gson();
    private boolean xboxAuthed;
    public final static int UI_PROFILE_CLASSIC = 0;
    public final static int UI_PROFILE_POCKET = 1;
    /// ////////////////////////////////////////////////////////////////////////

    private String username;
    private UUID clientUUID;
    private String xuid;
    private String identityPublicKey;
    private long clientId;
    private String serverAddress;
    private String deviceModel;
    private int deviceOS;
    private String deviceId;
    private String gameVersion;
    private int guiScale;
    private String languageCode;
    private int currentInputMode;
    private int defaultInputMode;
    private int UIProfile;
    private String capeData;
    private String titleId;
    private JsonObject rawData;
    private final BinaryStream bs = new BinaryStream();

    private ClientChainData(byte[] buffer) {
        bs.setBuffer(buffer, 0);
        decodeChainData();
        decodeSkinData();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Override

    private static class MapTypeToken extends TypeToken<Map<String, Object>> {
    }

    public static class TooBigSkinException extends RuntimeException {

        public TooBigSkinException(String s) {
            super(s);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal

    private void decodeChainData() {
        int size = bs.getLInt();
        if (size > 3145728) {
            throw new IllegalArgumentException("The chain data is too big: " + size);
        }

        Map<String, Object> map = GSON.fromJson(new String(bs.get(size), StandardCharsets.UTF_8), new MapTypeToken());

        // Since 1.21.90
        String certificate = (String) map.get("Certificate");
        if (certificate != null) {
            map = GSON.fromJson(certificate, new MapTypeToken());
        }

        List<String> chains = (List<String>) map.get("chain");
        if (chains == null || chains.isEmpty()) {
            return;
        }

        // Validate keys
        try {
            xboxAuthed = verifyChain(chains);
        } catch (Exception e) {
            xboxAuthed = false;
        }

        long time = System.currentTimeMillis();

        for (String c : chains) {
            JsonObject chainMap = decodeToken(c);
            if (chainMap == null) continue;

            if (chainMap.has("extraData")) {
                JsonObject extra = chainMap.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) this.username = extra.get("displayName").getAsString();
                if (extra.has("identity")) this.clientUUID = UUID.fromString(extra.get("identity").getAsString());
                if (extra.has("XUID")) this.xuid = extra.get("XUID").getAsString();

                JsonElement titleIdElement = extra.get("titleId");
                if (titleIdElement != null && !titleIdElement.isJsonNull()) {
                    this.titleId = titleIdElement.getAsString();
                }
            }

            if (xboxAuthed && chainMap.has("nbf") && chainMap.get("nbf").getAsLong() * 1000 > time + 60) {
                xboxAuthed = false;
                Server.getInstance().getLogger().info(this.username + ": expired login chain or time not in sync");
            }

            if (xboxAuthed && chainMap.has("exp") && chainMap.get("exp").getAsLong() * 1000 < time - 60) {
                xboxAuthed = false;
                Server.getInstance().getLogger().info(this.username + ": expired login chain or time not in sync");
            }

            if (chainMap.has("identityPublicKey")) {
                this.identityPublicKey = chainMap.get("identityPublicKey").getAsString();
            }
        }

        if (!xboxAuthed) {
            xuid = null;
        }
    }

    private void decodeSkinData() {
        int size = bs.getLInt();
        if (size > 4194304) {
            if (Server.getInstance().doNotLimitSkinGeometry) {
                Server.getInstance().getLogger().warning(username + ": got large skin data but do-not-limit-skin-geometry is enabled: " + size);
                if (size > 10485760) {
                    throw new TooBigSkinException("10 MB hard limit! The skin data is too big: " + size);
                }
            } else {
                throw new TooBigSkinException("The skin data is too big: " + size);
            }
        }

        JsonObject skinToken = decodeToken(new String(bs.get(size), StandardCharsets.UTF_8));
        if (skinToken == null) throw new RuntimeException("Invalid null skin token");
        if (skinToken.has("ClientRandomId")) this.clientId = skinToken.get("ClientRandomId").getAsLong();
        if (skinToken.has("ServerAddress")) this.serverAddress = skinToken.get("ServerAddress").getAsString();
        if (skinToken.has("DeviceModel")) this.deviceModel = skinToken.get("DeviceModel").getAsString();
        if (skinToken.has("DeviceOS")) this.deviceOS = skinToken.get("DeviceOS").getAsInt();
        if (skinToken.has("DeviceId")) this.deviceId = skinToken.get("DeviceId").getAsString();
        if (skinToken.has("GameVersion")) this.gameVersion = skinToken.get("GameVersion").getAsString();
        if (skinToken.has("GuiScale")) this.guiScale = skinToken.get("GuiScale").getAsInt();
        if (skinToken.has("LanguageCode")) this.languageCode = skinToken.get("LanguageCode").getAsString();
        if (skinToken.has("CurrentInputMode")) this.currentInputMode = skinToken.get("CurrentInputMode").getAsInt();
        if (skinToken.has("DefaultInputMode")) this.defaultInputMode = skinToken.get("DefaultInputMode").getAsInt();
        if (skinToken.has("UIProfile")) this.UIProfile = skinToken.get("UIProfile").getAsInt();
        if (skinToken.has("CapeData")) this.capeData = skinToken.get("CapeData").getAsString();
        if (!Server.getInstance().suomiCraftPEMode()) this.rawData = skinToken;
    }

    public static JsonObject decodeToken(String token) {
        String[] base = token.split("\\.", 5);
        if (base.length < 2) return null;
        byte[] decoded;
        try {
            decoded = Base64.getUrlDecoder().decode(base[1]);
        } catch (IllegalArgumentException ex) {
            Server.getInstance().getLogger().error("Unable to decode token: " + token, ex);
            decoded = Base64.getDecoder().decode(base[1]);
        }
        return GSON.fromJson(new String(decoded, StandardCharsets.UTF_8), JsonObject.class);
    }

    /// ////////////////////////////////////////////////////////////////////////

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClientChainData && Objects.equals(bs, ((ClientChainData) obj).bs);
    }

    private static ECPublicKey generateKey(String base64) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return (ECPublicKey) KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(base64)));
    }

    @Override
    public String getCapeData() {
        return capeData;
    }

    @Override
    public long getClientId() {
        return clientId;
    }

    @Override
    public UUID getClientUUID() {
        return clientUUID;
    }

    @Override
    public int getCurrentInputMode() {
        return currentInputMode;
    }

    @Override
    public int getDefaultInputMode() {
        return defaultInputMode;
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getDeviceModel() {
        return deviceModel;
    }

    @Override
    public int getDeviceOS() {
        return deviceOS;
    }

    @Override
    public String getGameVersion() {
        return gameVersion;
    }

    @Override
    public int getGuiScale() {
        return guiScale;
    }

    @Override
    public String getIdentityPublicKey() {
        return identityPublicKey;
    }

    @Override
    public String getLanguageCode() {
        return languageCode;
    }

    @Override
    public JsonObject getRawData() {
        return rawData;
    }

    @Override
    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public String getTitleId() {
        return titleId;
    }

    @Override
    public int getUIProfile() {
        return UIProfile;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getXUID() {
        return xuid;
    }

    @Override
    public int hashCode() {
        return bs.hashCode();
    }

    @Override
    public boolean isXboxAuthed() {
        return xboxAuthed;
    }

    public static ClientChainData of(byte[] buffer) {
        return new ClientChainData(buffer);
    }

    public static ClientChainData read(LoginPacket pk) {
        return of(pk.getBuffer());
    }

    private static boolean verifyChain(List<String> chains) throws Exception {
        ECPublicKey lastKey = null;
        boolean mojangKeyVerified = false;
        Iterator<String> iterator = chains.iterator();
        while (iterator.hasNext()) {
            JWSObject jws = JWSObject.parse(iterator.next());

            URI x5u = jws.getHeader().getX509CertURL();
            if (x5u == null) {
                return false;
            }

            ECPublicKey expectedKey = generateKey(x5u.toString());
            // First key is self-signed
            if (lastKey == null) {
                lastKey = expectedKey;
            } else if (!lastKey.equals(expectedKey)) {
                return false;
            }

            if (!jws.verify(new ECDSAVerifier(lastKey))) {
                return false;
            }

            if (mojangKeyVerified) {
                return !iterator.hasNext();
            }

            if (lastKey.equals(EncryptionUtils.getMojangPublicKey())) {
                mojangKeyVerified = true;
            }

            Object base64key = jws.getPayload().toJSONObject().get("identityPublicKey");
            if (!(base64key instanceof String)) {
                throw new RuntimeException("No key found");
            }
            lastKey = generateKey((String) base64key);
        }
        return mojangKeyVerified;
    }
}
