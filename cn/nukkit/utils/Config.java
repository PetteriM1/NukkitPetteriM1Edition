/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.NOBF;
import cn.nukkit.Server;
import cn.nukkit.scheduler.FileWriteTask;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.b;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Config {
    public static final int DETECT = -1;
    public static final int PROPERTIES = 0;
    public static final int CNF = 0;
    public static final int JSON = 1;
    public static final int YAML = 2;
    public static final int EXPORT = 3;
    public static final int SERIALIZED = 4;
    public static final int ENUM = 5;
    public static final int ENUMERATION = 5;
    private ConfigSection b = new ConfigSection();
    @NOBF
    private File file;
    private boolean c = false;
    private int a = -1;
    public static final Map<String, Integer> format = new TreeMap<String, Integer>();

    public Config(int n) {
        this.a = n;
        this.c = true;
        this.b = new ConfigSection();
    }

    public Config() {
        this(2);
    }

    public Config(String string) {
        this(string, -1);
    }

    public Config(File file) {
        this(file.toString(), -1);
    }

    public Config(String string, int n) {
        this(string, n, new ConfigSection());
    }

    public Config(File file, int n) {
        this(file.toString(), n, new ConfigSection());
    }

    public Config(String string, int n, LinkedHashMap<String, Object> linkedHashMap) {
        this.load(string, n, new ConfigSection(linkedHashMap));
    }

    public Config(String string, int n, ConfigSection configSection) {
        this.load(string, n, configSection);
    }

    public Config(File file, int n, ConfigSection configSection) {
        this.load(file.toString(), n, configSection);
    }

    public Config(File file, int n, LinkedHashMap<String, Object> linkedHashMap) {
        this(file.toString(), n, new ConfigSection(linkedHashMap));
    }

    public void reload() {
        this.b.clear();
        this.c = false;
        if (this.file == null) {
            throw new IllegalStateException("Failed to reload Config. File object is undefined.");
        }
        this.load(this.file.toString(), this.a);
    }

    public boolean load(String string) {
        return this.load(string, -1);
    }

    public boolean load(String string, int n) {
        return this.load(string, n, new ConfigSection());
    }

    public boolean load(String string, int n, ConfigSection configSection) {
        this.c = true;
        this.a = n;
        this.file = new File(string);
        if (!this.file.exists()) {
            try {
                this.file.getParentFile().mkdirs();
                this.file.createNewFile();
            }
            catch (IOException iOException) {
                MainLogger.getLogger().error("Could not create Config " + this.file.toString(), iOException);
            }
            this.b = configSection;
            this.save();
        } else {
            String string2;
            if (this.a == -1) {
                string2 = "";
                if (this.file.getName().lastIndexOf(46) != -1 && this.file.getName().lastIndexOf(46) != 0) {
                    string2 = this.file.getName().substring(this.file.getName().lastIndexOf(46) + 1);
                }
                if (format.containsKey(string2)) {
                    this.a = format.get(string2);
                } else {
                    this.c = false;
                }
            }
            if (this.c) {
                string2 = "";
                try {
                    string2 = Utils.readFile(this.file);
                }
                catch (IOException iOException) {
                    Server.getInstance().getLogger().logException(iOException);
                }
                this.a(string2);
                if (!this.c) {
                    return false;
                }
                if (this.setDefault(configSection) > 0) {
                    this.save();
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean load(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        if (this.c) {
            String string;
            try {
                string = Utils.readFile(inputStream);
            }
            catch (IOException iOException) {
                Server.getInstance().getLogger().logException(iOException);
                return false;
            }
            this.a(string);
        }
        return this.c;
    }

    public Config loadFromStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        if (this.c) {
            String string;
            try {
                string = Utils.readFile(inputStream);
            }
            catch (IOException iOException) {
                Server.getInstance().getLogger().logException(iOException);
                return null;
            }
            this.a(string);
        }
        return this;
    }

    public boolean check() {
        return this.c;
    }

    public boolean isCorrect() {
        return this.c;
    }

    public boolean save(File file, boolean bl) {
        this.file = file;
        return this.save(bl);
    }

    public boolean save(File file) {
        this.file = file;
        return this.save();
    }

    public boolean save() {
        return this.save(false);
    }

    public boolean save(Boolean bl) {
        if (this.file == null) {
            throw new IllegalStateException("Failed to save Config. File object is undefined.");
        }
        if (this.c) {
            StringBuilder stringBuilder = new StringBuilder();
            switch (this.a) {
                case 0: {
                    stringBuilder = new StringBuilder(this.a());
                    break;
                }
                case 1: {
                    stringBuilder = new StringBuilder(new GsonBuilder().setPrettyPrinting().create().toJson(this.b));
                    break;
                }
                case 2: {
                    DumperOptions dumperOptions = new DumperOptions();
                    dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                    Yaml yaml = new Yaml(dumperOptions);
                    stringBuilder = new StringBuilder(yaml.dump(this.b));
                    break;
                }
                case 5: {
                    Iterator iterator = this.b.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry;
                        Map.Entry entry2 = entry = iterator.next();
                        stringBuilder.append(entry2.getKey()).append("\r\n");
                    }
                    break;
                }
            }
            if (bl.booleanValue()) {
                Server.getInstance().getScheduler().scheduleAsyncTask(new FileWriteTask(this.file, stringBuilder.toString()));
            } else {
                try {
                    Utils.writeFile(this.file, stringBuilder.toString());
                }
                catch (IOException iOException) {
                    Server.getInstance().getLogger().logException(iOException);
                }
            }
            return true;
        }
        return false;
    }

    public void set(String string, Object object) {
        this.b.set(string, object);
    }

    public Object get(String string) {
        return this.get(string, null);
    }

    public <T> T get(String string, T t) {
        return this.c ? this.b.get(string, t) : t;
    }

    public ConfigSection getSection(String string) {
        return this.c ? this.b.getSection(string) : new ConfigSection();
    }

    public boolean isSection(String string) {
        return this.b.isSection(string);
    }

    public ConfigSection getSections(String string) {
        return this.c ? this.b.getSections(string) : new ConfigSection();
    }

    public ConfigSection getSections() {
        return this.c ? this.b.getSections() : new ConfigSection();
    }

    public int getInt(String string) {
        return this.getInt(string, 0);
    }

    public int getInt(String string, int n) {
        return this.c ? this.b.getInt(string, n) : n;
    }

    public boolean isInt(String string) {
        return this.b.isInt(string);
    }

    public long getLong(String string) {
        return this.getLong(string, 0L);
    }

    public long getLong(String string, long l) {
        return this.c ? this.b.getLong(string, l) : l;
    }

    public boolean isLong(String string) {
        return this.b.isLong(string);
    }

    public double getDouble(String string) {
        return this.getDouble(string, 0.0);
    }

    public double getDouble(String string, double d2) {
        return this.c ? this.b.getDouble(string, d2) : d2;
    }

    public boolean isDouble(String string) {
        return this.b.isDouble(string);
    }

    public String getString(String string) {
        return this.getString(string, "");
    }

    public String getString(String string, String string2) {
        return this.c ? this.b.getString(string, string2) : string2;
    }

    public boolean isString(String string) {
        return this.b.isString(string);
    }

    public boolean getBoolean(String string) {
        return this.getBoolean(string, false);
    }

    public boolean getBoolean(String string, boolean bl) {
        return this.c ? this.b.getBoolean(string, bl) : bl;
    }

    public boolean isBoolean(String string) {
        return this.b.isBoolean(string);
    }

    public List getList(String string) {
        return this.getList(string, null);
    }

    public List getList(String string, List list) {
        return this.c ? this.b.getList(string, list) : list;
    }

    public boolean isList(String string) {
        return this.b.isList(string);
    }

    public List<String> getStringList(String string) {
        return this.b.getStringList(string);
    }

    public List<Integer> getIntegerList(String string) {
        return this.b.getIntegerList(string);
    }

    public List<Boolean> getBooleanList(String string) {
        return this.b.getBooleanList(string);
    }

    public List<Double> getDoubleList(String string) {
        return this.b.getDoubleList(string);
    }

    public List<Float> getFloatList(String string) {
        return this.b.getFloatList(string);
    }

    public List<Long> getLongList(String string) {
        return this.b.getLongList(string);
    }

    public List<Byte> getByteList(String string) {
        return this.b.getByteList(string);
    }

    public List<Character> getCharacterList(String string) {
        return this.b.getCharacterList(string);
    }

    public List<Short> getShortList(String string) {
        return this.b.getShortList(string);
    }

    public List<Map> getMapList(String string) {
        return this.b.getMapList(string);
    }

    public void setAll(LinkedHashMap<String, Object> linkedHashMap) {
        this.b = new ConfigSection(linkedHashMap);
    }

    public void setAll(ConfigSection configSection) {
        this.b = configSection;
    }

    public boolean exists(String string) {
        return this.b.exists(string);
    }

    public boolean exists(String string, boolean bl) {
        return this.b.exists(string, bl);
    }

    public void remove(String string) {
        this.b.remove(string);
    }

    public Map<String, Object> getAll() {
        return this.b.getAllMap();
    }

    public ConfigSection getRootSection() {
        return this.b;
    }

    public int setDefault(LinkedHashMap<String, Object> linkedHashMap) {
        return this.setDefault(new ConfigSection(linkedHashMap));
    }

    public int setDefault(ConfigSection configSection) {
        int n = this.b.size();
        this.b = this.a(configSection, this.b);
        return this.b.size() - n;
    }

    private ConfigSection a(ConfigSection configSection, ConfigSection configSection2) {
        for (String string : configSection.keySet()) {
            if (configSection2.containsKey(string)) continue;
            configSection2.put(string, configSection.get(string));
        }
        return configSection2;
    }

    private void c(String string) {
        string = string.replace("\r\n", "\n");
        for (String string2 : string.split("\n")) {
            if (string2.trim().isEmpty()) continue;
            this.b.put(string2, true);
        }
    }

    private String a() {
        StringBuilder stringBuilder = new StringBuilder("#Properties Config File\r\n");
        Iterator iterator = this.b.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry;
            Map.Entry entry2 = entry = iterator.next();
            Object object = entry2.getValue();
            Object k = entry2.getKey();
            if (object instanceof Boolean) {
                object = (Boolean)object != false ? "on" : "off";
            }
            stringBuilder.append(k).append('=').append(object).append("\r\n");
        }
        return stringBuilder.toString();
    }

    private void b(String string) {
        block12: for (String string2 : string.split("\n")) {
            int n;
            if (!Pattern.compile("[a-zA-Z0-9\\-_.]*+=+[^\\r\\n]*").matcher(string2).matches() || (n = string2.indexOf(61)) == -1) continue;
            String string3 = string2.substring(0, n);
            String string4 = string2.substring(n + 1);
            if (this.b.containsKey(string3)) {
                MainLogger.getLogger().debug("[Config] Repeated property " + string3 + " in file " + this.file.toString());
            }
            switch (string4.toLowerCase()) {
                case "on": 
                case "true": 
                case "yes": {
                    this.b.put(string3, true);
                    continue block12;
                }
                case "off": 
                case "false": 
                case "no": {
                    this.b.put(string3, false);
                    continue block12;
                }
                default: {
                    this.b.put(string3, string4);
                }
            }
        }
    }

    public Object getNested(String string) {
        return this.get(string);
    }

    public <T> T getNested(String string, T t) {
        return this.get(string, t);
    }

    public <T> T getNestedAs(String string, Class<T> clazz) {
        return (T)this.get(string);
    }

    public void removeNested(String string) {
        this.remove(string);
    }

    private void a(String string) {
        switch (this.a) {
            case 0: {
                this.b(string);
                break;
            }
            case 1: {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                this.b = new ConfigSection((LinkedHashMap)gson.fromJson(string, new LinkedHashMapTypeToken(null).getType()));
                break;
            }
            case 2: {
                DumperOptions dumperOptions = new DumperOptions();
                dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                Yaml yaml = new Yaml(dumperOptions);
                this.b = new ConfigSection(yaml.loadAs(string, LinkedHashMap.class));
                break;
            }
            case 5: {
                this.c(string);
                break;
            }
            default: {
                this.c = false;
            }
        }
    }

    public Set<String> getKeys() {
        if (this.c) {
            return this.b.getKeys();
        }
        return new HashSet<String>();
    }

    public Set<String> getKeys(boolean bl) {
        if (this.c) {
            return this.b.getKeys(bl);
        }
        return new HashSet<String>();
    }

    static {
        format.put("properties", 0);
        format.put("con", 0);
        format.put("conf", 0);
        format.put("config", 0);
        format.put("js", 1);
        format.put("json", 1);
        format.put("yml", 2);
        format.put("yaml", 2);
        format.put("sl", 4);
        format.put("serialize", 4);
        format.put("txt", 5);
        format.put("list", 5);
        format.put("enum", 5);
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    private static class LinkedHashMapTypeToken
    extends TypeToken<LinkedHashMap<String, Object>> {
        private LinkedHashMapTypeToken() {
        }

        /* synthetic */ LinkedHashMapTypeToken(b b2) {
            this();
        }
    }
}

