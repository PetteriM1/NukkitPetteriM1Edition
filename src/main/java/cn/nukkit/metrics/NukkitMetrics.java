package cn.nukkit.metrics;

import cn.nukkit.Server;
import cn.nukkit.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NukkitMetrics {

    private static boolean metricsStarted = false;

    public NukkitMetrics(Server server) {
        if (metricsStarted) {
            return;
        }

        Metrics metrics = new Metrics("Nukkit", server.getServerUniqueId().toString(), true, server.getLogger());

        metrics.addCustomChart(new Metrics.SingleLineChart("players", server::getOnlinePlayersCount));
        metrics.addCustomChart(new Metrics.SimplePie("codename", () -> "PM1E"));
        metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> "PM1E"));
        metrics.addCustomChart(new Metrics.SimplePie("xbox_auth", () -> server.xboxAuth ? "Required" : "Not required"));

        metrics.addCustomChart(new Metrics.AdvancedPie("player_platform", () -> {
            Map<String, Integer> valueMap = new HashMap<>();

            server.getOnlinePlayers().forEach((uuid, player) -> {
                String deviceOS = Utils.getOS(player);
                if (!valueMap.containsKey(deviceOS)) {
                    valueMap.put(deviceOS, 1);
                } else {
                    valueMap.put(deviceOS, valueMap.get(deviceOS) + 1);
                }
            });
            return valueMap;
        }));

        metrics.addCustomChart(new Metrics.AdvancedPie("player_game_version", () -> {
            Map<String, Integer> valueMap = new HashMap<>();

            server.getOnlinePlayers().forEach((uuid, player) -> {
                String gameVersion = player.getLoginChainData().getGameVersion();
                if (!valueMap.containsKey(gameVersion)) {
                    valueMap.put(gameVersion, 1);
                } else {
                    valueMap.put(gameVersion, valueMap.get(gameVersion) + 1);
                }
            });
            return valueMap;
        }));

        // The following code can be attributed to the PaperMC project
        // https://github.com/PaperMC/Paper/blob/master/Spigot-Server-Patches/0005-Paper-Metrics.patch#L614
        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String javaVersion = System.getProperty("java.version");
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);

            // http://openjdk.java.net/jeps/223
            // Java decided to change their versioning scheme and in doing so modified the java.version system
            // property to return $major[.$minor][.$secuity][-ea], as opposed to 1.$major.0_$identifier
            // we can handle pre-9 by checking if the "major" is equal to "1", otherwise, 9+
            String majorVersion = javaVersion.split("\\.")[0];
            String release;

            int indexOf = javaVersion.lastIndexOf('.');

            if (majorVersion.equals("1")) {
                release = "Java " + javaVersion.substring(0, indexOf);
            } else {
                // of course, it really wouldn't be all that simple if they didn't add a quirk, now would it
                // valid strings for the major may potentially include values such as -ea to deannotate a pre release
                Matcher versionMatcher = Pattern.compile("\\d+").matcher(majorVersion);
                if (versionMatcher.find()) {
                    majorVersion = versionMatcher.group(0);
                }
                release = "Java " + majorVersion;
            }
            map.put(release, entry);
            return map;
        }));

        metricsStarted = true;
    }
}
