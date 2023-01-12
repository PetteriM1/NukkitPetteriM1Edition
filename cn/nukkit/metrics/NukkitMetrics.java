/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metrics;

import cn.nukkit.Server;
import cn.nukkit.metrics.Metrics;
import cn.nukkit.utils.Utils;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NukkitMetrics {
    private static boolean a = false;

    public NukkitMetrics(Server server) {
        if (a) {
            return;
        }
        Metrics metrics = new Metrics("Nukkit", server.getServerUniqueId().toString(), true, server.getLogger());
        metrics.addCustomChart(new Metrics.SingleLineChart("players", server::getOnlinePlayersCount));
        metrics.addCustomChart(new Metrics.SimplePie("codename", () -> "PM1E"));
        metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> "PM1E"));
        metrics.addCustomChart(new Metrics.SimplePie("xbox_auth", () -> server.xboxAuth ? "Required" : "Not required"));
        metrics.addCustomChart(new Metrics.AdvancedPie("player_platform", () -> {
            HashMap hashMap = new HashMap();
            server.getOnlinePlayers().forEach((uUID, player) -> {
                String string = Utils.getOS(player);
                if (!hashMap.containsKey(string)) {
                    hashMap.put(string, 1);
                } else {
                    hashMap.put(string, (Integer)hashMap.get(string) + 1);
                }
            });
            return hashMap;
        }));
        metrics.addCustomChart(new Metrics.AdvancedPie("player_game_version", () -> {
            HashMap hashMap = new HashMap();
            server.getOnlinePlayers().forEach((uUID, player) -> {
                String string = player.getLoginChainData().getGameVersion();
                if (!hashMap.containsKey(string)) {
                    hashMap.put(string, 1);
                } else {
                    hashMap.put(string, (Integer)hashMap.get(string) + 1);
                }
            });
            return hashMap;
        }));
        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            String string;
            HashMap hashMap = new HashMap();
            String string2 = System.getProperty("java.version");
            HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
            hashMap2.put(string2, 1);
            String string3 = string2.split("\\.")[0];
            int n = string2.lastIndexOf(46);
            if (string3.equals("1")) {
                string = "Java " + string2.substring(0, n);
            } else {
                Matcher matcher = Pattern.compile("\\d+").matcher(string3);
                if (matcher.find()) {
                    string3 = matcher.group(0);
                }
                string = "Java " + string3;
            }
            hashMap.put(string, hashMap2);
            return hashMap;
        }));
        a = true;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

