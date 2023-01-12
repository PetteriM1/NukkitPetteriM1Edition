/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.permission.BanEntry;
import cn.nukkit.permission.BanList;
import java.util.Iterator;

public class BanListCommand
extends VanillaCommand {
    public BanListCommand(String string) {
        super(string, "%nukkit.command.banlist.description", "%commands.banlist.usage");
        this.setPermission("nukkit.command.ban.list");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("ips|players", true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        BanList banList;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        boolean bl = false;
        if (stringArray.length > 0) {
            switch (stringArray[0].toLowerCase()) {
                case "ips": {
                    banList = commandSender.getServer().getIPBans();
                    bl = true;
                    break;
                }
                case "players": {
                    banList = commandSender.getServer().getNameBans();
                    break;
                }
                default: {
                    commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return false;
                }
            }
        } else {
            banList = commandSender.getServer().getNameBans();
        }
        CharSequence charSequence = new StringBuilder();
        Iterator<BanEntry> iterator = banList.getEntires().values().iterator();
        while (iterator.hasNext()) {
            ((StringBuilder)charSequence).append(iterator.next().getName());
            if (!iterator.hasNext()) continue;
            ((StringBuilder)charSequence).append(", ");
        }
        if (bl) {
            commandSender.sendMessage(new TranslationContainer("commands.banlist.ips", String.valueOf(banList.getEntires().size())));
        } else {
            commandSender.sendMessage(new TranslationContainer("commands.banlist.players", String.valueOf(banList.getEntires().size())));
        }
        commandSender.sendMessage(((StringBuilder)charSequence).toString());
        return true;
    }
}

