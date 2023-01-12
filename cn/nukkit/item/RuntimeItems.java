/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.item.RuntimeItemMapping;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RuntimeItems {
    private static final Logger i = LogManager.getLogger(RuntimeItems.class);
    private static final Map<String, Integer> g = new HashMap<String, Integer>();
    private static RuntimeItemMapping m;
    private static RuntimeItemMapping a;
    private static RuntimeItemMapping e;
    private static RuntimeItemMapping b;
    private static RuntimeItemMapping j;
    private static RuntimeItemMapping d;
    private static RuntimeItemMapping k;
    private static RuntimeItemMapping l;
    private static RuntimeItemMapping h;
    private static RuntimeItemMapping c;
    private static boolean f;

    public static void init() {
        if (f) {
            throw new IllegalStateException("RuntimeItems were already generated!");
        }
        f = true;
        i.debug("Loading runtime items...");
        InputStream inputStream = Server.class.getClassLoader().getResourceAsStream("legacy_item_ids.json");
        if (inputStream == null) {
            throw new AssertionError((Object)"Unable to load legacy_item_ids.json");
        }
        JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
        for (String object2 : jsonObject.keySet()) {
            g.put(object2, jsonObject.get(object2).getAsInt());
        }
        InputStream inputStream2 = Server.class.getClassLoader().getResourceAsStream("item_mappings.json");
        if (inputStream2 == null) {
            throw new AssertionError((Object)"Unable to load item_mappings.json");
        }
        JsonObject jsonObject2 = JsonParser.parseReader(new InputStreamReader(inputStream2)).getAsJsonObject();
        HashMap<String, MappingEntry> hashMap = new HashMap<String, MappingEntry>();
        for (String string : jsonObject2.keySet()) {
            JsonObject jsonObject3 = jsonObject2.getAsJsonObject(string);
            for (String string2 : jsonObject3.keySet()) {
                String string3 = jsonObject3.get(string2).getAsString();
                int n = Integer.parseInt(string2);
                hashMap.put(string3, new MappingEntry(string, n));
            }
        }
        m = new RuntimeItemMapping(hashMap, 361);
        a = new RuntimeItemMapping(hashMap, 419);
        e = new RuntimeItemMapping(hashMap, 440);
        b = new RuntimeItemMapping(hashMap, 448);
        j = new RuntimeItemMapping(hashMap, 475);
        d = new RuntimeItemMapping(hashMap, 486);
        k = new RuntimeItemMapping(hashMap, 503);
        l = new RuntimeItemMapping(hashMap, 527);
        h = new RuntimeItemMapping(hashMap, 534);
        c = new RuntimeItemMapping(hashMap, 560);
    }

    public static RuntimeItemMapping getMapping(int n) {
        if (n >= 560) {
            return c;
        }
        if (n >= 534) {
            return h;
        }
        if (n >= 524) {
            return l;
        }
        if (n >= 503) {
            return k;
        }
        if (n >= 485) {
            return d;
        }
        if (n >= 475) {
            return j;
        }
        if (n >= 448) {
            return b;
        }
        if (n >= 440) {
            return e;
        }
        if (n >= 419) {
            return a;
        }
        return m;
    }

    public static int getLegacyIdFromLegacyString(String string) {
        return g.getOrDefault(string, -1);
    }

    public static int getId(int n) {
        return (short)(n >> 16);
    }

    public static int getData(int n) {
        return n >> 1 & Short.MAX_VALUE;
    }

    public static int getFullId(int n, int n2) {
        return (short)n << 16 | (n2 & Short.MAX_VALUE) << 1;
    }

    public static int getNetworkId(int n) {
        return n >> 1;
    }

    public static boolean hasData(int n) {
        return (n & 1) != 0;
    }

    private RuntimeItems() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }

    public static class MappingEntry {
        private final String b;
        private final int a;

        public MappingEntry(String string, int n) {
            this.b = string;
            this.a = n;
        }

        public String getLegacyName() {
            return this.b;
        }

        public int getDamage() {
            return this.a;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof MappingEntry)) {
                return false;
            }
            MappingEntry mappingEntry = (MappingEntry)object;
            if (!mappingEntry.canEqual(this)) {
                return false;
            }
            String string = this.getLegacyName();
            String string2 = mappingEntry.getLegacyName();
            if (string == null ? string2 != null : !string.equals(string2)) {
                return false;
            }
            return this.getDamage() == mappingEntry.getDamage();
        }

        protected boolean canEqual(Object object) {
            return object instanceof MappingEntry;
        }

        public int hashCode() {
            int n = 59;
            int n2 = 1;
            String string = this.getLegacyName();
            n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
            n2 = n2 * 59 + this.getDamage();
            return n2;
        }

        public String toString() {
            return "RuntimeItems.MappingEntry(legacyName=" + this.getLegacyName() + ", damage=" + this.getDamage() + ")";
        }
    }
}

