/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.LoginChainData;
import cn.nukkit.utils.c;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ClientChainData
implements LoginChainData {
    private static final String e = "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V";
    private static final PublicKey v;
    private static final Gson c;
    private boolean d;
    public static final int UI_PROFILE_CLASSIC = 0;
    public static final int UI_PROFILE_POCKET = 1;
    private String b;
    private UUID p;
    private String i;
    private String f;
    private long r;
    private String o;
    private String n;
    private int g;
    private String j;
    private String u;
    private int w;
    private String s;
    private int a;
    private int t;
    private int m;
    private String l;
    private String h;
    private JsonObject q;
    private final BinaryStream k = new BinaryStream();

    public static ClientChainData of(byte[] byArray) {
        return new ClientChainData(byArray);
    }

    public static ClientChainData read(LoginPacket loginPacket) {
        return ClientChainData.of(loginPacket.getBuffer());
    }

    @Override
    public String getUsername() {
        return this.b;
    }

    @Override
    public UUID getClientUUID() {
        return this.p;
    }

    @Override
    public String getIdentityPublicKey() {
        return this.f;
    }

    @Override
    public long getClientId() {
        return this.r;
    }

    @Override
    public String getServerAddress() {
        return this.o;
    }

    @Override
    public String getDeviceModel() {
        return this.n;
    }

    @Override
    public int getDeviceOS() {
        return this.g;
    }

    @Override
    public String getDeviceId() {
        return this.j;
    }

    @Override
    public String getGameVersion() {
        return this.u;
    }

    @Override
    public int getGuiScale() {
        return this.w;
    }

    @Override
    public String getLanguageCode() {
        return this.s;
    }

    @Override
    public String getXUID() {
        return this.i;
    }

    @Override
    public int getCurrentInputMode() {
        return this.a;
    }

    @Override
    public int getDefaultInputMode() {
        return this.t;
    }

    @Override
    public String getCapeData() {
        return this.l;
    }

    @Override
    public int getUIProfile() {
        return this.m;
    }

    @Override
    public String getTitleId() {
        return this.h;
    }

    @Override
    public JsonObject getRawData() {
        return this.q;
    }

    public boolean equals(Object object) {
        return object instanceof ClientChainData && Objects.equals(this.k, ((ClientChainData)object).k);
    }

    public int hashCode() {
        return this.k.hashCode();
    }

    private static ECPublicKey a(String string) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return (ECPublicKey)KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(string)));
    }

    private ClientChainData(byte[] byArray) {
        this.k.setBuffer(byArray, 0);
        this.a();
        this.b();
    }

    @Override
    public boolean isXboxAuthed() {
        return this.d;
    }

    private void b() {
        block14: {
            int n = this.k.getLInt();
            if (n > 3000000) {
                throw new IllegalArgumentException("The skin data is too big: " + n);
            }
            JsonObject jsonObject = ClientChainData.b(new String(this.k.get(n), StandardCharsets.UTF_8));
            if (jsonObject == null) {
                throw new RuntimeException("Invalid null skin token");
            }
            if (jsonObject.has("ClientRandomId")) {
                this.r = jsonObject.get("ClientRandomId").getAsLong();
            }
            if (jsonObject.has("ServerAddress")) {
                this.o = jsonObject.get("ServerAddress").getAsString();
            }
            if (jsonObject.has("DeviceModel")) {
                this.n = jsonObject.get("DeviceModel").getAsString();
            }
            if (jsonObject.has("DeviceOS")) {
                this.g = jsonObject.get("DeviceOS").getAsInt();
            }
            if (jsonObject.has("DeviceId")) {
                this.j = jsonObject.get("DeviceId").getAsString();
            }
            if (jsonObject.has("GameVersion")) {
                this.u = jsonObject.get("GameVersion").getAsString();
            }
            if (jsonObject.has("GuiScale")) {
                this.w = jsonObject.get("GuiScale").getAsInt();
            }
            if (jsonObject.has("LanguageCode")) {
                this.s = jsonObject.get("LanguageCode").getAsString();
            }
            if (jsonObject.has("CurrentInputMode")) {
                this.a = jsonObject.get("CurrentInputMode").getAsInt();
            }
            if (jsonObject.has("DefaultInputMode")) {
                this.t = jsonObject.get("DefaultInputMode").getAsInt();
            }
            if (jsonObject.has("UIProfile")) {
                this.m = jsonObject.get("UIProfile").getAsInt();
            }
            if (jsonObject.has("CapeData")) {
                this.l = jsonObject.get("CapeData").getAsString();
            }
            if (Server.getInstance().suomiCraftPEMode()) break block14;
            this.q = jsonObject;
        }
    }

    private static JsonObject b(String string) {
        String[] stringArray = string.split("\\.");
        if (stringArray.length < 2) {
            return null;
        }
        return c.fromJson(new String(Base64.getDecoder().decode(stringArray[1]), StandardCharsets.UTF_8), JsonObject.class);
    }

    private void a() {
        block10: {
            int n = this.k.getLInt();
            if (n > 3000000) {
                throw new IllegalArgumentException("The chain data is too big: " + n);
            }
            Map map = (Map)c.fromJson(new String(this.k.get(n), StandardCharsets.UTF_8), new MapTypeToken(null).getType());
            if (map.isEmpty() || !map.containsKey("chain") || ((List)map.get("chain")).isEmpty()) {
                return;
            }
            List list = (List)map.get("chain");
            try {
                this.d = ClientChainData.a(list);
            }
            catch (Exception exception) {
                this.d = false;
            }
            for (String string : list) {
                JsonObject jsonObject = ClientChainData.b(string);
                if (jsonObject == null) continue;
                if (jsonObject.has("extraData")) {
                    JsonObject jsonObject2 = jsonObject.get("extraData").getAsJsonObject();
                    if (jsonObject2.has("displayName")) {
                        this.b = jsonObject2.get("displayName").getAsString();
                    }
                    if (jsonObject2.has("identity")) {
                        this.p = UUID.fromString(jsonObject2.get("identity").getAsString());
                    }
                    if (jsonObject2.has("XUID")) {
                        this.i = jsonObject2.get("XUID").getAsString();
                    }
                    if (jsonObject2.has("titleId")) {
                        this.h = jsonObject2.get("titleId").getAsString();
                    }
                }
                if (!jsonObject.has("identityPublicKey")) continue;
                this.f = jsonObject.get("identityPublicKey").getAsString();
            }
            if (this.d) break block10;
            this.i = null;
        }
    }

    private static boolean a(List<String> list) throws Exception {
        ECPublicKey eCPublicKey = null;
        boolean bl = false;
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object object;
            JWSObject jWSObject = JWSObject.parse(iterator.next());
            URI uRI = jWSObject.getHeader().getX509CertURL();
            if (uRI == null) {
                return false;
            }
            ECPublicKey eCPublicKey2 = ClientChainData.a(uRI.toString());
            if (eCPublicKey == null) {
                eCPublicKey = eCPublicKey2;
            } else if (!eCPublicKey.equals(eCPublicKey2)) {
                return false;
            }
            if (!jWSObject.verify(new ECDSAVerifier(eCPublicKey))) {
                return false;
            }
            if (bl) {
                return !iterator.hasNext();
            }
            if (eCPublicKey.equals(v)) {
                bl = true;
            }
            if (!((object = jWSObject.getPayload().toJSONObject().get("identityPublicKey")) instanceof String)) {
                throw new RuntimeException("No key found");
            }
            eCPublicKey = ClientChainData.a((String)object);
        }
        return bl;
    }

    static {
        c = new Gson();
        try {
            v = ClientChainData.a("MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V");
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException generalSecurityException) {
            throw new AssertionError((Object)generalSecurityException);
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    private static class MapTypeToken
    extends TypeToken<Map<String, List<String>>> {
        private MapTypeToken() {
        }

        /* synthetic */ MapTypeToken(c c2) {
            this();
        }
    }
}

