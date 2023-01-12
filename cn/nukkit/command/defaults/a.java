/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.StatusCommand;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.HastebinUtility;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

class a
extends AsyncTask {
    private final Server e;
    private final CommandSender d;

    public a(Server server, CommandSender commandSender) {
        this.e = server;
        this.d = commandSender;
    }

    @Override
    public void onRun() {
        try {
            new StatusCommand("status").execute(this.e.getConsoleSender(), "status", new String[0]);
            String string = this.e.getDataPath();
            String string2 = HastebinUtility.upload(new File(string, "server.properties"));
            String string3 = HastebinUtility.upload(new File(string, "/logs/server.log"));
            String string4 = HastebinUtility.upload(Utils.getAllThreadDumps());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("# Files\n");
            stringBuilder.append("links.server_properties: ").append(string2).append('\n');
            stringBuilder.append("links.server_log: ").append(string3).append('\n');
            stringBuilder.append("links.thread_dump: ").append(string4).append('\n');
            stringBuilder.append("\n# Server Information\n");
            stringBuilder.append("version.nukkit: ").append("Nukkit PetteriM1 Edition").append('\n');
            stringBuilder.append("version.build: ").append(Nukkit.getBranch()).append('/').append(Nukkit.VERSION.substring(4)).append(" (").append(Nukkit.BUILD_VERSION_NUMBER).append(")\n");
            stringBuilder.append("version.minecraft: ").append("v1.19.50").append('\n');
            stringBuilder.append("version.protocol: ").append(ProtocolInfo.CURRENT_PROTOCOL).append('\n');
            stringBuilder.append("plugins:");
            for (Plugin object2 : this.e.getPluginManager().getPlugins().values()) {
                boolean bl = object2.isEnabled();
                String string5 = object2.getName();
                PluginDescription pluginDescription = object2.getDescription();
                String string6 = pluginDescription.getVersion();
                stringBuilder.append("\n  ").append(string5).append(":\n    ").append("version: '").append(string6).append('\'').append("\n    enabled: ").append(bl);
            }
            stringBuilder.append("\n\n# Java Details\n");
            Runtime runtime = Runtime.getRuntime();
            stringBuilder.append("memory.free: ").append(runtime.freeMemory()).append('\n');
            stringBuilder.append("memory.max: ").append(runtime.maxMemory()).append('\n');
            stringBuilder.append("cpu.runtime: ").append(ManagementFactory.getRuntimeMXBean().getUptime()).append('\n');
            stringBuilder.append("cpu.processors: ").append(runtime.availableProcessors()).append('\n');
            stringBuilder.append("java.specification.version: '").append(System.getProperty("java.specification.version")).append("'\n");
            stringBuilder.append("java.vendor: '").append(System.getProperty("java.vendor")).append("'\n");
            stringBuilder.append("java.version: '").append(System.getProperty("java.version")).append("'\n");
            stringBuilder.append("os.arch: '").append(System.getProperty("os.arch")).append("'\n");
            stringBuilder.append("os.name: '").append(System.getProperty("os.name")).append("'\n");
            stringBuilder.append("os.version: '").append(System.getProperty("os.version")).append("'\n\n");
            String string7 = HastebinUtility.upload(stringBuilder.toString());
            this.d.sendMessage(string7);
        }
        catch (IOException iOException) {
            MainLogger.getLogger().logException(iOException);
        }
    }
}

