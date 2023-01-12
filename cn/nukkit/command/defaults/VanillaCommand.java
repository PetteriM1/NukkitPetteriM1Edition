/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;

public abstract class VanillaCommand
extends Command {
    public VanillaCommand(String string) {
        super(string);
    }

    public VanillaCommand(String string, String string2) {
        super(string, string2);
    }

    public VanillaCommand(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public VanillaCommand(String string, String string2, String string3, String[] stringArray) {
        super(string, string2, string3, stringArray);
    }
}

