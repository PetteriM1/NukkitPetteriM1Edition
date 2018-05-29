package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SummonCommand extends Command {
    public SummonCommand(String name) {
        super(name, "Summon entity", "/summon <entity>");
        this.setPermission("nukkit.command.summon");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        //TODO

        return true;
    }
}
