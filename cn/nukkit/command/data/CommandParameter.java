/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.data;

import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandParameter {
    public static final String ARG_TYPE_STRING;
    public static final String ARG_TYPE_STRING_ENUM;
    public static final String ARG_TYPE_BOOL;
    public static final String ARG_TYPE_TARGET;
    public static final String ARG_TYPE_PLAYER;
    public static final String ARG_TYPE_BLOCK_POS;
    public static final String ARG_TYPE_RAW_TEXT;
    public static final String ARG_TYPE_INT;
    public static final String ENUM_TYPE_ITEM_LIST;
    public static final String ENUM_TYPE_BLOCK_LIST;
    public static final String ENUM_TYPE_COMMAND_LIST;
    public static final String ENUM_TYPE_ENCHANTMENT_LIST;
    public static final String ENUM_TYPE_ENTITY_LIST;
    public static final String ENUM_TYPE_EFFECT_LIST;
    public static final String ENUM_TYPE_PARTICLE_LIST;
    public String name;
    public CommandParamType type;
    public boolean optional;
    public byte options = 0;
    public CommandEnum enumData;
    public String postFix;

    public CommandParameter(String string, String string2, boolean bl) {
        this(string, CommandParameter.fromString(string2), bl);
    }

    public CommandParameter(String string, CommandParamType commandParamType, boolean bl) {
        this.name = string;
        this.type = commandParamType;
        this.optional = bl;
    }

    public CommandParameter(String string, boolean bl) {
        this(string, CommandParamType.RAWTEXT, bl);
    }

    public CommandParameter(String string) {
        this(string, false);
    }

    public CommandParameter(String string, boolean bl, String string2) {
        this.name = string;
        this.type = CommandParamType.RAWTEXT;
        this.optional = bl;
        this.enumData = new CommandEnum(string2, new ArrayList<String>());
    }

    public CommandParameter(String string, boolean bl, String[] stringArray) {
        this.name = string;
        this.type = CommandParamType.RAWTEXT;
        this.optional = bl;
        this.enumData = new CommandEnum(string + "Enums", Arrays.asList(stringArray));
    }

    public CommandParameter(String string, String string2) {
        this(string, false, string2);
    }

    public CommandParameter(String string, String[] stringArray) {
        this(string, false, stringArray);
    }

    private CommandParameter(String string, boolean bl, CommandParamType commandParamType, CommandEnum commandEnum, String string2) {
        this.name = string;
        this.optional = bl;
        this.type = commandParamType;
        this.enumData = commandEnum;
        this.postFix = string2;
    }

    public static CommandParameter newType(String string, CommandParamType commandParamType) {
        return CommandParameter.newType(string, false, commandParamType);
    }

    public static CommandParameter newType(String string, boolean bl, CommandParamType commandParamType) {
        return new CommandParameter(string, bl, commandParamType, null, null);
    }

    public static CommandParameter newEnum(String string, String[] stringArray) {
        return CommandParameter.newEnum(string, false, stringArray);
    }

    public static CommandParameter newEnum(String string, boolean bl, String[] stringArray) {
        return CommandParameter.newEnum(string, bl, new CommandEnum(string + "Enums", stringArray));
    }

    public static CommandParameter newEnum(String string, String string2) {
        return CommandParameter.newEnum(string, false, string2);
    }

    public static CommandParameter newEnum(String string, boolean bl, String string2) {
        return CommandParameter.newEnum(string, bl, new CommandEnum(string2, new ArrayList<String>()));
    }

    public static CommandParameter newEnum(String string, CommandEnum commandEnum) {
        return CommandParameter.newEnum(string, false, commandEnum);
    }

    public static CommandParameter newEnum(String string, boolean bl, CommandEnum commandEnum) {
        return new CommandParameter(string, bl, CommandParamType.RAWTEXT, commandEnum, null);
    }

    public static CommandParameter newPostfix(String string, String string2) {
        return CommandParameter.newPostfix(string, false, string2);
    }

    public static CommandParameter newPostfix(String string, boolean bl, String string2) {
        return new CommandParameter(string, bl, CommandParamType.RAWTEXT, null, string2);
    }

    protected static CommandParamType fromString(String string) {
        switch (string) {
            case "string": 
            case "stringenum": {
                return CommandParamType.STRING;
            }
            case "target": {
                return CommandParamType.TARGET;
            }
            case "blockpos": {
                return CommandParamType.POSITION;
            }
            case "rawtext": {
                return CommandParamType.RAWTEXT;
            }
            case "int": {
                return CommandParamType.INT;
            }
        }
        return CommandParamType.RAWTEXT;
    }

    static {
        ARG_TYPE_TARGET = "target";
        ARG_TYPE_RAW_TEXT = "rawtext";
        ENUM_TYPE_ITEM_LIST = "Item";
        ARG_TYPE_INT = "int";
        ARG_TYPE_BOOL = "bool";
        ARG_TYPE_STRING = "string";
        ENUM_TYPE_EFFECT_LIST = "effectType";
        ARG_TYPE_STRING_ENUM = "stringenum";
        ENUM_TYPE_COMMAND_LIST = "commandName";
        ENUM_TYPE_BLOCK_LIST = "Block";
        ARG_TYPE_BLOCK_POS = "blockpos";
        ENUM_TYPE_ENCHANTMENT_LIST = "enchantmentType";
        ENUM_TYPE_PARTICLE_LIST = "particleType";
        ARG_TYPE_PLAYER = "target";
        ENUM_TYPE_ENTITY_LIST = "entityType";
    }
}

