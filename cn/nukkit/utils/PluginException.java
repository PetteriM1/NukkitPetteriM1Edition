/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.utils.ServerException;

public class PluginException
extends ServerException {
    public PluginException(String string) {
        super(string);
    }

    public PluginException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

