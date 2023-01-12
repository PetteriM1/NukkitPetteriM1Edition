/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.data.args;

import cn.nukkit.command.data.args.CommandArgRules;

public class CommandArg {
    private CommandArgRules[] b;
    private String a;

    public CommandArgRules[] getRules() {
        return this.b;
    }

    public String getSelector() {
        return this.a;
    }
}

