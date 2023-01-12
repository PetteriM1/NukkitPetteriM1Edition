/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandMap;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandData;
import cn.nukkit.command.data.CommandDataVersions;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandOverload;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.permission.Permissible;
import cn.nukkit.utils.TextFormat;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Command {
    protected CommandData commandData;
    private final String g;
    private String a;
    private String e;
    private String[] b;
    private String[] c;
    private CommandMap f = null;
    protected String description;
    protected String usageMessage;
    private String h = null;
    private String d = null;
    protected Map<String, CommandParameter[]> commandParameters = new HashMap<String, CommandParameter[]>();
    public Timing timing;

    public Command(String string) {
        this(string, "", null, new String[0]);
    }

    public Command(String string, String string2) {
        this(string, string2, null, new String[0]);
    }

    public Command(String string, String string2, String string3) {
        this(string, string2, string3, new String[0]);
    }

    public Command(String string, String string2, String string3, String[] stringArray) {
        this.commandData = new CommandData();
        this.g = string.toLowerCase();
        this.a = string;
        this.e = string;
        this.description = string2;
        this.usageMessage = string3 == null ? '/' + string : string3;
        this.b = stringArray;
        this.c = stringArray;
        this.timing = Timings.getCommandTiming(this);
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("args", CommandParamType.RAWTEXT, true)});
    }

    public CommandData getDefaultCommandData() {
        return this.commandData;
    }

    public CommandParameter[] getCommandParameters(String string) {
        return this.commandParameters.get(string);
    }

    public Map<String, CommandParameter[]> getCommandParameters() {
        return this.commandParameters;
    }

    public void setCommandParameters(Map<String, CommandParameter[]> map) {
        this.commandParameters = map;
    }

    public void addCommandParameters(String string, CommandParameter[] commandParameterArray) {
        this.commandParameters.put(string, commandParameterArray);
    }

    public CommandDataVersions generateCustomCommandData(Player player) {
        ArrayList<String> arrayList;
        if (!this.testPermissionSilent(player)) {
            return null;
        }
        CommandData commandData = this.commandData.clone();
        if (this.getAliases().length > 0) {
            arrayList = new ArrayList<String>(Arrays.asList(this.getAliases()));
            if (!arrayList.contains(this.g)) {
                arrayList.add(this.g);
            }
            commandData.aliases = new CommandEnum(this.g + "Aliases", (List<String>)arrayList);
        }
        commandData.description = player.getServer().getLanguage().translateString(this.getDescription());
        this.commandParameters.forEach((string, commandParameterArray) -> {
            CommandOverload commandOverload = new CommandOverload();
            commandOverload.input.parameters = commandParameterArray;
            commandData.overloads.put((String)string, commandOverload);
        });
        if (commandData.overloads.isEmpty()) {
            commandData.overloads.put("default", new CommandOverload());
        }
        arrayList = new CommandDataVersions();
        ((CommandDataVersions)((Object)arrayList)).versions.add(commandData);
        return arrayList;
    }

    public Map<String, CommandOverload> getOverloads() {
        return this.commandData.overloads;
    }

    public abstract boolean execute(CommandSender var1, String var2, String[] var3);

    public String getName() {
        return this.g;
    }

    public String getPermission() {
        return this.h;
    }

    public void setPermission(String string) {
        this.h = string;
    }

    public boolean testPermission(CommandSender commandSender) {
        if (this.testPermissionSilent(commandSender)) {
            return true;
        }
        if (this.d == null) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.unknown", this.g));
        } else if (!this.d.isEmpty()) {
            commandSender.sendMessage(this.d.replace("<permission>", this.h));
        }
        return false;
    }

    public boolean testPermissionSilent(CommandSender commandSender) {
        String[] stringArray;
        if (this.h == null || this.h.isEmpty()) {
            return true;
        }
        for (String string : stringArray = this.h.split(";")) {
            if (!commandSender.hasPermission(string)) continue;
            return true;
        }
        return false;
    }

    public String getLabel() {
        return this.e;
    }

    public boolean setLabel(String string) {
        this.a = string;
        if (!this.isRegistered()) {
            this.e = string;
            this.timing = Timings.getCommandTiming(this);
            return true;
        }
        return false;
    }

    public boolean register(CommandMap commandMap) {
        if (this.allowChangesFrom(commandMap)) {
            this.f = commandMap;
            return true;
        }
        return false;
    }

    public boolean unregister(CommandMap commandMap) {
        if (this.allowChangesFrom(commandMap)) {
            this.f = null;
            this.c = this.b;
            this.e = this.a;
            return true;
        }
        return false;
    }

    public boolean allowChangesFrom(CommandMap commandMap) {
        return commandMap != null && !commandMap.equals(this.f);
    }

    public boolean isRegistered() {
        return this.f != null;
    }

    public String[] getAliases() {
        return this.c;
    }

    public String getPermissionMessage() {
        return this.d;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUsage() {
        return this.usageMessage;
    }

    public void setAliases(String[] stringArray) {
        this.b = stringArray;
        if (!this.isRegistered()) {
            this.c = stringArray;
        }
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public void setPermissionMessage(String string) {
        this.d = string;
    }

    public void setUsage(String string) {
        this.usageMessage = string;
    }

    public static CommandData generateDefaultData() {
        return null;
    }

    public static void broadcastCommandMessage(CommandSender commandSender, String string) {
        Command.broadcastCommandMessage(commandSender, string, true);
    }

    public static void broadcastCommandMessage(CommandSender commandSender, String string, boolean bl) {
        Set<Permissible> set = commandSender.getServer().getPluginManager().getPermissionSubscriptions("nukkit.broadcast.admin");
        TranslationContainer translationContainer = new TranslationContainer("chat.type.admin", commandSender.getName(), string);
        TranslationContainer translationContainer2 = new TranslationContainer((Object)((Object)TextFormat.GRAY) + "" + (Object)((Object)TextFormat.ITALIC) + "%chat.type.admin", commandSender.getName(), string);
        if (bl && !(commandSender instanceof ConsoleCommandSender)) {
            commandSender.sendMessage(string);
        }
        for (Permissible permissible : set) {
            if (!(permissible instanceof CommandSender)) continue;
            if (permissible instanceof ConsoleCommandSender) {
                ((ConsoleCommandSender)permissible).sendMessage(translationContainer);
                continue;
            }
            if (permissible.equals(commandSender)) continue;
            ((CommandSender)permissible).sendMessage(translationContainer2);
        }
    }

    public static void broadcastCommandMessage(CommandSender commandSender, TextContainer textContainer) {
        Command.broadcastCommandMessage(commandSender, textContainer, true);
    }

    public static void broadcastCommandMessage(CommandSender commandSender, TextContainer textContainer, boolean bl) {
        TextContainer textContainer2 = textContainer.clone();
        String string = '[' + commandSender.getName() + ": " + (!textContainer2.getText().equals(commandSender.getServer().getLanguage().get(textContainer2.getText())) ? "%" : "") + textContainer2.getText() + ']';
        Set<Permissible> set = commandSender.getServer().getPluginManager().getPermissionSubscriptions("nukkit.broadcast.admin");
        String string2 = (Object)((Object)TextFormat.GRAY) + "" + (Object)((Object)TextFormat.ITALIC) + string;
        textContainer2.setText(string);
        TextContainer textContainer3 = textContainer2.clone();
        textContainer2.setText(string2);
        TextContainer textContainer4 = textContainer2.clone();
        if (bl && !(commandSender instanceof ConsoleCommandSender)) {
            commandSender.sendMessage(textContainer);
        }
        for (Permissible permissible : set) {
            if (!(permissible instanceof CommandSender)) continue;
            if (permissible instanceof ConsoleCommandSender) {
                ((ConsoleCommandSender)permissible).sendMessage(textContainer3);
                continue;
            }
            if (permissible.equals(commandSender)) continue;
            ((CommandSender)permissible).sendMessage(textContainer4);
        }
    }

    public String toString() {
        return this.g;
    }
}

