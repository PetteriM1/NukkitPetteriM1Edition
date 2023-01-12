/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.custom;

import cn.nukkit.entity.custom.EntityDefinition;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AvailableEntityIdentifiersPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EntityManager {
    private static final EntityManager a = new EntityManager();
    private final Map<String, EntityDefinition> b = new HashMap<String, EntityDefinition>();
    private final Map<String, EntityDefinition> g = new HashMap<String, EntityDefinition>();
    private final Int2ObjectMap<EntityDefinition> f = new Int2ObjectOpenHashMap<EntityDefinition>();
    private final Map<String, Integer> c = new HashMap<String, Integer>();
    private byte[] e;
    private byte[] d;

    public static EntityManager get() {
        return a;
    }

    public EntityManager() {
        AddEntityPacket.LEGACY_IDS.forEach((n, string) -> this.c.put((String)string, (Integer)n));
    }

    public void registerDefinition(EntityDefinition entityDefinition) {
        block1: {
            if (this.b.containsKey(entityDefinition.getIdentifier())) {
                throw new IllegalArgumentException("Custom entity " + entityDefinition.getIdentifier() + " was already registered");
            }
            this.b.put(entityDefinition.getIdentifier(), entityDefinition);
            this.f.put(entityDefinition.getRuntimeId(), entityDefinition);
            if (entityDefinition.getAlternateName() == null) break block1;
            this.g.put(entityDefinition.getAlternateName(), entityDefinition);
        }
    }

    public EntityDefinition getDefinition(String string) {
        EntityDefinition entityDefinition = this.b.get(string);
        if (entityDefinition == null) {
            entityDefinition = this.g.get(string);
        }
        return entityDefinition;
    }

    public EntityDefinition getDefinition(int n) {
        return (EntityDefinition)this.f.get(n);
    }

    public int getRuntimeId(String string) {
        EntityDefinition entityDefinition = this.b.get(string);
        if (entityDefinition == null) {
            return this.c.getOrDefault(string, 0);
        }
        return entityDefinition.getRuntimeId();
    }

    private void a(int n) {
        try {
            CompoundTag compoundTag = (CompoundTag)NBTIO.readNetwork(new ByteArrayInputStream(AvailableEntityIdentifiersPacket.TAG));
            ListTag<CompoundTag> listTag = compoundTag.getList("idlist", CompoundTag.class);
            for (EntityDefinition entityDefinition : this.b.values()) {
                CompoundTag compoundTag2 = n <= 407 ? entityDefinition.getNetworkTagOld() : entityDefinition.getNetworkTag();
                listTag.add(compoundTag2);
            }
            compoundTag.putList(listTag);
            if (n > 407) {
                this.e = NBTIO.writeNetwork(compoundTag);
            } else {
                this.d = NBTIO.writeNetwork(compoundTag);
            }
        }
        catch (IOException iOException) {
            throw new RuntimeException("Unable to init entityIdentifiers", iOException);
        }
    }

    public byte[] getNetworkTagCached() {
        block0: {
            if (this.e != null) break block0;
            this.a(ProtocolInfo.CURRENT_PROTOCOL);
        }
        return this.e;
    }

    public byte[] getNetworkTagCachedOld() {
        block0: {
            if (this.d != null) break block0;
            this.a(407);
        }
        return this.d;
    }

    public boolean hasCustomEntities() {
        return !this.b.isEmpty();
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

