/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public interface CommandExecutor {
    public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4);
}

