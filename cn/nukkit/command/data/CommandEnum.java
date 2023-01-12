/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.data;

import cn.nukkit.block.BlockID;
import cn.nukkit.item.ItemID;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class CommandEnum {
    public static final CommandEnum ENUM_BOOLEAN = new CommandEnum("Boolean", ImmutableList.of("true", "false"));
    public static final CommandEnum ENUM_GAMEMODE = new CommandEnum("GameMode", ImmutableList.of("survival", "creative", "s", "c", "adventure", "a", "spectator", "view", "v", "spc"));
    public static final CommandEnum ENUM_BLOCK;
    public static final CommandEnum ENUM_ITEM;
    private final String b;
    private final List<String> a;

    public CommandEnum(String string, String ... stringArray) {
        this(string, Arrays.asList(stringArray));
    }

    public CommandEnum(String string, List<String> list) {
        this.b = string;
        this.a = list;
    }

    public String getName() {
        return this.b;
    }

    public List<String> getValues() {
        return this.a;
    }

    public int hashCode() {
        return this.b.hashCode();
    }

    static {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (Field field : BlockID.class.getDeclaredFields()) {
            builder.add(field.getName().toLowerCase());
        }
        ENUM_BLOCK = new CommandEnum("Block", (List<String>)((Object)builder.build()));
        ImmutableList.Builder builder2 = ImmutableList.builder();
        for (Field field : ItemID.class.getDeclaredFields()) {
            builder2.add(field.getName().toLowerCase());
        }
        builder2.addAll(ENUM_BLOCK.getValues());
        ENUM_ITEM = new CommandEnum("Item", (List<String>)((Object)builder2.build()));
    }
}

