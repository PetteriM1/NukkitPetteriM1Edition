package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.network.protocol.SetDefaultGameTypePacket;

/**
 * Created on 2015/11/12 by xtypr.
 * Package cn.nukkit.command.defaults in project Nukkit .
 */
public class DefaultGamemodeCommand extends VanillaCommand {

    public DefaultGamemodeCommand(String name) {
        super(name, "%nukkit.command.defaultgamemode.description", "%commands.defaultgamemode.usage");
        this.setPermission("nukkit.command.defaultgamemode");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("mode", CommandParamType.INT, false)
        });
        this.commandParameters.put("byString", new CommandParameter[]{
                new CommandParameter("mode", new String[]{"survival", "creative", "s", "c",
                        "adventure", "a", "spectator", "view", "v"})
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        int gameMode = Server.getGamemodeFromString(args[0]);
        if (gameMode != -1) {
            sender.getServer().setPropertyInt("gamemode", gameMode);
            sender.getServer().gamemode = gameMode;
            sender.sendMessage(new TranslationContainer("commands.defaultgamemode.success", new String[]{Server.getGamemodeString(gameMode)}));
            Command.broadcastCommandMessage(sender, new TranslationContainer("commands.defaultgamemode.success", new String[]{Server.getGamemodeString(sender.getServer().getDefaultGamemode())}));

            SetDefaultGameTypePacket gameTypePacket = new SetDefaultGameTypePacket();
            gameTypePacket.gamemode = sender.getServer().getDefaultGamemode();
            Server.broadcastPacket(sender.getServer().getOnlinePlayers().values(), gameTypePacket);
        } else {
            sender.sendMessage("Unknown game mode");
        }
        return true;
    }
}
