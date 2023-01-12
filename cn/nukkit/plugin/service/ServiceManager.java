/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin.service;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.service.RegisteredServiceProvider;
import cn.nukkit.plugin.service.ServicePriority;
import java.util.List;

public interface ServiceManager {
    public <T> boolean register(Class<T> var1, T var2, Plugin var3, ServicePriority var4);

    public List<RegisteredServiceProvider<?>> cancel(Plugin var1);

    public <T> RegisteredServiceProvider<T> cancel(Class<T> var1, T var2);

    public <T> RegisteredServiceProvider<T> getProvider(Class<T> var1);

    public List<Class<?>> getKnownService();

    public List<RegisteredServiceProvider<?>> getRegistrations(Plugin var1);

    public <T> List<RegisteredServiceProvider<T>> getRegistrations(Class<T> var1);

    public <T> boolean isProvidedFor(Class<T> var1);
}

