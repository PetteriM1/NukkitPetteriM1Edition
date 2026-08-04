package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

public class WorldCommand extends VanillaCommand {

    public WorldCommand(String name) {
        super(name, "%nukkit.command.world.description", "%nukkit.command.world.usage");
        this.setPermission("nukkit.command.world");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("world", CommandParamType.STRING, false)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Available levels:");
            for (Level level : Server.getInstance().getLevels().values()) {
                sender.sendMessage(level.getName());
            }
            return true;
        }

        if (args.length == 2) {
            if (!sender.hasPermission("nukkit.command.world.others")) {
                return false;
            }

            Player player = Server.getInstance().getPlayerExact(args[1].replace("@s", sender.getName()));
            if (player == null) {
                sender.sendMessage(TextFormat.RED + "Unknown player");
                return true;
            }
            if (Server.getInstance().getLevelByName(args[0]) == null) {
                sender.sendMessage(TextFormat.RED + "Unknown level");
                return true;
            }
            player.teleport(Server.getInstance().getLevelByName(args[0]).getSafeSpawn(), PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return true;
        }

        if (args.length == 1) {
            if (Server.getInstance().getLevelByName(args[0]) != null) {
                ((Player) sender).teleport(Server.getInstance().getLevelByName(args[0]).getSafeSpawn(), PlayerTeleportEvent.TeleportCause.COMMAND);
            } else {
                sender.sendMessage(TextFormat.RED + "Unknown level");
            }
            return true;
        }

        return false;
    }
}
