/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin.service;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.service.ServicePriority;

public class RegisteredServiceProvider<T>
implements Comparable<RegisteredServiceProvider<T>> {
    private final Plugin c;
    private final ServicePriority d;
    private final Class<T> b;
    private final T a;

    RegisteredServiceProvider(Class<T> clazz, T t, ServicePriority servicePriority, Plugin plugin) {
        this.c = plugin;
        this.a = t;
        this.b = clazz;
        this.d = servicePriority;
    }

    public Class<T> getService() {
        return this.b;
    }

    public Plugin getPlugin() {
        return this.c;
    }

    public T getProvider() {
        return this.a;
    }

    public ServicePriority getPriority() {
        return this.d;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        RegisteredServiceProvider registeredServiceProvider = (RegisteredServiceProvider)object;
        return this.a == registeredServiceProvider.a || this.a.equals(registeredServiceProvider.a);
    }

    public int hashCode() {
        return this.a.hashCode();
    }

    @Override
    public int compareTo(RegisteredServiceProvider<T> registeredServiceProvider) {
        return registeredServiceProvider.d.ordinal() - this.d.ordinal();
    }
}

