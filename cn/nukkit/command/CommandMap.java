/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import java.util.List;

public interface CommandMap {
    public void registerAll(String var1, List<? extends Command> var2);

    public boolean register(String var1, Command var2);

    public boolean register(String var1, Command var2, String var3);

    public void registerSimpleCommands(Object var1);

    public boolean dispatch(CommandSender var1, String var2);

    public void clearCommands();

    public Command getCommand(String var1);
}

