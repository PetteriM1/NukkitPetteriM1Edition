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

    /* From < 1.16.100 */
    public static final int ARG_TYPE_INT = 1;
    public static final int ARG_TYPE_FLOAT = 2;
    public static final int ARG_TYPE_VALUE = 3;
    public static final int ARG_TYPE_WILDCARD_INT = 4;
    public static final int ARG_TYPE_OPERATOR = 5;
    public static final int ARG_TYPE_TARGET = 6;
    public static final int ARG_TYPE_WILDCARD_TARGET = 7;
    public static final int ARG_TYPE_FILE_PATH = 14;
    public static final int ARG_TYPE_STRING = 29;
    public static final int ARG_TYPE_BLOCK_POSITION = 37;
    public static final int ARG_TYPE_POSITION = 38;
    public static final int ARG_TYPE_MESSAGE = 41;
    public static final int ARG_TYPE_RAWTEXT = 43;
    public static final int ARG_TYPE_JSON = 47;
    public static final int ARG_TYPE_COMMAND = 54;

    public Map<String, CommandDataVersions> commands;
    public final Map<String, List<String>> softEnums = new HashMap<>();

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        /*commands = new HashMap<>();

        List<String> enumValues = new ArrayList<>();
        List<String> postFixes = new ArrayList<>();
        List<CommandEnum> enums = new ArrayList<>();

        int len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            enumValues.add(getString());
        }

        len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            postFixes.add(getString());
        }

        ToIntFunction<BinaryStream> indexReader;
        if (enumValues.size() < 256) {
            indexReader = READ_BYTE;
        } else if (enumValues.size() < 65536) {
            indexReader = READ_SHORT;
        } else {
            indexReader = READ_INT;
        }

        len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            String enumName = getString();
            int enumLength = (int) getUnsignedVarInt();

            List<String> values = new ArrayList<>();

            while (enumLength-- > 0) {
                int index = indexReader.applyAsInt(this);

                String enumValue;

                if (index < 0 || (enumValue = enumValues.get(index)) == null) {
                    throw new IllegalStateException("Enum value not found for index " + index);
                }

                values.add(enumValue);
            }

            enums.add(new CommandEnum(enumName, values));
        }

        len = (int) getUnsignedVarInt();

        while (len-- > 0) {
            String name = getString();
            String description = getString();
            int flags = getByte();
            int permission = getByte();
            CommandEnum alias = null;

            int aliasIndex = getLInt();
            if (aliasIndex >= 0) {
                alias = enums.get(aliasIndex);
            }

            Map<String, CommandOverload> overloads = new HashMap<>();

            int length = (int) getUnsignedVarInt();
            while (length-- > 0) {
                CommandOverload overload = new CommandOverload();

                int paramLen = (int) getUnsignedVarInt();

                overload.input.parameters = new CommandParameter[paramLen];
                for (int i = 0; i < paramLen; i++) {
                    String paramName = getString();
                    int type = getLInt();
                    boolean optional = getBoolean();

                    CommandParameter parameter = new CommandParameter(paramName, optional);

                    if ((type & ARG_FLAG_POSTFIX) != 0) {
                        parameter.postFix = postFixes.get(type & 0xffff);
                    } else if ((type & ARG_FLAG_VALID) == 0) {
                        throw new IllegalStateException("Invalid parameter type received");
                    } else {
                        int index = type & 0xffff;
                        if ((type & ARG_FLAG_ENUM) != 0) {
                            parameter.enumData = enums.get(index);
                        } else if ((type & ARG_FLAG_SOFT_ENUM) != 0) {
                            // TODO: soft enums
                        } else {
                            throw new IllegalStateException("Unknown parameter type!");
                        }
                    }

                    overload.input.parameters[i] = parameter;
                }

                overloads.put(Integer.toString(length), overload);
            }

            CommandData data = new CommandData();
            data.aliases = alias;
            data.overloads = overloads;
            data.description = description;
            data.flags = flags;
            data.permission = permission;

            CommandDataVersions versions = new CommandDataVersions();
            versions.versions.add(data);

            this.commands.put(name, versions);
        }*/
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

        this.putUnsignedVarInt(enumValues.size());
        enumValues.forEach(this::putString);

        this.putUnsignedVarInt(postFixes.size());
        postFixes.forEach(this::putString);

        ObjIntConsumer<BinaryStream> indexWriter;
        if (enumValues.size() < 256) {
            indexWriter = WRITE_BYTE;
        } else if (enumValues.size() < 65536) {
            indexWriter = WRITE_SHORT;
        } else {
            indexWriter = WRITE_INT;
        }

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

        putUnsignedVarInt(commands.size());

        commands.forEach((name, cmdData) -> {
            CommandData data = cmdData.versions.get(0);

            putString(name);
            putString(data.description);
            if (this.protocol >= ProtocolInfo.v1_17_10) {
                putLShort((byte) data.flags);
            } else {
                putByte((byte) data.flags);
            }
            putByte((byte) data.permission);

            putLInt(data.aliases == null ? -1 : enums.indexOf(data.aliases));

            putUnsignedVarInt(data.overloads.size());
            for (CommandOverload overload : data.overloads.values()) {
                putUnsignedVarInt(overload.input.parameters.length);

                for (CommandParameter parameter : overload.input.parameters) {
                    putString(parameter.name);

                    int type = 0;
                    if (parameter.postFix != null) {
                        int i = postFixes.indexOf(parameter.postFix);
                        if (i < 0) {
                            throw new IllegalStateException(
                                    "Postfix '" + parameter.postFix + "' isn't in postfix array");
                        }
                        type = ARG_FLAG_POSTFIX | i;
                    } else {
                        type |= ARG_FLAG_VALID;
                        if (parameter.enumData != null) {
                            type |= ARG_FLAG_ENUM | enums.indexOf(parameter.enumData);
                        } else {
                            int id = parameter.type.getId();
                            if (protocol < ProtocolInfo.v1_8_0) {
                                switch (parameter.type) {
                                    case STRING:
                                        id = 0x0f;
                                        break;
                                    case POSITION:
                                    case BLOCK_POSITION:
                                        id = 0x10;
                                        break;
                                    case MESSAGE:
                                        id = 0x13;
                                        break;
                                    case RAWTEXT:
                                        id = 0x15;
                                        break;
                                    case JSON:
                                        id = 0x18;
                                        break;
                                    case COMMAND:
                                        id = 0x1f;
                                        break;
                                }
                            } else if (protocol < ProtocolInfo.v1_13_0) {
                                switch (parameter.type) {
                                    case STRING:
                                        id = 27;
                                        break;
                                    case POSITION:
                                    case BLOCK_POSITION:
                                        id = 29;
                                        break;
                                    case MESSAGE:
                                        id = 32;
                                        break;
                                    case RAWTEXT:
                                        id = 34;
                                        break;
                                    case JSON:
                                        id = 37;
                                        break;
                                    case COMMAND:
                                        id = 44;
                                        break;
                                }
                            } else if (protocol >= ProtocolInfo.v1_16_210) { //TODO: proper implementation for 1.16.210 command params
                                if (id == ARG_TYPE_COMMAND) {
                                    id = 63;
                                } else if (id == ARG_TYPE_FILE_PATH) {
                                    id = id + 2; // +1 from .100 and +1 from .210
                                } else if (id >= ARG_TYPE_STRING) {
                                    id = id + 3; // +2 from .100 and +1 from .210
                                } else if (id >= ARG_TYPE_FLOAT) {
                                    id++;
                                }
                            } else if (protocol >= ProtocolInfo.v1_16_100) { //TODO: proper implementation for 1.16.100 command params
                                if (id == ARG_TYPE_FILE_PATH) {
                                    id++;
                                } else if (id >= ARG_TYPE_STRING) {
                                    id = id + 2;
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
            this.putUnsignedVarInt(0);
        }
    }
}
