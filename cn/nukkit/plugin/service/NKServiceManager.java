/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin.service;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.service.RegisteredServiceProvider;
import cn.nukkit.plugin.service.ServiceManager;
import cn.nukkit.plugin.service.ServicePriority;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NKServiceManager
implements ServiceManager {
    private final Map<Class<?>, List<RegisteredServiceProvider<?>>> a = new HashMap();

    @Override
    public <T> boolean register(Class<T> clazz, T t, Plugin plugin, ServicePriority servicePriority) {
        Preconditions.checkNotNull(t);
        Preconditions.checkNotNull(servicePriority);
        Preconditions.checkNotNull(clazz);
        if (plugin == null && t.getClass().getClassLoader() != Server.class.getClassLoader()) {
            throw new NullPointerException("plugin");
        }
        return this.provide(clazz, t, plugin, servicePriority);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected <T> boolean provide(Class<T> clazz2, T t, Plugin plugin, ServicePriority servicePriority) {
        Map<Class<?>, List<RegisteredServiceProvider<?>>> map = this.a;
        synchronized (map) {
            List list = this.a.computeIfAbsent(clazz2, clazz -> new ArrayList());
            RegisteredServiceProvider<T> registeredServiceProvider = new RegisteredServiceProvider<T>(clazz2, t, servicePriority, plugin);
            int n = Collections.binarySearch(list, registeredServiceProvider);
            if (n > -1) {
                return false;
            }
            list.add(-(n + 1), registeredServiceProvider);
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<RegisteredServiceProvider<?>> cancel(Plugin plugin) {
        ImmutableList.Builder builder = ImmutableList.builder();
        Map<Class<?>, List<RegisteredServiceProvider<?>>> map = this.a;
        synchronized (map) {
            for (List<RegisteredServiceProvider<?>> list : this.a.values()) {
                Iterator<RegisteredServiceProvider<?>> iterator = list.iterator();
                while (iterator.hasNext()) {
                    RegisteredServiceProvider<?> registeredServiceProvider = iterator.next();
                    if (registeredServiceProvider.getPlugin() != plugin) continue;
                    iterator.remove();
                    builder.add(registeredServiceProvider);
                }
            }
        }
        return builder.build();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> RegisteredServiceProvider<T> cancel(Class<T> clazz, T t) {
        RegisteredServiceProvider<?> registeredServiceProvider = null;
        Map<Class<?>, List<RegisteredServiceProvider<?>>> map = this.a;
        synchronized (map) {
            Iterator<RegisteredServiceProvider<?>> iterator = this.a.get(clazz).iterator();
            while (iterator.hasNext() && registeredServiceProvider == null) {
                RegisteredServiceProvider<?> registeredServiceProvider2 = iterator.next();
                if (registeredServiceProvider2.getProvider() != t) continue;
                iterator.remove();
                registeredServiceProvider = registeredServiceProvider2;
            }
        }
        return registeredServiceProvider;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> RegisteredServiceProvider<T> getProvider(Class<T> clazz) {
        Map<Class<?>, List<RegisteredServiceProvider<?>>> map = this.a;
        synchronized (map) {
            List<RegisteredServiceProvider<?>> list = this.a.get(clazz);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
    }

    @Override
    public List<Class<?>> getKnownService() {
        return ImmutableList.copyOf(this.a.keySet());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<RegisteredServiceProvider<?>> getRegistrations(Plugin plugin) {
        ImmutableList.Builder builder = ImmutableList.builder();
        Map<Class<?>, List<RegisteredServiceProvider<?>>> map = this.a;
        synchronized (map) {
            for (List<RegisteredServiceProvider<?>> list : this.a.values()) {
                for (RegisteredServiceProvider<?> registeredServiceProvider : list) {
                    if (!registeredServiceProvider.getPlugin().equals(plugin)) continue;
                    builder.add(registeredServiceProvider);
                }
            }
        }
        return builder.build();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> List<RegisteredServiceProvider<T>> getRegistrations(Class<T> clazz) {
        ImmutableList.Builder builder = ImmutableList.builder();
        Map<Class<?>, List<RegisteredServiceProvider<?>>> map = this.a;
        synchronized (map) {
            List<RegisteredServiceProvider<?>> list = this.a.get(clazz);
            if (list == null) {
                return ImmutableList.of();
            }
            for (RegisteredServiceProvider<?> registeredServiceProvider : list) {
                builder.add(registeredServiceProvider);
            }
        }
        return builder.build();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> boolean isProvidedFor(Class<T> clazz) {
        Map<Class<?>, List<RegisteredServiceProvider<?>>> map = this.a;
        synchronized (map) {
            return this.a.containsKey(clazz);
        }
    }

    private static NullPointerException a(NullPointerException nullPointerException) {
        return nullPointerException;
    }
}

