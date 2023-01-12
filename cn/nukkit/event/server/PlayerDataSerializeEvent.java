/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.server;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.server.ServerEvent;
import cn.nukkit.utils.PlayerDataSerializer;
import com.google.common.base.Preconditions;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataSerializeEvent
extends ServerEvent {
    private static final HandlerList d = new HandlerList();
    private final Optional<String> e;
    private final Optional<UUID> b;
    private PlayerDataSerializer c;

    public PlayerDataSerializeEvent(String string, PlayerDataSerializer playerDataSerializer) {
        Preconditions.checkNotNull(string);
        this.c = Preconditions.checkNotNull(playerDataSerializer);
        UUID uUID = null;
        try {
            uUID = UUID.fromString(string);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.b = Optional.ofNullable(uUID);
        this.e = this.b.isPresent() ? Optional.empty() : Optional.of(string);
    }

    public Optional<String> getName() {
        return this.e;
    }

    public Optional<UUID> getUuid() {
        return this.b;
    }

    public PlayerDataSerializer getSerializer() {
        return this.c;
    }

    public void setSerializer(PlayerDataSerializer playerDataSerializer) {
        this.c = Preconditions.checkNotNull(playerDataSerializer, "serializer");
    }

    public static HandlerList getHandlers() {
        return d;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

