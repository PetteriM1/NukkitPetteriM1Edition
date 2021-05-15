package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;

public class SummonCommand extends Command {

    public SummonCommand(String name) {
        super(name, "%nukkit.command.summon.description", "%nukkit.command.summon.usage");
        this.setPermission("nukkit.command.summon");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("entityType", false, CommandParameter.ENUM_TYPE_ENTITY_LIST),
                new CommandParameter("player", CommandParamType.TARGET, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 0 || (args.length == 1 && !(sender instanceof Player))) {
            return false;
        }

        // Convert Minecraft format to the format what Nukkit uses
        String mob = Character.toUpperCase(args[0].charAt(0)) + args[0].substring(1);
        int max = mob.length() - 1;
        for (int x = 2; x < max; x++) {
            if (mob.charAt(x) == '_') {
                mob = mob.substring(0, x) + Character.toUpperCase(mob.charAt(x + 1)) + mob.substring(x + 2);
            }
        }

        Player playerThatSpawns;

        if (args.length == 2) {
            playerThatSpawns = Server.getInstance().getPlayer(args[1].replace("@s", sender.getName()));
        } else {
            playerThatSpawns = (Player) sender;
        }

        if (playerThatSpawns != null) {
            Position pos = playerThatSpawns.getPosition().floor().add(0.5, 0, 0.5);
            Entity ent;
            if ((ent = Entity.createEntity(mob, pos)) != null) {
                ent.spawnToAll();
                sender.sendMessage("\u00A76Spawned " + mob + " to " + playerThatSpawns.getName());
            } else {
                sender.sendMessage("\u00A7cUnable to spawn " + mob);
            }
        } else {
            sender.sendMessage("\u00A7cUnknown player " + (args.length == 2 ? args[1] : sender.getName()));
        }

        return true;
    }
}
