package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.generator.Generator;

public class GenerateWorldCommand extends Command {

    public GenerateWorldCommand(String name) {
        super(name, "%nukkit.command.generateworld.description", "%nukkit.command.generateworld.usage");
        this.setPermission("nukkit.command.generateworld");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("name", CommandParamType.STRING, false),
                new CommandParameter("type", CommandParamType.STRING, false),
                new CommandParameter("seed", CommandParamType.INT, false)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 3) {
            if (Server.getInstance().isLevelGenerated(args[0])) {
                sender.sendMessage("\u00A7cWorld \u00A77" + args[0] + " \u00A7calready exists");
                return true;
            }

            long seed;

            try {
                seed = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("\u00A7cThe seed must be numeric");
                return true;
            }

            Server.getInstance().generateLevel(args[0], seed, Generator.getGenerator(args[1]));
            
            sender.sendMessage("\u00A72Generating world \u00A77" + args[0] + "\u00A72...");

            return true;
        }

        return false;
    }
}
