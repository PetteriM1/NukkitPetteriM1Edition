/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.NOBF;
import cn.nukkit.Server;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.f;
import cn.nukkit.level.g;
import cn.nukkit.level.h;
import cn.nukkit.level.i;
import cn.nukkit.level.j;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class GameRules {
    private final EnumMap<GameRule, Value> a = new EnumMap(GameRule.class);
    private boolean b;

    public static GameRules getDefault() {
        GameRules gameRules = new GameRules();
        gameRules.a.put(GameRule.COMMAND_BLOCKS_ENABLED, new Value<Boolean>(Type.BOOLEAN, false, 291));
        gameRules.a.put(GameRule.COMMAND_BLOCK_OUTPUT, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.DO_DAYLIGHT_CYCLE, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.DO_ENTITY_DROPS, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.DO_FIRE_TICK, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.DO_INSOMNIA, new Value<Boolean>(Type.BOOLEAN, false, 281));
        gameRules.a.put(GameRule.DO_IMMEDIATE_RESPAWN, new Value<Boolean>(Type.BOOLEAN, false, 332));
        gameRules.a.put(GameRule.DO_MOB_LOOT, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.DO_MOB_SPAWNING, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.DO_TILE_DROPS, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.DO_WEATHER_CYCLE, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.DROWNING_DAMAGE, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.FALL_DAMAGE, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.FIRE_DAMAGE, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.FREEZE_DAMAGE, new Value<Boolean>(Type.BOOLEAN, true, 440));
        gameRules.a.put(GameRule.FUNCTION_COMMAND_LIMIT, new Value<Integer>(Type.INTEGER, 10000, 332));
        gameRules.a.put(GameRule.KEEP_INVENTORY, new Value<Boolean>(Type.BOOLEAN, false));
        gameRules.a.put(GameRule.MAX_COMMAND_CHAIN_LENGTH, new Value<Integer>(Type.INTEGER, 65536));
        gameRules.a.put(GameRule.MOB_GRIEFING, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.NATURAL_REGENERATION, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.PVP, new Value<Boolean>(Type.BOOLEAN, true));
        gameRules.a.put(GameRule.RANDOM_TICK_SPEED, new Value<Integer>(Type.INTEGER, 3, 313));
        gameRules.a.put(GameRule.RESPAWN_BLOCKS_EXPLODE, new Value<Boolean>(Type.BOOLEAN, true, 475));
        gameRules.a.put(GameRule.SEND_COMMAND_FEEDBACK, new Value<Boolean>(Type.BOOLEAN, true, 361));
        gameRules.a.put(GameRule.SHOW_COORDINATES, new Value<Boolean>(Type.BOOLEAN, false));
        gameRules.a.put(GameRule.SHOW_DEATH_MESSAGES, new Value<Boolean>(Type.BOOLEAN, true, 332));
        gameRules.a.put(GameRule.SHOW_TAGS, new Value<Boolean>(Type.BOOLEAN, true, 389));
        gameRules.a.put(GameRule.SPAWN_RADIUS, new Value<Integer>(Type.INTEGER, 5, 361));
        gameRules.a.put(GameRule.TNT_EXPLODES, new Value<Boolean>(Type.BOOLEAN, true));
        return gameRules;
    }

    public Map<GameRule, Value> getGameRules() {
        return ImmutableMap.copyOf(this.a);
    }

    public boolean isStale() {
        return this.b;
    }

    public void refresh() {
        this.b = false;
    }

    public void setGameRule(GameRule gameRule, boolean bl) {
        if (!this.a.containsKey((Object)gameRule)) {
            throw new IllegalArgumentException("Gamerule does not exist");
        }
        this.a.get((Object)gameRule).a(bl, Type.BOOLEAN);
        this.b = true;
    }

    public void setGameRule(GameRule gameRule, int n) {
        if (!this.a.containsKey((Object)gameRule)) {
            throw new IllegalArgumentException("Gamerule does not exist");
        }
        this.a.get((Object)gameRule).a(n, Type.INTEGER);
        this.b = true;
    }

    public void setGameRule(GameRule gameRule, float f2) {
        if (!this.a.containsKey((Object)gameRule)) {
            throw new IllegalArgumentException("Gamerule does not exist");
        }
        this.a.get((Object)gameRule).a(Float.valueOf(f2), Type.FLOAT);
        this.b = true;
    }

    public void setGameRules(GameRule gameRule, String string) throws IllegalArgumentException {
        Preconditions.checkNotNull(gameRule, "gameRule");
        Preconditions.checkNotNull(string, "value");
        switch (this.getGameRuleType(gameRule)) {
            case BOOLEAN: {
                if (string.equalsIgnoreCase("true")) {
                    this.setGameRule(gameRule, true);
                    break;
                }
                if (string.equalsIgnoreCase("false")) {
                    this.setGameRule(gameRule, false);
                    break;
                }
                throw new IllegalArgumentException("Was not a boolean");
            }
            case INTEGER: {
                this.setGameRule(gameRule, Integer.parseInt(string));
                break;
            }
            case FLOAT: {
                this.setGameRule(gameRule, Float.parseFloat(string));
            }
        }
    }

    public boolean getBoolean(GameRule gameRule) {
        return this.a.get((Object)gameRule).c();
    }

    public int getInteger(GameRule gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        return this.a.get((Object)gameRule).a();
    }

    public float getFloat(GameRule gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        return this.a.get((Object)gameRule).b();
    }

    public String getString(GameRule gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        return this.a.get((Object)gameRule).a.toString();
    }

    public Type getGameRuleType(GameRule gameRule) {
        Preconditions.checkNotNull(gameRule, "gameRule");
        return this.a.get((Object)gameRule).getType();
    }

    public boolean hasRule(GameRule gameRule) {
        return this.a.containsKey((Object)gameRule);
    }

    public GameRule[] getRules() {
        return this.a.keySet().toArray(new GameRule[0]);
    }

    public CompoundTag writeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        for (Map.Entry<GameRule, Value> entry : this.a.entrySet()) {
            compoundTag.putString(entry.getKey().getName(), entry.getValue().a.toString());
        }
        return compoundTag;
    }

    public void readNBT(CompoundTag compoundTag) {
        Preconditions.checkNotNull(compoundTag);
        for (String string : compoundTag.getTags().keySet()) {
            Optional<GameRule> optional = GameRule.parseString(string);
            if (!optional.isPresent()) continue;
            this.setGameRules(optional.get(), compoundTag.getString(string));
        }
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }

    @NOBF
    public static class Value<T> {
        private final Type d;
        private T a;
        private boolean c;
        private int b;

        public Value(Type type, T t) {
            this.d = type;
            this.a = t;
        }

        public Value(Type type, T t, int n) {
            this.d = type;
            this.a = t;
            this.b = n;
        }

        private void a(T t, Type type) {
            if (this.d != type) {
                throw new UnsupportedOperationException("Rule not of type " + type.name().toLowerCase());
            }
            this.a = t;
        }

        public boolean isCanBeChanged() {
            return this.c;
        }

        public void setCanBeChanged(boolean bl) {
            this.c = bl;
        }

        public Type getType() {
            return this.d;
        }

        public int getMinProtocol() {
            return this.b;
        }

        private boolean c() {
            if (this.d != Type.BOOLEAN) {
                throw new UnsupportedOperationException("Rule not of type boolean");
            }
            return (Boolean)this.a;
        }

        private int a() {
            if (this.d != Type.INTEGER) {
                throw new UnsupportedOperationException("Rule not of type integer");
            }
            return (Integer)this.a;
        }

        private float b() {
            if (this.d != Type.FLOAT) {
                throw new UnsupportedOperationException("Rule not of type float");
            }
            return ((Float)this.a).floatValue();
        }

        public void write(BinaryStream binaryStream) {
            Server.mvw("GameRules#write(BinaryStream)");
            this.write(ProtocolInfo.CURRENT_PROTOCOL, binaryStream);
        }

        public void write(int n, BinaryStream binaryStream) {
            if (n >= 440) {
                binaryStream.putBoolean(this.c);
            }
            binaryStream.putUnsignedVarInt(this.d.ordinal());
            this.d.a(binaryStream, this);
        }

        private static UnsupportedOperationException a(UnsupportedOperationException unsupportedOperationException) {
            return unsupportedOperationException;
        }
    }

    @NOBF
    public static abstract class Type
    extends Enum<Type> {
        public static final /* enum */ Type UNKNOWN = new f();
        public static final /* enum */ Type BOOLEAN = new g();
        public static final /* enum */ Type INTEGER = new h();
        public static final /* enum */ Type FLOAT = new i();
        private static final /* synthetic */ Type[] a;

        public static Type[] values() {
            return (Type[])a.clone();
        }

        public static Type valueOf(String string) {
            return Enum.valueOf(Type.class, string);
        }

        private Type() {
        }

        abstract void a(BinaryStream var1, Value var2);

        /* synthetic */ Type(String string, int n, j j2) {
            this();
        }

        static {
            a = new Type[]{UNKNOWN, BOOLEAN, INTEGER, FLOAT};
        }
    }
}

