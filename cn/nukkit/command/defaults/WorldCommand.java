/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;

public class WorldCommand
extends Command {
    public WorldCommand(String string) {
        super(string, "%nukkit.command.world.description", "%nukkit.command.world.usage");
        this.setPermission("nukkit.command.world");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("world", CommandParamType.STRING, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0 && !Server.getInstance().suomiCraftPEMode()) {
            commandSender.sendMessage("Available worlds:");
            for (Level level : Server.getInstance().getLevels().values()) {
                commandSender.sendMessage(level.getName());
            }
            return true;
        }
        if (stringArray.length == 2) {
            if (!commandSender.hasPermission("nukkit.command.world.others")) {
                return false;
            }
            if (Server.getInstance().getPlayer(stringArray[1].replace("@s", commandSender.getName())) == null) {
                commandSender.sendMessage("\u00a7cUnknown player");
                return true;
            }
            if (Server.getInstance().getLevelByName(stringArray[0]) == null) {
                commandSender.sendMessage("\u00a7cUnknown level");
                return true;
            }
            Server.getInstance().getPlayer(stringArray[1].replace("@s", commandSender.getName())).teleport(Server.getInstance().getLevelByName(stringArray[0]).getSafeSpawn(), PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        }
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return true;
        }
        if (stringArray.length == 1) {
            if (Server.getInstance().getLevelByName(stringArray[0]) != null) {
                ((Player)commandSender).teleport(Server.getInstance().getLevelByName(stringArray[0]).getSafeSpawn(), PlayerTeleportEvent.TeleportCause.COMMAND);
            } else {
                commandSender.sendMessage("\u00a7cUnknown level");
            }
            return true;
        }
        return false;
    }
}

