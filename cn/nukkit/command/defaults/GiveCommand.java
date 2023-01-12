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
import cn.nukkit.item.Item;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;

public class GiveCommand
extends VanillaCommand {
    public GiveCommand(String string) {
        super(string, "%nukkit.command.give.description", "%nukkit.command.give.usage");
        this.setPermission("nukkit.command.give");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("itemName", false, "Item"), new CommandParameter("amount", CommandParamType.INT, true), new CommandParameter("meta", CommandParamType.INT, true), new CommandParameter("tags...", CommandParamType.RAWTEXT, true)});
        this.commandParameters.put("toPlayerById", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("item ID", CommandParamType.INT, false), new CommandParameter("amount", CommandParamType.INT, true), new CommandParameter("tags...", CommandParamType.RAWTEXT, true)});
        this.commandParameters.put("toPlayerByIdMeta", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("item ID:meta", CommandParamType.RAWTEXT, false), new CommandParameter("amount", CommandParamType.INT, true), new CommandParameter("tags...", CommandParamType.RAWTEXT, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        Item item;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length < 2) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        Player player = commandSender.getServer().getPlayer(stringArray[0].replace("@s", commandSender.getName()));
        try {
            item = Item.fromString(stringArray[1]);
        }
        catch (Exception exception) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        if (item.getDamage() < 0) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        try {
            item.setCount(Integer.parseInt(stringArray[2]));
        }
        catch (Exception exception) {
            item.setCount(item.getMaxStackSize());
        }
        if (player != null) {
            if (item.getId() == 0) {
                commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.give.item.notFound", stringArray[1]));
                return true;
            }
        } else {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
            return true;
        }
        player.getInventory().addItem(item);
        Command.broadcastCommandMessage(commandSender, new TranslationContainer("%commands.give.success", item.getName() + " (" + item.getId() + ':' + item.getDamage() + ')', String.valueOf(item.getCount()), player.getName()));
        return true;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

