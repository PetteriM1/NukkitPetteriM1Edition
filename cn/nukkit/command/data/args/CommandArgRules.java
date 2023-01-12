/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.data.args;

public class CommandArgRules {
    private boolean c;
    private String a;
    private String b;

    public boolean isInverted() {
        return this.c;
    }

    public String getName() {
        return this.a;
    }

    public String getValue() {
        return this.b;
    }
}

