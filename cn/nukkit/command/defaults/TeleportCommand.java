/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Location;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.TextFormat;

public class TeleportCommand
extends VanillaCommand {
    public TeleportCommand(String string) {
        super(string, "%nukkit.command.tp.description", "%commands.tp.usage");
        this.setPermission("nukkit.command.teleport");
        this.commandParameters.clear();
        this.commandParameters.put("->Player", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false)});
        this.commandParameters.put("Player->Player", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("target", CommandParamType.TARGET, false)});
        this.commandParameters.put("Player->Pos", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("blockPos", CommandParamType.POSITION, false)});
        this.commandParameters.put("->Pos", new CommandParameter[]{new CommandParameter("blockPos", CommandParamType.POSITION, false)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        CommandSender commandSender2;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length < 1 || stringArray.length > 6) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        CommandSender commandSender3 = commandSender;
        if (stringArray.length == 1 || stringArray.length == 3) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
            commandSender2 = commandSender;
            if (stringArray.length == 1 && (commandSender2 = commandSender.getServer().getPlayer(stringArray[0].replace("@s", commandSender.getName()))) == null) {
                commandSender.sendMessage((Object)((Object)TextFormat.RED) + "Can't find player " + stringArray[0]);
                return true;
            }
        } else {
            commandSender2 = commandSender.getServer().getPlayer(stringArray[0].replace("@s", commandSender.getName()));
            if (commandSender2 == null) {
                commandSender.sendMessage((Object)((Object)TextFormat.RED) + "Can't find player " + stringArray[0]);
                return true;
            }
            if (stringArray.length == 2) {
                commandSender3 = commandSender2;
                commandSender2 = commandSender.getServer().getPlayer(stringArray[1].replace("@s", commandSender.getName()));
                if (commandSender2 == null) {
                    commandSender.sendMessage((Object)((Object)TextFormat.RED) + "Can't find player " + stringArray[1]);
                    return true;
                }
            }
        }
        if (stringArray.length < 3) {
            ((Player)commandSender3).teleport((Player)commandSender2, PlayerTeleportEvent.TeleportCause.COMMAND);
            Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.tp.success", commandSender3.getName(), commandSender2.getName()));
            return true;
        }
        if (((Player)commandSender2).getLevel() != null) {
            double d2;
            double d3;
            double d4;
            double d5;
            double d6;
            int n = stringArray.length == 4 || stringArray.length == 6 ? 1 : 0;
            try {
                d6 = Double.parseDouble(stringArray[n++].replace("~", "" + ((Player)commandSender2).x));
                d5 = Double.parseDouble(stringArray[n++].replace("~", "" + ((Player)commandSender2).y));
                d4 = Double.parseDouble(stringArray[n++].replace("~", "" + ((Player)commandSender2).z));
                d3 = ((Player)commandSender2).getYaw();
                d2 = ((Player)commandSender2).getPitch();
            }
            catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
            if (d6 < -3.0E7) {
                d6 = -3.0E7;
            }
            if (d6 > 3.0E7) {
                d6 = 3.0E7;
            }
            if (d5 < -3.0E7) {
                d5 = -3.0E7;
            }
            if (d5 > 3.0E7) {
                d5 = 3.0E7;
            }
            if (d4 < -3.0E7) {
                d4 = -3.0E7;
            }
            if (d4 > 3.0E7) {
                d4 = 3.0E7;
            }
            if (stringArray.length == 6 || stringArray.length == 5 && n == 3) {
                d3 = Integer.parseInt(stringArray[n++]);
                d2 = Integer.parseInt(stringArray[n]);
            }
            ((Player)commandSender2).teleport(new Location(d6, d5, d4, d3, d2, ((Player)commandSender2).getLevel()), PlayerTeleportEvent.TeleportCause.COMMAND);
            Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.tp.success.coordinates", commandSender2.getName(), String.valueOf(NukkitMath.round(d6, 2)), String.valueOf(NukkitMath.round(d5, 2)), String.valueOf(NukkitMath.round(d4, 2))));
            return true;
        }
        commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        return true;
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

