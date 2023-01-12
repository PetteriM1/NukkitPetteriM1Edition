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
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import java.text.DecimalFormat;

public class SpawnpointCommand
extends VanillaCommand {
    public SpawnpointCommand(String string) {
        super(string, "%nukkit.command.spawnpoint.description", "%commands.spawnpoint.usage");
        this.setPermission("nukkit.command.spawnpoint");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("blockPos", CommandParamType.POSITION, true)});
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        Player player;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
            player = (Player)commandSender;
        } else {
            player = commandSender.getServer().getPlayer(stringArray[0]);
            if (player == null) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
                return true;
            }
        }
        Level level = player.getLevel();
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        if (stringArray.length == 4) {
            if (level != null) {
                int n;
                int n2;
                int n3;
                try {
                    n3 = Integer.parseInt(stringArray[1]);
                    n2 = Integer.parseInt(stringArray[2]);
                    n = Integer.parseInt(stringArray[3]);
                }
                catch (NumberFormatException numberFormatException) {
                    commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
                }
                if (n2 < 0) {
                    n2 = 0;
                }
                if (n2 > 256) {
                    n2 = 256;
                }
                player.setSpawn(new Position(n3, n2, n, level));
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.spawnpoint.success", player.getName(), decimalFormat.format(n3), decimalFormat.format(n2), decimalFormat.format(n)));
                return true;
            }
        } else if (stringArray.length <= 1) {
            if (commandSender instanceof Player) {
                Position position = (Position)((Object)commandSender);
                player.setSpawn(position);
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.spawnpoint.success", player.getName(), decimalFormat.format(position.x), decimalFormat.format(position.y), decimalFormat.format(position.z)));
                return true;
            }
            commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return true;
        }
        commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        return true;
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

