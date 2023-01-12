/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginLoadOrder;
import cn.nukkit.utils.PluginException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class PluginDescription {
    private String i;
    private String g;
    private List<String> e;
    private List<String> d = new ArrayList<String>();
    private List<String> h = new ArrayList<String>();
    private List<String> j = new ArrayList<String>();
    private String c;
    private Map<String, Object> l = new HashMap<String, Object>();
    private String k;
    private final List<String> m = new ArrayList<String>();
    private String b;
    private String n;
    private PluginLoadOrder f = PluginLoadOrder.POSTWORLD;
    private List<Permission> a = new ArrayList<Permission>();

    public PluginDescription(Map<String, Object> map) {
        this.a(map);
    }

    public PluginDescription(String string) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        this.a(yaml.loadAs(string, LinkedHashMap.class));
    }

    private void a(Map<String, Object> map) throws PluginException {
        block16: {
            Object object;
            this.i = ((String)map.get("name")).replaceAll("[^A-Za-z0-9 _.-]", "");
            if (this.i.isEmpty()) {
                throw new PluginException("Invalid PluginDescription name");
            }
            this.i = this.i.replace(" ", "_");
            this.c = ((StringBuilder)map.get("version")).toString();
            this.g = (String)map.get("main");
            Object object2 = map.get("api");
            if (object2 instanceof List) {
                this.e = (List)object2;
            } else {
                object = new ArrayList<String>();
                object.add((String)object2);
                this.e = object;
            }
            if (this.g.startsWith("cn.nukkit.")) {
                throw new PluginException("Invalid PluginDescription main, cannot start within the cn.nukkit. package");
            }
            if (map.containsKey("commands") && map.get("commands") instanceof Map) {
                this.l = (Map)map.get("commands");
            }
            if (map.containsKey("depend")) {
                this.d = (List)map.get("depend");
            }
            if (map.containsKey("softdepend")) {
                this.h = (List)map.get("softdepend");
            }
            if (map.containsKey("loadbefore")) {
                this.j = (List)map.get("loadbefore");
            }
            if (map.containsKey("website")) {
                this.b = (String)map.get("website");
            }
            if (map.containsKey("description")) {
                this.k = (String)map.get("description");
            }
            if (map.containsKey("prefix")) {
                this.n = (String)map.get("prefix");
            }
            if (map.containsKey("load")) {
                object = (String)map.get("load");
                try {
                    this.f = PluginLoadOrder.valueOf((String)object);
                }
                catch (Exception exception) {
                    throw new PluginException("Invalid PluginDescription load");
                }
            }
            if (map.containsKey("author")) {
                this.m.add((String)map.get("author"));
            }
            if (map.containsKey("authors")) {
                this.m.addAll((Collection)map.get("authors"));
            }
            if (!map.containsKey("permissions")) break block16;
            this.a = Permission.loadPermissions((Map)map.get("permissions"));
        }
    }

    public String getFullName() {
        return this.i + " v" + this.c;
    }

    public List<String> getCompatibleAPIs() {
        return this.e;
    }

    public List<String> getAuthors() {
        return this.m;
    }

    public String getPrefix() {
        return this.n;
    }

    public Map<String, Object> getCommands() {
        return this.l;
    }

    public List<String> getDepend() {
        return this.d;
    }

    public String getDescription() {
        return this.k;
    }

    public List<String> getLoadBefore() {
        return this.j;
    }

    public String getMain() {
        return this.g;
    }

    public String getName() {
        return this.i;
    }

    public PluginLoadOrder getOrder() {
        return this.f;
    }

    public List<Permission> getPermissions() {
        return this.a;
    }

    public List<String> getSoftDepend() {
        return this.h;
    }

    public String getVersion() {
        return this.c;
    }

    public String getWebsite() {
        return this.b;
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

