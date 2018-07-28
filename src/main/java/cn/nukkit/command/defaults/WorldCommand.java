package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TranslationContainer;

public class WorldCommand extends Command {
    public WorldCommand(String name) {
        super(name, "nukkit.command.world.description", "nukkit.command.world.usage");
        this.setPermission("nukkit.command.world");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        
        if (args.length == 0) {
            return false;
        }
        
        if (args.length == 2) {
            if (Server.getInstance().getPlayer(args[1]) == null) {
                sender.sendMessage("\u00A7cUnknown player");
                return true;
            }
            Server.getInstance().getPlayer(args[1]).teleport(Server.getInstance().getLevelByName(args[0]).getSafeSpawn());
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return true;
        }

        if (args.length == 1) {
            ((Player) sender).teleport(Server.getInstance().getLevelByName(args[0]).getSafeSpawn());
            return true;
        }

        return false;
    }
}
