/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.data;

public enum CommandParamType {
    INT(0),
    FLOAT(1),
    VALUE(2),
    WILDCARD_INT(3),
    TARGET(6),
    WILDCARD_TARGET(7),
    EQUIPMENT_SLOT(10),
    STRING(11),
    BLOCK_POSITION(12),
    POSITION(13),
    MESSAGE(14),
    RAWTEXT(15),
    JSON(16),
    TEXT(15),
    COMMAND(18),
    FILE_PATH(8),
    OPERATOR(4),
    COMPARE_OPERATOR(5),
    FULL_INTEGER_RANGE(9),
    BLOCK_STATES(17);

    private final int b;

    private CommandParamType(int n2) {
        this.b = n2;
    }

    public int getId() {
        return this.b;
    }
}

