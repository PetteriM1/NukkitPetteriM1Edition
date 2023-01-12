/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigSection
extends LinkedHashMap<String, Object> {
    public ConfigSection() {
    }

    public ConfigSection(String string, Object object) {
        this();
        this.set(string, object);
    }

    public ConfigSection(LinkedHashMap<String, Object> linkedHashMap) {
        this();
        if (linkedHashMap == null || linkedHashMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {
            if (entry.getValue() instanceof LinkedHashMap) {
                super.put(entry.getKey(), new ConfigSection((LinkedHashMap)entry.getValue()));
                continue;
            }
            if (entry.getValue() instanceof List) {
                super.put(entry.getKey(), this.a((List)entry.getValue()));
                continue;
            }
            super.put(entry.getKey(), entry.getValue());
        }
    }

    private List a(List list) {
        ArrayList<ConfigSection> arrayList = new ArrayList<ConfigSection>();
        for (Object e2 : list) {
            if (e2 instanceof LinkedHashMap) {
                arrayList.add(new ConfigSection((LinkedHashMap)e2));
                continue;
            }
            arrayList.add((ConfigSection)e2);
        }
        return arrayList;
    }

    public Map<String, Object> getAllMap() {
        return new LinkedHashMap<String, Object>(this);
    }

    public ConfigSection getAll() {
        return new ConfigSection(this);
    }

    public Object get(String string) {
        return this.get(string, null);
    }

    public <T> T get(String string, T t) {
        if (string == null || string.isEmpty()) {
            return t;
        }
        if (super.containsKey(string)) {
            return (T)super.get(string);
        }
        String[] stringArray = string.split("\\.", 2);
        if (!super.containsKey(stringArray[0])) {
            return t;
        }
        Object v = super.get(stringArray[0]);
        if (v instanceof ConfigSection) {
            ConfigSection configSection = (ConfigSection)v;
            return configSection.get(stringArray[1], t);
        }
        return t;
    }

    public void set(String string, Object object) {
        String[] stringArray = string.split("\\.", 2);
        if (stringArray.length > 1) {
            ConfigSection configSection = new ConfigSection();
            if (this.containsKey(stringArray[0]) && super.get(stringArray[0]) instanceof ConfigSection) {
                configSection = (ConfigSection)super.get(stringArray[0]);
            }
            configSection.set(stringArray[1], object);
            super.put(stringArray[0], configSection);
        } else {
            super.put(stringArray[0], object);
        }
    }

    public boolean isSection(String string) {
        return this.get(string) instanceof ConfigSection;
    }

    public ConfigSection getSection(String string) {
        return this.get(string, new ConfigSection());
    }

    public ConfigSection getSections() {
        return this.getSections(null);
    }

    public ConfigSection getSections(String string2) {
        ConfigSection configSection;
        ConfigSection configSection2 = new ConfigSection();
        ConfigSection configSection3 = configSection = string2 == null || string2.isEmpty() ? this.getAll() : this.getSection(string2);
        if (configSection == null) {
            return configSection2;
        }
        configSection.forEach((string, object) -> {
            if (object instanceof ConfigSection) {
                configSection2.put(string, object);
            }
        });
        return configSection2;
    }

    public int getInt(String string) {
        return this.getInt(string, 0);
    }

    public int getInt(String string, int n) {
        return ((Number)this.get(string, n)).intValue();
    }

    public boolean isInt(String string) {
        return this.get(string) instanceof Integer;
    }

    public long getLong(String string) {
        return this.getLong(string, 0L);
    }

    public long getLong(String string, long l) {
        return ((Number)this.get(string, l)).longValue();
    }

    public boolean isLong(String string) {
        return this.get(string) instanceof Long;
    }

    public double getDouble(String string) {
        return this.getDouble(string, 0.0);
    }

    public double getDouble(String string, double d2) {
        return ((Number)this.get(string, d2)).doubleValue();
    }

    public boolean isDouble(String string) {
        return this.get(string) instanceof Double;
    }

    public String getString(String string) {
        return this.getString(string, "");
    }

    public String getString(String string, String string2) {
        return ((StringBuilder)((Object)this.get(string, string2))).toString();
    }

    public boolean isString(String string) {
        return this.get(string) instanceof String;
    }

    public boolean getBoolean(String string) {
        return this.getBoolean(string, false);
    }

    public boolean getBoolean(String string, boolean bl) {
        return this.get(string, bl);
    }

    public boolean isBoolean(String string) {
        return this.get(string) instanceof Boolean;
    }

    public List getList(String string) {
        return this.getList(string, null);
    }

    public List getList(String string, List list) {
        return this.get(string, list);
    }

    public boolean isList(String string) {
        return this.get(string) instanceof List;
    }

    public List<String> getStringList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<String>(0);
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (Object e2 : list) {
            if (!(e2 instanceof String) && !(e2 instanceof Number) && !(e2 instanceof Boolean) && !(e2 instanceof Character)) continue;
            arrayList.add(((StringBuilder)e2).toString());
        }
        return arrayList;
    }

    public List<Integer> getIntegerList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<Integer>(0);
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (Object e2 : list) {
            if (e2 instanceof Integer) {
                arrayList.add((Integer)e2);
                continue;
            }
            if (e2 instanceof String) {
                try {
                    arrayList.add(Integer.valueOf((String)e2));
                }
                catch (Exception exception) {}
                continue;
            }
            if (e2 instanceof Character) {
                arrayList.add(Integer.valueOf(((Character)e2).charValue()));
                continue;
            }
            if (!(e2 instanceof Number)) continue;
            arrayList.add(((Number)e2).intValue());
        }
        return arrayList;
    }

    public List<Boolean> getBooleanList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<Boolean>(0);
        }
        ArrayList<Boolean> arrayList = new ArrayList<Boolean>();
        for (Object e2 : list) {
            if (e2 instanceof Boolean) {
                arrayList.add((Boolean)e2);
                continue;
            }
            if (!(e2 instanceof String)) continue;
            if (Boolean.TRUE.toString().equals(e2)) {
                arrayList.add(true);
                continue;
            }
            if (!Boolean.FALSE.toString().equals(e2)) continue;
            arrayList.add(false);
        }
        return arrayList;
    }

    public List<Double> getDoubleList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<Double>(0);
        }
        ArrayList<Double> arrayList = new ArrayList<Double>();
        for (Object e2 : list) {
            if (e2 instanceof Double) {
                arrayList.add((Double)e2);
                continue;
            }
            if (e2 instanceof String) {
                try {
                    arrayList.add(Double.valueOf((String)e2));
                }
                catch (Exception exception) {}
                continue;
            }
            if (e2 instanceof Character) {
                arrayList.add(Double.valueOf(((Character)e2).charValue()));
                continue;
            }
            if (!(e2 instanceof Number)) continue;
            arrayList.add(((Number)e2).doubleValue());
        }
        return arrayList;
    }

    public List<Float> getFloatList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<Float>(0);
        }
        ArrayList<Float> arrayList = new ArrayList<Float>();
        for (Object e2 : list) {
            if (e2 instanceof Float) {
                arrayList.add((Float)e2);
                continue;
            }
            if (e2 instanceof String) {
                try {
                    arrayList.add(Float.valueOf((String)e2));
                }
                catch (Exception exception) {}
                continue;
            }
            if (e2 instanceof Character) {
                arrayList.add(Float.valueOf(((Character)e2).charValue()));
                continue;
            }
            if (!(e2 instanceof Number)) continue;
            arrayList.add(Float.valueOf(((Number)e2).floatValue()));
        }
        return arrayList;
    }

    public List<Long> getLongList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<Long>(0);
        }
        ArrayList<Long> arrayList = new ArrayList<Long>();
        for (Object e2 : list) {
            if (e2 instanceof Long) {
                arrayList.add((Long)e2);
                continue;
            }
            if (e2 instanceof String) {
                try {
                    arrayList.add(Long.valueOf((String)e2));
                }
                catch (Exception exception) {}
                continue;
            }
            if (e2 instanceof Character) {
                arrayList.add(Long.valueOf(((Character)e2).charValue()));
                continue;
            }
            if (!(e2 instanceof Number)) continue;
            arrayList.add(((Number)e2).longValue());
        }
        return arrayList;
    }

    public List<Byte> getByteList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<Byte>(0);
        }
        ArrayList<Byte> arrayList = new ArrayList<Byte>();
        for (Object e2 : list) {
            if (e2 instanceof Byte) {
                arrayList.add((Byte)e2);
                continue;
            }
            if (e2 instanceof String) {
                try {
                    arrayList.add(Byte.valueOf((String)e2));
                }
                catch (Exception exception) {}
                continue;
            }
            if (e2 instanceof Character) {
                arrayList.add((byte)((Character)e2).charValue());
                continue;
            }
            if (!(e2 instanceof Number)) continue;
            arrayList.add(((Number)e2).byteValue());
        }
        return arrayList;
    }

    public List<Character> getCharacterList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<Character>(0);
        }
        ArrayList<Character> arrayList = new ArrayList<Character>();
        for (Object e2 : list) {
            if (e2 instanceof Character) {
                arrayList.add((Character)e2);
                continue;
            }
            if (e2 instanceof String) {
                String string2 = (String)e2;
                if (string2.length() != 1) continue;
                arrayList.add(Character.valueOf(string2.charAt(0)));
                continue;
            }
            if (!(e2 instanceof Number)) continue;
            arrayList.add(Character.valueOf((char)((Number)e2).intValue()));
        }
        return arrayList;
    }

    public List<Short> getShortList(String string) {
        List list = this.getList(string);
        if (list == null) {
            return new ArrayList<Short>(0);
        }
        ArrayList<Short> arrayList = new ArrayList<Short>();
        for (Object e2 : list) {
            if (e2 instanceof Short) {
                arrayList.add((Short)e2);
                continue;
            }
            if (e2 instanceof String) {
                try {
                    arrayList.add(Short.valueOf((String)e2));
                }
                catch (Exception exception) {}
                continue;
            }
            if (e2 instanceof Character) {
                arrayList.add((short)((Character)e2).charValue());
                continue;
            }
            if (!(e2 instanceof Number)) continue;
            arrayList.add(((Number)e2).shortValue());
        }
        return arrayList;
    }

    public List<Map> getMapList(String string) {
        List list = this.getList(string);
        ArrayList<Map> arrayList = new ArrayList<Map>();
        if (list == null) {
            return arrayList;
        }
        for (Object e2 : list) {
            if (!(e2 instanceof Map)) continue;
            arrayList.add((Map)e2);
        }
        return arrayList;
    }

    public boolean exists(String string, boolean bl) {
        if (bl) {
            string = string.toLowerCase();
        }
        for (String string2 : this.getKeys(true)) {
            if (bl) {
                string2 = string2.toLowerCase();
            }
            if (!string2.equals(string)) continue;
            return true;
        }
        return false;
    }

    public boolean exists(String string) {
        return this.exists(string, false);
    }

    public void remove(String string) {
        String[] stringArray;
        if (string == null || string.isEmpty()) {
            return;
        }
        if (super.containsKey(string)) {
            super.remove(string);
        } else if (this.containsKey(".") && super.get((stringArray = string.split("\\.", 2))[0]) instanceof ConfigSection) {
            ConfigSection configSection = (ConfigSection)super.get(stringArray[0]);
            configSection.remove(stringArray[1]);
        }
    }

    public Set<String> getKeys(boolean bl) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        this.forEach((string, object) -> {
            linkedHashSet.add((String)string);
            if (object instanceof ConfigSection && bl) {
                ((ConfigSection)object).getKeys(true).forEach((? super T string2) -> linkedHashSet.add(string + '.' + string2));
            }
        });
        return linkedHashSet;
    }

    public Set<String> getKeys() {
        return this.getKeys(true);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

