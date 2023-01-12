/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class DeopCommand
extends VanillaCommand {
    public DeopCommand(String string) {
        super(string, "%nukkit.command.deop.description", "%commands.deop.description");
        this.setPermission("nukkit.command.op.take");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        String string2 = stringArray[0].replace("@s", commandSender.getName());
        IPlayer iPlayer = commandSender.getServer().getOfflinePlayer(string2);
        if (iPlayer instanceof Player) {
            iPlayer.setOp(false);
            ((Player)iPlayer).sendMessage(new TranslationContainer((Object)((Object)TextFormat.GRAY) + "%commands.deop.message"));
        } else {
            commandSender.getServer().removeOp(string2);
        }
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.deop.success", new String[]{iPlayer.getName()}));
        return true;
    }
}

