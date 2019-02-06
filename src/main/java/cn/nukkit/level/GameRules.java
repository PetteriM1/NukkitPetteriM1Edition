package cn.nukkit.level;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.BinaryStream;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Created by CreeperFace on 25.6.2017.
 */
@SuppressWarnings("all")
public class GameRules {

    private final TreeMap<String, ValueOld> theGameRules = new TreeMap<>();
    private final EnumMap<GameRule, Value> gameRules = new EnumMap<>(GameRule.class);
    private boolean stale;

    public GameRules() {
        this.addGameRule("doFireTick", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("mobGriefing", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("keepInventory", "false", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobSpawning", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobLoot", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doTileDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doEntityDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("commandBlockOutput", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("naturalRegeneration", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doDaylightCycle", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doWeatherCycle", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("drowningdamage", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("falldamage", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("firedamage", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("sendCommandFeedback", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("showcoordinates", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("pvp", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("tntexplodes", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("showDeathMessages", "true", ValueType.BOOLEAN_VALUE);
    }

    public void addGameRule(String key, String value, ValueType type) {
        this.theGameRules.put(key, new GameRules.ValueOld(value, type));
    }

    public void setGameRule(String key, String ruleValue) {
        ValueOld value = this.theGameRules.get(key);

        if (value != null) {
            value.setValue(ruleValue);
        } else {
            this.addGameRule(key, ruleValue, ValueType.ANY_VALUE);
        }
    }

    public String getString(String name) {
        ValueOld value = this.theGameRules.get(name);
        return value != null ? value.getString() : "";
    }

    public boolean getBoolean(String name) {
        ValueOld value = this.theGameRules.get(name);
        return value != null && value.getBoolean();
    }

    public int getInt(String name) {
        ValueOld value = this.theGameRules.get(name);
        return value != null ? value.getInt() : 0;
    }

    public boolean hasRule(String name) {
        return this.theGameRules.containsKey(name);
    }

    public boolean areSameType(String key, ValueType otherValue) {
        ValueOld value = this.theGameRules.get(key);
        return value != null && (value.getType() == otherValue || otherValue == ValueType.ANY_VALUE);
    }

    public static GameRules getDefault() {
        GameRules gameRules = new GameRules();

        gameRules.gameRules.put(GameRule.COMMAND_BLOCK_OUTPUT, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.DO_DAYLIGHT_CYCLE, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.DO_ENTITY_DROPS, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.DO_FIRE_TICK, new Value(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.DO_MOB_LOOT, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.DO_MOB_SPAWNING, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.DO_TILE_DROPS, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.DO_WEATHER_CYCLE, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.DROWNING_DAMAGE, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.FALL_DAMAGE, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.FIRE_DAMAGE, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.KEEP_INVENTORY, new Value<>(Type.BOOLEAN, false));
        gameRules.gameRules.put(GameRule.MOB_GRIEFING, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.NATURAL_REGENERATION, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.PVP, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.SEND_COMMAND_FEEDBACK, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.SHOW_COORDINATES, new Value<>(Type.BOOLEAN, false));
        gameRules.gameRules.put(GameRule.TNT_EXPLODES, new Value<>(Type.BOOLEAN, true));
        gameRules.gameRules.put(GameRule.SHOW_DEATH_MESSAGE, new Value<>(Type.BOOLEAN, true));

        return gameRules;
    }

    public Map<GameRule, Value> getGameRules() {
        return ImmutableMap.copyOf(gameRules);
    }

    public boolean isStale() {
        return stale;
    }

    public void refresh() {
        stale = false;
    }

    public void setGameRule(GameRule gameRule, boolean value) {
        if (!gameRules.containsKey(gameRule)) {
            throw new IllegalArgumentException("Gamerule does not exist");
        }
        gameRules.get(gameRule).setValue(value, Type.BOOLEAN);
        stale = true;
    }

    public void setGameRule(GameRule gameRule, int value) {
        if (!gameRules.containsKey(gameRule)) {
            throw new IllegalArgumentException("Gamerule does not exist");
        }
        gameRules.get(gameRule).setValue(value, Type.INTEGER);
        stale = true;
    }

    public void setGameRule(GameRule gameRule, float value) {
        if (!gameRules.containsKey(gameRule)) {
            throw new IllegalArgumentException("Gamerule does not exist");
        }
        gameRules.get(gameRule).setValue(value, Type.FLOAT);
        stale = true;
    }

    public void setGameRules(GameRule gameRule, String value) throws IllegalArgumentException {
        Preconditions.checkNotNull(gameRule, "gameRule");
        Preconditions.checkNotNull(value, "value");

        switch (getGameRuleType(gameRule)) {
            case BOOLEAN:
                if (value.equalsIgnoreCase("true")) {
                    setGameRule(gameRule, true);
                } else if (value.equalsIgnoreCase("false")) {
                    setGameRule(gameRule, false);
                } else {
                    throw new IllegalArgumentException("Was not a boolean");
                }
                break;
            case INTEGER:
                setGameRule(gameRule, Integer.parseInt(value));
                break;
            case FLOAT:
                setGameRule(gameRule, Float.parseFloat(value));
        }
    }

    public boolean getBoolean(GameRule gameRule) {
        return gameRules.get(gameRule).getValueAsBoolean();
    }

    public int getInteger(GameRule gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        return gameRules.get(gameRule).getValueAsInteger();
    }

    public float getFloat(GameRule gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        return gameRules.get(gameRule).getValueAsFloat();
    }

    public String getString(GameRule gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        return gameRules.get(gameRule).value.toString();
    }

    public Type getGameRuleType(GameRule gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        return gameRules.get(gameRule).getType();
    }

    public boolean hasRule(GameRule gameRule) {
        return gameRules.containsKey(gameRule);
    }

    public GameRule[] getRules() {
        return gameRules.keySet().toArray(new GameRule[0]);
    }

    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();

        for (Entry<GameRule, Value> entry : gameRules.entrySet()) {
            nbt.putString(entry.getKey().getName(), entry.getValue().value.toString());
        }

        return nbt;
    }

    public void readNBT(CompoundTag nbt) {
        Preconditions.checkNotNull(nbt);
        for (String key : nbt.getTags().keySet()) {
            Optional<GameRule> gameRule = GameRule.parseString(key);
            if (!gameRule.isPresent()) {
                continue;
            }

            setGameRules(gameRule.get(), nbt.getString(key));
        }
    }

    private static class ValueOld {

        private String valueString;

        private boolean valueBoolean;

        private int valueInteger;

        private final ValueType type;

        public ValueOld(String value, ValueType type) {
            this.type = type;
            this.setValue(value);
        }

        public void setValue(String value) {
            this.valueString = value;
            this.valueBoolean = Boolean.parseBoolean(value);
            this.valueInteger = this.valueBoolean ? 1 : 0;

            try {
                this.valueInteger = Integer.parseInt(value);
            } catch (NumberFormatException ex) {}
        }

        public String getString() {
            return this.valueString;
        }

        public boolean getBoolean() {
            return this.valueBoolean;
        }

        public int getInt() {
            return this.valueInteger;
        }

        public ValueType getType() {
            return this.type;
        }
    }

    public enum ValueType {
        ANY_VALUE,
        BOOLEAN_VALUE,
        NUMERICAL_VALUE
    }

    @SuppressWarnings("rawtypes")
    public enum Type {

        UNKNOWN {
            @Override
            void write(BinaryStream pk, Value value) {
            }
        },
        BOOLEAN {
            @Override
            void write(BinaryStream pk, Value value) {
                pk.putBoolean(value.getValueAsBoolean());
            }
        },
        INTEGER {
            @Override
            void write(BinaryStream pk, Value value) {
                pk.putUnsignedVarInt(value.getValueAsInteger());
            }
        },
        FLOAT {
            @Override
            void write(BinaryStream pk, Value value) {
                pk.putLFloat(value.getValueAsFloat());
            }
        };

        abstract void write(BinaryStream pk, Value value);
    }

    public static class Value<T> {

        private final Type type;
        private T value;

        public Value(Type type, T value) {
            this.type = type;
            this.value = value;
        }

        private void setValue(T value, Type type) {
            if (this.type != type) {
                throw new UnsupportedOperationException("Rule not of type " + type.name().toLowerCase());
            }
            this.value = value;
        }

        public Type getType() {
            return type;
        }

        private boolean getValueAsBoolean() {
            if (type != Type.BOOLEAN) {
                throw new UnsupportedOperationException("Rule not of type boolean");
            }
            return (Boolean) value;
        }

        private int getValueAsInteger() {
            if (type != Type.INTEGER) {
                throw new UnsupportedOperationException("Rule not of type integer");
            }
            return (Integer) value;
        }

        private float getValueAsFloat() {
            if (type != Type.FLOAT) {
                throw new UnsupportedOperationException("Rule not of type float");
            }
            return (Float) value;
        }

        public void write(BinaryStream pk) {
            pk.putUnsignedVarInt(type.ordinal());
            type.write(pk, this);
        }
    }
}
