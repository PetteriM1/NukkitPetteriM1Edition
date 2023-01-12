/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import com.google.gson.JsonObject;
import java.util.UUID;

public interface LoginChainData {
    public static final String x = "LoginChainData#getTitleId not implemented!";

    public String getUsername();

    public UUID getClientUUID();

    public String getIdentityPublicKey();

    public long getClientId();

    public String getServerAddress();

    public String getDeviceModel();

    public int getDeviceOS();

    public String getDeviceId();

    public String getGameVersion();

    public int getGuiScale();

    public String getLanguageCode();

    public String getXUID();

    public boolean isXboxAuthed();

    public int getCurrentInputMode();

    public int getDefaultInputMode();

    public String getCapeData();

    public int getUIProfile();

    public JsonObject getRawData();

    default public String getTitleId() {
        return x;
    }
}

