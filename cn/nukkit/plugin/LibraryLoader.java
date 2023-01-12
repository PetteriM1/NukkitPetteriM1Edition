/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.plugin.Library;
import cn.nukkit.plugin.LibraryLoadException;
import cn.nukkit.plugin.a;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.logging.Logger;

public class LibraryLoader {
    private static final File c;
    private static final Logger b;
    private static final String a;

    public static void load(String string) {
        String[] stringArray = string.split(":");
        if (stringArray.length != 3) {
            throw new IllegalArgumentException(string);
        }
        LibraryLoader.load(new a(stringArray));
    }

    public static void load(Library library) {
        Object object;
        File file;
        String string = library.getGroupId().replace('.', '/') + '/' + library.getArtifactId() + '/' + library.getVersion();
        String string2 = library.getArtifactId() + '-' + library.getVersion() + ".jar";
        File file2 = new File(c, string);
        if (file2.mkdirs()) {
            b.info("Created " + file2.getPath() + '.');
        }
        if (!(file = new File(file2, string2)).isFile()) {
            try {
                object = new URL("https://repo1.maven.org/maven2/" + string + '/' + string2);
                b.info("Get library from " + object + '.');
                Files.copy(((URL)object).openStream(), file.toPath(), new CopyOption[0]);
                b.info("Get library " + string2 + " done!");
            }
            catch (IOException iOException) {
                throw new LibraryLoadException(library);
            }
        }
        try {
            object = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            boolean bl = ((AccessibleObject)object).isAccessible();
            if (!bl) {
                ((AccessibleObject)object).setAccessible(true);
            }
            URLClassLoader uRLClassLoader = (URLClassLoader)Thread.currentThread().getContextClassLoader();
            URL uRL = file.toURI().toURL();
            ((Method)object).invoke(uRLClassLoader, uRL);
            ((AccessibleObject)object).setAccessible(bl);
        }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | MalformedURLException exception) {
            throw new LibraryLoadException(library);
        }
        b.info("Load library " + string2 + " done!");
    }

    public static File getBaseFolder() {
        return c;
    }

    static {
        block0: {
            a = ".jar";
            c = new File("./libraries");
            b = Logger.getLogger("LibraryLoader");
            if (!c.mkdir()) break block0;
            b.info("[LibraryLoader] Created libraries folder");
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

