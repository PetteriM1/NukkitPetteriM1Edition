/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.server;

import cn.nukkit.command.CommandSender;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.server.ServerCommandEvent;

public class RemoteServerCommandEvent
extends ServerCommandEvent {
    private static final HandlerList c = new HandlerList();

    public static HandlerList getHandlers() {
        return c;
    }

    public RemoteServerCommandEvent(CommandSender commandSender, String string) {
        super(commandSender, string);
    }
}

