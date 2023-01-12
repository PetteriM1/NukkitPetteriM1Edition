/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.server;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.server.ServerEvent;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Binary;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryRegenerateEvent
extends ServerEvent {
    private static final HandlerList g = new HandlerList();
    private int e;
    private String i;
    private boolean c;
    private Plugin[] h;
    private Player[] j;
    private final String d;
    private final String m;
    private String l;
    private int b;
    private int n;
    private final String o;
    private final int k;
    private final String f;

    public static HandlerList getHandlers() {
        return g;
    }

    public QueryRegenerateEvent(Server server) {
        this(server, 5);
    }

    public QueryRegenerateEvent(Server server, int n) {
        this.e = n;
        this.i = server.getMotd();
        this.c = server.queryPlugins;
        this.h = server.getPluginManager().getPlugins().values().toArray(new Plugin[0]);
        this.j = server.getOnlinePlayers().values().toArray(new Player[0]);
        this.d = server.getGamemode() == 1 ? "CMP" : "SMP";
        this.m = server.getVersion();
        this.l = server.getDefaultLevel() == null ? "unknown" : server.getDefaultLevel().getName();
        this.b = this.j.length;
        this.n = server.getMaxPlayers();
        this.o = server.hasWhitelist() ? "on" : "off";
        this.k = server.getPort();
        this.f = server.getIp();
    }

    public int getTimeout() {
        return this.e;
    }

    public void setTimeout(int n) {
        this.e = n;
    }

    public String getServerName() {
        return this.i;
    }

    public void setServerName(String string) {
        this.i = string;
    }

    public boolean canListPlugins() {
        return this.c;
    }

    public void setListPlugins(boolean bl) {
        this.c = bl;
    }

    public Plugin[] getPlugins() {
        return this.h;
    }

    public void setPlugins(Plugin[] pluginArray) {
        this.h = pluginArray;
    }

    public Player[] getPlayerList() {
        return this.j;
    }

    public void setPlayerList(Player[] playerArray) {
        this.j = playerArray;
    }

    public int getPlayerCount() {
        return this.b;
    }

    public void setPlayerCount(int n) {
        this.b = n;
    }

    public int getMaxPlayerCount() {
        return this.n;
    }

    public void setMaxPlayerCount(int n) {
        this.n = n;
    }

    public String getWorld() {
        return this.l;
    }

    public void setWorld(String string) {
        this.l = string;
    }

    public byte[] getLongQuery() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
        StringBuilder stringBuilder = new StringBuilder("Nukkit PetteriM1 Edition");
        if (this.c && this.h.length > 0) {
            stringBuilder.append(':');
            for (Plugin plugin : this.h) {
                PluginDescription object = plugin.getDescription();
                stringBuilder.append(' ').append(object.getName().replace(";", "").replace(":", "").replace(" ", "_")).append(' ').append(object.getVersion().replace(";", "").replace(":", "").replace(" ", "_")).append(';');
            }
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 2));
        }
        byteBuffer.put("splitnum".getBytes(StandardCharsets.UTF_8));
        byteBuffer.put((byte)0);
        byteBuffer.put((byte)-128);
        byteBuffer.put((byte)0);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("hostname", this.i);
        linkedHashMap.put("gametype", this.d);
        linkedHashMap.put("game_id", "MINECRAFTPE");
        linkedHashMap.put("version", this.m);
        linkedHashMap.put("server_engine", "Nukkit PetteriM1 Edition");
        linkedHashMap.put("plugins", stringBuilder.toString());
        linkedHashMap.put("map", this.l);
        linkedHashMap.put("numplayers", String.valueOf(this.b));
        linkedHashMap.put("maxplayers", String.valueOf(this.n));
        linkedHashMap.put("whitelist", this.o);
        linkedHashMap.put("hostip", this.f);
        linkedHashMap.put("hostport", String.valueOf(this.k));
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            byteBuffer.put(((String)entry.getKey()).getBytes(StandardCharsets.UTF_8));
            byteBuffer.put((byte)0);
            byteBuffer.put(((String)entry.getValue()).getBytes(StandardCharsets.UTF_8));
            byteBuffer.put((byte)0);
        }
        byteBuffer.put(new byte[]{0, 1}).put("player_".getBytes(StandardCharsets.UTF_8)).put(new byte[]{0, 0});
        for (Player player : this.j) {
            byteBuffer.put(player.getName().getBytes(StandardCharsets.UTF_8));
            byteBuffer.put((byte)0);
        }
        byteBuffer.put((byte)0);
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.position());
    }

    public byte[] getShortQuery() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
        byteBuffer.put(this.i.getBytes(StandardCharsets.UTF_8));
        byteBuffer.put((byte)0);
        byteBuffer.put(this.d.getBytes(StandardCharsets.UTF_8));
        byteBuffer.put((byte)0);
        byteBuffer.put(this.l.getBytes(StandardCharsets.UTF_8));
        byteBuffer.put((byte)0);
        byteBuffer.put(String.valueOf(this.b).getBytes(StandardCharsets.UTF_8));
        byteBuffer.put((byte)0);
        byteBuffer.put(String.valueOf(this.n).getBytes(StandardCharsets.UTF_8));
        byteBuffer.put((byte)0);
        byteBuffer.put(Binary.writeLShort(this.k));
        byteBuffer.put(this.f.getBytes(StandardCharsets.UTF_8));
        byteBuffer.put((byte)0);
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.position());
    }
}

