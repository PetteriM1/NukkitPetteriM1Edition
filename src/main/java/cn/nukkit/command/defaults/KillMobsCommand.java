package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;

public class KillMobsCommand extends Command {
    public KillMobsCommand(String name) {
        super(name, "%nukkit.command.killmobs.description", "%commands.killmobs.usage");
        this.setPermission("nukkit.command.killmobs");
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
