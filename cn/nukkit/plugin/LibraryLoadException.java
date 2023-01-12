/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.plugin.Library;

public class LibraryLoadException
extends RuntimeException {
    public LibraryLoadException(Library library) {
        super("Load library " + library.getArtifactId() + " failed!");
    }
}

