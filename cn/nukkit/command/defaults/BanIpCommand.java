/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class BanIpCommand
extends VanillaCommand {
    public BanIpCommand(String string) {
        super(string, "%nukkit.command.ban.ip.description", "%commands.banip.usage");
        this.setPermission("nukkit.command.ban.ip");
        this.setAliases(new String[]{"banip"});
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("reason", CommandParamType.STRING, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        String string2 = stringArray[0];
        StringBuilder stringBuilder = new StringBuilder();
        for (int k = 1; k < stringArray.length; ++k) {
            stringBuilder.append(stringArray[k]).append(' ');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
        if (Pattern.matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$", string2)) {
            BanIpCommand.a(string2, commandSender, stringBuilder.toString());
            Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.banip.success", string2));
        } else {
            Player player = commandSender.getServer().getPlayer(string2);
            if (player != null) {
                BanIpCommand.a(player.getAddress(), commandSender, stringBuilder.toString());
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.banip.success.players", player.getAddress(), player.getName()));
            } else {
                String string3 = string2.toLowerCase();
                String string4 = commandSender.getServer().getDataPath() + "players/";
                File file = new File(string4 + string3 + ".dat");
                CompoundTag compoundTag = null;
                if (file.exists()) {
                    try {
                        compoundTag = NBTIO.readCompressed(new FileInputStream(file));
                    }
                    catch (IOException iOException) {
                        throw new RuntimeException(iOException);
                    }
                }
                if (compoundTag != null && compoundTag.contains("lastIP") && Pattern.matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$", string2 = compoundTag.getString("lastIP"))) {
                    BanIpCommand.a(string2, commandSender, stringBuilder.toString());
                    Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.banip.success", string2));
                } else {
                    commandSender.sendMessage(new TranslationContainer("commands.banip.invalid"));
                    return false;
                }
            }
        }
        return true;
    }

    private static void a(String string, CommandSender commandSender, String string2) {
        commandSender.getServer().getIPBans().addBan(string, string2, null, commandSender.getName());
        for (Player player : commandSender.getServer().getOnlinePlayers().values()) {
            if (!player.getAddress().equals(string)) continue;
            player.kick(PlayerKickEvent.Reason.IP_BANNED, !string2.isEmpty() ? string2 : "IP banned", true, "source=" + commandSender.getName() + ", reason=" + string2);
        }
        try {
            commandSender.getServer().getNetwork().blockAddress(InetAddress.getByName(string));
        }
        catch (UnknownHostException unknownHostException) {
            // empty catch block
        }
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

