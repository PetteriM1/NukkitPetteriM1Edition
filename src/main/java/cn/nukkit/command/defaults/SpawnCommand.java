package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.lang.TranslationContainer;

public class SpawnCommand extends VanillaCommand {

    public SpawnCommand(String name) {
        super(name, "%nukkit.command.spawn.description", "%commands.spawn.usage");
        this.setPermission("nukkit.command.spawn");
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (sender instanceof Player) {
            ((Player) sender).teleport(((Player) sender).getLevel().getSafeSpawn(), PlayerTeleportEvent.TeleportCause.COMMAND);
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
        }

        return true;
    }
}
