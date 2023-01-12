/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.command.data.CommandData;
import cn.nukkit.command.data.CommandDataVersions;
import cn.nukkit.command.data.CommandOverload;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.BinaryStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.ObjIntConsumer;

public class AvailableCommandsPacket
extends DataPacket {
    public static final byte NETWORK_ID = 76;
    private static final ObjIntConsumer<BinaryStream> g = (binaryStream, n) -> binaryStream.putByte((byte)n);
    private static final ObjIntConsumer<BinaryStream> e = BinaryStream::putLShort;
    private static final ObjIntConsumer<BinaryStream> f = BinaryStream::putLInt;
    public static final int ARG_FLAG_VALID = 0x100000;
    public static final int ARG_FLAG_ENUM = 0x200000;
    public static final int ARG_FLAG_POSTFIX = 0x1000000;
    public static final int ARG_FLAG_SOFT_ENUM = 0x4000000;
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
    public final Map<String, List<String>> softEnums = new HashMap<String, List<String>>();

    @Override
    public byte pid() {
        return 76;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        block1: {
            this.reset();
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            LinkedHashSet linkedHashSet2 = new LinkedHashSet();
            LinkedHashSet linkedHashSet3 = new LinkedHashSet();
            this.commands.forEach((string, commandDataVersions) -> {
                CommandData commandData = commandDataVersions.versions.get(0);
                if (commandData.aliases != null) {
                    linkedHashSet3.add(commandData.aliases);
                    linkedHashSet.addAll(commandData.aliases.getValues());
                }
                for (CommandOverload commandOverload : commandData.overloads.values()) {
                    for (CommandParameter commandParameter : commandOverload.input.parameters) {
                        if (commandParameter.enumData != null) {
                            linkedHashSet3.add(commandParameter.enumData);
                            linkedHashSet.addAll(commandParameter.enumData.getValues());
                        }
                        if (commandParameter.postFix == null) continue;
                        linkedHashSet2.add(commandParameter.postFix);
                    }
                }
            });
            ArrayList arrayList = new ArrayList(linkedHashSet);
            ArrayList arrayList2 = new ArrayList(linkedHashSet3);
            ArrayList arrayList3 = new ArrayList(linkedHashSet2);
            this.putUnsignedVarInt(arrayList.size());
            arrayList.forEach(this::putString);
            this.putUnsignedVarInt(arrayList3.size());
            arrayList3.forEach(this::putString);
            ObjIntConsumer<BinaryStream> objIntConsumer = arrayList.size() < 256 ? g : (arrayList.size() < 65536 ? e : f);
            this.putUnsignedVarInt(arrayList2.size());
            arrayList2.forEach(commandEnum -> {
                this.putString(commandEnum.getName());
                List<String> list2 = commandEnum.getValues();
                this.putUnsignedVarInt(list2.size());
                for (String string : list2) {
                    int n = arrayList.indexOf(string);
                    if (n < 0) {
                        throw new IllegalStateException("Enum value '" + string + "' not found");
                    }
                    objIntConsumer.accept(this, n);
                }
            });
            this.putUnsignedVarInt(this.commands.size());
            this.commands.forEach((string, commandDataVersions) -> {
                CommandData commandData = commandDataVersions.versions.get(0);
                this.putString((String)string);
                this.putString(commandData.description);
                if (this.protocol >= 448) {
                    this.putLShort(commandData.flags);
                } else {
                    this.putByte((byte)commandData.flags);
                }
                this.putByte((byte)commandData.permission);
                this.putLInt(commandData.aliases == null ? -1 : arrayList2.indexOf(commandData.aliases));
                this.putUnsignedVarInt(commandData.overloads.size());
                for (CommandOverload commandOverload : commandData.overloads.values()) {
                    this.putUnsignedVarInt(commandOverload.input.parameters.length);
                    for (CommandParameter commandParameter : commandOverload.input.parameters) {
                        int n;
                        this.putString(commandParameter.name);
                        int n2 = 0;
                        if (commandParameter.postFix != null) {
                            n = arrayList3.indexOf(commandParameter.postFix);
                            if (n < 0) {
                                throw new IllegalStateException("Postfix '" + commandParameter.postFix + "' isn't in postfix array");
                            }
                            n2 = 0x1000000 | n;
                        } else {
                            n2 |= 0x100000;
                            if (commandParameter.enumData != null) {
                                n2 |= 0x200000 | arrayList2.indexOf(commandParameter.enumData);
                            } else {
                                n = commandParameter.type.getId();
                                if (this.protocol >= 524) {
                                    switch (n) {
                                        case 0: {
                                            n = 1;
                                            break;
                                        }
                                        case 1: {
                                            n = 3;
                                            break;
                                        }
                                        case 2: {
                                            n = 4;
                                            break;
                                        }
                                        case 3: {
                                            n = 5;
                                            break;
                                        }
                                        case 4: {
                                            n = 6;
                                            break;
                                        }
                                        case 5: {
                                            n = 7;
                                            break;
                                        }
                                        case 6: {
                                            n = 8;
                                            break;
                                        }
                                        case 7: {
                                            n = 10;
                                            break;
                                        }
                                        case 8: {
                                            n = 17;
                                            break;
                                        }
                                        case 9: {
                                            n = 23;
                                            break;
                                        }
                                        case 10: {
                                            n = 38;
                                            break;
                                        }
                                        case 11: {
                                            n = 39;
                                            break;
                                        }
                                        case 12: {
                                            n = 47;
                                            break;
                                        }
                                        case 13: {
                                            n = 48;
                                            break;
                                        }
                                        case 14: {
                                            n = 51;
                                            break;
                                        }
                                        case 15: {
                                            n = 53;
                                            break;
                                        }
                                        case 16: {
                                            n = 57;
                                            break;
                                        }
                                        case 17: {
                                            n = 67;
                                            break;
                                        }
                                        case 18: {
                                            n = 70;
                                        }
                                    }
                                } else if (this.protocol >= 503) {
                                    switch (n) {
                                        case 0: {
                                            n = 1;
                                            break;
                                        }
                                        case 1: {
                                            n = 3;
                                            break;
                                        }
                                        case 2: {
                                            n = 4;
                                            break;
                                        }
                                        case 3: {
                                            n = 5;
                                            break;
                                        }
                                        case 4: {
                                            n = 6;
                                            break;
                                        }
                                        case 5: {
                                            n = 52;
                                            break;
                                        }
                                        case 6: {
                                            n = 7;
                                            break;
                                        }
                                        case 7: {
                                            n = 9;
                                            break;
                                        }
                                        case 8: {
                                            n = 16;
                                            break;
                                        }
                                        case 9: {
                                            n = 52;
                                            break;
                                        }
                                        case 10: {
                                            n = 37;
                                            break;
                                        }
                                        case 11: {
                                            n = 38;
                                            break;
                                        }
                                        case 12: {
                                            n = 46;
                                            break;
                                        }
                                        case 13: {
                                            n = 47;
                                            break;
                                        }
                                        case 14: {
                                            n = 50;
                                            break;
                                        }
                                        case 15: {
                                            n = 52;
                                            break;
                                        }
                                        case 16: {
                                            n = 56;
                                            break;
                                        }
                                        case 17: {
                                            n = 52;
                                            break;
                                        }
                                        case 18: {
                                            n = 69;
                                        }
                                    }
                                } else if (this.protocol >= 428) {
                                    switch (n) {
                                        case 0: {
                                            n = 1;
                                            break;
                                        }
                                        case 1: {
                                            n = 3;
                                            break;
                                        }
                                        case 2: {
                                            n = 4;
                                            break;
                                        }
                                        case 3: {
                                            n = 5;
                                            break;
                                        }
                                        case 4: {
                                            n = 6;
                                            break;
                                        }
                                        case 5: {
                                            n = 46;
                                            break;
                                        }
                                        case 6: {
                                            n = 7;
                                            break;
                                        }
                                        case 7: {
                                            n = 8;
                                            break;
                                        }
                                        case 8: {
                                            n = 16;
                                            break;
                                        }
                                        case 9: {
                                            n = 46;
                                            break;
                                        }
                                        case 10: {
                                            n = 46;
                                            break;
                                        }
                                        case 11: {
                                            n = 32;
                                            break;
                                        }
                                        case 12: {
                                            n = 40;
                                            break;
                                        }
                                        case 13: {
                                            n = 41;
                                            break;
                                        }
                                        case 14: {
                                            n = 44;
                                            break;
                                        }
                                        case 15: {
                                            n = 46;
                                            break;
                                        }
                                        case 16: {
                                            n = 50;
                                            break;
                                        }
                                        case 17: {
                                            n = 46;
                                            break;
                                        }
                                        case 18: {
                                            n = 63;
                                        }
                                    }
                                } else if (this.protocol >= 419) {
                                    switch (n) {
                                        case 0: {
                                            n = 1;
                                            break;
                                        }
                                        case 1: {
                                            n = 2;
                                            break;
                                        }
                                        case 2: {
                                            n = 3;
                                            break;
                                        }
                                        case 3: {
                                            n = 4;
                                            break;
                                        }
                                        case 4: {
                                            n = 5;
                                            break;
                                        }
                                        case 5: {
                                            n = 45;
                                            break;
                                        }
                                        case 6: {
                                            n = 6;
                                            break;
                                        }
                                        case 7: {
                                            n = 7;
                                            break;
                                        }
                                        case 8: {
                                            n = 15;
                                            break;
                                        }
                                        case 9: {
                                            n = 45;
                                            break;
                                        }
                                        case 10: {
                                            n = 45;
                                            break;
                                        }
                                        case 11: {
                                            n = 31;
                                            break;
                                        }
                                        case 12: {
                                            n = 39;
                                            break;
                                        }
                                        case 13: {
                                            n = 40;
                                            break;
                                        }
                                        case 14: {
                                            n = 43;
                                            break;
                                        }
                                        case 15: {
                                            n = 45;
                                            break;
                                        }
                                        case 16: {
                                            n = 49;
                                            break;
                                        }
                                        case 17: {
                                            n = 45;
                                            break;
                                        }
                                        case 18: {
                                            n = 56;
                                        }
                                    }
                                } else if (this.protocol >= 388) {
                                    switch (n) {
                                        case 0: {
                                            n = 1;
                                            break;
                                        }
                                        case 1: {
                                            n = 2;
                                            break;
                                        }
                                        case 2: {
                                            n = 3;
                                            break;
                                        }
                                        case 3: {
                                            n = 4;
                                            break;
                                        }
                                        case 4: {
                                            n = 5;
                                            break;
                                        }
                                        case 5: {
                                            n = 43;
                                            break;
                                        }
                                        case 6: {
                                            n = 6;
                                            break;
                                        }
                                        case 7: {
                                            n = 7;
                                            break;
                                        }
                                        case 8: {
                                            n = 14;
                                            break;
                                        }
                                        case 9: {
                                            n = 43;
                                            break;
                                        }
                                        case 10: {
                                            n = 43;
                                            break;
                                        }
                                        case 11: {
                                            n = 29;
                                            break;
                                        }
                                        case 12: {
                                            n = 37;
                                            break;
                                        }
                                        case 13: {
                                            n = 38;
                                            break;
                                        }
                                        case 14: {
                                            n = 41;
                                            break;
                                        }
                                        case 15: {
                                            n = 43;
                                            break;
                                        }
                                        case 16: {
                                            n = 47;
                                            break;
                                        }
                                        case 17: {
                                            n = 43;
                                            break;
                                        }
                                        case 18: {
                                            n = 54;
                                        }
                                    }
                                } else if (this.protocol >= 340) {
                                    switch (n) {
                                        case 0: {
                                            n = 1;
                                            break;
                                        }
                                        case 1: {
                                            n = 2;
                                            break;
                                        }
                                        case 2: {
                                            n = 3;
                                            break;
                                        }
                                        case 3: {
                                            n = 4;
                                            break;
                                        }
                                        case 4: {
                                            n = 5;
                                            break;
                                        }
                                        case 5: {
                                            n = 34;
                                            break;
                                        }
                                        case 6: {
                                            n = 6;
                                            break;
                                        }
                                        case 7: {
                                            n = 7;
                                            break;
                                        }
                                        case 8: {
                                            n = 14;
                                            break;
                                        }
                                        case 9: {
                                            n = 34;
                                            break;
                                        }
                                        case 10: {
                                            n = 34;
                                            break;
                                        }
                                        case 11: {
                                            n = 27;
                                            break;
                                        }
                                        case 12: {
                                            n = 29;
                                            break;
                                        }
                                        case 13: {
                                            n = 29;
                                            break;
                                        }
                                        case 14: {
                                            n = 32;
                                            break;
                                        }
                                        case 15: {
                                            n = 34;
                                            break;
                                        }
                                        case 16: {
                                            n = 37;
                                            break;
                                        }
                                        case 17: {
                                            n = 34;
                                            break;
                                        }
                                        case 18: {
                                            n = 44;
                                        }
                                    }
                                } else if (this.protocol >= 332) {
                                    switch (n) {
                                        case 0: {
                                            n = 1;
                                            break;
                                        }
                                        case 1: {
                                            n = 2;
                                            break;
                                        }
                                        case 2: {
                                            n = 3;
                                            break;
                                        }
                                        case 3: {
                                            n = 4;
                                            break;
                                        }
                                        case 4: {
                                            n = 5;
                                            break;
                                        }
                                        case 5: {
                                            n = 35;
                                            break;
                                        }
                                        case 6: {
                                            n = 6;
                                            break;
                                        }
                                        case 7: {
                                            n = 7;
                                            break;
                                        }
                                        case 8: {
                                            n = 15;
                                            break;
                                        }
                                        case 9: {
                                            n = 35;
                                            break;
                                        }
                                        case 10: {
                                            n = 35;
                                            break;
                                        }
                                        case 11: {
                                            n = 28;
                                            break;
                                        }
                                        case 12: {
                                            n = 30;
                                            break;
                                        }
                                        case 13: {
                                            n = 30;
                                            break;
                                        }
                                        case 14: {
                                            n = 33;
                                            break;
                                        }
                                        case 15: {
                                            n = 35;
                                            break;
                                        }
                                        case 16: {
                                            n = 38;
                                            break;
                                        }
                                        case 17: {
                                            n = 35;
                                            break;
                                        }
                                        case 18: {
                                            n = 45;
                                        }
                                    }
                                } else {
                                    switch (n) {
                                        case 0: {
                                            n = 1;
                                            break;
                                        }
                                        case 1: {
                                            n = 2;
                                            break;
                                        }
                                        case 2: {
                                            n = 3;
                                            break;
                                        }
                                        case 3: {
                                            n = 4;
                                            break;
                                        }
                                        case 4: {
                                            n = 5;
                                            break;
                                        }
                                        case 5: {
                                            n = 33;
                                            break;
                                        }
                                        case 6: {
                                            n = 6;
                                            break;
                                        }
                                        case 7: {
                                            n = 7;
                                            break;
                                        }
                                        case 8: {
                                            n = 14;
                                            break;
                                        }
                                        case 9: {
                                            n = 33;
                                            break;
                                        }
                                        case 10: {
                                            n = 33;
                                            break;
                                        }
                                        case 11: {
                                            n = 26;
                                            break;
                                        }
                                        case 12: {
                                            n = 28;
                                            break;
                                        }
                                        case 13: {
                                            n = 28;
                                            break;
                                        }
                                        case 14: {
                                            n = 31;
                                            break;
                                        }
                                        case 15: {
                                            n = 33;
                                            break;
                                        }
                                        case 16: {
                                            n = 36;
                                            break;
                                        }
                                        case 17: {
                                            n = 33;
                                            break;
                                        }
                                        case 18: {
                                            n = 43;
                                        }
                                    }
                                }
                                n2 |= n;
                            }
                        }
                        this.putLInt(n2);
                        this.putBoolean(commandParameter.optional);
                        if (this.protocol < 340) continue;
                        this.putByte(commandParameter.options);
                    }
                }
            });
            if (this.protocol > 274) {
                this.putUnsignedVarInt(this.softEnums.size());
                this.softEnums.forEach((string, list) -> {
                    this.putString((String)string);
                    this.putUnsignedVarInt(list.size());
                    list.forEach(this::putString);
                });
            }
            if (this.protocol < 407) break block1;
            this.putUnsignedVarInt(0L);
        }
    }

    public String toString() {
        return "AvailableCommandsPacket(commands=" + this.commands + ", softEnums=" + this.softEnums + ")";
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

