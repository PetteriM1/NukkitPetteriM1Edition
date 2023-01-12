/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.console;

import cn.nukkit.Server;
import cn.nukkit.console.NukkitConsoleCompleter;
import cn.nukkit.event.server.ServerCommandEvent;
import co.aikar.timings.Timings;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public class NukkitConsole
extends SimpleTerminalConsole {
    private final BlockingQueue<String> b = new LinkedBlockingQueue<String>();
    private final AtomicBoolean a = new AtomicBoolean(false);

    @Override
    protected boolean isRunning() {
        return Server.getInstance().isRunning();
    }

    @Override
    protected void runCommand(String string) {
        if (this.a.get()) {
            if (Timings.serverCommandTimer != null) {
                Timings.serverCommandTimer.startTiming();
            }
            ServerCommandEvent serverCommandEvent = new ServerCommandEvent(Server.getInstance().getConsoleSender(), string);
            if (Server.getInstance().getPluginManager() != null) {
                Server.getInstance().getPluginManager().callEvent(serverCommandEvent);
            }
            if (!serverCommandEvent.isCancelled()) {
                Server.getInstance().getScheduler().scheduleTask(() -> Server.getInstance().dispatchCommand(serverCommandEvent.getSender(), serverCommandEvent.getCommand()));
            }
            if (Timings.serverCommandTimer != null) {
                Timings.serverCommandTimer.stopTiming();
            }
        } else {
            this.b.add(string);
        }
    }

    public String readLine() {
        try {
            return this.b.take();
        }
        catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        }
    }

    @Override
    protected void shutdown() {
        Server.getInstance().shutdown();
    }

    @Override
    protected LineReader buildReader(LineReaderBuilder lineReaderBuilder) {
        lineReaderBuilder.completer(new NukkitConsoleCompleter());
        lineReaderBuilder.appName("Nukkit PetteriM1 Edition");
        lineReaderBuilder.option(LineReader.Option.HISTORY_BEEP, false);
        lineReaderBuilder.option(LineReader.Option.HISTORY_IGNORE_DUPS, true);
        lineReaderBuilder.option(LineReader.Option.HISTORY_IGNORE_SPACE, true);
        return super.buildReader(lineReaderBuilder);
    }

    public boolean isExecutingCommands() {
        return this.a.get();
    }

    public void setExecutingCommands(boolean bl) {
        block0: {
            if (!this.a.compareAndSet(!bl, bl) || !bl) break block0;
            this.b.clear();
        }
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }
}

