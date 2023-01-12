/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.event.Event;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.Listener;
import cn.nukkit.permission.Permissible;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.EventExecutor;
import cn.nukkit.plugin.MethodEventExecutor;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.plugin.PluginLoader;
import cn.nukkit.plugin.RegisteredListener;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.PluginException;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import io.netty.util.internal.ConcurrentSet;
import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class PluginManager {
    private final Server b;
    private final SimpleCommandMap a;
    protected final Map<String, Plugin> plugins = new LinkedHashMap<String, Plugin>();
    protected final Map<String, Permission> permissions = new ConcurrentHashMap<String, Permission>();
    protected final Map<String, Permission> defaultPerms = new ConcurrentHashMap<String, Permission>();
    protected final Map<String, Permission> defaultPermsOp = new ConcurrentHashMap<String, Permission>();
    protected final Map<String, Set<Permissible>> permSubs = new ConcurrentHashMap<String, Set<Permissible>>();
    protected final Set<Permissible> defSubs = new ConcurrentSet<Permissible>();
    protected final Set<Permissible> defSubsOp = new ConcurrentSet<Permissible>();
    protected final Map<String, PluginLoader> fileAssociations = new HashMap<String, PluginLoader>();

    public PluginManager(Server server, SimpleCommandMap simpleCommandMap) {
        this.b = server;
        this.a = simpleCommandMap;
    }

    public Plugin getPlugin(String string) {
        if (this.plugins.containsKey(string)) {
            return this.plugins.get(string);
        }
        return null;
    }

    public boolean registerInterface(Class<? extends PluginLoader> clazz) {
        if (clazz != null) {
            try {
                Constructor<? extends PluginLoader> constructor = clazz.getDeclaredConstructor(Server.class);
                constructor.setAccessible(true);
                this.fileAssociations.put(clazz.getName(), constructor.newInstance(this.b));
                return true;
            }
            catch (Exception exception) {
                return false;
            }
        }
        return false;
    }

    public Map<String, Plugin> getPlugins() {
        return this.plugins;
    }

    public Plugin loadPlugin(String string) {
        return this.loadPlugin(string, null);
    }

    public Plugin loadPlugin(File file) {
        return this.loadPlugin(file, null);
    }

    public Plugin loadPlugin(String string, Map<String, PluginLoader> map) {
        return this.loadPlugin(new File(string), map);
    }

    public Plugin loadPlugin(File file, Map<String, PluginLoader> map) {
        for (PluginLoader pluginLoader : (map == null ? this.fileAssociations : map).values()) {
            for (Pattern pattern : pluginLoader.getPluginFilters()) {
                PluginDescription pluginDescription;
                if (!pattern.matcher(file.getName()).matches() || (pluginDescription = pluginLoader.getPluginDescription(file)) == null) continue;
                try {
                    Plugin plugin = pluginLoader.loadPlugin(file);
                    if (plugin == null) continue;
                    this.plugins.put(plugin.getDescription().getName(), plugin);
                    List<PluginCommand> list = this.parseYamlCommands(plugin);
                    if (!list.isEmpty()) {
                        this.a.registerAll(plugin.getDescription().getName(), list);
                    }
                    return plugin;
                }
                catch (Exception exception) {
                    Server.getInstance().getLogger().critical("Could not load plugin", exception);
                    return null;
                }
            }
        }
        return null;
    }

    public Map<String, Plugin> loadPlugins(String string) {
        return this.loadPlugins(new File(string));
    }

    public Map<String, Plugin> loadPlugins(File file) {
        return this.loadPlugins(file, null);
    }

    public Map<String, Plugin> loadPlugins(String string, List<String> list) {
        return this.loadPlugins(new File(string), list);
    }

    public Map<String, Plugin> loadPlugins(File file, List<String> list) {
        return this.loadPlugins(file, list, false);
    }

    public Map<String, Plugin> loadPlugins(File file2, List<String> list, boolean bl) {
        if (file2.isDirectory()) {
            LinkedHashMap<String, File> linkedHashMap = new LinkedHashMap<String, File>();
            LinkedHashMap<String, Plugin> linkedHashMap2 = new LinkedHashMap<String, Plugin>();
            LinkedHashMap<String, List<String>> linkedHashMap3 = new LinkedHashMap<String, List<String>>();
            LinkedHashMap<String, List<String>> linkedHashMap4 = new LinkedHashMap<String, List<String>>();
            Map<Object, Object> map = new LinkedHashMap();
            if (list != null) {
                for (String object : list) {
                    if (!this.fileAssociations.containsKey(object)) continue;
                    map.put(object, this.fileAssociations.get(object));
                }
            } else {
                map = this.fileAssociations;
            }
            for (PluginLoader pluginLoader : map.values()) {
                for (File file3 : file2.listFiles((file, string) -> {
                    for (Pattern pattern : pluginLoader.getPluginFilters()) {
                        if (!pattern.matcher(string).matches()) continue;
                        return true;
                    }
                    return false;
                })) {
                    Object object;
                    if (file3.isDirectory() && !bl) continue;
                    try {
                        PluginDescription pluginDescription = pluginLoader.getPluginDescription(file3);
                        if (pluginDescription == null) continue;
                        object = pluginDescription.getName();
                        if (linkedHashMap.containsKey(object) || this.getPlugin((String)object) != null) {
                            this.b.getLogger().error(this.b.getLanguage().translateString("nukkit.plugin.duplicateError", new String[]{object}));
                            continue;
                        }
                        linkedHashMap.put((String)object, file3);
                        linkedHashMap4.put((String)object, pluginDescription.getSoftDepend());
                        linkedHashMap3.put((String)object, pluginDescription.getDepend());
                        for (String string2 : pluginDescription.getLoadBefore()) {
                            if (linkedHashMap4.containsKey(string2)) {
                                ((List)linkedHashMap4.get(string2)).add(object);
                                continue;
                            }
                            ArrayList<Object> arrayList = new ArrayList<Object>();
                            arrayList.add(object);
                            linkedHashMap4.put(string2, arrayList);
                        }
                    }
                    catch (Exception exception) {
                        this.b.getLogger().error(this.b.getLanguage().translateString("nukkit.plugin.fileError", file3.getName(), file2.toString(), Utils.getExceptionMessage(exception)));
                        object = this.b.getLogger();
                        if (object == null) continue;
                        ((MainLogger)object).logException(exception);
                    }
                }
            }
            while (!linkedHashMap.isEmpty()) {
                boolean bl2 = true;
                for (String string3 : new ArrayList(linkedHashMap.keySet())) {
                    File file4 = (File)linkedHashMap.get(string3);
                    if (linkedHashMap3.containsKey(string3)) {
                        for (String string4 : new ArrayList((Collection)linkedHashMap3.get(string3))) {
                            if (linkedHashMap2.containsKey(string4) || this.getPlugin(string4) != null) {
                                ((List)linkedHashMap3.get(string3)).remove(string4);
                                continue;
                            }
                            if (linkedHashMap.containsKey(string4)) continue;
                            this.b.getLogger().critical(this.b.getLanguage().translateString("nukkit.plugin.loadError", new String[]{string3, "%nukkit.plugin.unknownDependency"}) + ' ' + string4);
                            break;
                        }
                        if (((List)linkedHashMap3.get(string3)).isEmpty()) {
                            linkedHashMap3.remove(string3);
                        }
                    }
                    if (linkedHashMap4.containsKey(string3)) {
                        for (String string5 : new ArrayList((Collection)linkedHashMap4.get(string3))) {
                            if (!linkedHashMap2.containsKey(string5) && this.getPlugin(string5) == null) continue;
                            ((List)linkedHashMap4.get(string3)).remove(string5);
                        }
                        if (((List)linkedHashMap4.get(string3)).isEmpty()) {
                            linkedHashMap4.remove(string3);
                        }
                    }
                    if (linkedHashMap3.containsKey(string3) || linkedHashMap4.containsKey(string3)) continue;
                    linkedHashMap.remove(string3);
                    bl2 = false;
                    Plugin plugin = this.loadPlugin(file4, map);
                    if (plugin != null) {
                        linkedHashMap2.put(string3, plugin);
                        continue;
                    }
                    this.b.getLogger().critical(this.b.getLanguage().translateString("nukkit.plugin.genericLoadError", string3));
                }
                if (!bl2) continue;
                for (String string6 : new ArrayList(linkedHashMap.keySet())) {
                    File file5 = (File)linkedHashMap.get(string6);
                    if (linkedHashMap3.containsKey(string6)) continue;
                    linkedHashMap4.remove(string6);
                    linkedHashMap.remove(string6);
                    bl2 = false;
                    Plugin plugin = this.loadPlugin(file5, map);
                    if (plugin != null) {
                        linkedHashMap2.put(string6, plugin);
                        continue;
                    }
                    this.b.getLogger().critical(this.b.getLanguage().translateString("nukkit.plugin.genericLoadError", string6));
                }
                if (!bl2) continue;
                for (String string7 : linkedHashMap.keySet()) {
                    this.b.getLogger().critical(this.b.getLanguage().translateString("nukkit.plugin.loadError", new String[]{string7, "%nukkit.plugin.circularDependency"}));
                }
                linkedHashMap.clear();
            }
            return linkedHashMap2;
        }
        return new HashMap<String, Plugin>();
    }

    public Permission getPermission(String string) {
        if (this.permissions.containsKey(string)) {
            return this.permissions.get(string);
        }
        return null;
    }

    public boolean addPermission(Permission permission) {
        if (!this.permissions.containsKey(permission.getName())) {
            this.permissions.put(permission.getName(), permission);
            this.a(permission);
            return true;
        }
        return false;
    }

    public void removePermission(String string) {
        this.permissions.remove(string);
    }

    public void removePermission(Permission permission) {
        this.removePermission(permission.getName());
    }

    public Map<String, Permission> getDefaultPermissions(boolean bl) {
        if (bl) {
            return this.defaultPermsOp;
        }
        return this.defaultPerms;
    }

    public void recalculatePermissionDefaults(Permission permission) {
        block0: {
            if (!this.permissions.containsKey(permission.getName())) break block0;
            this.defaultPermsOp.remove(permission.getName());
            this.defaultPerms.remove(permission.getName());
            this.a(permission);
        }
    }

    private void a(Permission permission) {
        block3: {
            if (Timings.permissionDefaultTimer != null) {
                Timings.permissionDefaultTimer.startTiming();
            }
            if (permission.getDefault().equals("op") || permission.getDefault().equals("true")) {
                this.defaultPermsOp.put(permission.getName(), permission);
                this.a(true);
            }
            if (permission.getDefault().equals("notop") || permission.getDefault().equals("true")) {
                this.defaultPerms.put(permission.getName(), permission);
                this.a(false);
            }
            if (Timings.permissionDefaultTimer == null) break block3;
            Timings.permissionDefaultTimer.stopTiming();
        }
    }

    private void a(boolean bl) {
        for (Permissible permissible : this.getDefaultPermSubscriptions(bl)) {
            permissible.recalculatePermissions();
        }
    }

    public void subscribeToPermission(String string, Permissible permissible) {
        if (!this.permSubs.containsKey(string)) {
            this.permSubs.put(string, new ConcurrentSet());
        }
        this.permSubs.get(string).add(permissible);
    }

    public void unsubscribeFromPermission(String string, Permissible permissible) {
        block1: {
            if (!this.permSubs.containsKey(string)) break block1;
            this.permSubs.get(string).remove(permissible);
            if (this.permSubs.get(string).isEmpty()) {
                this.permSubs.remove(string);
            }
        }
    }

    public Set<Permissible> getPermissionSubscriptions(String string) {
        if (this.permSubs.containsKey(string)) {
            return new HashSet<Permissible>((Collection)this.permSubs.get(string));
        }
        return new HashSet<Permissible>();
    }

    public void subscribeToDefaultPerms(boolean bl, Permissible permissible) {
        if (bl) {
            this.defSubsOp.add(permissible);
        } else {
            this.defSubs.add(permissible);
        }
    }

    public void unsubscribeFromDefaultPerms(boolean bl, Permissible permissible) {
        if (bl) {
            this.defSubsOp.remove(permissible);
        } else {
            this.defSubs.remove(permissible);
        }
    }

    public Set<Permissible> getDefaultPermSubscriptions(boolean bl) {
        if (bl) {
            return new HashSet<Permissible>(this.defSubsOp);
        }
        return new HashSet<Permissible>(this.defSubs);
    }

    public Map<String, Permission> getPermissions() {
        return this.permissions;
    }

    public boolean isPluginEnabled(Plugin plugin) {
        if (plugin != null && this.plugins.containsKey(plugin.getDescription().getName())) {
            return plugin.isEnabled();
        }
        return false;
    }

    public void enablePlugin(Plugin plugin) {
        if (!plugin.isEnabled()) {
            try {
                for (Permission permission : plugin.getDescription().getPermissions()) {
                    this.addPermission(permission);
                }
                plugin.getPluginLoader().enablePlugin(plugin);
            }
            catch (Throwable throwable) {
                MainLogger mainLogger = this.b.getLogger();
                if (mainLogger != null) {
                    mainLogger.logException(new RuntimeException(throwable));
                }
                this.disablePlugin(plugin);
            }
        }
    }

    protected List<PluginCommand> parseYamlCommands(Plugin plugin) {
        ArrayList<PluginCommand> arrayList = new ArrayList<PluginCommand>();
        for (Map.Entry<String, Object> entry : plugin.getDescription().getCommands().entrySet()) {
            Object v;
            String string = entry.getKey();
            Object object = entry.getValue();
            if (string.contains(":")) {
                this.b.getLogger().critical(this.b.getLanguage().translateString("nukkit.plugin.commandError", new String[]{string, plugin.getDescription().getFullName()}));
                continue;
            }
            if (!(object instanceof Map)) continue;
            PluginCommand<Plugin> pluginCommand = new PluginCommand<Plugin>(string, plugin);
            if (((Map)object).containsKey("description")) {
                pluginCommand.setDescription((String)((Map)object).get("description"));
            }
            if (((Map)object).containsKey("usage")) {
                pluginCommand.setUsage((String)((Map)object).get("usage"));
            }
            if (((Map)object).containsKey("aliases") && (v = ((Map)object).get("aliases")) instanceof List) {
                ArrayList<String> arrayList2 = new ArrayList<String>();
                for (String string2 : (List)v) {
                    if (string2.contains(":")) {
                        this.b.getLogger().critical(this.b.getLanguage().translateString("nukkit.plugin.aliasError", new String[]{string2, plugin.getDescription().getFullName()}));
                        continue;
                    }
                    arrayList2.add(string2);
                }
                pluginCommand.setAliases(arrayList2.toArray(new String[0]));
            }
            if (((Map)object).containsKey("permission")) {
                pluginCommand.setPermission((String)((Map)object).get("permission"));
            }
            if (((Map)object).containsKey("permission-message")) {
                pluginCommand.setPermissionMessage((String)((Map)object).get("permission-message"));
            }
            arrayList.add(pluginCommand);
        }
        return arrayList;
    }

    public void disablePlugins() {
        ListIterator<Plugin> listIterator = new ArrayList<Plugin>(this.plugins.values()).listIterator(this.plugins.size());
        while (listIterator.hasPrevious()) {
            this.disablePlugin(listIterator.previous());
        }
    }

    public void disablePlugin(Plugin plugin) {
        if (plugin.isEnabled()) {
            block4: {
                try {
                    plugin.getPluginLoader().disablePlugin(plugin);
                }
                catch (Exception exception) {
                    MainLogger object = this.b.getLogger();
                    if (object == null) break block4;
                    object.logException(exception);
                }
            }
            this.b.getScheduler().cancelTask(plugin);
            HandlerList.unregisterAll(plugin);
            for (Permission permission : plugin.getDescription().getPermissions()) {
                this.removePermission(permission);
            }
        }
    }

    public void clearPlugins() {
        this.disablePlugins();
        this.plugins.clear();
        this.fileAssociations.clear();
        this.permissions.clear();
        this.defaultPerms.clear();
        this.defaultPermsOp.clear();
    }

    public void callEvent(Event event) {
        try {
            for (RegisteredListener registeredListener : this.a(event.getClass()).getRegisteredListeners()) {
                if (!registeredListener.getPlugin().isEnabled()) continue;
                try {
                    registeredListener.callEvent(event);
                }
                catch (Exception exception) {
                    this.b.getLogger().critical(this.b.getLanguage().translateString("nukkit.plugin.eventError", event.getEventName(), registeredListener.getPlugin().getDescription().getFullName(), exception.getMessage(), registeredListener.getListener().getClass().getName()));
                    this.b.getLogger().logException(exception);
                }
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            this.b.getLogger().logException(illegalAccessException);
        }
    }

    public void registerEvents(Listener listener, Plugin plugin) {
        HashSet hashSet;
        if (!plugin.isEnabled()) {
            throw new PluginException("Plugin attempted to register " + listener.getClass().getName() + " while not enabled");
        }
        try {
            Method[] methodArray = listener.getClass().getMethods();
            Object object = listener.getClass().getDeclaredMethods();
            hashSet = new HashSet(methodArray.length + ((Method[])object).length, 1.0f);
            Collections.addAll(hashSet, methodArray);
            Collections.addAll(hashSet, object);
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            plugin.getLogger().error("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + noClassDefFoundError.getMessage() + " does not exist.");
            return;
        }
        for (Object object : hashSet) {
            Class<?> clazz;
            EventHandler eventHandler = ((Method)object).getAnnotation(EventHandler.class);
            if (eventHandler == null || ((Method)object).isBridge() || ((Method)object).isSynthetic()) continue;
            if (((Method)object).getParameterTypes().length != 1 || !Event.class.isAssignableFrom(clazz = ((Method)object).getParameterTypes()[0])) {
                plugin.getLogger().error(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + ((Method)object).toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            Class<Event> clazz2 = clazz.asSubclass(Event.class);
            ((AccessibleObject)object).setAccessible(true);
            if (Nukkit.DEBUG > 1) {
                Class<Event> clazz3 = clazz2;
                while (Event.class.isAssignableFrom(clazz3)) {
                    if (clazz3.getAnnotation(Deprecated.class) != null) {
                        this.b.getLogger().warning(this.b.getLanguage().translateString("nukkit.plugin.deprecatedEvent", plugin.getName(), clazz3.getName(), listener.getClass().getName() + "." + ((Method)object).getName() + "()"));
                        break;
                    }
                    clazz3 = clazz3.getSuperclass();
                }
            }
            this.registerEvent(clazz2, listener, eventHandler.priority(), new MethodEventExecutor((Method)object), plugin, eventHandler.ignoreCancelled());
        }
    }

    public void registerEvent(Class<? extends Event> clazz, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, Plugin plugin) throws PluginException {
        this.registerEvent(clazz, listener, eventPriority, eventExecutor, plugin, false);
    }

    public void registerEvent(Class<? extends Event> clazz, Listener listener, EventPriority eventPriority, EventExecutor eventExecutor, Plugin plugin, boolean bl) throws PluginException {
        if (!plugin.isEnabled()) {
            throw new PluginException("Plugin attempted to register " + clazz + " while not enabled");
        }
        try {
            Timing timing = Timings.getPluginEventTiming(clazz, listener, eventExecutor, plugin);
            this.a(clazz).register(new RegisteredListener(listener, eventExecutor, eventPriority, plugin, bl, timing));
        }
        catch (IllegalAccessException illegalAccessException) {
            Server.getInstance().getLogger().logException(illegalAccessException);
        }
    }

    private HandlerList a(Class<? extends Event> clazz) throws IllegalAccessException {
        try {
            Method method = this.b(clazz).getDeclaredMethod("getHandlers", new Class[0]);
            method.setAccessible(true);
            return (HandlerList)method.invoke(null, new Object[0]);
        }
        catch (NullPointerException nullPointerException) {
            throw new IllegalArgumentException("getHandlers method in " + clazz.getName() + " was not static!");
        }
        catch (Exception exception) {
            throw new IllegalAccessException(Utils.getExceptionMessage(exception));
        }
    }

    private Class<? extends Event> b(Class<? extends Event> clazz) throws IllegalAccessException {
        try {
            clazz.getDeclaredMethod("getHandlers", new Class[0]);
            return clazz;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            if (clazz.getSuperclass() != null && clazz.getSuperclass() != Event.class && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return this.b(clazz.getSuperclass().asSubclass(Event.class));
            }
            throw new IllegalAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlers method required!");
        }
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

