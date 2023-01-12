/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.InstantEffect;
import cn.nukkit.utils.ServerException;
import cn.nukkit.utils.TextFormat;

public class EffectCommand
extends Command {
    public EffectCommand(String string) {
        super(string, "%nukkit.command.effect.description", "%commands.effect.usage");
        this.setPermission("nukkit.command.effect");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("effect", CommandParamType.STRING, false), new CommandParameter("seconds", CommandParamType.INT, true), new CommandParameter("amplifier", true), CommandParameter.newEnum("hideParticle", true, CommandEnum.ENUM_BOOLEAN)});
        this.commandParameters.put("clear", new CommandParameter[]{new CommandParameter("player", CommandParamType.TARGET, false), new CommandParameter("clear", new String[]{"clear"})});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        String string2;
        Effect effect;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length < 2) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        Player player = commandSender.getServer().getPlayer(stringArray[0].replace("@s", commandSender.getName()));
        if (player == null) {
            commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.player.notFound"));
            return true;
        }
        if (stringArray[1].equalsIgnoreCase("clear")) {
            for (Effect effect2 : player.getEffects().values()) {
                player.removeEffect(effect2.getId());
            }
            commandSender.sendMessage(new TranslationContainer("commands.effect.success.removed.all", player.getDisplayName()));
            return true;
        }
        try {
            effect = Effect.getEffect(Integer.parseInt(stringArray[1]));
        }
        catch (ServerException | NumberFormatException runtimeException) {
            try {
                effect = Effect.getEffectByName(stringArray[1]);
            }
            catch (Exception exception) {
                commandSender.sendMessage(new TranslationContainer("commands.effect.notFound", stringArray[1]));
                return true;
            }
        }
        int n = 300;
        int n2 = 1;
        if (stringArray.length >= 3) {
            try {
                n = Integer.parseInt(stringArray[2]);
            }
            catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
            if (!(effect instanceof InstantEffect)) {
                n *= 20;
            }
        } else if (effect instanceof InstantEffect) {
            n = 1;
        }
        if (stringArray.length >= 4) {
            try {
                n2 = Integer.parseInt(stringArray[3]);
            }
            catch (NumberFormatException numberFormatException) {
                commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
        }
        if (stringArray.length >= 5 && (string2 = stringArray[4].toLowerCase()).matches("(?i)|on|true|t|1")) {
            effect.setVisible(false);
        }
        if (n == 0) {
            if (!player.hasEffect(effect.getId())) {
                if (player.getEffects().isEmpty()) {
                    commandSender.sendMessage(new TranslationContainer("commands.effect.failure.notActive.all", player.getDisplayName()));
                } else {
                    commandSender.sendMessage(new TranslationContainer("commands.effect.failure.notActive", effect.getName(), player.getDisplayName()));
                }
                return true;
            }
            player.removeEffect(effect.getId());
            commandSender.sendMessage(new TranslationContainer("commands.effect.success.removed", effect.getName(), player.getDisplayName()));
        } else {
            effect.setDuration(n).setAmplifier(n2);
            player.addEffect(effect);
            Command.broadcastCommandMessage(commandSender, new TranslationContainer("%commands.effect.success", effect.getName(), String.valueOf(effect.getAmplifier()), player.getDisplayName(), String.valueOf(effect.getDuration() / 20)));
        }
        return true;
    }

    private static NumberFormatException a(NumberFormatException numberFormatException) {
        return numberFormatException;
    }
}

