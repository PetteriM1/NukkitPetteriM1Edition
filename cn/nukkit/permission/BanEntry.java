/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.permission;

import cn.nukkit.Server;
import cn.nukkit.permission.a;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class BanEntry {
    public static final String format = "yyyy-MM-dd HH:mm:ss Z";
    private final String b;
    private Date d;
    private String c = "(Unknown)";
    private Date e = null;
    private String a = "Banned";

    public BanEntry(String string) {
        this.b = string.toLowerCase();
        this.d = new Date();
    }

    public String getName() {
        return this.b;
    }

    public Date getCreationDate() {
        return this.d;
    }

    public void setCreationDate(Date date) {
        this.d = date;
    }

    public String getSource() {
        return this.c;
    }

    public void setSource(String string) {
        this.c = string;
    }

    public Date getExpirationDate() {
        return this.e;
    }

    public void setExpirationDate(Date date) {
        this.e = date;
    }

    public boolean hasExpired() {
        Date date = new Date();
        return this.e != null && this.e.before(date);
    }

    public String getReason() {
        return this.a;
    }

    public void setReason(String string) {
        this.a = string;
    }

    public LinkedHashMap<String, String> getMap() {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("name", this.b);
        linkedHashMap.put("creationDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(this.d));
        linkedHashMap.put("source", this.c);
        linkedHashMap.put("expireDate", this.e != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(this.e) : "Forever");
        linkedHashMap.put("reason", this.a);
        return linkedHashMap;
    }

    public static BanEntry fromMap(Map<String, String> map) {
        BanEntry banEntry = new BanEntry(map.get("name"));
        try {
            banEntry.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(map.get("creationDate")));
            banEntry.setExpirationDate(!map.get("expireDate").equals("Forever") ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(map.get("expireDate")) : null);
        }
        catch (ParseException parseException) {
            Server.getInstance().getLogger().logException(parseException);
        }
        banEntry.setSource(map.get("source"));
        banEntry.setReason(map.get("reason"));
        return banEntry;
    }

    public String getString() {
        return new Gson().toJson(this.getMap());
    }

    public static BanEntry fromString(String string) {
        Map map = (Map)new Gson().fromJson(string, new TreeMapTypeToken(null).getType());
        BanEntry banEntry = new BanEntry((String)map.get("name"));
        try {
            banEntry.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse((String)map.get("creationDate")));
            banEntry.setExpirationDate(!((String)map.get("expireDate")).equals("Forever") ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse((String)map.get("expireDate")) : null);
        }
        catch (ParseException parseException) {
            Server.getInstance().getLogger().logException(parseException);
        }
        banEntry.setSource((String)map.get("source"));
        banEntry.setReason((String)map.get("reason"));
        return banEntry;
    }

    private static ParseException a(ParseException parseException) {
        return parseException;
    }

    private static class TreeMapTypeToken
    extends TypeToken<TreeMap<String, String>> {
        private TreeMapTypeToken() {
        }

        /* synthetic */ TreeMapTypeToken(a a2) {
            this();
        }
    }
}

