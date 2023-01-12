/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.permission;

import cn.nukkit.permission.BanEntry;
import cn.nukkit.permission.b;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class BanList {
    private LinkedHashMap<String, BanEntry> a = new LinkedHashMap();
    private final String b;
    private boolean c = true;

    public BanList(String string) {
        this.b = string;
    }

    public boolean isEnable() {
        return this.c;
    }

    public void setEnable(boolean bl) {
        this.c = bl;
    }

    public LinkedHashMap<String, BanEntry> getEntires() {
        this.removeExpired();
        return this.a;
    }

    public boolean isBanned(String string) {
        if (!this.c || string == null) {
            return false;
        }
        this.removeExpired();
        return this.a.containsKey(string.toLowerCase());
    }

    public void add(BanEntry banEntry) {
        this.a.put(banEntry.getName(), banEntry);
        this.save();
    }

    public BanEntry addBan(String string) {
        return this.addBan(string, null);
    }

    public BanEntry addBan(String string, String string2) {
        return this.addBan(string, string2, null);
    }

    public BanEntry addBan(String string, String string2, Date date) {
        return this.addBan(string, string2, date, null);
    }

    public BanEntry addBan(String string, String string2, Date date, String string3) {
        BanEntry banEntry = new BanEntry(string);
        banEntry.setSource(string3 != null ? string3 : banEntry.getSource());
        banEntry.setExpirationDate(date);
        banEntry.setReason(string2 != null ? string2 : banEntry.getReason());
        this.add(banEntry);
        return banEntry;
    }

    public void remove(String string) {
        if (this.a.containsKey(string = string.toLowerCase())) {
            this.a.remove(string);
            this.save();
        }
    }

    public void removeExpired() {
        for (String string : new ArrayList<String>(this.a.keySet())) {
            BanEntry banEntry = this.a.get(string);
            if (!banEntry.hasExpired()) continue;
            this.a.remove(string);
        }
    }

    public void load() {
        this.a = new LinkedHashMap();
        File file = new File(this.b);
        try {
            if (!file.exists()) {
                file.createNewFile();
                this.save();
            } else {
                LinkedList linkedList = (LinkedList)new Gson().fromJson(Utils.readFile(this.b), new LinkedListTypeToken(null).getType());
                for (TreeMap treeMap : linkedList) {
                    BanEntry banEntry = BanEntry.fromMap(treeMap);
                    this.a.put(banEntry.getName(), banEntry);
                }
            }
        }
        catch (IOException iOException) {
            MainLogger.getLogger().error("Could not load ban list: ", iOException);
        }
    }

    public void save() {
        this.removeExpired();
        try {
            File file = new File(this.b);
            if (!file.exists()) {
                file.createNewFile();
            }
            LinkedList<LinkedHashMap<String, String>> linkedList = new LinkedList<LinkedHashMap<String, String>>();
            for (BanEntry banEntry : this.a.values()) {
                linkedList.add(banEntry.getMap());
            }
            Utils.writeFile(this.b, (InputStream)new ByteArrayInputStream(new GsonBuilder().setPrettyPrinting().create().toJson(linkedList).getBytes(StandardCharsets.UTF_8)));
        }
        catch (IOException iOException) {
            MainLogger.getLogger().error("Could not save ban list ", iOException);
        }
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }

    private static class LinkedListTypeToken
    extends TypeToken<LinkedList<TreeMap<String, String>>> {
        private LinkedListTypeToken() {
        }

        /* synthetic */ LinkedListTypeToken(b b2) {
            this();
        }
    }
}

