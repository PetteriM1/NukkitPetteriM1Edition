/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class OpCommand
extends VanillaCommand {
    public OpCommand(String string) {
        super(string, "%nukkit.command.op.description", "%nukkit.command.op.usage");
        this.setPermission("nukkit.command.op.give");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (commandSender instanceof Player && !Server.getInstance().opInGame) {
            commandSender.sendMessage("\u00a7cCan't use this command in game");
            return true;
        }
        if (stringArray.length == 0) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        String string2 = stringArray[0];
        IPlayer iPlayer = commandSender.getServer().getOfflinePlayer(string2);
        if (iPlayer instanceof Player) {
            iPlayer.setOp(true);
            ((Player)iPlayer).sendMessage(new TranslationContainer((Object)((Object)TextFormat.GRAY) + "%commands.op.message"));
        } else {
            commandSender.getServer().addOp(string2);
        }
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.op.success", iPlayer.getName()));
        return true;
    }
}

