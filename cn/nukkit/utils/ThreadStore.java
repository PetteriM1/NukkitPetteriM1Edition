/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadStore {
    public static final Map<String, Object> store = new ConcurrentHashMap<String, Object>();
}

