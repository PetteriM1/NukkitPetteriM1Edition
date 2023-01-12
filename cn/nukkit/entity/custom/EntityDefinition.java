/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.custom;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityDefinition {
    public static final AtomicInteger ID_ALLOCATOR = new AtomicInteger(10000);
    private final String f;
    private final String a;
    private final boolean b;
    private final String c;
    private final Class<? extends Entity> d;
    private final int h;
    private CompoundTag e;
    private CompoundTag g;

    public EntityDefinition(String string, String string2, boolean bl, String string3, Class<? extends Entity> clazz) {
        if (!CustomEntity.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Implementation class must implement CustomEntity interface");
        }
        this.f = string;
        this.a = string2;
        this.b = bl;
        this.c = string3;
        this.d = clazz;
        this.h = ID_ALLOCATOR.getAndIncrement();
    }

    private CompoundTag a() {
        CompoundTag compoundTag = new CompoundTag("");
        compoundTag.putBoolean("hasspawnegg", this.b);
        compoundTag.putBoolean("summonable", true);
        compoundTag.putString("id", this.f);
        compoundTag.putString("bid", this.a == null ? "" : this.a);
        compoundTag.putInt("rid", this.h);
        return compoundTag;
    }

    public CompoundTag getNetworkTag() {
        block0: {
            if (this.e != null) break block0;
            this.e = this.a();
        }
        return this.e;
    }

    public CompoundTag getNetworkTagOld() {
        block0: {
            if (this.g != null) break block0;
            this.g = this.a();
            this.g.putBoolean("experimental", false);
        }
        return this.g;
    }

    public static EntityDefinitionBuilder builder() {
        return new EntityDefinitionBuilder();
    }

    public String getIdentifier() {
        return this.f;
    }

    public String getParentEntity() {
        return this.a;
    }

    public boolean isSpawnEgg() {
        return this.b;
    }

    public String getAlternateName() {
        return this.c;
    }

    public Class<? extends Entity> getImplementation() {
        return this.d;
    }

    public int getRuntimeId() {
        return this.h;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }

    public static class EntityDefinitionBuilder {
        private String e;
        private String a;
        private boolean c;
        private String b;
        private Class<? extends Entity> d;

        EntityDefinitionBuilder() {
        }

        public EntityDefinitionBuilder identifier(String string) {
            this.e = string;
            return this;
        }

        public EntityDefinitionBuilder parentEntity(String string) {
            this.a = string;
            return this;
        }

        public EntityDefinitionBuilder spawnEgg(boolean bl) {
            this.c = bl;
            return this;
        }

        public EntityDefinitionBuilder alternateName(String string) {
            this.b = string;
            return this;
        }

        public EntityDefinitionBuilder implementation(Class<? extends Entity> clazz) {
            this.d = clazz;
            return this;
        }

        public EntityDefinition build() {
            return new EntityDefinition(this.e, this.a, this.c, this.b, this.d);
        }

        public String toString() {
            return "EntityDefinition.EntityDefinitionBuilder(identifier=" + this.e + ", parentEntity=" + this.a + ", spawnEgg=" + this.c + ", alternateName=" + this.b + ", implementation=" + this.d + ")";
        }
    }
}

