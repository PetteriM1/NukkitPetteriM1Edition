/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import java.util.Optional;

public enum GameRule {
    COMMAND_BLOCKS_ENABLED("commandBlocksEnabled"),
    COMMAND_BLOCK_OUTPUT("commandBlockOutput"),
    DO_DAYLIGHT_CYCLE("doDaylightCycle"),
    DO_ENTITY_DROPS("doEntityDrops"),
    DO_FIRE_TICK("doFireTick"),
    DO_INSOMNIA("doInsomnia"),
    DO_IMMEDIATE_RESPAWN("doImmediateRespawn"),
    DO_MOB_LOOT("doMobLoot"),
    DO_MOB_SPAWNING("doMobSpawning"),
    DO_TILE_DROPS("doTileDrops"),
    DO_WEATHER_CYCLE("doWeatherCycle"),
    DROWNING_DAMAGE("drowningDamage"),
    FALL_DAMAGE("fallDamage"),
    FIRE_DAMAGE("fireDamage"),
    FREEZE_DAMAGE("freezeDamage"),
    FUNCTION_COMMAND_LIMIT("functionCommandLimit"),
    KEEP_INVENTORY("keepInventory"),
    MAX_COMMAND_CHAIN_LENGTH("maxCommandChainLength"),
    MOB_GRIEFING("mobGriefing"),
    NATURAL_REGENERATION("naturalRegeneration"),
    PVP("pvp"),
    RANDOM_TICK_SPEED("randomTickSpeed"),
    RESPAWN_BLOCKS_EXPLODE("respawnBlocksExplode"),
    SEND_COMMAND_FEEDBACK("sendCommandFeedback"),
    SHOW_COORDINATES("showCoordinates"),
    SHOW_DEATH_MESSAGES("showDeathMessages"),
    SHOW_TAGS("showTags"),
    SPAWN_RADIUS("spawnRadius"),
    TNT_EXPLODES("tntExplodes");

    private final String b;

    private GameRule(String string2) {
        this.b = string2;
    }

    public static Optional<GameRule> parseString(String string) {
        for (GameRule gameRule : GameRule.values()) {
            if (!gameRule.b.equalsIgnoreCase(string)) continue;
            return Optional.of(gameRule);
        }
        return Optional.empty();
    }

    public static String[] getNames() {
        String[] stringArray = new String[GameRule.values().length];
        for (int k = 0; k < GameRule.values().length; ++k) {
            stringArray[k] = GameRule.values()[k].b;
        }
        return stringArray;
    }

    public static String[] getNamesLowerCase() {
        String[] stringArray = new String[GameRule.values().length];
        for (int k = 0; k < GameRule.values().length; ++k) {
            stringArray[k] = GameRule.values()[k].b.toLowerCase();
        }
        return stringArray;
    }

    public String getName() {
        return this.b;
    }
}

