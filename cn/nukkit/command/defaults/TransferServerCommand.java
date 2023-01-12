/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;

public class TransferServerCommand
extends VanillaCommand {
    public TransferServerCommand(String string) {
        super(string, "%nukkit.command.transferserver.description", "%commands.transferserver.usage");
        this.setPermission("nukkit.command.transferserver");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("serverAddress", CommandParamType.STRING, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return false;
        }
        if (stringArray.length == 0 || stringArray.length > 2) {
            commandSender.sendMessage(new TranslationContainer("commands.transferserver.usage", this.usageMessage));
            return true;
        }
        int n = stringArray.length == 2 ? Integer.parseInt(stringArray[1]) : 19132;
        ((Player)commandSender).transfer(stringArray[0], n);
        return true;
    }
}

