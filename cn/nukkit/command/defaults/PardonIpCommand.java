/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class PardonIpCommand
extends VanillaCommand {
    public PardonIpCommand(String string) {
        super(string, "%nukkit.command.unban.ip.description", "%commands.unbanip.usage");
        this.setPermission("nukkit.command.unban.ip");
        this.setAliases(new String[]{"unbanip", "unban-ip", "pardonip"});
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("ip")});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length != 1) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }
        String string2 = stringArray[0];
        if (Pattern.matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$", string2)) {
            commandSender.getServer().getIPBans().remove(string2);
            try {
                commandSender.getServer().getNetwork().unblockAddress(InetAddress.getByName(string2));
            }
            catch (UnknownHostException unknownHostException) {
                commandSender.sendMessage(new TranslationContainer("commands.unbanip.invalid"));
                return true;
            }
            Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.unbanip.success", string2));
        } else {
            commandSender.sendMessage(new TranslationContainer("commands.unbanip.invalid"));
        }
        return true;
    }

    private static UnknownHostException a(UnknownHostException unknownHostException) {
        return unknownHostException;
    }
}

