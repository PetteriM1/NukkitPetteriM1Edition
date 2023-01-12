/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GameRules;
import java.util.Arrays;
import java.util.Optional;
import java.util.StringJoiner;

public class GameruleCommand
extends VanillaCommand {
    public GameruleCommand(String string) {
        super(string, "%nukkit.command.gamerule.description", "%commands.gamerule.usage");
        this.setPermission("nukkit.command.gamerule");
        this.commandParameters.clear();
        this.commandParameters.put("byString", new CommandParameter[]{new CommandParameter("gamerule", true, GameRule.getNamesLowerCase()), new CommandParameter("value", CommandParamType.STRING, true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (!commandSender.isPlayer()) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return true;
        }
        GameRules gameRules = ((Player)commandSender).getLevel().getGameRules();
        switch (stringArray.length) {
            case 0: {
                StringJoiner stringJoiner = new StringJoiner(", ");
                for (GameRule gameRule : gameRules.getRules()) {
                    stringJoiner.add(gameRule.getName().toLowerCase());
                }
                commandSender.sendMessage(stringJoiner.toString());
                return true;
            }
            case 1: {
                Optional<GameRule> optional = GameRule.parseString(stringArray[0]);
                if (!optional.isPresent() || !gameRules.hasRule(optional.get())) {
                    commandSender.sendMessage(new TranslationContainer("commands.generic.syntax", "/gamerule", stringArray[0]));
                    return true;
                }
                commandSender.sendMessage(optional.get().getName() + " = " + gameRules.getString(optional.get()));
                return true;
            }
        }
        Optional<GameRule> optional = GameRule.parseString(stringArray[0]);
        if (!optional.isPresent()) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.syntax", "/gamerule ", stringArray[0], ' ' + String.join((CharSequence)" ", Arrays.copyOfRange(stringArray, 1, stringArray.length))));
            return true;
        }
        try {
            gameRules.setGameRules(optional.get(), stringArray[1]);
            commandSender.sendMessage(new TranslationContainer("commands.gamerule.success", optional.get().getName(), stringArray[1]));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.syntax", "/gamerule " + stringArray[0] + ' ', stringArray[1], ' ' + String.join((CharSequence)" ", Arrays.copyOfRange(stringArray, 2, stringArray.length))));
        }
        return true;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

