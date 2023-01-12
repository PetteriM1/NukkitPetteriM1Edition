/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.permission;

import cn.nukkit.NOBF;
import cn.nukkit.Server;
import cn.nukkit.permission.Permissible;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Permission {
    public static final String DEFAULT_OP;
    public static final String DEFAULT_NOT_OP;
    public static final String DEFAULT_TRUE;
    public static final String DEFAULT_FALSE;
    public static final String DEFAULT_PERMISSION;
    @NOBF
    private final String name;
    @NOBF
    private String description;
    @NOBF
    private final Map<String, Boolean> children;
    @NOBF
    private String defaultValue;

    public static String getByName(String string) {
        switch (string.toLowerCase()) {
            case "op": 
            case "isop": 
            case "operator": 
            case "isoperator": 
            case "admin": 
            case "isadmin": {
                return "op";
            }
            case "!op": 
            case "notop": 
            case "!operator": 
            case "notoperator": 
            case "!admin": 
            case "notadmin": {
                return "notop";
            }
            case "true": {
                return "true";
            }
        }
        return "false";
    }

    public Permission(String string) {
        this(string, null, null, new HashMap<String, Boolean>());
    }

    public Permission(String string, String string2) {
        this(string, string2, null, new HashMap<String, Boolean>());
    }

    public Permission(String string, String string2, String string3) {
        this(string, string2, string3, new HashMap<String, Boolean>());
    }

    public Permission(String string, String string2, String string3, Map<String, Boolean> map) {
        this.name = string;
        this.description = string2 != null ? string2 : "";
        this.defaultValue = string3 != null ? string3 : "op";
        this.children = map;
        this.recalculatePermissibles();
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Boolean> getChildren() {
        return this.children;
    }

    public String getDefault() {
        return this.defaultValue;
    }

    public void setDefault(String string) {
        block0: {
            if (string.equals(this.defaultValue)) break block0;
            this.defaultValue = string;
            this.recalculatePermissibles();
        }
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public Set<Permissible> getPermissibles() {
        return Server.getInstance().getPluginManager().getPermissionSubscriptions(this.name);
    }

    public void recalculatePermissibles() {
        Set<Permissible> set = this.getPermissibles();
        Server.getInstance().getPluginManager().recalculatePermissionDefaults(this);
        for (Permissible permissible : set) {
            permissible.recalculatePermissions();
        }
    }

    public void addParent(Permission permission, boolean bl) {
        this.children.put(this.name, bl);
        permission.recalculatePermissibles();
    }

    public Permission addParent(String string, boolean bl) {
        Permission permission = Server.getInstance().getPluginManager().getPermission(string);
        if (permission == null) {
            permission = new Permission(string);
            Server.getInstance().getPluginManager().addPermission(permission);
        }
        this.addParent(permission, bl);
        return permission;
    }

    public static List<Permission> loadPermissions(Map<String, Object> map) {
        return Permission.loadPermissions(map, "op");
    }

    public static List<Permission> loadPermissions(Map<String, Object> map, String string) {
        ArrayList<Permission> arrayList = new ArrayList<Permission>();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String string2 = entry.getKey();
                Map map2 = (Map)entry.getValue();
                arrayList.add(Permission.loadPermission(string2, map2, string, arrayList));
            }
        }
        return arrayList;
    }

    public static Permission loadPermission(String string, Map<String, Object> map) {
        return Permission.loadPermission(string, map, "op", new ArrayList<Permission>());
    }

    public static Permission loadPermission(String string, Map<String, Object> map, String string2) {
        return Permission.loadPermission(string, map, string2, new ArrayList<Permission>());
    }

    public static Permission loadPermission(String string, Map<String, Object> map, String string2, List<Permission> list) {
        String string3 = null;
        HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
        if (map.containsKey("default")) {
            String string4 = Permission.getByName(((StringBuilder)map.get("default")).toString());
            if (string4 != null) {
                string2 = string4;
            } else {
                throw new IllegalStateException("'default' key contained unknown value");
            }
        }
        if (map.containsKey("children")) {
            if (map.get("children") instanceof Map) {
                for (Map.Entry entry : ((Map)map.get("children")).entrySet()) {
                    String string5 = (String)entry.getKey();
                    Object v = entry.getValue();
                    if (v instanceof Map) {
                        Permission permission = Permission.loadPermission(string5, (Map)v, string2, list);
                        list.add(permission);
                    }
                    hashMap.put(string5, true);
                }
            } else {
                throw new IllegalStateException("'children' key is of wrong type");
            }
        }
        if (map.containsKey("description")) {
            string3 = (String)map.get("description");
        }
        return new Permission(string, string3, string2, hashMap);
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }

    static {
        DEFAULT_NOT_OP = "notop";
        DEFAULT_TRUE = "true";
        DEFAULT_PERMISSION = "op";
        DEFAULT_OP = "op";
        DEFAULT_FALSE = "false";
    }
}

