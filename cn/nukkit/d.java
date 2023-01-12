/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import java.util.HashMap;

class d
extends HashMap<Integer, Level> {
    final Server this$0;

    d(Server server) {
        this.this$0 = server;
    }

    @Override
    public Level put(Integer n, Level level) {
        Level level2 = super.put(n, level);
        Server.access$002(this.this$0, Server.access$100(this.this$0).values().toArray(new Level[0]));
        return level2;
    }

    @Override
    public boolean remove(Object object, Object object2) {
        boolean bl = super.remove(object, object2);
        Server.access$002(this.this$0, Server.access$100(this.this$0).values().toArray(new Level[0]));
        return bl;
    }

    @Override
    public Level remove(Object object) {
        Level level = (Level)super.remove(object);
        Server.access$002(this.this$0, Server.access$100(this.this$0).values().toArray(new Level[0]));
        return level;
    }
}

