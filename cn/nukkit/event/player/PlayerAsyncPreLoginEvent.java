/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.player;

import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.utils.LoginChainData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerAsyncPreLoginEvent
extends PlayerEvent {
    private static final HandlerList h = new HandlerList();
    private final String d;
    private final UUID f;
    private final LoginChainData j;
    private Skin g;
    private final String i;
    private final int e;
    private LoginResult k = LoginResult.SUCCESS;
    private String b = "Plugin Reason";
    private final List<Consumer<Server>> c = new ArrayList<Consumer<Server>>();

    public static HandlerList getHandlers() {
        return h;
    }

    public PlayerAsyncPreLoginEvent(String string, UUID uUID, LoginChainData loginChainData, Skin skin, String string2, int n) {
        this.d = string;
        this.f = uUID;
        this.j = loginChainData;
        this.g = skin;
        this.i = string2;
        this.e = n;
    }

    public String getName() {
        return this.d;
    }

    public UUID getUuid() {
        return this.f;
    }

    public LoginChainData getChainData() {
        return this.j;
    }

    public String getXuid() {
        return this.j.getXUID();
    }

    public Skin getSkin() {
        return this.g;
    }

    public void setSkin(Skin skin) {
        this.g = skin;
    }

    public String getAddress() {
        return this.i;
    }

    public int getPort() {
        return this.e;
    }

    public LoginResult getLoginResult() {
        return this.k;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.k = loginResult;
    }

    public String getKickMessage() {
        return this.b;
    }

    public void setKickMessage(String string) {
        this.b = string;
    }

    public void scheduleSyncAction(Consumer<Server> consumer) {
        this.c.add(consumer);
    }

    public List<Consumer<Server>> getScheduledActions() {
        return new ArrayList<Consumer<Server>>(this.c);
    }

    public void allow() {
        this.k = LoginResult.SUCCESS;
    }

    public void disAllow(String string) {
        this.k = LoginResult.KICK;
        this.b = string;
    }

    public static enum LoginResult {
        SUCCESS,
        KICK;

    }
}

