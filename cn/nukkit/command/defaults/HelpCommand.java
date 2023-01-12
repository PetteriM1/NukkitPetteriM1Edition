/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import java.util.TreeMap;

public class HelpCommand
extends VanillaCommand {
    public HelpCommand(String string) {
        super(string, "%nukkit.command.help.description", "%commands.help.usage", new String[]{"?"});
        this.setPermission("nukkit.command.help");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("page", CommandParamType.INT, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        Object object;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n = 1;
        int n2 = 5;
        if (stringArray.length != 0) {
            try {
                n = Integer.parseInt(stringArray[stringArray.length - 1]);
                if (n <= 0) {
                    n = 1;
                }
                object = new String[stringArray.length - 1];
                System.arraycopy(stringArray, 0, object, 0, ((String[])object).length);
                stringArray = object;
                for (String stringArray2 : stringArray) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(' ');
                    }
                    stringBuilder.append(stringArray2);
                }
            }
            catch (NumberFormatException numberFormatException) {
                n = 1;
                for (String string2 : stringArray) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(' ');
                    }
                    stringBuilder.append(string2);
                }
            }
        }
        if (commandSender instanceof ConsoleCommandSender) {
            n2 = Integer.MAX_VALUE;
        }
        if (stringBuilder.length() == 0) {
            object = new TreeMap();
            for (Command command : commandSender.getServer().getCommandMap().getCommands().values()) {
                if (!command.testPermissionSilent(commandSender)) continue;
                object.put(command.getName(), command);
            }
            int n3 = object.size() % n2 == 0 ? object.size() / n2 : object.size() / n2 + 1;
            if ((n = Math.min(n, n3)) < 1) {
                n = 1;
            }
            commandSender.sendMessage(new TranslationContainer("commands.help.header", String.valueOf(n), String.valueOf(n3)));
            int n4 = 1;
            for (Command command : object.values()) {
                if (n4 >= (n - 1) * n2 + 1 && n4 <= Math.min(object.size(), n * n2)) {
                    commandSender.sendMessage((Object)((Object)TextFormat.DARK_GREEN) + "/" + command.getName() + ": " + (Object)((Object)TextFormat.WHITE) + command.getDescription());
                }
                ++n4;
            }
            return true;
        }
        object = commandSender.getServer().getCommandMap().getCommand(stringBuilder.toString().toLowerCase());
        if (object != null && object.testPermissionSilent(commandSender)) {
            String[] stringArray3;
            Object object2 = (Object)((Object)TextFormat.YELLOW) + "--------- " + (Object)((Object)TextFormat.WHITE) + " Help: /" + object.getName() + (Object)((Object)TextFormat.YELLOW) + " ---------\n";
            object2 = (String)object2 + (Object)((Object)TextFormat.GOLD) + "Description: " + (Object)((Object)TextFormat.WHITE) + object.getDescription() + '\n';
            StringBuilder stringBuilder2 = new StringBuilder();
            for (String string3 : stringArray3 = object.getUsage().split("\n")) {
                if (stringBuilder2.length() > 0) {
                    stringBuilder2.append("\n" + (Object)((Object)TextFormat.WHITE));
                }
                stringBuilder2.append(string3);
            }
            object2 = (String)object2 + (Object)((Object)TextFormat.GOLD) + "Usage: " + (Object)((Object)TextFormat.WHITE) + stringBuilder2 + '\n';
            commandSender.sendMessage((String)object2);
            return true;
        }
        commandSender.sendMessage((Object)((Object)TextFormat.RED) + "No help for " + stringBuilder.toString().toLowerCase());
        return true;
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

