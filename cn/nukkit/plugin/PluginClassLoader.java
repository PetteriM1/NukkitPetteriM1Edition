/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.block.i;
import cn.nukkit.plugin.JavaPluginLoader;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PluginClassLoader
extends URLClassLoader {
    private final JavaPluginLoader a;
    private final Map<String, Class> b = new HashMap<String, Class>();

    public PluginClassLoader(JavaPluginLoader javaPluginLoader, ClassLoader classLoader, File file) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, classLoader);
        this.a = javaPluginLoader;
    }

    @Override
    protected Class<?> findClass(String string) throws ClassNotFoundException {
        return this.findClass(string, true);
    }

    protected Class<?> findClass(String string, boolean bl) throws ClassNotFoundException {
        if (string.startsWith("cn.nukkit.") || string.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(string);
        }
        Class<?> clazz = this.b.get(string);
        if (clazz == null) {
            if (bl) {
                clazz = this.a.a(string);
            }
            if (clazz == null && (clazz = super.findClass(i.a(string))) != null) {
                this.a.a(string, clazz);
            }
            this.b.put(string, clazz);
        }
        return clazz;
    }

    private static ClassNotFoundException a(ClassNotFoundException classNotFoundException) {
        return classNotFoundException;
    }
}

