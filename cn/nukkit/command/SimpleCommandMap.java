/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command;

import cn.nukkit.Server;
import cn.nukkit.command.CommandMap;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.BanCommand;
import cn.nukkit.command.defaults.BanIpCommand;
import cn.nukkit.command.defaults.BanListCommand;
import cn.nukkit.command.defaults.DebugPasteCommand;
import cn.nukkit.command.defaults.DefaultGamemodeCommand;
import cn.nukkit.command.defaults.DeopCommand;
import cn.nukkit.command.defaults.DifficultyCommand;
import cn.nukkit.command.defaults.EffectCommand;
import cn.nukkit.command.defaults.EnchantCommand;
import cn.nukkit.command.defaults.GamemodeCommand;
import cn.nukkit.command.defaults.GameruleCommand;
import cn.nukkit.command.defaults.GarbageCollectorCommand;
import cn.nukkit.command.defaults.GenerateWorldCommand;
import cn.nukkit.command.defaults.GiveCommand;
import cn.nukkit.command.defaults.HelpCommand;
import cn.nukkit.command.defaults.KickCommand;
import cn.nukkit.command.defaults.KillCommand;
import cn.nukkit.command.defaults.ListCommand;
import cn.nukkit.command.defaults.MeCommand;
import cn.nukkit.command.defaults.OpCommand;
import cn.nukkit.command.defaults.PardonCommand;
import cn.nukkit.command.defaults.PardonIpCommand;
import cn.nukkit.command.defaults.ParticleCommand;
import cn.nukkit.command.defaults.PlaySoundCommand;
import cn.nukkit.command.defaults.PluginsCommand;
import cn.nukkit.command.defaults.ReloadCommand;
import cn.nukkit.command.defaults.SaveCommand;
import cn.nukkit.command.defaults.SaveOffCommand;
import cn.nukkit.command.defaults.SaveOnCommand;
import cn.nukkit.command.defaults.SayCommand;
import cn.nukkit.command.defaults.SeedCommand;
import cn.nukkit.command.defaults.SetWorldSpawnCommand;
import cn.nukkit.command.defaults.SpawnCommand;
import cn.nukkit.command.defaults.SpawnpointCommand;
import cn.nukkit.command.defaults.StatusCommand;
import cn.nukkit.command.defaults.StopCommand;
import cn.nukkit.command.defaults.SummonCommand;
import cn.nukkit.command.defaults.TeleportCommand;
import cn.nukkit.command.defaults.TellCommand;
import cn.nukkit.command.defaults.TimeCommand;
import cn.nukkit.command.defaults.TimingsCommand;
import cn.nukkit.command.defaults.TitleCommand;
import cn.nukkit.command.defaults.TransferServerCommand;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.command.defaults.VersionCommand;
import cn.nukkit.command.defaults.WeatherCommand;
import cn.nukkit.command.defaults.WhitelistCommand;
import cn.nukkit.command.defaults.WorldCommand;
import cn.nukkit.command.defaults.XpCommand;
import cn.nukkit.command.simple.Arguments;
import cn.nukkit.command.simple.Command;
import cn.nukkit.command.simple.CommandParameters;
import cn.nukkit.command.simple.CommandPermission;
import cn.nukkit.command.simple.ForbidConsole;
import cn.nukkit.command.simple.Parameters;
import cn.nukkit.command.simple.SimpleCommand;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleCommandMap
implements CommandMap {
    protected final Map<String, cn.nukkit.command.Command> knownCommands = new HashMap<String, cn.nukkit.command.Command>();
    private final Server a;

    public SimpleCommandMap(Server server) {
        this.a = server;
        this.a();
    }

    private void a() {
        this.register("nukkit", new VersionCommand("version"));
        this.register("nukkit", new PluginsCommand("plugins"));
        this.register("nukkit", new HelpCommand("help"));
        this.register("nukkit", new StopCommand("stop"));
        this.register("nukkit", new TellCommand("tell"));
        this.register("nukkit", new BanCommand("ban"));
        this.register("nukkit", new BanIpCommand("ban-ip"));
        this.register("nukkit", new BanListCommand("banlist"));
        this.register("nukkit", new PardonCommand("pardon"));
        this.register("nukkit", new PardonIpCommand("pardon-ip"));
        this.register("nukkit", new ListCommand("list"));
        this.register("nukkit", new KickCommand("kick"));
        this.register("nukkit", new OpCommand("op"));
        this.register("nukkit", new DeopCommand("deop"));
        this.register("nukkit", new SaveCommand("save-all"));
        this.register("nukkit", new GiveCommand("give"));
        this.register("nukkit", new EffectCommand("effect"));
        this.register("nukkit", new EnchantCommand("enchant"));
        this.register("nukkit", new GamemodeCommand("gamemode"));
        this.register("nukkit", new KillCommand("kill"));
        this.register("nukkit", new SetWorldSpawnCommand("setworldspawn"));
        this.register("nukkit", new TeleportCommand("tp"));
        this.register("nukkit", new TimeCommand("time"));
        this.register("nukkit", new ReloadCommand("reload"));
        this.register("nukkit", new WeatherCommand("weather"));
        this.register("nukkit", new XpCommand("xp"));
        this.register("nukkit", new StatusCommand("status"));
        this.register("nukkit", new SummonCommand("summon"));
        this.register("nukkit", new WorldCommand("world"));
        this.register("nukkit", new GenerateWorldCommand("genworld"));
        this.register("nukkit", new WhitelistCommand("whitelist"));
        this.register("nukkit", new GameruleCommand("gamerule"));
        this.register("nukkit", new SpawnCommand("spawn"));
        if (!Server.getInstance().suomiCraftPEMode()) {
            this.register("nukkit", new DefaultGamemodeCommand("defaultgamemode"));
            this.register("nukkit", new SayCommand("say"));
            this.register("nukkit", new MeCommand("me"));
            this.register("nukkit", new SaveOnCommand("save-on"));
            this.register("nukkit", new SaveOffCommand("save-off"));
            this.register("nukkit", new DifficultyCommand("difficulty"));
            this.register("nukkit", new ParticleCommand("particle"));
            this.register("nukkit", new SpawnpointCommand("spawnpoint"));
            this.register("nukkit", new TitleCommand("title"));
            this.register("nukkit", new TransferServerCommand("transfer"));
            this.register("nukkit", new SeedCommand("seed"));
            this.register("nukkit", new PlaySoundCommand("playsound"));
            this.register("nukkit", new DebugPasteCommand("debugpaste"));
            this.register("nukkit", new GarbageCollectorCommand("gc"));
            this.register("nukkit", new TimingsCommand("timings"));
        }
    }

    @Override
    public void registerAll(String string, List<? extends cn.nukkit.command.Command> list) {
        for (cn.nukkit.command.Command command : list) {
            this.register(string, command);
        }
    }

    @Override
    public boolean register(String string, cn.nukkit.command.Command command) {
        return this.register(string, command, null);
    }

    @Override
    public boolean register(String string, cn.nukkit.command.Command command, String string2) {
        if (string2 == null) {
            string2 = command.getName();
        }
        string2 = string2.trim().toLowerCase();
        string = string.trim().toLowerCase();
        boolean bl = this.a(command, false, string, string2);
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(command.getAliases()));
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            String string3 = (String)iterator.next();
            if (this.a(command, true, string, string3)) continue;
            iterator.remove();
        }
        command.setAliases(arrayList.toArray(new String[0]));
        if (!bl) {
            command.setLabel(string + ':' + string2);
        }
        command.register(this);
        return bl;
    }

    @Override
    public void registerSimpleCommands(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            CommandParameters commandParameters;
            CommandPermission commandPermission;
            Command command = method.getAnnotation(Command.class);
            if (command == null) continue;
            SimpleCommand simpleCommand = new SimpleCommand(object, method, command.name(), command.description(), command.usageMessage(), command.aliases());
            Arguments arguments = method.getAnnotation(Arguments.class);
            if (arguments != null) {
                simpleCommand.setMaxArgs(arguments.max());
                simpleCommand.setMinArgs(arguments.min());
            }
            if ((commandPermission = method.getAnnotation(CommandPermission.class)) != null) {
                simpleCommand.setPermission(commandPermission.value());
            }
            if (method.isAnnotationPresent(ForbidConsole.class)) {
                simpleCommand.setForbidConsole(true);
            }
            if ((commandParameters = method.getAnnotation(CommandParameters.class)) != null) {
                Map<String, CommandParameter[]> map = Arrays.stream(commandParameters.parameters()).collect(Collectors.toMap(Parameters::name, parameters -> (CommandParameter[])Arrays.stream(parameters.parameters()).map(parameter -> new CommandParameter(parameter.name(), parameter.type(), parameter.optional())).distinct().toArray(CommandParameter[]::new)));
                simpleCommand.commandParameters.putAll(map);
            }
            this.register(command.name(), simpleCommand);
        }
    }

    private boolean a(cn.nukkit.command.Command command, boolean bl, String string, String string2) {
        boolean bl2;
        this.knownCommands.put(string + ':' + string2, command);
        boolean bl3 = this.knownCommands.containsKey(string2);
        cn.nukkit.command.Command command2 = this.knownCommands.get(string2);
        boolean bl4 = bl2 = bl3 && !(command2 instanceof VanillaCommand);
        if ((command instanceof VanillaCommand || bl) && bl3 && bl2) {
            return false;
        }
        if (bl3 && command2.getLabel() != null && command2.getLabel().equals(string2) && bl2) {
            return false;
        }
        if (!bl) {
            command.setLabel(string2);
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, cn.nukkit.command.Command> entry : this.knownCommands.entrySet()) {
            cn.nukkit.command.Command command3 = entry.getValue();
            if (!command3.getLabel().equalsIgnoreCase(command.getLabel()) || command3.equals(command) || !(command3 instanceof VanillaCommand)) continue;
            arrayList.add(entry.getKey());
        }
        for (Map.Entry<String, cn.nukkit.command.Command> entry : arrayList) {
            this.knownCommands.remove(entry);
        }
        this.knownCommands.put(string2, command);
        return true;
    }

    private static ArrayList<String> a(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        ArrayList<String> arrayList = new ArrayList<String>();
        boolean bl = true;
        int n = 0;
        for (int k = 0; k < stringBuilder.length(); ++k) {
            if (stringBuilder.charAt(k) == '\\') {
                stringBuilder.deleteCharAt(k);
                continue;
            }
            if (stringBuilder.charAt(k) == ' ' && bl) {
                String string2 = stringBuilder.substring(n, k);
                if (!string2.isEmpty()) {
                    arrayList.add(string2);
                }
                n = k + 1;
                continue;
            }
            if (stringBuilder.charAt(k) != '\"') continue;
            stringBuilder.deleteCharAt(k);
            --k;
            bl = !bl;
        }
        String string3 = stringBuilder.substring(n);
        if (!string3.isEmpty()) {
            arrayList.add(string3);
        }
        return arrayList;
    }

    @Override
    public boolean dispatch(CommandSender commandSender, String string) {
        block6: {
            cn.nukkit.command.Command command;
            block5: {
                ArrayList<String> arrayList = SimpleCommandMap.a(string);
                if (arrayList.isEmpty()) {
                    return false;
                }
                String string2 = arrayList.remove(0).toLowerCase();
                String[] stringArray = arrayList.toArray(new String[0]);
                command = this.getCommand(string2);
                if (command == null) {
                    return false;
                }
                if (command.timing != null) {
                    command.timing.startTiming();
                }
                try {
                    command.execute(commandSender, string2, stringArray);
                }
                catch (Exception exception) {
                    commandSender.sendMessage(new TranslationContainer((Object)((Object)TextFormat.RED) + "%commands.generic.exception"));
                    this.a.getLogger().critical(this.a.getLanguage().translateString("nukkit.command.exception", string, command.toString(), Utils.getExceptionMessage(exception)));
                    MainLogger mainLogger = commandSender.getServer().getLogger();
                    if (mainLogger == null) break block5;
                    mainLogger.logException(exception);
                }
            }
            if (command.timing == null) break block6;
            command.timing.stopTiming();
        }
        return true;
    }

    @Override
    public void clearCommands() {
        for (cn.nukkit.command.Command command : this.knownCommands.values()) {
            command.unregister(this);
        }
        this.knownCommands.clear();
        this.a();
    }

    @Override
    public cn.nukkit.command.Command getCommand(String string) {
        if (this.knownCommands.containsKey(string)) {
            return this.knownCommands.get(string);
        }
        return null;
    }

    public Map<String, cn.nukkit.command.Command> getCommands() {
        return this.knownCommands;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

