/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event.server;

import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.server.ServerEvent;

public class ServerCommandEvent
extends ServerEvent
implements Cancellable {
    private static final HandlerList b = new HandlerList();
    protected String command;
    protected final CommandSender sender;

    public ServerCommandEvent(CommandSender commandSender, String string) {
        this.sender = commandSender;
        this.command = string;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String string) {
        this.command = string;
    }

    public static HandlerList getHandlers() {
        return b;
    }
}

