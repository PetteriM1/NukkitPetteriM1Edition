/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.math.Vector3;

public class PlaySoundCommand
extends VanillaCommand {
    public PlaySoundCommand(String string) {
        super(string, "%nukkit.command.playsound.description", "%commands.playsound.usage");
        this.setPermission("nukkit.command.playsound");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("sound", CommandParamType.STRING, false), new CommandParameter("player", CommandParamType.TARGET, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0) {
            commandSender.sendMessage(new TranslationContainer("%commands.playsound.usage", this.usageMessage));
            return false;
        }
        if (stringArray.length == 1) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
                return true;
            }
            Player player = (Player)commandSender;
            player.getLevel().addSound((Vector3)player, stringArray[0], player);
            player.sendMessage(new TranslationContainer("commands.playsound.success", stringArray[0], player.getName()));
            return true;
        }
        if (stringArray[1].equalsIgnoreCase("@a")) {
            for (Player player : Server.getInstance().getOnlinePlayers().values()) {
                player.getLevel().addSound((Vector3)player, stringArray[0], player);
            }
            commandSender.sendMessage(new TranslationContainer("commands.playsound.success", stringArray[0], "@a"));
            return true;
        }
        if (stringArray[1].equalsIgnoreCase("@s") && commandSender instanceof Player) {
            Player player = (Player)commandSender;
            player.getLevel().addSound((Vector3)player, stringArray[0], player);
            commandSender.sendMessage(new TranslationContainer("commands.playsound.success", stringArray[0], player.getName()));
            return true;
        }
        Player player = Server.getInstance().getPlayer(stringArray[1]);
        if (player == null) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.player.notFound"));
            return true;
        }
        player.getLevel().addSound((Vector3)player, stringArray[0], player);
        commandSender.sendMessage(new TranslationContainer("commands.playsound.success", stringArray[0], player.getName()));
        return true;
    }
}

