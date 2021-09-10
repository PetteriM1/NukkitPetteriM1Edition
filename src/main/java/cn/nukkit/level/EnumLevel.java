package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.level.generator.Generator;

import java.util.StringTokenizer;

/**
 * Default dimensions and their Levels
 */
public enum EnumLevel {

    OVERWORLD,
    NETHER,
    THE_END;

    Level level;

    /**
     * Get Level
     *
     * @return Level or null if the dimension is not enabled
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Internal: Initialize default overworld, nether and the end Levels
     */
    public static void initLevels() {
        Server server = Server.getInstance();
        OVERWORLD.level = server.getDefaultLevel();
        if (server.netherEnabled) {
            if (server.getLevelByName("nether") == null) {
                server.generateLevel("nether", System.currentTimeMillis(), Generator.getGenerator(Generator.TYPE_NETHER));
                server.loadLevel("nether");
            }
            NETHER.level = server.getLevelByName("nether");
            String list = server.getPropertyString("multi-nether-worlds");
            if (!list.trim().isEmpty()) {
                StringTokenizer tokenizer = new StringTokenizer(list, ", ");
                while (tokenizer.hasMoreTokens()) {
                    String world = tokenizer.nextToken();
                    Server.multiNetherWorlds.add(world);
                    String nether = world + "-nether";
                    if (server.getLevelByName(nether) == null) {
                        server.generateLevel(nether, System.currentTimeMillis(), Generator.getGenerator(Generator.TYPE_NETHER));
                        server.loadLevel(nether);
                    }
                }
            }
        }
        if (server.endEnabled) {
            if (server.getLevelByName("the_end") == null) {
                server.generateLevel("the_end", System.currentTimeMillis(), Generator.getGenerator(Generator.TYPE_THE_END));
                server.loadLevel("the_end");
            }
            THE_END.level = server.getLevelByName("the_end");
        }
    }
}
