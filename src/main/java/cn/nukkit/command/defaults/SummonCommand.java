package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityUtils;
import cn.nukkit.level.Position;

public class SummonCommand extends Command {
    public SummonCommand(String name) {
        super(name, "nukkit.command.summon.description", "nukkit.command.summon.usage");
        this.setPermission("nukkit.command.summon");
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

        String mob = args[0];
        Player playerThatSpawns = null;
            if (args.length == 2) {
            playerThatSpawns = Server.getInstance().getPlayer(args[1]);
        } else {
            playerThatSpawns = (Player) sender;
        }
            if (playerThatSpawns != null) {
            Position pos = playerThatSpawns.getPosition();
            Entity ent;
                if ((ent = EntityUtils.create(mob, pos)) != null) {
                ent.spawnToAll();
                sender.sendMessage("\u00A76Spawned " + mob + " to " + playerThatSpawns.getName());
            } else {
                sender.sendMessage("\u00A7cUnable to spawn " + mob);
            }
        } else {
            sender.sendMessage("\u00A7cUnknown player " + (args.length == 2 ? args[1] : ((Player) sender).getName()));
        }

        return true;
    }
}
