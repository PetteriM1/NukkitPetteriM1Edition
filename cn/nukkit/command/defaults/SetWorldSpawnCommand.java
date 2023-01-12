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
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import java.text.DecimalFormat;

public class SetWorldSpawnCommand
extends VanillaCommand {
    public SetWorldSpawnCommand(String string) {
        super(string, "%nukkit.command.setworldspawn.description", "%commands.setworldspawn.usage");
        this.setPermission("nukkit.command.setworldspawn");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("blockPos", CommandParamType.POSITION, true)});
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        void var5_7;
        Level level;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
            level = ((Player)commandSender).getLevel();
            Location location = ((Player)commandSender).round();
        } else {
            if (stringArray.length != 3) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
            level = commandSender.getServer().getDefaultLevel();
            try {
                Vector3 vector3 = new Vector3(Integer.parseInt(stringArray[0]), Integer.parseInt(stringArray[1]), Integer.parseInt(stringArray[2]));
            }
            catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
        }
        level.setSpawnLocation((Vector3)var5_7);
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.setworldspawn.success", decimalFormat.format(var5_7.x), decimalFormat.format(var5_7.y), decimalFormat.format(var5_7.z)));
        return true;
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

