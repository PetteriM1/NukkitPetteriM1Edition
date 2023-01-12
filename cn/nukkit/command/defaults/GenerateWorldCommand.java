/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.generator.Generator;

public class GenerateWorldCommand
extends Command {
    public GenerateWorldCommand(String string) {
        super(string, "%nukkit.command.generateworld.description", "%nukkit.command.generateworld.usage");
        this.setPermission("nukkit.command.generateworld");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("name", CommandParamType.STRING, false), new CommandParameter("type", CommandParamType.STRING, false), new CommandParameter("seed", CommandParamType.INT, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 3) {
            long l;
            if (Server.getInstance().isLevelGenerated(stringArray[0])) {
                commandSender.sendMessage("\u00a7cWorld \u00a77" + stringArray[0] + " \u00a7calready exists");
                return true;
            }
            try {
                l = Long.parseLong(stringArray[2]);
            }
            catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage("\u00a7cThe seed must be numeric");
                return true;
            }
            Server.getInstance().generateLevel(stringArray[0], l, Generator.getGenerator(stringArray[1]));
            commandSender.sendMessage("\u00a72Generating world \u00a77" + stringArray[0] + "\u00a72...");
            return true;
        }
        return false;
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

