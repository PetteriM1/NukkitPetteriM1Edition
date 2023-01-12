/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format;

import cn.nukkit.Server;
import cn.nukkit.level.format.LevelProvider;
import java.util.HashMap;
import java.util.Map;

public abstract class LevelProviderManager {
    protected static final Map<String, Class<? extends LevelProvider>> providers = new HashMap<String, Class<? extends LevelProvider>>();

    public static void addProvider(Server server, Class<? extends LevelProvider> clazz) {
        try {
            providers.put((String)clazz.getMethod("getProviderName", new Class[0]).invoke(null, new Object[0]), clazz);
        }
        catch (Exception exception) {
            Server.getInstance().getLogger().logException(exception);
        }
    }

    public static Class<? extends LevelProvider> getProvider(String string) {
        for (Class<? extends LevelProvider> clazz : providers.values()) {
            try {
                if (!((Boolean)clazz.getMethod("isValid", String.class).invoke(null, string)).booleanValue()) continue;
                return clazz;
            }
            catch (Exception exception) {
                Server.getInstance().getLogger().logException(exception);
            }
        }
        return null;
    }

    public static Class<? extends LevelProvider> getProviderByName(String string) {
        return providers.getOrDefault(string.trim().toLowerCase(), null);
    }
}

