/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.Generator;
import java.util.StringTokenizer;

public enum EnumLevel {
    OVERWORLD,
    NETHER,
    THE_END;

    Level b;

    public Level getLevel() {
        return this.b;
    }

    public static void initLevels() {
        Server server = Server.getInstance();
        EnumLevel.OVERWORLD.b = server.getDefaultLevel();
        if (server.netherEnabled) {
            if (server.getLevelByName("nether") == null) {
                server.generateLevel("nether", System.currentTimeMillis(), Generator.getGenerator(3));
                server.loadLevel("nether");
            }
            EnumLevel.NETHER.b = server.getLevelByName("nether");
            String string = server.getPropertyString("multi-nether-worlds");
            if (!string.trim().isEmpty()) {
                StringTokenizer stringTokenizer = new StringTokenizer(string, ", ");
                while (stringTokenizer.hasMoreTokens()) {
                    String string2 = stringTokenizer.nextToken();
                    Server.multiNetherWorlds.add(string2);
                    String string3 = string2 + "-nether";
                    if (server.getLevelByName(string3) != null) continue;
                    server.generateLevel(string3, System.currentTimeMillis(), Generator.getGenerator(3));
                    server.loadLevel(string3);
                }
            }
        }
        if (server.endEnabled) {
            if (server.getLevelByName("the_end") == null) {
                server.generateLevel("the_end", System.currentTimeMillis(), Generator.getGenerator(4));
                server.loadLevel("the_end");
            }
            EnumLevel.THE_END.b = server.getLevelByName("the_end");
        }
    }

    static int a(int n) {
        return Math.round((float)n / 32.0f) * 32;
    }
}

