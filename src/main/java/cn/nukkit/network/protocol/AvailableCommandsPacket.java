package cn.nukkit.network.protocol;

import cn.nukkit.command.data.*;
import cn.nukkit.utils.BinaryStream;
import lombok.ToString;

import java.util.*;
import java.util.function.ObjIntConsumer;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class AvailableCommandsPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.AVAILABLE_COMMANDS_PACKET;

    private static final ObjIntConsumer<BinaryStream> WRITE_BYTE = (s, v) -> s.putByte((byte) v);
    private static final ObjIntConsumer<BinaryStream> WRITE_SHORT = BinaryStream::putLShort;
    private static final ObjIntConsumer<BinaryStream> WRITE_INT = BinaryStream::putLInt;
    //private static final ToIntFunction<BinaryStream> READ_BYTE = BinaryStream::getByte;
    //private static final ToIntFunction<BinaryStream> READ_SHORT = BinaryStream::getLShort;
    //private static final ToIntFunction<BinaryStream> READ_INT = BinaryStream::getLInt;

    public static final int ARG_FLAG_VALID = 0x100000;
    public static final int ARG_FLAG_ENUM = 0x200000;
    public static final int ARG_FLAG_POSTFIX = 0x1000000;
    public static final int ARG_FLAG_SOFT_ENUM = 0x4000000;

    /* These numbers are for internal use only and will be replaced by the multiversion. */
    public static final int ARG_TYPE_INT = 0;
    public static final int ARG_TYPE_FLOAT = 1;
    public static final int ARG_TYPE_VALUE = 2;
    public static final int ARG_TYPE_WILDCARD_INT = 3;
    public static final int ARG_TYPE_OPERATOR = 4;
    public static final int ARG_TYPE_COMPARE_OPERATOR = 5;
    public static final int ARG_TYPE_TARGET = 6;
    public static final int ARG_TYPE_WILDCARD_TARGET = 7;
    public static final int ARG_TYPE_FILE_PATH = 8;
    public static final int ARG_TYPE_FULL_INTEGER_RANGE = 9;
    public static final int ARG_TYPE_EQUIPMENT_SLOT = 10;
    public static final int ARG_TYPE_STRING = 11;
    public static final int ARG_TYPE_BLOCK_POSITION = 12;
    public static final int ARG_TYPE_POSITION = 13;
    public static final int ARG_TYPE_MESSAGE = 14;
    public static final int ARG_TYPE_RAWTEXT = 15;
    public static final int ARG_TYPE_JSON = 16;
    public static final int ARG_TYPE_BLOCK_STATES = 17;
    public static final int ARG_TYPE_COMMAND = 18;

    public Map<String, CommandDataVersions> commands;
    public final Map<String, List<String>> softEnums = new HashMap<>();

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();

        LinkedHashSet<String> enumValuesSet = new LinkedHashSet<>();
        LinkedHashSet<String> postFixesSet = new LinkedHashSet<>();
        LinkedHashSet<CommandEnum> enumsSet = new LinkedHashSet<>();

        commands.forEach((name, data) -> {
            CommandData cmdData = data.versions.get(0);

            if (cmdData.aliases != null) {
                enumsSet.add(cmdData.aliases);

                enumValuesSet.addAll(cmdData.aliases.getValues());
            }

            for (CommandOverload overload : cmdData.overloads.values()) {
                for (CommandParameter parameter : overload.input.parameters) {
                    if (parameter.enumData != null) {
                        enumsSet.add(parameter.enumData);

                        enumValuesSet.addAll(parameter.enumData.getValues());
                    }

                    if (parameter.postFix != null) {
                        postFixesSet.add(parameter.postFix);
                    }
                }
            }
        });

        List<String> enumValues = new ArrayList<>(enumValuesSet);
        List<CommandEnum> enums = new ArrayList<>(enumsSet);
        List<String> postFixes = new ArrayList<>(postFixesSet);

        ObjIntConsumer<BinaryStream> indexWriter;
        if (enumValues.size() < 256) {
            indexWriter = WRITE_BYTE;
        } else if (enumValues.size() < 65536) {
            indexWriter = WRITE_SHORT;
        } else {
            indexWriter = WRITE_INT;
        }

        this.putUnsignedVarInt(enumValues.size());
        enumValues.forEach(this::putString);

        if (protocol >= ProtocolInfo.v1_20_10_21) {
            this.putUnsignedVarInt(0); //subCommandValues
        }

        this.putUnsignedVarInt(postFixes.size());
        postFixes.forEach(this::putString);

        this.putUnsignedVarInt(enums.size());
        enums.forEach((cmdEnum) -> {
            putString(cmdEnum.getName());

            List<String> values = cmdEnum.getValues();
            putUnsignedVarInt(values.size());

            for (String val : values) {
                int i = enumValues.indexOf(val);

                if (i < 0) {
                    throw new IllegalStateException("Enum value '" + val + "' not found");
                }

                indexWriter.accept(this, i);
            }
        });

        if (protocol >= ProtocolInfo.v1_20_10_21) {
            this.putUnsignedVarInt(0); //subCommandData
        }

        this.putUnsignedVarInt(commands.size());
        commands.forEach((name, cmdData) -> {
            CommandData data = cmdData.versions.get(0);

            putString(name);
            putString(data.description);
            if (protocol >= ProtocolInfo.v1_17_10) {
                putLShort(data.flags);
            } else {
                putByte((byte) data.flags);
            }
            putByte((byte) data.permission);

            putLInt(data.aliases == null ? -1 : enums.indexOf(data.aliases));

            if (protocol >= ProtocolInfo.v1_20_10_21) {
                putUnsignedVarInt(0); //subcommands
            }

            putUnsignedVarInt(data.overloads.size());
            for (CommandOverload overload : data.overloads.values()) {
                if (protocol >= ProtocolInfo.v1_20_10_21) {
                    putBoolean(false); //isChaining
                }
                putUnsignedVarInt(overload.input.parameters.length);

                for (CommandParameter parameter : overload.input.parameters) {
                    putString(parameter.name);

                    int type = 0;
                    if (parameter.postFix != null) {
                        int i = postFixes.indexOf(parameter.postFix);
                        if (i < 0) {
                            throw new IllegalStateException("Postfix '" + parameter.postFix + "' isn't in postfix array");
                        }
                        type = ARG_FLAG_POSTFIX | i;
                    } else {
                        type |= ARG_FLAG_VALID;
                        if (parameter.enumData != null) {
                            int i = enums.indexOf(parameter.enumData);
                            if (i < 0) {
                                throw new IllegalStateException("Enum '" + parameter.enumData.getName() + "' isn't in enums array");
                            }
                            type |= ARG_FLAG_ENUM | i;
                        } else {
                            int id = parameter.type.getId();
                            if (protocol >= ProtocolInfo.v1_20_70) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_TARGET:
                                        id = 8;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 10;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 17;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 23;
                                        break;
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 47;
                                        break; // shift(24, 4)
                                    case ARG_TYPE_STRING:
                                        id = 56;
                                        break; // shift(48, 8)
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 64;
                                        break;
                                    case ARG_TYPE_POSITION:
                                        id = 65;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 68;
                                        break; // +1 according to pmmp, fixes client crashing
                                    case ARG_TYPE_RAWTEXT:
                                        id = 70;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 74;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 84;
                                        break; // shift by 1 according to pmmp
                                    case ARG_TYPE_COMMAND:
                                        id = 87;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_19_80) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_TARGET:
                                        id = 8;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 10;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 17;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 23;
                                        break;
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 43;
                                        break;
                                    case ARG_TYPE_STRING:
                                        id = 44;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 52;
                                        break;
                                    case ARG_TYPE_POSITION:
                                        id = 53;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 55;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 58;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 62;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 71;
                                        break;
                                    case ARG_TYPE_COMMAND:
                                        id = 74;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_19_0_29) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_TARGET:
                                        id = 8;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 10;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 17;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 23;
                                        break;
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 38;
                                        break;
                                    case ARG_TYPE_STRING:
                                        id = 39;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 47;
                                        break;
                                    case ARG_TYPE_POSITION:
                                        id = 48;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 51;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 53;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 57;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 67;
                                        break;
                                    case ARG_TYPE_COMMAND:
                                        id = 70;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_18_30) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 52;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_TARGET:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 9;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 16;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 52;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 37;
                                        break;
                                    case ARG_TYPE_STRING:
                                        id = 38;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 46;
                                        break;
                                    case ARG_TYPE_POSITION:
                                        id = 47;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 50;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 52;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 56;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 52;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_COMMAND:
                                        id = 69;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_16_210) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 46;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_TARGET:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 8;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 16;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 46;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 46;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_STRING:
                                        id = 32;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 40;
                                        break;
                                    case ARG_TYPE_POSITION:
                                        id = 41;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 44;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 46;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 50;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 46;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_COMMAND:
                                        id = 63;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_16_100) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 2;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 45;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_TARGET:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 15;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 45;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 45;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_STRING:
                                        id = 31;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 39;
                                        break;
                                    case ARG_TYPE_POSITION:
                                        id = 40;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 43;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 45;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 49;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 45;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_COMMAND:
                                        id = 56;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_13_0) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 2;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 43;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_TARGET:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 14;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 43;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 43;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_STRING:
                                        id = 29;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 37;
                                        break;
                                    case ARG_TYPE_POSITION:
                                        id = 38;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 41;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 43;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 47;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 43;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_COMMAND:
                                        id = 54;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_10_0) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 2;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 34;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_TARGET:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 14;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 34;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 34;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_STRING:
                                        id = 27;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 29;
                                        break; // Doesn't exist? Map to POSITION
                                    case ARG_TYPE_POSITION:
                                        id = 29;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 32;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 34;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 37;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 34;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_COMMAND:
                                        id = 44;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_9_0) {
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 2;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 35;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_TARGET:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 15;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 35;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 35;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_STRING:
                                        id = 28;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 30;
                                        break; // Doesn't exist? Map to POSITION
                                    case ARG_TYPE_POSITION:
                                        id = 30;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 33;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 35;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 38;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 35;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_COMMAND:
                                        id = 45;
                                        break;
                                }
                            } else { // 1.7.0
                                switch (id) {
                                    case ARG_TYPE_INT:
                                        id = 1;
                                        break;
                                    case ARG_TYPE_FLOAT:
                                        id = 2;
                                        break;
                                    case ARG_TYPE_VALUE:
                                        id = 3;
                                        break;
                                    case ARG_TYPE_WILDCARD_INT:
                                        id = 4;
                                        break;
                                    case ARG_TYPE_OPERATOR:
                                        id = 5;
                                        break;
                                    case ARG_TYPE_COMPARE_OPERATOR:
                                        id = 33;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_TARGET:
                                        id = 6;
                                        break;
                                    case ARG_TYPE_WILDCARD_TARGET:
                                        id = 7;
                                        break;
                                    case ARG_TYPE_FILE_PATH:
                                        id = 14;
                                        break;
                                    case ARG_TYPE_FULL_INTEGER_RANGE:
                                        id = 33;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_EQUIPMENT_SLOT:
                                        id = 33;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_STRING:
                                        id = 26;
                                        break;
                                    case ARG_TYPE_BLOCK_POSITION:
                                        id = 28;
                                        break; // Doesn't exist? Map to POSITION
                                    case ARG_TYPE_POSITION:
                                        id = 28;
                                        break;
                                    case ARG_TYPE_MESSAGE:
                                        id = 31;
                                        break;
                                    case ARG_TYPE_RAWTEXT:
                                        id = 33;
                                        break;
                                    case ARG_TYPE_JSON:
                                        id = 36;
                                        break;
                                    case ARG_TYPE_BLOCK_STATES:
                                        id = 33;
                                        break; // Doesn't exist? Map to RAWTEXT
                                    case ARG_TYPE_COMMAND:
                                        id = 43;
                                        break;
                                }
                            }

                            type |= id;
                        }
                    }

                    putLInt(type);
                    putBoolean(parameter.optional);
                    if (protocol >= 340) {
                        putByte(parameter.options);
                    }
                }
            }
        });

        if (protocol > 274) {
            this.putUnsignedVarInt(softEnums.size());

            softEnums.forEach((name, values) -> {
                this.putString(name);
                this.putUnsignedVarInt(values.size());
                values.forEach(this::putString);
            });
        }

        if (protocol >= 407) {
            this.putUnsignedVarInt(0); //enumConstraints
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
