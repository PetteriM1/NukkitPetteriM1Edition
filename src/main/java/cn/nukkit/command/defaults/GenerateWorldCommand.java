package cn.nukkit.command.defaults;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.format.LevelProviderManager;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.utils.TextFormat;

import java.util.Arrays;
import java.util.HashMap;

public class GenerateWorldCommand extends VanillaCommand {

    public GenerateWorldCommand(String name) {
        super(name, "%nukkit.command.generateworld.description", "%nukkit.command.generateworld.usage");
        this.setPermission("nukkit.command.generateworld");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("name", CommandParamType.STRING, false),
                new CommandParameter("type", CommandParamType.STRING, false),
                new CommandParameter("seed", CommandParamType.INT, false)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        boolean generateAnvil = false;
        if (args.length == 3 || (generateAnvil = (args.length == 4 && args[3].equalsIgnoreCase("anvil")))) {
            if (Server.getInstance().isLevelGenerated(args[0])) {
                sender.sendMessage(TextFormat.RED + "Level \"" + args[0] + "\" already exists");
                return true;
            }

            long seed;

            try {
                seed = Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(TextFormat.RED + "The seed must be numeric");
                return true;
            }

            String[] generators = Generator.getGeneratorList();
            boolean generatorFound = false;

            for (String generator : generators) {
                if (args[1].equalsIgnoreCase(generator)) {
                    generatorFound = true;
                    break;
                }
            }

            if (!generatorFound) {
                sender.sendMessage(TextFormat.RED + "Unknown generator. Available: " + Arrays.toString(generators));
                return true;
            }

            if (generateAnvil) {
                Server.getInstance().generateLevel(args[0], seed, Generator.getGenerator(args[1]), new HashMap<>(), LevelProviderManager.getProviderByName("anvil"));
            } else {
                Server.getInstance().generateLevel(args[0], seed, Generator.getGenerator(args[1]));
            }

            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage("Preparing level \"" + args[0] + "\"");
            }

            return true;
        }

        sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
        return true;
    }
}
