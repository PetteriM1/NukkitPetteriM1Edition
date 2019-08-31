package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

import java.text.DecimalFormat;

/**
 * Created on 2015/12/13 by xtypr.
 * Package cn.nukkit.command.defaults in project Nukkit .
 */
public class SetWorldSpawnCommand extends VanillaCommand {

    public SetWorldSpawnCommand(String name) {
        super(name, "%nukkit.command.setworldspawn.description", "%commands.setworldspawn.usage");
        this.setPermission("nukkit.command.setworldspawn");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("blockPos", CommandParamType.POSITION, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        Level level;
        Vector3 pos;
        DecimalFormat round2 = new DecimalFormat("##0.00");
        if (args.length == 0) {
            if (sender instanceof Player) {
                level = ((Player) sender).getLevel();
                pos = new Vector3(Double.parseDouble(round2.format(((Player) sender).x).replace(',', '.')), Double.parseDouble(round2.format(((Player) sender).y).replace(',', '.')), Double.parseDouble(round2.format(((Player) sender).z).replace(',', '.')));
            } else {
                sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
        } else if (args.length == 3) {
            level = sender.getServer().getDefaultLevel();
            try {
                pos = new Vector3(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
            } catch (NumberFormatException e1) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        level.setSpawnLocation(pos);
        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.setworldspawn.success", String.valueOf(pos.x), String.valueOf(pos.y), String.valueOf(pos.z)));
        return true;
    }
}
