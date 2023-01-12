/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.function.Function;

public class JsonUtil {
    private static final Gson a = new GsonBuilder().setPrettyPrinting().create();

    public static JsonArray toArray(Object ... objectArray) {
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, objectArray);
        return a.toJsonTree(arrayList).getAsJsonArray();
    }

    public static JsonObject toObject(Object object) {
        return a.toJsonTree(object).getAsJsonObject();
    }

    public static <E> JsonObject mapToObject(Iterable<E> iterable, Function<E, JSONPair> function) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        for (E e2 : iterable) {
            JSONPair jSONPair = function.apply(e2);
            if (jSONPair == null) continue;
            linkedHashMap.put(jSONPair.key, jSONPair.value);
        }
        return a.toJsonTree(linkedHashMap).getAsJsonObject();
    }

    public static <E> JsonArray mapToArray(E[] EArray, Function<E, Object> function) {
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, EArray);
        return JsonUtil.mapToArray(arrayList, function);
    }

    public static <E> JsonArray mapToArray(Iterable<E> iterable, Function<E, Object> function) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (E e2 : iterable) {
            Object object = function.apply(e2);
            if (object == null) continue;
            arrayList.add(object);
        }
        return a.toJsonTree(arrayList).getAsJsonArray();
    }

    public static class JSONPair {
        public final String key;
        public final Object value;

        public JSONPair(String string, Object object) {
            this.key = string;
            this.value = object;
        }

        public JSONPair(int n, Object object) {
            this.key = String.valueOf(n);
            this.value = object;
        }
    }
}

