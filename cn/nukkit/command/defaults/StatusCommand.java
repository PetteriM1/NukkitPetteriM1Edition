/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.TextFormat;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class StatusCommand
extends VanillaCommand {
    private static final String i = (Object)((Object)TextFormat.RED) + "%d" + (Object)((Object)TextFormat.GOLD) + " days " + (Object)((Object)TextFormat.RED) + "%d" + (Object)((Object)TextFormat.GOLD) + " hours " + (Object)((Object)TextFormat.RED) + "%d" + (Object)((Object)TextFormat.GOLD) + " minutes " + (Object)((Object)TextFormat.RED) + "%d" + (Object)((Object)TextFormat.GOLD) + " seconds";

    public StatusCommand(String string) {
        super(string, "%nukkit.command.status.description", "%nukkit.command.status.usage");
        this.setPermission("nukkit.command.status");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        Server server = commandSender.getServer();
        commandSender.sendMessage((Object)((Object)TextFormat.GREEN) + "---- " + (Object)((Object)TextFormat.WHITE) + "Server status" + (Object)((Object)TextFormat.GREEN) + " ----");
        long l = System.currentTimeMillis() - Nukkit.START_TIME;
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Uptime: " + StatusCommand.formatUptime(l));
        TextFormat textFormat = TextFormat.GREEN;
        float f2 = server.getTicksPerSecond();
        if (f2 < 12.0f) {
            textFormat = TextFormat.RED;
        } else if (f2 < 17.0f) {
            textFormat = TextFormat.GOLD;
        }
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Current TPS: " + (Object)((Object)textFormat) + NukkitMath.round(f2, 2));
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Load: " + (Object)((Object)textFormat) + server.getTickUsage() + '%');
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Network upload: " + (Object)((Object)TextFormat.GREEN) + NukkitMath.round(server.getNetwork().getUpload() / 1024.0 * 1000.0, 2) + " kB/s");
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Network download: " + (Object)((Object)TextFormat.GREEN) + NukkitMath.round(server.getNetwork().getDownload() / 1024.0 * 1000.0, 2) + " kB/s");
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Thread count: " + (Object)((Object)TextFormat.GREEN) + Thread.getAllStackTraces().size());
        Runtime runtime = Runtime.getRuntime();
        double d2 = NukkitMath.round((double)runtime.totalMemory() / 1024.0 / 1024.0, 2);
        double d3 = NukkitMath.round((double)(runtime.totalMemory() - runtime.freeMemory()) / 1024.0 / 1024.0, 2);
        double d4 = NukkitMath.round((double)runtime.maxMemory() / 1024.0 / 1024.0, 2);
        double d5 = d3 / d4 * 100.0;
        TextFormat textFormat2 = TextFormat.GREEN;
        if (d5 > 85.0) {
            textFormat2 = TextFormat.GOLD;
        }
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Used memory: " + (Object)((Object)textFormat2) + d3 + " MB (" + NukkitMath.round(d5, 2) + "%)");
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Total memory: " + (Object)((Object)TextFormat.RED) + d2 + " MB");
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Maximum VM memory: " + (Object)((Object)TextFormat.RED) + d4 + " MB");
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Available processors: " + (Object)((Object)TextFormat.GREEN) + runtime.availableProcessors());
        int n = server.getOnlinePlayersCount();
        TextFormat textFormat3 = TextFormat.GREEN;
        if ((double)((float)n / (float)server.getMaxPlayers()) > 0.85) {
            textFormat3 = TextFormat.GOLD;
        }
        commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "Players: " + (Object)((Object)textFormat3) + n + (Object)((Object)TextFormat.GREEN) + " online, " + (Object)((Object)TextFormat.RED) + server.getMaxPlayers() + (Object)((Object)TextFormat.GREEN) + " max");
        for (Level level : server.getLevels().values()) {
            commandSender.sendMessage((Object)((Object)TextFormat.GOLD) + "World \"" + level.getFolderName() + '\"' + (!Objects.equals(level.getFolderName(), level.getName()) ? " (" + level.getName() + ')' : "") + ": " + (Object)((Object)TextFormat.RED) + level.getChunks().size() + (Object)((Object)TextFormat.GREEN) + " chunks, " + (Object)((Object)TextFormat.RED) + level.getEntities().length + (Object)((Object)TextFormat.GREEN) + " entities, " + (Object)((Object)TextFormat.RED) + level.getBlockEntities().size() + (Object)((Object)TextFormat.GREEN) + " block entities. Time " + (Object)((Object)(level.getTickRate() > 1 || level.getTickRateTime() > 40 ? TextFormat.RED : TextFormat.YELLOW)) + NukkitMath.round(level.getTickRateTime(), 2) + "ms" + (level.getTickRate() > 1 ? " (tick rate " + level.getTickRate() + ')' : ""));
        }
        return true;
    }

    public static String formatUptime(long l) {
        long l2 = TimeUnit.MILLISECONDS.toDays(l);
        long l3 = TimeUnit.MILLISECONDS.toHours(l -= TimeUnit.DAYS.toMillis(l2));
        long l4 = TimeUnit.MILLISECONDS.toMinutes(l -= TimeUnit.HOURS.toMillis(l3));
        long l5 = TimeUnit.MILLISECONDS.toSeconds(l -= TimeUnit.MINUTES.toMillis(l4));
        return String.format(i, l2, l3, l4, l5);
    }
}

