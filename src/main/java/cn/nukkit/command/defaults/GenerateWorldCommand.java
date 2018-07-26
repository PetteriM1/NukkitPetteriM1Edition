package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class GenerateWorldCommand extends Command {
    public GenerateWorldCommand(String name) {
        super(name, "nukkit.command.generateworld.description", "nukkit.command.generateworld.usage");
        this.setPermission("nukkit.command.generateworld");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (args.length == 3) {
            return true;
        }

        //TODO /genworld <name> <type> <seed>

        return false;
    }
}
