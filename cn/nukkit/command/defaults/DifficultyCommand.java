/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.SetDifficultyPacket;

public class DifficultyCommand
extends VanillaCommand {
    public DifficultyCommand(String string) {
        super(string, "%nukkit.command.difficulty.description", "%commands.difficulty.usage");
        this.setPermission("nukkit.command.difficulty");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("difficulty", CommandParamType.INT, false)});
        this.commandParameters.put("byString", new CommandParameter[]{new CommandParameter("difficulty", new String[]{"peaceful", "p", "easy", "e", "normal", "n", "hard", "h"})});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length != 1) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        int n = Server.getDifficultyFromString(stringArray[0]);
        if (commandSender.getServer().isHardcore()) {
            n = 3;
        }
        if (n == -1) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        commandSender.getServer().setDifficulty(n);
        SetDifficultyPacket setDifficultyPacket = new SetDifficultyPacket();
        setDifficultyPacket.difficulty = commandSender.getServer().getDifficulty();
        Server.broadcastPacket(commandSender.getServer().getOnlinePlayers().values(), (DataPacket)setDifficultyPacket);
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.difficulty.success", String.valueOf(n)));
        return true;
    }
}

