/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.simple;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.lang.TranslationContainer;
import java.lang.reflect.Method;

public class SimpleCommand
extends Command {
    private final Object m;
    private final Method i;
    private boolean k;
    private int l;
    private int j;

    public SimpleCommand(Object object, Method method, String string, String string2, String string3, String[] stringArray) {
        super(string, string2, string3, stringArray);
        this.m = object;
        this.i = method;
    }

    public void setForbidConsole(boolean bl) {
        this.k = bl;
    }

    public void setMaxArgs(int n) {
        this.l = n;
    }

    public void setMinArgs(int n) {
        this.j = n;
    }

    public void sendUsageMessage(CommandSender commandSender) {
        if (!this.usageMessage.isEmpty()) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        }
    }

    public void sendInGameMessage(CommandSender commandSender) {
        commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        boolean bl;
        block6: {
            if (this.k && commandSender instanceof ConsoleCommandSender) {
                this.sendInGameMessage(commandSender);
                return false;
            }
            if (!this.testPermission(commandSender)) {
                return false;
            }
            if (this.l != 0 && stringArray.length > this.l) {
                this.sendUsageMessage(commandSender);
                return false;
            }
            if (this.j != 0 && stringArray.length < this.j) {
                this.sendUsageMessage(commandSender);
                return false;
            }
            bl = false;
            try {
                bl = (Boolean)this.i.invoke(this.m, commandSender, string, stringArray);
            }
            catch (Exception exception) {
                Server.getInstance().getLogger().logException(exception);
            }
            if (bl) break block6;
            this.sendUsageMessage(commandSender);
        }
        return bl;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

