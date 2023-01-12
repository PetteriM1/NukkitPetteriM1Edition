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
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;

public class WeatherCommand
extends VanillaCommand {
    public WeatherCommand(String string) {
        super(string, "%nukkit.command.weather.description", "%commands.weather.usage");
        this.setPermission("nukkit.command.weather");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("clear|rain|thunder", CommandParamType.STRING, false), new CommandParameter("duration in seconds", CommandParamType.INT, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        int n;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length == 0 || stringArray.length > 2) {
            commandSender.sendMessage(new TranslationContainer("commands.weather.usage", this.usageMessage));
            return false;
        }
        String string2 = stringArray[0];
        if (stringArray.length > 1) {
            try {
                n = Integer.parseInt(stringArray[1]);
            }
            catch (Exception exception) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
        } else {
            n = 12000;
        }
        Level level = commandSender instanceof Player ? ((Player)commandSender).getLevel() : commandSender.getServer().getDefaultLevel();
        switch (string2) {
            case "clear": {
                level.setRaining(false);
                level.setThundering(false);
                level.setRainTime(n * 20);
                level.setThunderTime(n * 20);
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.weather.clear"));
                return true;
            }
            case "rain": {
                level.setRaining(true);
                level.setRainTime(n * 20);
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.weather.rain"));
                return true;
            }
            case "thunder": {
                level.setThundering(true);
                level.setRainTime(n * 20);
                level.setThunderTime(n * 20);
                Command.broadcastCommandMessage(commandSender, new TranslationContainer("commands.weather.thunder"));
                return true;
            }
        }
        commandSender.sendMessage(new TranslationContainer("commands.weather.usage", this.usageMessage));
        return false;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

