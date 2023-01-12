/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.rcon;

import cn.nukkit.Server;
import cn.nukkit.command.RemoteConsoleCommandSender;
import cn.nukkit.event.server.RemoteServerCommandEvent;
import cn.nukkit.network.rcon.RCONCommand;
import cn.nukkit.network.rcon.RCONServer;
import cn.nukkit.utils.TextFormat;
import java.io.IOException;

public class RCON {
    private final Server a;
    private final RCONServer b;

    public RCON(Server server, String string, String string2, int n) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException("nukkit.server.rcon.emptyPasswordError");
        }
        this.a = server;
        try {
            this.b = new RCONServer(string2, n, string);
            this.b.start();
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("nukkit.server.rcon.startupError", iOException);
        }
        this.a.getLogger().info(this.a.getLanguage().translateString("nukkit.server.rcon.running", new String[]{string2, String.valueOf(n)}));
    }

    public void check() {
        RCONCommand rCONCommand;
        if (this.b == null) {
            return;
        }
        if (!this.b.isAlive()) {
            return;
        }
        while ((rCONCommand = this.b.receive()) != null) {
            RemoteConsoleCommandSender remoteConsoleCommandSender = new RemoteConsoleCommandSender();
            RemoteServerCommandEvent remoteServerCommandEvent = new RemoteServerCommandEvent(remoteConsoleCommandSender, rCONCommand.getCommand());
            this.a.getPluginManager().callEvent(remoteServerCommandEvent);
            if (!remoteServerCommandEvent.isCancelled()) {
                this.a.dispatchCommand(remoteConsoleCommandSender, rCONCommand.getCommand());
            }
            this.b.respond(rCONCommand.getSender(), rCONCommand.getId(), TextFormat.clean(remoteConsoleCommandSender.getMessages()));
            remoteConsoleCommandSender.clearMessages();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void close() {
        try {
            RCONServer rCONServer = this.b;
            synchronized (rCONServer) {
                this.b.close();
                this.b.wait(5000L);
            }
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

