package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

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

        if (args.length == 1) {
            return true;
        }

        if (args.length == 2) {
            return true;
        }

        //TODO /world <world> [player]

        return false;
    }
}
