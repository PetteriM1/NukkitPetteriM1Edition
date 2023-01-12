/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.PopChunkManager;
import cn.nukkit.math.NukkitRandom;
import java.util.Map;

class a
extends ThreadLocal<Generator> {
    final Level this$0;

    a(Level level) {
        this.this$0 = level;
    }

    @Override
    public Generator initialValue() {
        try {
            Generator generator = (Generator)Level.access$200(this.this$0).getConstructor(Map.class).newInstance(Level.access$100(this.this$0).getGeneratorOptions());
            NukkitRandom nukkitRandom = new NukkitRandom(this.this$0.getSeed());
            if (Server.getInstance().isPrimaryThread()) {
                generator.init(this.this$0, nukkitRandom);
            }
            generator.init(new PopChunkManager(this.this$0.getSeed()), nukkitRandom);
            return generator;
        }
        catch (Throwable throwable) {
            Server.getInstance().getLogger().logException(throwable);
            return null;
        }
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

