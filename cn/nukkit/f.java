/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.d;
import cn.nukkit.utils.ConfigSection;
import java.util.Base64;
import java.util.UUID;

class f
extends ConfigSection {
    private f() {
        this.put("motd", "Minecraft Server");
        this.put("sub-motd", "Powered by Nukkit PM1E");
        this.put("server-port", 19132);
        this.put("server-ip", "0.0.0.0");
        this.put("view-distance", 8);
        this.put("white-list", false);
        this.put("achievements", true);
        this.put("announce-player-achievements", true);
        this.put("spawn-protection", 10);
        this.put("max-players", 50);
        this.put("spawn-animals", true);
        this.put("spawn-mobs", true);
        this.put("gamemode", 0);
        this.put("force-gamemode", true);
        this.put("hardcore", false);
        this.put("pvp", true);
        this.put("difficulty", 2);
        this.put("generator-settings", "");
        this.put("level-name", "world");
        this.put("level-seed", "");
        this.put("level-type", "default");
        this.put("enable-query", false);
        this.put("enable-rcon", false);
        this.put("rcon.password", Base64.getEncoder().encodeToString(UUID.randomUUID().toString().replace("-", "").getBytes()).substring(3, 13));
        this.put("auto-save", true);
        this.put("force-resources", false);
        this.put("force-resources-allow-client-packs", false);
        this.put("xbox-auth", true);
        this.put("bed-spawnpoints", true);
        this.put("explosion-break-blocks", true);
        this.put("stop-in-game", false);
        this.put("op-in-game", true);
        this.put("xp-bottles-on-creative", true);
        this.put("spawn-eggs", true);
        this.put("mob-ai", true);
        this.put("entity-despawn-task", true);
        this.put("force-language", false);
        this.put("shutdown-message", "Server closed");
        this.put("save-player-data", true);
        this.put("query-plugins", false);
        this.put("debug-level", 1);
        this.put("async-workers", "auto");
        this.put("zlib-provider", 2);
        this.put("compression-level", 4);
        this.put("auto-tick-rate", true);
        this.put("auto-tick-rate-limit", 20);
        this.put("base-tick-rate", 1);
        this.put("always-tick-players", false);
        this.put("enable-timings", false);
        this.put("timings-verbose", false);
        this.put("timings-privacy", false);
        this.put("timings-history-interval", 6000);
        this.put("timings-history-length", 72000);
        this.put("timings-bypass-max", false);
        this.put("light-updates", false);
        this.put("clear-chunk-tick-list", true);
        this.put("cache-chunks", false);
        this.put("spawn-threshold", 56);
        this.put("chunk-sending-per-tick", 4);
        this.put("chunk-ticking-per-tick", 40);
        this.put("chunk-ticking-radius", 3);
        this.put("chunk-generation-queue-size", 8);
        this.put("chunk-generation-population-queue-size", 8);
        this.put("ticks-per-autosave", 6000);
        this.put("ticks-per-entity-spawns", 200);
        this.put("ticks-per-entity-despawns", 12000);
        this.put("thread-watchdog", true);
        this.put("thread-watchdog-tick", 60000);
        this.put("nether", true);
        this.put("end", false);
        this.put("suomicraft-mode", false);
        this.put("do-not-tick-worlds", "");
        this.put("load-all-worlds", true);
        this.put("ansi-title", true);
        this.put("worlds-entity-spawning-disabled", "");
        this.put("block-listener", true);
        this.put("allow-flight", false);
        this.put("timeout-milliseconds", 25000);
        this.put("multiversion-min-protocol", 0);
        this.put("vanilla-bossbars", false);
        this.put("dimensions", true);
        this.put("whitelist-reason", "Server is white-listed");
        this.put("chemistry-resources-enabled", false);
        this.put("strong-ip-bans", false);
        this.put("worlds-level-auto-save-disabled", "");
        this.put("temp-ip-ban-failed-xbox-auth", false);
        this.put("call-data-pk-send-event", true);
        this.put("call-batch-pk-send-event", true);
        this.put("do-level-gc", true);
        this.put("skin-change-cooldown", 30);
        this.put("check-op-movement", false);
        this.put("do-not-limit-interactions", false);
        this.put("do-not-limit-skin-geometry", true);
        this.put("automatic-bug-report", true);
        this.put("anvils-enabled", true);
        this.put("save-player-data-by-uuid", true);
        this.put("vanilla-portals", true);
        this.put("persona-skins", true);
        this.put("multi-nether-worlds", "");
        this.put("call-entity-motion-event", true);
        this.put("update-notifications", true);
        this.put("anti-xray-worlds", "");
        this.put("bstats-metrics", true);
        this.put("min-mtu", 576);
        this.put("max-mtu", 1492);
        this.put("async-chunks", true);
        this.put("log-join-location", true);
        this.put("unsafe-redstone", true);
        this.put("disable-new-blocks", false);
        this.put("attack-stop-sprint", true);
        this.put("chunk-compression-level", 7);
    }

    f(d d2) {
        this();
    }
}

