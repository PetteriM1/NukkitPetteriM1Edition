package cn.nukkit.utils;

import com.google.gson.JsonObject;

import java.util.UUID;

/**
 * Login chain data
 *
 * @author CreeperFace
 */
public interface LoginChainData {

    String getCapeData();

    long getClientId();

    UUID getClientUUID();

    int getCurrentInputMode();

    int getDefaultInputMode();

    String getDeviceId();

    String getDeviceModel();

    int getDeviceOS();

    String getGameVersion();

    int getGuiScale();

    String getIdentityPublicKey();

    String getLanguageCode();

    JsonObject getRawData();

    String getServerAddress();

    String getTitleId();

    int getUIProfile();

    String getUsername();

    String getXUID();

    boolean isXboxAuthed();
}
