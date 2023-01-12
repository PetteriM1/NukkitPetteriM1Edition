/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

import java.util.OptionalLong;
import java.util.UUID;

public final class CommandOriginData {
    public final Origin type;
    public final UUID uuid;
    public final String requestId;
    private final Long a;

    public CommandOriginData(Origin origin, UUID uUID, String string, Long l) {
        this.type = origin;
        this.uuid = uUID;
        this.requestId = string;
        this.a = l;
    }

    public OptionalLong getVarLong() {
        if (this.a == null) {
            return OptionalLong.empty();
        }
        return OptionalLong.of(this.a);
    }

    public String toString() {
        return "CommandOriginData(type=" + (Object)((Object)this.type) + ", uuid=" + this.uuid + ", requestId=" + this.requestId + ", varlong=" + this.getVarLong() + ")";
    }

    public static enum Origin {
        PLAYER,
        BLOCK,
        MINECART_BLOCK,
        DEV_CONSOLE,
        TEST,
        AUTOMATION_PLAYER,
        CLIENT_AUTOMATION,
        DEDICATED_SERVER,
        ENTITY,
        VIRTUAL,
        GAME_ARGUMENT,
        ENTITY_SERVER;

    }
}

