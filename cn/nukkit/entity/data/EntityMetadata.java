/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity.data;

import cn.nukkit.entity.data.ByteEntityData;
import cn.nukkit.entity.data.EntityData;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.entity.data.IntPositionEntityData;
import cn.nukkit.entity.data.LongEntityData;
import cn.nukkit.entity.data.NBTEntityData;
import cn.nukkit.entity.data.ShortEntityData;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.nbt.tag.CompoundTag;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Map;
import java.util.TreeMap;

public class EntityMetadata {
    private Int2ObjectMap<EntityData> a = new Int2ObjectOpenHashMap<EntityData>();

    public EntityData get(int n) {
        return this.getOrDefault(n, null);
    }

    public EntityData getOrDefault(int n, EntityData entityData) {
        try {
            return this.a.getOrDefault(n, entityData).setId(n);
        }
        catch (Exception exception) {
            if (entityData != null) {
                return entityData.setId(n);
            }
            return null;
        }
    }

    public boolean exists(int n) {
        return this.a.containsKey(n);
    }

    public EntityMetadata put(EntityData entityData) {
        this.a.put(entityData.getId(), entityData);
        return this;
    }

    public int getByte(int n) {
        return (Integer)this.getOrDefault(n, new ByteEntityData(n, 0)).getData() & 0xFF;
    }

    public int getShort(int n) {
        return (Integer)this.getOrDefault(n, new ShortEntityData(n, 0)).getData();
    }

    public int getInt(int n) {
        return (Integer)this.getOrDefault(n, new IntEntityData(n, 0)).getData();
    }

    public long getLong(int n) {
        return (Long)this.getOrDefault(n, new LongEntityData(n, 0L)).getData();
    }

    public float getFloat(int n) {
        return ((Float)this.getOrDefault(n, new FloatEntityData(n, 0.0f)).getData()).floatValue();
    }

    public boolean getBoolean(int n) {
        return this.getByte(n) == 1;
    }

    public CompoundTag getNBT(int n) {
        return (CompoundTag)this.getOrDefault(n, new NBTEntityData(n, new CompoundTag())).getData();
    }

    public String getString(int n) {
        return (String)this.getOrDefault(n, new StringEntityData(n, "")).getData();
    }

    public Vector3 getPosition(int n) {
        return (Vector3)this.getOrDefault(n, new IntPositionEntityData(n, new Vector3())).getData();
    }

    public Vector3f getFloatPosition(int n) {
        return (Vector3f)this.getOrDefault(n, new Vector3fEntityData(n, new Vector3f())).getData();
    }

    public EntityMetadata putByte(int n, int n2) {
        return this.put(new ByteEntityData(n, n2));
    }

    public EntityMetadata putShort(int n, int n2) {
        return this.put(new ShortEntityData(n, n2));
    }

    public EntityMetadata putInt(int n, int n2) {
        return this.put(new IntEntityData(n, n2));
    }

    public EntityMetadata putLong(int n, long l) {
        return this.put(new LongEntityData(n, l));
    }

    public EntityMetadata putFloat(int n, float f2) {
        return this.put(new FloatEntityData(n, f2));
    }

    public EntityMetadata putBoolean(int n, boolean bl) {
        return this.putByte(n, bl ? 1 : 0);
    }

    public EntityMetadata putNBT(int n, CompoundTag compoundTag) {
        return this.put(new NBTEntityData(n, compoundTag));
    }

    public EntityMetadata putSlot(int n, Item item) {
        return this.put(new NBTEntityData(n, item.getNamedTag()));
    }

    public EntityMetadata putString(int n, String string) {
        return this.put(new StringEntityData(n, string));
    }

    public Map<Integer, EntityData> getMap() {
        return new TreeMap<Integer, EntityData>(this.a);
    }

    private EntityMetadata a(Int2ObjectMap<EntityData> int2ObjectMap) {
        this.a = int2ObjectMap;
        return this;
    }

    public EntityMetadata clone() {
        return new EntityMetadata().a(new Int2ObjectOpenHashMap<EntityData>(this.a));
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

