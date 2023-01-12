/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.types.CommandOriginData;
import java.util.UUID;

public class CommandRequestPacket
extends DataPacket {
    public static final byte NETWORK_ID = 77;
    public static final int TYPE_PLAYER = 0;
    public static final int TYPE_COMMAND_BLOCK = 1;
    public static final int TYPE_MINECART_COMMAND_BLOCK = 2;
    public static final int TYPE_DEV_CONSOLE = 3;
    public static final int TYPE_AUTOMATION_PLAYER = 4;
    public static final int TYPE_CLIENT_AUTOMATION = 5;
    public static final int TYPE_DEDICATED_SERVER = 6;
    public static final int TYPE_ENTITY = 7;
    public static final int TYPE_VIRTUAL = 8;
    public static final int TYPE_GAME_ARGUMENT = 9;
    public static final int TYPE_INTERNAL = 10;
    public String command;
    public CommandOriginData data;

    @Override
    public byte pid() {
        return 77;
    }

    @Override
    public void decode() {
        this.command = this.getString();
        CommandOriginData.Origin origin = CommandOriginData.Origin.values()[this.getVarInt()];
        UUID uUID = this.protocol > 137 ? this.getUUID() : null;
        String string = this.getString();
        Long l = null;
        if (origin == CommandOriginData.Origin.DEV_CONSOLE || origin == CommandOriginData.Origin.TEST) {
            l = this.getVarLong();
        }
        this.data = new CommandOriginData(origin, uUID, string, l);
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "CommandRequestPacket(command=" + this.command + ", data=" + this.data + ")";
    }
}

