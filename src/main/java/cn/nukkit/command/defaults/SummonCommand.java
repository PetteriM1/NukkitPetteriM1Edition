package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;

public class SummonCommand extends Command {
    public SummonCommand(String name) {
        super(name, "%nukkit.command.summon.description", "%commands.summon.usage");
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
