/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.plugin.Plugin;

public class PluginCommand<T extends Plugin>
extends Command
implements PluginIdentifiableCommand {
    private final T j;
    private CommandExecutor i;

    public PluginCommand(String string, T t) {
        super(string);
        this.j = t;
        this.i = t;
        this.usageMessage = "";
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.j.isEnabled()) {
            return false;
        }
        if (!this.testPermission(commandSender)) {
            return false;
        }
        boolean bl = this.i.onCommand(commandSender, this, string, stringArray);
        if (!bl && !this.usageMessage.isEmpty()) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        }
        return bl;
    }

    public CommandExecutor getExecutor() {
        return this.i;
    }

    public void setExecutor(CommandExecutor commandExecutor) {
        this.i = commandExecutor != null ? commandExecutor : this.j;
    }

    public T getPlugin() {
        return this.j;
    }
}

